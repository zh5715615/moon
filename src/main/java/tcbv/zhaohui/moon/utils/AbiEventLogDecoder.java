package tcbv.zhaohui.moon.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhaohui
 * @Title: AbiEventLogDecoder
 * @Description:
 * @date: 2025/12/20 22:44
 */
public class AbiEventLogDecoder {
    @Data
    public static class DecodedEvent {
        public String contractAddress;
        public String eventName;
        public String eventSignature;   // EventName(type1,type2,...)
        public String topic0;           // keccak256(signature)
        public String txHash;
        public BigInteger logIndex;
        public Map<String, Object> args = new LinkedHashMap<>();

        @Override
        public String toString() {
            return "DecodedEvent{" +
                    "contractAddress='" + contractAddress + '\'' +
                    ", eventName='" + eventName + '\'' +
                    ", eventSignature='" + eventSignature + '\'' +
                    ", txHash='" + txHash + '\'' +
                    ", logIndex=" + logIndex +
                    ", args=" + args +
                    '}';
        }
    }

    public static class AbiEvent {
        public String name;
        public boolean anonymous;
        public List<AbiEventInput> inputs = new ArrayList<>();

        public String signature() {
            String types = inputs.stream().map(i -> canonicalType(i.type)).collect(Collectors.joining(","));
            return name + "(" + types + ")";
        }

        public String topic0() {
            // 匹配 log.topics[0]
            return Hash.sha3String(signature()); // 0x...
        }
    }

    public static class AbiEventInput {
        public String name;
        public String type;
        public boolean indexed;
    }

    // ---------------- Public APIs ----------------

    /**
     * 通过 txHash 解码所有事件日志（emit）
     *
     * @param web3j  web3j instance
     * @param txHash 交易hash
     * @param abiJson 既可以是 truffle artifact JSON（含 abi 字段），也可以是纯 ABI 数组 JSON
     */
    public static List<DecodedEvent> decodeTxEvents(Web3j web3j, String txHash, String abiJson) throws Exception {
        TransactionReceipt receipt = getReceiptOrThrow(web3j, txHash);
        List<AbiEvent> events = parseAbiEvents(abiJson);

        // topic0 -> event
        Map<String, AbiEvent> topic0Map = new HashMap<>();
        for (AbiEvent e : events) {
            if (!e.anonymous) {
                topic0Map.put(e.topic0().toLowerCase(Locale.ROOT), e);
            }
        }

        List<DecodedEvent> out = new ArrayList<>();
        for (Log log : receipt.getLogs()) {
            if (log.getTopics() == null || log.getTopics().isEmpty()) continue;

            String topic0 = log.getTopics().get(0);
            AbiEvent ev = topic0Map.get(topic0.toLowerCase(Locale.ROOT));
            if (ev == null) {
                // 不是你这个 ABI 里的 event（或者是 anonymous event）
                continue;
            }
            out.add(decodeSingleLog(log, receipt.getTransactionHash(), ev));
        }
        return out;
    }

    // ---------------- Core decode ----------------

    private static DecodedEvent decodeSingleLog(Log log, String txHash, AbiEvent ev) {
        DecodedEvent de = new DecodedEvent();
        de.contractAddress = log.getAddress();
        de.eventName = ev.name;
        de.eventSignature = ev.signature();
        de.topic0 = ev.topic0();
        de.txHash = txHash;
        de.logIndex = log.getLogIndex();

        // 1) indexed 从 topics[1..] 解
        List<AbiEventInput> indexedInputs = ev.inputs.stream().filter(i -> i.indexed).collect(Collectors.toList());
        List<String> topics = log.getTopics();
        for (int i = 0; i < indexedInputs.size(); i++) {
            AbiEventInput in = indexedInputs.get(i);
            String topic = (topics.size() > i + 1) ? topics.get(i + 1) : null;
            Object val = decodeIndexed(in.type, topic);
            String key = StringUtils.isBlank(in.name) ? ("arg" + i) : in.name;
            de.args.put(key, val);
        }

        // 2) 非 indexed 从 data 解
        List<AbiEventInput> nonIndexedInputs = ev.inputs.stream().filter(i -> !i.indexed).collect(Collectors.toList());
        String data = log.getData(); // 0x...
        if (nonIndexedInputs.size() > 0) {
            List<TypeReference<Type>> typeRefs = new ArrayList<>();
            for (AbiEventInput in : nonIndexedInputs) {
                typeRefs.add(asTypeRef(buildTypeReference(in.type)));
            }
            List<Type> decoded = FunctionReturnDecoder.decode(cleanHexPrefix(data), typeRefs);

            for (int i = 0; i < nonIndexedInputs.size(); i++) {
                AbiEventInput in = nonIndexedInputs.get(i);
                Type t = decoded.get(i);
                Object val = unwrapType(t);
                String key = StringUtils.isBlank(in.name) ? ("arg" + i) : in.name;
                de.args.put(key, val);
            }
        }

        return de;
    }

    /**
     * indexed 参数解码：
     * - static 类型（uint/address/bool/bytes32 等）可以解码出值
     * - dynamic 类型（string/bytes/数组）topic 里只有 hash，无法还原原文，只能返回 hash
     */
    private static Object decodeIndexed(String solidityType, String topicHex) {
        if (topicHex == null) return null;
        String t = canonicalType(solidityType);

        // dynamic indexed -> only hash in topic
        if (isDynamicType(t)) {
            return topicHex; // 直接返回 0x... hash
        }

        // static indexed -> decode
        TypeReference<?> tr = buildTypeReference(t);
        // web3j 提供 decodeIndexedValue
        Type v = org.web3j.abi.FunctionReturnDecoder.decodeIndexedValue(topicHex, tr);
        return unwrapType(v);
    }

    private static Object unwrapType(Type t) {
        if (t == null) return null;
        Object v = t.getValue();
        if (v instanceof byte[]) return Numeric.toHexString((byte[]) v);
        return v;
    }

    // ---------------- ABI parsing ----------------

    private static List<AbiEvent> parseAbiEvents(String abiJson) throws IOException {
        ObjectMapper om = new ObjectMapper();
        JsonNode root = om.readTree(abiJson);

        // 支持：truffle artifact {"abi":[...]} 或 纯数组 [...]
        JsonNode abiNode = root.isArray() ? root : root.get("abi");
        if (abiNode == null || !abiNode.isArray()) {
            throw new IllegalArgumentException("Invalid ABI json: no abi array found.");
        }

        List<AbiEvent> events = new ArrayList<>();
        for (JsonNode item : abiNode) {
            if (!"event".equals(item.path("type").asText())) continue;

            AbiEvent ev = new AbiEvent();
            ev.name = item.path("name").asText();
            ev.anonymous = item.path("anonymous").asBoolean(false);

            JsonNode inputs = item.path("inputs");
            if (inputs.isArray()) {
                for (JsonNode in : inputs) {
                    AbiEventInput ei = new AbiEventInput();
                    ei.name = in.path("name").asText();
                    ei.type = in.path("type").asText();
                    ei.indexed = in.path("indexed").asBoolean(false);
                    ev.inputs.add(ei);
                }
            }
            events.add(ev);
        }
        return events;
    }

    // ---------------- web3 helper ----------------

    private static TransactionReceipt getReceiptOrThrow(Web3j web3j, String txHash) throws Exception {
        EthGetTransactionReceipt r = web3j.ethGetTransactionReceipt(txHash).send();
        Optional<TransactionReceipt> opt = r.getTransactionReceipt();
        if (!opt.isPresent()) {
            throw new IllegalStateException("Transaction receipt not found yet (tx pending?) txHash=" + txHash);
        }
        return opt.get();
    }

    // ---------------- TypeReference builder ----------------

    /**
     * 注意：这里只覆盖常见类型（address/bool/uint/int/string/bytes/bytes32）
     * 你如果用到 array / tuple，需要扩展
     */
    private static TypeReference<?> buildTypeReference(String solidityType) {
        String t = canonicalType(solidityType);

        switch (t) {
            case "address":
                return new TypeReference<Address>() {};
            case "bool":
                return new TypeReference<Bool>() {};
            case "string":
                return new TypeReference<Utf8String>() {};
            case "bytes":
                return new TypeReference<DynamicBytes>() {};
            case "bytes32":
                return new TypeReference<Bytes32>() {};
            default:
                if (t.startsWith("uint")) return new TypeReference<Uint256>() {};
                if (t.startsWith("int")) return new TypeReference<Int256>() {};
                // 需要更多类型的话在这里补
                throw new UnsupportedOperationException("Unsupported solidity type: " + t);
        }
    }

    @SuppressWarnings("unchecked")
    private static TypeReference<Type> asTypeRef(TypeReference<?> ref) {
        return (TypeReference<Type>) ref;
    }

    private static String cleanHexPrefix(String hex) {
        return Numeric.cleanHexPrefix(hex == null ? "0x" : hex);
    }

    private static boolean isDynamicType(String canonicalType) {
        // 动态类型（topic 只能拿到 hash）
        if ("string".equals(canonicalType)) return true;
        if ("bytes".equals(canonicalType)) return true;
        // array 也属于动态（这里先粗略判断）
        if (canonicalType.endsWith("]")) return true;
        return false;
    }

    private static String canonicalType(String type) {
        // 规范化：uint -> uint256 / int -> int256
        if (type == null) return "";
        String t = type.trim();
        if (t.equals("uint")) return "uint256";
        if (t.equals("int")) return "int256";
        return t;
    }
}
