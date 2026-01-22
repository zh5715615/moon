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
    public static final String BINARY = "608060405234801561000f575f5ffd5b5060405161144b38038061144b833981810160405281019061003191906102f9565b825f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036100a2575f6040517f1e4fbdf70000000000000000000000000000000000000000000000000000000081526004016100999190610358565b60405180910390fd5b6100b1816101da60201b60201c565b508060025f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663313ce5676040518163ffffffff1660e01b8152600401602060405180830381865afa15801561015c573d5f5f3e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061018091906103a7565b600a61018c9190610537565b6001819055508160035f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050610581565b5f5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f5f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f5ffd5b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f6102c88261029f565b9050919050565b6102d8816102be565b81146102e2575f5ffd5b50565b5f815190506102f3816102cf565b92915050565b5f5f5f606084860312156103105761030f61029b565b5b5f61031d868287016102e5565b935050602061032e868287016102e5565b925050604061033f868287016102e5565b9150509250925092565b610352816102be565b82525050565b5f60208201905061036b5f830184610349565b92915050565b5f60ff82169050919050565b61038681610371565b8114610390575f5ffd5b50565b5f815190506103a18161037d565b92915050565b5f602082840312156103bc576103bb61029b565b5b5f6103c984828501610393565b91505092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f8160011c9050919050565b5f5f8291508390505b6001851115610454578086048111156104305761042f6103d2565b5b600185161561043f5780820291505b808102905061044d856103ff565b9450610414565b94509492505050565b5f8261046c5760019050610527565b81610479575f9050610527565b816001811461048f5760028114610499576104c8565b6001915050610527565b60ff8411156104ab576104aa6103d2565b5b8360020a9150848211156104c2576104c16103d2565b5b50610527565b5060208310610133831016604e8410600b84101617156104fd5782820a9050838111156104f8576104f76103d2565b5b610527565b61050a848484600161040b565b92509050818404811115610521576105206103d2565b5b81810290505b9392505050565b5f819050919050565b5f6105418261052e565b915061054c83610371565b92506105797fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff848461045d565b905092915050565b610ebd8061058e5f395ff3fe608060405234801561000f575f5ffd5b5060043610610086575f3560e01c80637f1f3d3b116100595780637f1f3d3b146100ea5780638da5cb5b14610108578063abd70aa214610126578063f2fde38b1461014457610086565b80632b019aac1461008a5780633af401f6146100a85780635d0202a0146100c4578063715018a6146100e0575b5f5ffd5b610092610160565b60405161009f9190610910565b60405180910390f35b6100c260048036038101906100bd91906109e7565b610185565b005b6100de60048036038101906100d99190610a98565b61031f565b005b6100e8610469565b005b6100f261047c565b6040516100ff9190610ad2565b60405180910390f35b610110610482565b60405161011d9190610b0b565b60405180910390f35b61012e6104a9565b60405161013b9190610ad2565b60405180910390f35b61015e60048036038101906101599190610b4e565b610548565b005b60025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b61018d6105cc565b8181905084849050146101d5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101cc90610bd3565b60405180910390fd5b5f5f90505b84849050811015610318575f8585838181106101f9576101f8610bf1565b5b905060200201602081019061020e9190610b4e565b90505f84848481811061022457610223610bf1565b5b9050602002013590505f73ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff160361029b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161029290610c68565b60405180910390fd5b5f81116102dd576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016102d490610cd0565b60405180910390fd5b61030960025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff168383610653565b505080806001019150506101da565b5050505050565b5f8111610361576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161035890610d38565b60405180910390fd5b5f6001546005836103729190610d83565b61037c9190610d83565b90505f606460058361038e9190610d83565b6103989190610df1565b90506103c760025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff163330856106a6565b61041460025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1660035f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1683610653565b3373ffffffffffffffffffffffffffffffffffffffff167f9f21c127e9dfa81793a8d9771f90107cbfef4161d13ac060fdcb40a7d35fc03d838560405161045c929190610e21565b60405180910390a2505050565b6104716105cc565b61047a5f6106fb565b565b60015481565b5f5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b5f60025f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166370a08231306040518263ffffffff1660e01b81526004016105049190610b0b565b602060405180830381865afa15801561051f573d5f5f3e3d5ffd5b505050506040513d601f19601f820116820180604052508101906105439190610e5c565b905090565b6105506105cc565b5f73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036105c0575f6040517f1e4fbdf70000000000000000000000000000000000000000000000000000000081526004016105b79190610b0b565b60405180910390fd5b6105c9816106fb565b50565b6105d46107bc565b73ffffffffffffffffffffffffffffffffffffffff166105f2610482565b73ffffffffffffffffffffffffffffffffffffffff1614610651576106156107bc565b6040517f118cdaa70000000000000000000000000000000000000000000000000000000081526004016106489190610b0b565b60405180910390fd5b565b61066083838360016107c3565b6106a157826040517f5274afe70000000000000000000000000000000000000000000000000000000081526004016106989190610b0b565b60405180910390fd5b505050565b6106b4848484846001610825565b6106f557836040517f5274afe70000000000000000000000000000000000000000000000000000000081526004016106ec9190610b0b565b60405180910390fd5b50505050565b5f5f5f9054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050815f5f6101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b5f33905090565b5f5f63a9059cbb60e01b9050604051815f525f1960601c86166004528460245260205f60445f5f8b5af1925060015f5114831661081757838315161561080b573d5f823e3d81fd5b5f873b113d1516831692505b806040525050949350505050565b5f5f6323b872dd60e01b9050604051815f525f1960601c87166004525f1960601c86166024528460445260205f60645f5f8c5af1925060015f51148316610883578383151615610877573d5f823e3d81fd5b5f883b113d1516831692505b806040525f606052505095945050505050565b5f73ffffffffffffffffffffffffffffffffffffffff82169050919050565b5f819050919050565b5f6108d86108d36108ce84610896565b6108b5565b610896565b9050919050565b5f6108e9826108be565b9050919050565b5f6108fa826108df565b9050919050565b61090a816108f0565b82525050565b5f6020820190506109235f830184610901565b92915050565b5f5ffd5b5f5ffd5b5f5ffd5b5f5ffd5b5f5ffd5b5f5f83601f84011261095257610951610931565b5b8235905067ffffffffffffffff81111561096f5761096e610935565b5b60208301915083602082028301111561098b5761098a610939565b5b9250929050565b5f5f83601f8401126109a7576109a6610931565b5b8235905067ffffffffffffffff8111156109c4576109c3610935565b5b6020830191508360208202830111156109e0576109df610939565b5b9250929050565b5f5f5f5f604085870312156109ff576109fe610929565b5b5f85013567ffffffffffffffff811115610a1c57610a1b61092d565b5b610a288782880161093d565b9450945050602085013567ffffffffffffffff811115610a4b57610a4a61092d565b5b610a5787828801610992565b925092505092959194509250565b5f819050919050565b610a7781610a65565b8114610a81575f5ffd5b50565b5f81359050610a9281610a6e565b92915050565b5f60208284031215610aad57610aac610929565b5b5f610aba84828501610a84565b91505092915050565b610acc81610a65565b82525050565b5f602082019050610ae55f830184610ac3565b92915050565b5f610af582610896565b9050919050565b610b0581610aeb565b82525050565b5f602082019050610b1e5f830184610afc565b92915050565b610b2d81610aeb565b8114610b37575f5ffd5b50565b5f81359050610b4881610b24565b92915050565b5f60208284031215610b6357610b62610929565b5b5f610b7084828501610b3a565b91505092915050565b5f82825260208201905092915050565b7f417272617973206c656e677468206d69736d61746368000000000000000000005f82015250565b5f610bbd601683610b79565b9150610bc882610b89565b602082019050919050565b5f6020820190508181035f830152610bea81610bb1565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52603260045260245ffd5b7f496e76616c6964206164647265737300000000000000000000000000000000005f82015250565b5f610c52600f83610b79565b9150610c5d82610c1e565b602082019050919050565b5f6020820190508181035f830152610c7f81610c46565b9050919050565b7f416d6f756e74206d757374206265203e203000000000000000000000000000005f82015250565b5f610cba601283610b79565b9150610cc582610c86565b602082019050919050565b5f6020820190508181035f830152610ce781610cae565b9050919050565b7f4275792074696d6573206d757374203e203000000000000000000000000000005f82015250565b5f610d22601283610b79565b9150610d2d82610cee565b602082019050919050565b5f6020820190508181035f830152610d4f81610d16565b9050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f610d8d82610a65565b9150610d9883610a65565b9250828202610da681610a65565b91508282048414831517610dbd57610dbc610d56565b5b5092915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601260045260245ffd5b5f610dfb82610a65565b9150610e0683610a65565b925082610e1657610e15610dc4565b5b828204905092915050565b5f604082019050610e345f830185610ac3565b610e416020830184610ac3565b9392505050565b5f81519050610e5681610a6e565b92915050565b5f60208284031215610e7157610e70610929565b5b5f610e7e84828501610e48565b9150509291505056fea264697066735822122089bdb4c6d819456074d201387d3a48558cb56405b90078b4ce2fc22eb226f6e264736f6c634300081f0033";

    public static final String ABI_JSON = "[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"beneficiary\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_projectParty\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_spaceJediAddress\",\"type\":\"address\"}],\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"OwnableInvalidOwner\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"}],\"name\":\"OwnableUnauthorizedAccount\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"token\",\"type\":\"address\"}],\"name\":\"SafeERC20FailedOperation\",\"type\":\"error\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"times\",\"type\":\"uint256\"}],\"name\":\"BuyGameTimes\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"previousOwner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"OwnershipTransferred\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"times\",\"type\":\"uint256\"}],\"name\":\"buyGameTimes\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getPoolBalance\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"renounceOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address[]\",\"name\":\"userAddresses\",\"type\":\"address[]\"},{\"internalType\":\"uint256[]\",\"name\":\"amounts\",\"type\":\"uint256[]\"}],\"name\":\"reward\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"sjTokenUnit\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"spaceJediToken\",\"outputs\":[{\"internalType\":\"contract IERC20Metadata\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"transferOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";

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

    public RemoteFunctionCall<TransactionReceipt> reward(List<String> userAddresses, List<BigInteger> amounts) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REWARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(userAddresses, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(amounts, org.web3j.abi.datatypes.generated.Uint256.class))),
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
