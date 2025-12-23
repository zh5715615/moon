package tcbv.zhaohui.moon.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import tcbv.zhaohui.moon.contract.DappPool;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class AbiInputDecoder {

    private static final ObjectMapper OM = new ObjectMapper();

    /** 用 txHash 解码：内部会先通过 web3j 拉取交易 input */
    public static DecodedCall decodeTxHash(Web3j web3j, String txHash, String abiJson) throws Exception {
        EthTransaction txResp = web3j.ethGetTransactionByHash(txHash).send();
        Optional<Transaction> opt = txResp.getTransaction();
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("tx not found: " + txHash);
        }
        return decodeInput(opt.get().getInput(), abiJson);
    }

    public static LinkedHashMap<String, Object> decodeTxHash(Web3j web3j, String txHash, String abiJson, String func) throws Exception {
        AbiInputDecoder.DecodedCall call = AbiInputDecoder.decodeTxHash(web3j, txHash, abiJson);
        if (call.getFunctionName().equals(func)) {
            return call.getArgs();
        }
        return new LinkedHashMap<>();
    }

    /** 用 input 解码：input = 0x + 4字节methodId + 参数data */
    public static DecodedCall decodeInput(String input, String abiJson) throws Exception {
        if (input == null || input.length() < 10 || !input.startsWith("0x")) {
            throw new IllegalArgumentException("bad input: " + input);
        }
        String methodId = input.substring(0, 10).toLowerCase(Locale.ROOT); // 0x + 8 hex
        String data = "0x" + input.substring(10);

        List<AbiFunction> functions = parseFunctions(abiJson);

        Map<String, AbiFunction> selectorMap = new HashMap<>();
        for (AbiFunction f : functions) {
            selectorMap.put(selectorOf(f.signature()), f);
        }

        AbiFunction matched = selectorMap.get(methodId);
        if (matched == null) {
            throw new IllegalStateException("No function matched methodId=" + methodId + " in ABI");
        }

        // 构造 TypeReference 列表（用于解码）
        List<TypeReference<?>> typeRefs = new ArrayList<>();
        for (AbiParam p : matched.inputs) {
            typeRefs.add(buildTypeReference(p.type));
        }

        List<TypeReference<Type>> typeReferenceList = new ArrayList<>();
        for (TypeReference<?> typeRef : typeRefs) {
            TypeReference<Type> typeReference = (TypeReference<Type>) typeRef;
            typeReferenceList.add(typeReference);
        }
        List<Type> decoded = FunctionReturnDecoder.decode(data, typeReferenceList);

        // name -> value
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        for (int i = 0; i < matched.inputs.size(); i++) {
            AbiParam p = matched.inputs.get(i);
            String name = (p.name == null || p.name.trim().isEmpty()) ? ("arg" + i) : p.name;
            args.put(name, toJavaValue(decoded.get(i)));
        }

        List<String> inputTypes = matched.inputs.stream().map(x -> x.type).collect(Collectors.toList());

        return new DecodedCall(
                matched.name,
                matched.signature(),
                methodId,
                inputTypes,
                args
        );
    }

    // -----------------------------
    // ABI parsing
    // -----------------------------

    /** 支持：纯 ABI 数组 / Truffle/Hardhat artifact（里面有 abi 字段） */
    private static List<AbiFunction> parseFunctions(String abiJson) throws Exception {
        JsonNode root = OM.readTree(abiJson);

        JsonNode abiNode;
        if (root.isArray()) {
            abiNode = root;
        } else if (root.has("abi")) {
            abiNode = root.get("abi");
        } else {
            throw new IllegalArgumentException("Unsupported ABI json format: expect array or {abi:[...]}");
        }

        List<AbiFunction> out = new ArrayList<>();
        for (JsonNode item : abiNode) {
            if (!item.has("type") || !"function".equals(item.get("type").asText())) continue;
            if (!item.has("name")) continue;

            String name = item.get("name").asText();
            List<AbiParam> inputs = new ArrayList<>();

            JsonNode inputsNode = item.get("inputs");
            if (inputsNode != null && inputsNode.isArray()) {
                for (JsonNode in : inputsNode) {
                    String inName = in.has("name") ? in.get("name").asText() : "";
                    String inType = in.has("type") ? in.get("type").asText() : null;
                    if (inType == null) throw new IllegalArgumentException("ABI input type missing for function " + name);
                    inputs.add(new AbiParam(inName, inType));
                }
            }

            out.add(new AbiFunction(name, inputs));
        }
        return out;
    }

    // -----------------------------
    // selector / signature
    // -----------------------------

    /** keccak256(signature) 前4字节 */
    private static String selectorOf(String signature) {
        String hash = Hash.sha3String(signature); // 0x + 64bytes hex
        return hash.substring(0, 10).toLowerCase(Locale.ROOT);
    }

    // -----------------------------
    // TypeReference builder
    // -----------------------------

    /** 根据 solidity type 构造 TypeReference。支持基础类型 + 一维数组；遇到 tuple 直接报错。 */
    private static TypeReference<?> buildTypeReference(String solidityType) {
        if (solidityType == null || solidityType.trim().isEmpty()) {
            throw new IllegalArgumentException("empty solidity type");
        }
        if (solidityType.startsWith("tuple")) {
            throw new UnsupportedOperationException("tuple is not supported in this generic decoder. Please flatten tuple or add tuple-components handler.");
        }

        // array?
        if (solidityType.endsWith("]")) {
            int idx = solidityType.indexOf('[');
            String base = solidityType.substring(0, idx);
            String dim = solidityType.substring(idx); // "[...]" maybe multi-dim

            // 只支持一维 array：[] 或 [N]
            if (!dim.matches("\\[(\\d*)\\]")) {
                throw new UnsupportedOperationException("Only 1D array supported, got: " + solidityType);
            }

            final TypeReference<?> baseRef = buildTypeReference(base);

            // 用 DynamicArray 的 TypeReference 泛化（static array 也能解出来，一般会返回 StaticArray）
            return new TypeReference<DynamicArray<Type>>() {
                @Override
                public java.lang.reflect.Type getType() {
                    return new ParameterizedTypeImpl(DynamicArray.class, new java.lang.reflect.Type[]{baseRef.getType()});
                }
            };
        }

        return scalarTypeRef(solidityType);
    }

    private static TypeReference<?> scalarTypeRef(String t) {
        // address
        if ("address".equals(t)) return new TypeReference<Address>() {};
        // bool
        if ("bool".equals(t)) return new TypeReference<Bool>() {};
        // string
        if ("string".equals(t)) return new TypeReference<Utf8String>() {};
        // bytes (dynamic)
        if ("bytes".equals(t)) return new TypeReference<DynamicBytes>() {};

        // bytesN
        if (t.startsWith("bytes")) {
            int n = Integer.parseInt(t.substring("bytes".length()));
            if (n < 1 || n > 32) throw new IllegalArgumentException("bad bytesN: " + t);
            return bytesNRef(n);
        }

        // uint/int
        if (t.startsWith("uint")) {
            int bits = t.equals("uint") ? 256 : Integer.parseInt(t.substring(4));
            return uintRef(bits);
        }
        if (t.startsWith("int")) {
            int bits = t.equals("int") ? 256 : Integer.parseInt(t.substring(3));
            return intRef(bits);
        }

        throw new UnsupportedOperationException("Unsupported solidity type: " + t);
    }

    private static TypeReference<?> uintRef(int bits) {
        switch (bits) {
            case 8: return new TypeReference<Uint8>() {};
            case 16: return new TypeReference<Uint16>() {};
            case 24: return new TypeReference<Uint24>() {};
            case 32: return new TypeReference<Uint32>() {};
            case 40: return new TypeReference<Uint40>() {};
            case 48: return new TypeReference<Uint48>() {};
            case 56: return new TypeReference<Uint56>() {};
            case 64: return new TypeReference<Uint64>() {};
            case 72: return new TypeReference<Uint72>() {};
            case 80: return new TypeReference<Uint80>() {};
            case 88: return new TypeReference<Uint88>() {};
            case 96: return new TypeReference<Uint96>() {};
            case 104: return new TypeReference<Uint104>() {};
            case 112: return new TypeReference<Uint112>() {};
            case 120: return new TypeReference<Uint120>() {};
            case 128: return new TypeReference<Uint128>() {};
            case 136: return new TypeReference<Uint136>() {};
            case 144: return new TypeReference<Uint144>() {};
            case 152: return new TypeReference<Uint152>() {};
            case 160: return new TypeReference<Uint160>() {};
            case 168: return new TypeReference<Uint168>() {};
            case 176: return new TypeReference<Uint176>() {};
            case 184: return new TypeReference<Uint184>() {};
            case 192: return new TypeReference<Uint192>() {};
            case 200: return new TypeReference<Uint200>() {};
            case 208: return new TypeReference<Uint208>() {};
            case 216: return new TypeReference<Uint216>() {};
            case 224: return new TypeReference<Uint224>() {};
            case 232: return new TypeReference<Uint232>() {};
            case 240: return new TypeReference<Uint240>() {};
            case 248: return new TypeReference<Uint248>() {};
            case 256: return new TypeReference<Uint256>() {};
            default: throw new UnsupportedOperationException("Unsupported uint bits: " + bits);
        }
    }

    private static TypeReference<?> intRef(int bits) {
        switch (bits) {
            case 8: return new TypeReference<Int8>() {};
            case 16: return new TypeReference<Int16>() {};
            case 24: return new TypeReference<Int24>() {};
            case 32: return new TypeReference<Int32>() {};
            case 40: return new TypeReference<Int40>() {};
            case 48: return new TypeReference<Int48>() {};
            case 56: return new TypeReference<Int56>() {};
            case 64: return new TypeReference<Int64>() {};
            case 72: return new TypeReference<Int72>() {};
            case 80: return new TypeReference<Int80>() {};
            case 88: return new TypeReference<Int88>() {};
            case 96: return new TypeReference<Int96>() {};
            case 104: return new TypeReference<Int104>() {};
            case 112: return new TypeReference<Int112>() {};
            case 120: return new TypeReference<Int120>() {};
            case 128: return new TypeReference<Int128>() {};
            case 136: return new TypeReference<Int136>() {};
            case 144: return new TypeReference<Int144>() {};
            case 152: return new TypeReference<Int152>() {};
            case 160: return new TypeReference<Int160>() {};
            case 168: return new TypeReference<Int168>() {};
            case 176: return new TypeReference<Int176>() {};
            case 184: return new TypeReference<Int184>() {};
            case 192: return new TypeReference<Int192>() {};
            case 200: return new TypeReference<Int200>() {};
            case 208: return new TypeReference<Int208>() {};
            case 216: return new TypeReference<Int216>() {};
            case 224: return new TypeReference<Int224>() {};
            case 232: return new TypeReference<Int232>() {};
            case 240: return new TypeReference<Int240>() {};
            case 248: return new TypeReference<Int248>() {};
            case 256: return new TypeReference<Int256>() {};
            default: throw new UnsupportedOperationException("Unsupported int bits: " + bits);
        }
    }

    private static TypeReference<?> bytesNRef(int n) {
        switch (n) {
            case 1: return new TypeReference<Bytes1>() {};
            case 2: return new TypeReference<Bytes2>() {};
            case 3: return new TypeReference<Bytes3>() {};
            case 4: return new TypeReference<Bytes4>() {};
            case 5: return new TypeReference<Bytes5>() {};
            case 6: return new TypeReference<Bytes6>() {};
            case 7: return new TypeReference<Bytes7>() {};
            case 8: return new TypeReference<Bytes8>() {};
            case 9: return new TypeReference<Bytes9>() {};
            case 10: return new TypeReference<Bytes10>() {};
            case 11: return new TypeReference<Bytes11>() {};
            case 12: return new TypeReference<Bytes12>() {};
            case 13: return new TypeReference<Bytes13>() {};
            case 14: return new TypeReference<Bytes14>() {};
            case 15: return new TypeReference<Bytes15>() {};
            case 16: return new TypeReference<Bytes16>() {};
            case 17: return new TypeReference<Bytes17>() {};
            case 18: return new TypeReference<Bytes18>() {};
            case 19: return new TypeReference<Bytes19>() {};
            case 20: return new TypeReference<Bytes20>() {};
            case 21: return new TypeReference<Bytes21>() {};
            case 22: return new TypeReference<Bytes22>() {};
            case 23: return new TypeReference<Bytes23>() {};
            case 24: return new TypeReference<Bytes24>() {};
            case 25: return new TypeReference<Bytes25>() {};
            case 26: return new TypeReference<Bytes26>() {};
            case 27: return new TypeReference<Bytes27>() {};
            case 28: return new TypeReference<Bytes28>() {};
            case 29: return new TypeReference<Bytes29>() {};
            case 30: return new TypeReference<Bytes30>() {};
            case 31: return new TypeReference<Bytes31>() {};
            case 32: return new TypeReference<Bytes32>() {};
            default: throw new UnsupportedOperationException("Unsupported bytesN: " + n);
        }
    }

    private static Object toJavaValue(Type t) {
        if (t == null) return null;

        if (t instanceof Address) return ((Address) t).getValue();
        if (t instanceof Utf8String) return ((Utf8String) t).getValue();
        if (t instanceof Bool) return ((Bool) t).getValue();
        if (t instanceof NumericType) return ((NumericType) t).getValue();

        if (t instanceof DynamicBytes) return ((DynamicBytes) t).getValue();
        if (t instanceof BytesType) return ((BytesType) t).getValue();

        if (t instanceof Array) {
            @SuppressWarnings("unchecked")
            List<Type> values = ((Array<Type>) t).getValue();
            List<Object> out = new ArrayList<>(values.size());
            for (Type v : values) out.add(toJavaValue(v));
            return out;
        }

        return t.getValue();
    }

    // -----------------------------
    // Model classes
    // -----------------------------

    private static class AbiParam {
        final String name;
        final String type;

        AbiParam(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    private static class AbiFunction {
        final String name;
        final List<AbiParam> inputs;

        AbiFunction(String name, List<AbiParam> inputs) {
            this.name = name;
            this.inputs = inputs != null ? inputs : Collections.<AbiParam>emptyList();
        }

        String signature() {
            String types = inputs.stream().map(p -> p.type).collect(Collectors.joining(","));
            return name + "(" + types + ")";
        }
    }

    /** 这个就是你问的 DecodedCall：返回结果对象（我自己定义的） */
    public static class DecodedCall {
        private final String functionName;
        private final String signature;
        private final String methodId;
        private final List<String> inputTypes;
        private final LinkedHashMap<String, Object> args;

        public DecodedCall(String functionName, String signature, String methodId,
                           List<String> inputTypes, LinkedHashMap<String, Object> args) {
            this.functionName = functionName;
            this.signature = signature;
            this.methodId = methodId;
            this.inputTypes = inputTypes;
            this.args = args;
        }

        public String getFunctionName() { return functionName; }
        public String getSignature() { return signature; }
        public String getMethodId() { return methodId; }
        public List<String> getInputTypes() { return inputTypes; }
        public LinkedHashMap<String, Object> getArgs() { return args; }

        @Override
        public String toString() {
            return "DecodedCall{" +
                    "functionName='" + functionName + '\'' +
                    ", signature='" + signature + '\'' +
                    ", methodId='" + methodId + '\'' +
                    ", inputTypes=" + inputTypes +
                    ", args=" + args +
                    '}';
        }
    }

    // -----------------------------
    // helper: ParameterizedType for DynamicArray<T>
    // -----------------------------
    private static class ParameterizedTypeImpl implements java.lang.reflect.ParameterizedType {
        private final Class<?> raw;
        private final java.lang.reflect.Type[] args;

        ParameterizedTypeImpl(Class<?> raw, java.lang.reflect.Type[] args) {
            this.raw = raw;
            this.args = args;
        }

        @Override public java.lang.reflect.Type[] getActualTypeArguments() { return args; }
        @Override public java.lang.reflect.Type getRawType() { return raw; }
        @Override public java.lang.reflect.Type getOwnerType() { return null; }
    }
}