package tcbv.zhaohui.moon.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 5.0.0.
 */
@SuppressWarnings("rawtypes")
public class BullfightingGameSample extends Contract {
    public static final String BINARY = "608060405234801562000010575f80fd5b50604051620010e0380380620010e0833981810160405281019062000036919062000310565b825f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603620000aa575f6040517f1e4fbdf7000000000000000000000000000000000000000000000000000000008152600401620000a191906200037a565b60405180910390fd5b620000bb81620001ea60201b60201c565b508060025f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663313ce5676040518163ffffffff1660e01b8152600401602060405180830381865afa15801562000167573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906200018d9190620003d0565b600a6200019b919062000586565b6001819055508160035f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050620005d6565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f620002da82620002af565b9050919050565b620002ec81620002ce565b8114620002f7575f80fd5b50565b5f815190506200030a81620002e1565b92915050565b5f805f606084860312156200032a5762000329620002ab565b5b5f6200033986828701620002fa565b93505060206200034c86828701620002fa565b92505060406200035f86828701620002fa565b9150509250925092565b6200037481620002ce565b82525050565b5f6020820190506200038f5f83018462000369565b92915050565b5f60ff82169050919050565b620003ac8162000395565b8114620003b7575f80fd5b50565b5f81519050620003ca81620003a1565b92915050565b5f60208284031215620003e857620003e7620002ab565b5b5f620003f784828501620003ba565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f8160011c9050919050565b5f808291508390505b60018511156200048a5780860481111562000462576200046162000400565b5b6001851615620004725780820291505b808102905062000482856200042d565b945062000442565b94509492505050565b5f82620004a4576001905062000576565b81620004b3575f905062000576565b8160018114620004cc5760028114620004d7576200050d565b600191505062000576565b60ff841115620004ec57620004eb62000400565b5b8360020a91508482111562000506576200050562000400565b5b5062000576565b5060208310610133831016604e8410600b8410161715620005475782820a90508381111562000541576200054062000400565b5b62000576565b62000556848484600162000439565b9250905081840481111562000570576200056f62000400565b5b81810290505b9392505050565b5f819050919050565b5f62000592826200057d565b91506200059f8362000395565b9250620005ce7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff848462000493565b905092915050565b610afc80620005e45f395ff3fe608060405234801561000f575f80fd5b5060043610610086575f3560e01c80637f1f3d3b116100595780637f1f3d3b146100ea5780638da5cb5b14610108578063abd70aa214610126578063f2fde38b1461014457610086565b806321670f221461008a5780632b019aac146100a65780635d0202a0146100c4578063715018a6146100e0575b5f80fd5b6100a4600480360381019061009f91906107c5565b610160565b005b6100ae610198565b6040516100bb919061085e565b60405180910390f35b6100de60048036038101906100d99190610877565b6101bd565b005b6100e8610307565b005b6100f261031a565b6040516100ff91906108b1565b60405180910390f35b610110610320565b60405161011d91906108d9565b60405180910390f35b61012e610347565b60405161013b91906108b1565b60405180910390f35b61015e600480360381019061015991906108f2565b6103e6565b005b61016861046a565b61019460025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1683836104f1565b5050565b60025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b5f81116101ff576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101f690610977565b60405180910390fd5b5f60015460058361021091906109c2565b61021a91906109c2565b90505f606460058361022c91906109c2565b6102369190610a30565b905061026560025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16333085610544565b6102b260025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660035f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16836104f1565b3373ffffffffffffffffffffffffffffffffffffffff167f9f21c127e9dfa81793a8d9771f90107cbfef4161d13ac060fdcb40a7d35fc03d83856040516102fa929190610a60565b60405180910390a2505050565b61030f61046a565b6103185f610599565b565b60015481565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b5f60025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166370a08231306040518263ffffffff1660e01b81526004016103a291906108d9565b602060405180830381865afa1580156103bd573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906103e19190610a9b565b905090565b6103ee61046a565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff160361045e575f6040517f1e4fbdf700000000000000000000000000000000000000000000000000000000815260040161045591906108d9565b60405180910390fd5b61046781610599565b50565b61047261065a565b73ffffffffffffffffffffffffffffffffffffffff16610490610320565b73ffffffffffffffffffffffffffffffffffffffff16146104ef576104b361065a565b6040517f118cdaa70000000000000000000000000000000000000000000000000000000081526004016104e691906108d9565b60405180910390fd5b565b6104fe8383836001610661565b61053f57826040517f5274afe700000000000000000000000000000000000000000000000000000000815260040161053691906108d9565b60405180910390fd5b505050565b6105528484848460016106c3565b61059357836040517f5274afe700000000000000000000000000000000000000000000000000000000815260040161058a91906108d9565b60405180910390fd5b50505050565b5f805f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b5f8063a9059cbb60e01b9050604051815f525f1960601c86166004528460245260205f60445f808b5af1925060015f511483166106b55783831516156106a9573d5f823e3d81fd5b5f873b113d1516831692505b806040525050949350505050565b5f806323b872dd60e01b9050604051815f525f1960601c87166004525f1960601c86166024528460445260205f60645f808c5af1925060015f51148316610721578383151615610715573d5f823e3d81fd5b5f883b113d1516831692505b806040525f606052505095945050505050565b5f80fd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f61076182610738565b9050919050565b61077181610757565b811461077b575f80fd5b50565b5f8135905061078c81610768565b92915050565b5f819050919050565b6107a481610792565b81146107ae575f80fd5b50565b5f813590506107bf8161079b565b92915050565b5f80604083850312156107db576107da610734565b5b5f6107e88582860161077e565b92505060206107f9858286016107b1565b9150509250929050565b5f819050919050565b5f61082661082161081c84610738565b610803565b610738565b9050919050565b5f6108378261080c565b9050919050565b5f6108488261082d565b9050919050565b6108588161083e565b82525050565b5f6020820190506108715f83018461084f565b92915050565b5f6020828403121561088c5761088b610734565b5b5f610899848285016107b1565b91505092915050565b6108ab81610792565b82525050565b5f6020820190506108c45f8301846108a2565b92915050565b6108d381610757565b82525050565b5f6020820190506108ec5f8301846108ca565b92915050565b5f6020828403121561090757610906610734565b5b5f6109148482850161077e565b91505092915050565b5f82825260208201905092915050565b7f4275792074696d6573206d757374203e203000000000000000000000000000005f82015250565b5f61096160128361091d565b915061096c8261092d565b602082019050919050565b5f6020820190508181035f83015261098e81610955565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f6109cc82610792565b91506109d783610792565b92508282026109e581610792565b915082820484148315176109fc576109fb610995565b5b5092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601260045260245ffd5b5f610a3a82610792565b9150610a4583610792565b925082610a5557610a54610a03565b5b828204905092915050565b5f604082019050610a735f8301856108a2565b610a8060208301846108a2565b9392505050565b5f81519050610a958161079b565b92915050565b5f60208284031215610ab057610aaf610734565b5b5f610abd84828501610a87565b9150509291505056fea2646970667358221220b9d669494b2d314432756c8b87acc07ed60337cfe04bc3a4381942cdb6556c9364736f6c63430008140033";

    public static final String ABI_JSON = "[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"beneficiary\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_projectParty\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_spaceJediAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"OwnableInvalidOwner\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"}],\"name\":\"OwnableUnauthorizedAccount\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"token\",\"type\":\"address\"}],\"name\":\"SafeERC20FailedOperation\",\"type\":\"error\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"times\",\"type\":\"uint256\"}],\"name\":\"BuyGameTimes\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"previousOwner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"OwnershipTransferred\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"times\",\"type\":\"uint256\"}],\"name\":\"buyGameTimes\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getPoolBalance\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"renounceOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"userAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"reward\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"sjTokenUnit\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"spaceJediToken\",\"outputs\":[{\"internalType\":\"contract IERC20Metadata\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"transferOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";

    public static final String FUNC_BUYGAMETIMES = "buyGameTimes";

    public static final String FUNC_GETPOOLBALANCE = "getPoolBalance";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REWARD = "reward";

    public static final String FUNC_SJTOKENUNIT = "sjTokenUnit";

    public static final String FUNC_SPACEJEDITOKEN = "spaceJediToken";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event BUYGAMETIMES_EVENT = new Event("BuyGameTimes",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected BullfightingGameSample(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BullfightingGameSample(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected BullfightingGameSample(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected BullfightingGameSample(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<BuyGameTimesEventResponse> getBuyGameTimesEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BUYGAMETIMES_EVENT, transactionReceipt);
        ArrayList<BuyGameTimesEventResponse> responses = new ArrayList<BuyGameTimesEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BuyGameTimesEventResponse typedResponse = new BuyGameTimesEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.times = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BuyGameTimesEventResponse> buyGameTimesEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BuyGameTimesEventResponse>() {
            @Override
            public BuyGameTimesEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BUYGAMETIMES_EVENT, log);
                BuyGameTimesEventResponse typedResponse = new BuyGameTimesEventResponse();
                typedResponse.log = log;
                typedResponse.user = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.times = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BuyGameTimesEventResponse> buyGameTimesEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BUYGAMETIMES_EVENT));
        return buyGameTimesEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> buyGameTimes(BigInteger times) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BUYGAMETIMES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(times)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getPoolBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETPOOLBALANCE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> reward(String userAddress, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REWARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> sjTokenUnit() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SJTOKENUNIT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> spaceJediToken() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SPACEJEDITOKEN,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static BullfightingGameSample load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BullfightingGameSample(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static BullfightingGameSample load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BullfightingGameSample(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static BullfightingGameSample load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new BullfightingGameSample(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static BullfightingGameSample load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new BullfightingGameSample(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<BullfightingGameSample> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String beneficiary, String _projectParty, String _spaceJediAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, beneficiary),
                new org.web3j.abi.datatypes.Address(160, _projectParty),
                new org.web3j.abi.datatypes.Address(160, _spaceJediAddress)));
        return deployRemoteCall(BullfightingGameSample.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<BullfightingGameSample> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String beneficiary, String _projectParty, String _spaceJediAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, beneficiary),
                new org.web3j.abi.datatypes.Address(160, _projectParty),
                new org.web3j.abi.datatypes.Address(160, _spaceJediAddress)));
        return deployRemoteCall(BullfightingGameSample.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BullfightingGameSample> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String beneficiary, String _projectParty, String _spaceJediAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, beneficiary),
                new org.web3j.abi.datatypes.Address(160, _projectParty),
                new org.web3j.abi.datatypes.Address(160, _spaceJediAddress)));
        return deployRemoteCall(BullfightingGameSample.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<BullfightingGameSample> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String beneficiary, String _projectParty, String _spaceJediAddress) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, beneficiary),
                new org.web3j.abi.datatypes.Address(160, _projectParty),
                new org.web3j.abi.datatypes.Address(160, _spaceJediAddress)));
        return deployRemoteCall(BullfightingGameSample.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class BuyGameTimesEventResponse extends BaseEventResponse {
        public String user;

        public BigInteger amount;

        public BigInteger times;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
