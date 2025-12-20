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
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class Token20Contract extends Contract {
    public static final String BINARY = "6080604052604051620024bd380380620024bd8339818101604052810190620000299190620006c0565b60016040518060400160405280600481526020017f54415447000000000000000000000000000000000000000000000000000000008152506040518060400160405280600481526020017f54415447000000000000000000000000000000000000000000000000000000008152508160039081620000a89190620009a2565b508060049081620000ba9190620009a2565b505050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1603620001325760006040517f1e4fbdf700000000000000000000000000000000000000000000000000000000815260040162000129919062000a9a565b60405180910390fd5b62000143816200024a60201b60201c565b5083600660006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600660146101000a81548167ffffffffffffffff021916908367ffffffffffffffff16021790555081600760006101000a81548167ffffffffffffffff021916908367ffffffffffffffff1602179055508060088190555062000240600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16620002156200031060201b60201c565b600a62000223919062000c47565b6305f5e10062000234919062000c98565b6200031960201b60201c565b5050505062000d89565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b60006012905090565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16036200038e5760006040517fec442f0500000000000000000000000000000000000000000000000000000000815260040162000385919062000a9a565b60405180910390fd5b620003a260008383620003a660201b60201c565b5050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603620003fc578060026000828254620003ef919062000ce3565b92505081905550620004d2565b60008060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050818110156200048b578381836040517fe450d38c000000000000000000000000000000000000000000000000000000008152600401620004829392919062000d2f565b60405180910390fd5b8181036000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550505b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16036200051d57806002600082825403925050819055506200056a565b806000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055505b8173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef83604051620005c9919062000d6c565b60405180910390a3505050565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006200060882620005db565b9050919050565b6200061a81620005fb565b81146200062657600080fd5b50565b6000815190506200063a816200060f565b92915050565b600067ffffffffffffffff82169050919050565b6200065f8162000640565b81146200066b57600080fd5b50565b6000815190506200067f8162000654565b92915050565b6000819050919050565b6200069a8162000685565b8114620006a657600080fd5b50565b600081519050620006ba816200068f565b92915050565b60008060008060808587031215620006dd57620006dc620005d6565b5b6000620006ed8782880162000629565b945050602062000700878288016200066e565b935050604062000713878288016200066e565b92505060606200072687828801620006a9565b91505092959194509250565b600081519050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b60006002820490506001821680620007b457607f821691505b602082108103620007ca57620007c96200076c565b5b50919050565b60008190508160005260206000209050919050565b60006020601f8301049050919050565b600082821b905092915050565b600060088302620008347fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82620007f5565b620008408683620007f5565b95508019841693508086168417925050509392505050565b6000819050919050565b6000620008836200087d620008778462000685565b62000858565b62000685565b9050919050565b6000819050919050565b6200089f8362000862565b620008b7620008ae826200088a565b84845462000802565b825550505050565b600090565b620008ce620008bf565b620008db81848462000894565b505050565b5b818110156200090357620008f7600082620008c4565b600181019050620008e1565b5050565b601f82111562000952576200091c81620007d0565b6200092784620007e5565b8101602085101562000937578190505b6200094f6200094685620007e5565b830182620008e0565b50505b505050565b600082821c905092915050565b6000620009776000198460080262000957565b1980831691505092915050565b600062000992838362000964565b9150826002028217905092915050565b620009ad8262000732565b67ffffffffffffffff811115620009c957620009c86200073d565b5b620009d582546200079b565b620009e282828562000907565b600060209050601f83116001811462000a1a576000841562000a05578287015190505b62000a11858262000984565b86555062000a81565b601f19841662000a2a86620007d0565b60005b8281101562000a545784890151825560018201915060208501945060208101905062000a2d565b8683101562000a74578489015162000a70601f89168262000964565b8355505b6001600288020188555050505b505050505050565b62000a9481620005fb565b82525050565b600060208201905062000ab1600083018462000a89565b92915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60008160011c9050919050565b6000808291508390505b600185111562000b455780860481111562000b1d5762000b1c62000ab7565b5b600185161562000b2d5780820291505b808102905062000b3d8562000ae6565b945062000afd565b94509492505050565b60008262000b60576001905062000c33565b8162000b70576000905062000c33565b816001811462000b89576002811462000b945762000bca565b600191505062000c33565b60ff84111562000ba95762000ba862000ab7565b5b8360020a91508482111562000bc35762000bc262000ab7565b5b5062000c33565b5060208310610133831016604e8410600b841016171562000c045782820a90508381111562000bfe5762000bfd62000ab7565b5b62000c33565b62000c13848484600162000af3565b9250905081840481111562000c2d5762000c2c62000ab7565b5b81810290505b9392505050565b600060ff82169050919050565b600062000c548262000685565b915062000c618362000c3a565b925062000c907fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff848462000b4e565b905092915050565b600062000ca58262000685565b915062000cb28362000685565b925082820262000cc28162000685565b9150828204841483151762000cdc5762000cdb62000ab7565b5b5092915050565b600062000cf08262000685565b915062000cfd8362000685565b925082820190508082111562000d185762000d1762000ab7565b5b92915050565b62000d298162000685565b82525050565b600060608201905062000d46600083018662000a89565b62000d55602083018562000d1e565b62000d64604083018462000d1e565b949350505050565b600060208201905062000d83600083018462000d1e565b92915050565b6117248062000d996000396000f3fe608060405234801561001057600080fd5b50600436106101005760003560e01c8063715018a611610097578063a9059cbb11610066578063a9059cbb1461028f578063b91d4001146102bf578063dd62ed3e146102dd578063f2fde38b1461030d57610100565b8063715018a61461022b5780638402b2a1146102355780638da5cb5b1461025357806395d89b411461027157610100565b806323b872dd116100d357806323b872dd1461018f578063313ce567146101bf5780636825e4d2146101dd57806370a08231146101fb57610100565b8063045544431461010557806306fdde0314610123578063095ea7b31461014157806318160ddd14610171575b600080fd5b61010d610329565b60405161011a9190611151565b60405180910390f35b61012b610343565b60405161013891906111fc565b60405180910390f35b61015b600480360381019061015691906112b7565b6103d5565b6040516101689190611312565b60405180910390f35b6101796103f8565b604051610186919061133c565b60405180910390f35b6101a960048036038101906101a49190611357565b610402565b6040516101b69190611312565b60405180910390f35b6101c76105b6565b6040516101d491906113c6565b60405180910390f35b6101e56105bf565b6040516101f29190611151565b60405180910390f35b610215600480360381019061021091906113e1565b6105d9565b604051610222919061133c565b60405180910390f35b610233610621565b005b61023d610635565b60405161024a919061133c565b60405180910390f35b61025b61063b565b604051610268919061141d565b60405180910390f35b610279610665565b60405161028691906111fc565b60405180910390f35b6102a960048036038101906102a491906112b7565b6106f7565b6040516102b69190611312565b60405180910390f35b6102c761089f565b6040516102d49190611151565b60405180910390f35b6102f760048036038101906102f29190611438565b6108de565b604051610304919061133c565b60405180910390f35b610327600480360381019061032291906113e1565b610965565b005b600760009054906101000a900467ffffffffffffffff1681565b606060038054610352906114a7565b80601f016020809104026020016040519081016040528092919081815260200182805461037e906114a7565b80156103cb5780601f106103a0576101008083540402835291602001916103cb565b820191906000526020600020905b8154815290600101906020018083116103ae57829003601f168201915b5050505050905090565b6000806103e06109eb565b90506103ed8185856109f3565b600191505092915050565b6000600254905090565b600080429050600660149054906101000a900467ffffffffffffffff1667ffffffffffffffff168167ffffffffffffffff161015806104455750610444610a05565b5b610484576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161047b9061154a565b60405180910390fd5b60003073ffffffffffffffffffffffffffffffffffffffff166370a08231866040518263ffffffff1660e01b81526004016104bf919061141d565b602060405180830381865afa1580156104dc573d6000803e3d6000fd5b505050506040513d601f19601f82011682018060405250810190610500919061157f565b905061050a61089f565b67ffffffffffffffff168267ffffffffffffffff161015806105385750600854848261053691906115db565b105b806105475750610546610a05565b5b610586576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161057d9061165b565b60405180910390fd5b60006105906109eb565b905061059d878287610a5d565b6105a8878787610af1565b600193505050509392505050565b60006012905090565b600660149054906101000a900467ffffffffffffffff1681565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b610629610be5565b6106336000610c6c565b565b60085481565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b606060048054610674906114a7565b80601f01602080910402602001604051908101604052809291908181526020018280546106a0906114a7565b80156106ed5780601f106106c2576101008083540402835291602001916106ed565b820191906000526020600020905b8154815290600101906020018083116106d057829003601f168201915b5050505050905090565b600080429050600660149054906101000a900467ffffffffffffffff1667ffffffffffffffff168167ffffffffffffffff1610158061073a5750610739610a05565b5b610779576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016107709061154a565b60405180910390fd5b60003073ffffffffffffffffffffffffffffffffffffffff166370a08231866040518263ffffffff1660e01b81526004016107b4919061141d565b602060405180830381865afa1580156107d1573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052508101906107f5919061157f565b90506107ff61089f565b67ffffffffffffffff168267ffffffffffffffff1610158061082d5750600854848261082b91906115db565b105b8061083c575061083b610a05565b5b61087b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016108729061165b565b60405180910390fd5b60006108856109eb565b9050610892818787610af1565b6001935050505092915050565b6000600760009054906101000a900467ffffffffffffffff16600660149054906101000a900467ffffffffffffffff166108d9919061167b565b905090565b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b61096d610be5565b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16036109df5760006040517f1e4fbdf70000000000000000000000000000000000000000000000000000000081526004016109d6919061141d565b60405180910390fd5b6109e881610c6c565b50565b600033905090565b610a008383836001610d32565b505050565b6000600660009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163273ffffffffffffffffffffffffffffffffffffffff1614905090565b6000610a6984846108de565b90507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8114610aeb5781811015610adb578281836040517ffb8f41b2000000000000000000000000000000000000000000000000000000008152600401610ad2939291906116b7565b60405180910390fd5b610aea84848484036000610d32565b5b50505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603610b635760006040517f96c6fd1e000000000000000000000000000000000000000000000000000000008152600401610b5a919061141d565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1603610bd55760006040517fec442f05000000000000000000000000000000000000000000000000000000008152600401610bcc919061141d565b60405180910390fd5b610be0838383610f09565b505050565b610bed6109eb565b73ffffffffffffffffffffffffffffffffffffffff16610c0b61063b565b73ffffffffffffffffffffffffffffffffffffffff1614610c6a57610c2e6109eb565b6040517f118cdaa7000000000000000000000000000000000000000000000000000000008152600401610c61919061141d565b60405180910390fd5b565b6000600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905081600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508173ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a35050565b600073ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1603610da45760006040517fe602df05000000000000000000000000000000000000000000000000000000008152600401610d9b919061141d565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603610e165760006040517f94280d62000000000000000000000000000000000000000000000000000000008152600401610e0d919061141d565b60405180910390fd5b81600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508015610f03578273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b92584604051610efa919061133c565b60405180910390a35b50505050565b600073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1603610f5b578060026000828254610f4f91906115db565b9250508190555061102e565b60008060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905081811015610fe7578381836040517fe450d38c000000000000000000000000000000000000000000000000000000008152600401610fde939291906116b7565b60405180910390fd5b8181036000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550505b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff160361107757806002600082825403925050819055506110c4565b806000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055505b8173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef83604051611121919061133c565b60405180910390a3505050565b600067ffffffffffffffff82169050919050565b61114b8161112e565b82525050565b60006020820190506111666000830184611142565b92915050565b600081519050919050565b600082825260208201905092915050565b60005b838110156111a657808201518184015260208101905061118b565b60008484015250505050565b6000601f19601f8301169050919050565b60006111ce8261116c565b6111d88185611177565b93506111e8818560208601611188565b6111f1816111b2565b840191505092915050565b6000602082019050818103600083015261121681846111c3565b905092915050565b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061124e82611223565b9050919050565b61125e81611243565b811461126957600080fd5b50565b60008135905061127b81611255565b92915050565b6000819050919050565b61129481611281565b811461129f57600080fd5b50565b6000813590506112b18161128b565b92915050565b600080604083850312156112ce576112cd61121e565b5b60006112dc8582860161126c565b92505060206112ed858286016112a2565b9150509250929050565b60008115159050919050565b61130c816112f7565b82525050565b60006020820190506113276000830184611303565b92915050565b61133681611281565b82525050565b6000602082019050611351600083018461132d565b92915050565b6000806000606084860312156113705761136f61121e565b5b600061137e8682870161126c565b935050602061138f8682870161126c565b92505060406113a0868287016112a2565b9150509250925092565b600060ff82169050919050565b6113c0816113aa565b82525050565b60006020820190506113db60008301846113b7565b92915050565b6000602082840312156113f7576113f661121e565b5b60006114058482850161126c565b91505092915050565b61141781611243565b82525050565b6000602082019050611432600083018461140e565b92915050565b6000806040838503121561144f5761144e61121e565b5b600061145d8582860161126c565b925050602061146e8582860161126c565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b600060028204905060018216806114bf57607f821691505b6020821081036114d2576114d1611478565b5b50919050565b7f43757272656e742074696d6520646f6573206e6f7420616c6c6f77206f72646960008201527f6e61727920757365727320746f20747261646500000000000000000000000000602082015250565b6000611534603383611177565b915061153f826114d8565b604082019050919050565b6000602082019050818103600083015261156381611527565b9050919050565b6000815190506115798161128b565b92915050565b6000602082840312156115955761159461121e565b5b60006115a38482850161156a565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006115e682611281565b91506115f183611281565b9250828201905080821115611609576116086115ac565b5b92915050565b7f52657374726963742074726164696e672074696d650000000000000000000000600082015250565b6000611645601583611177565b91506116508261160f565b602082019050919050565b6000602082019050818103600083015261167481611638565b9050919050565b60006116868261112e565b91506116918361112e565b9250828201905067ffffffffffffffff8111156116b1576116b06115ac565b5b92915050565b60006060820190506116cc600083018661140e565b6116d9602083018561132d565b6116e6604083018461132d565b94935050505056fea2646970667358221220e43d4e7ec0566e07d8db174aad69329509c8d1007ebdb699f4b242a324c0ece564736f6c63430008110033\r\n";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_LARGEAMOUNT = "largeAmount";

    public static final String FUNC_LOCKDURATION = "lockDuration";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RELEASETIME = "releaseTime";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_STARTTXTIME = "startTxTime";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Token20Contract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Token20Contract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Token20Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Token20Contract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
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

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> allowance(String owner, String spender) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner),
                        new org.web3j.abi.datatypes.Address(160, spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(String spender, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, spender),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> largeAmount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LARGEAMOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> lockDuration() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_LOCKDURATION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> name() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> releaseTime() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_RELEASETIME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> startTxTime() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STARTTXTIME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint64>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, from),
                        new org.web3j.abi.datatypes.Address(160, to),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Token20Contract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Token20Contract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Token20Contract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Token20Contract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Token20Contract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Token20Contract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Token20Contract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Token20Contract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Token20Contract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _initialOwner, BigInteger _startTxTime, BigInteger _lockDuration, BigInteger _largeAmount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _initialOwner),
                new org.web3j.abi.datatypes.generated.Uint64(_startTxTime),
                new org.web3j.abi.datatypes.generated.Uint64(_lockDuration),
                new org.web3j.abi.datatypes.generated.Uint256(_largeAmount)));
        return deployRemoteCall(Token20Contract.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Token20Contract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _initialOwner, BigInteger _startTxTime, BigInteger _lockDuration, BigInteger _largeAmount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _initialOwner),
                new org.web3j.abi.datatypes.generated.Uint64(_startTxTime),
                new org.web3j.abi.datatypes.generated.Uint64(_lockDuration),
                new org.web3j.abi.datatypes.generated.Uint256(_largeAmount)));
        return deployRemoteCall(Token20Contract.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Token20Contract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _initialOwner, BigInteger _startTxTime, BigInteger _lockDuration, BigInteger _largeAmount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _initialOwner),
                new org.web3j.abi.datatypes.generated.Uint64(_startTxTime),
                new org.web3j.abi.datatypes.generated.Uint64(_lockDuration),
                new org.web3j.abi.datatypes.generated.Uint256(_largeAmount)));
        return deployRemoteCall(Token20Contract.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Token20Contract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _initialOwner, BigInteger _startTxTime, BigInteger _lockDuration, BigInteger _largeAmount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _initialOwner),
                new org.web3j.abi.datatypes.generated.Uint64(_startTxTime),
                new org.web3j.abi.datatypes.generated.Uint64(_lockDuration),
                new org.web3j.abi.datatypes.generated.Uint256(_largeAmount)));
        return deployRemoteCall(Token20Contract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public String from;

        public String to;

        public BigInteger value;
    }
}
