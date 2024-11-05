package tcbv.zhaohui.moon.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
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
public class MoonBase extends Contract {
    public static final String BINARY = "6080604052600280546001600160a81b03191674dac17f958d2ee523a2206206994597c13d831ec7011790556004805463ffffffff60a01b19166309c4007d60a21b17905534801561004f575f80fd5b50338061007557604051631e4fbdf760e01b81525f600482015260240160405180910390fd5b61007e81610130565b506060805161009290600590608090610211565b50600380546001600160a01b031990811673a841b274de2b521c10e77643071206004bbf8c0717909155600480549091167357ae92e2611887ef4c8ab58c45e74a16fbc8329e1790556100f25f805160206123f58339815191523361017f565b506100fd5f3361017f565b506101295f805160206123f583398151915273171d1460a0b3a91b8438f6f776a8cd8a34d49dd461017f565b5050610288565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b5f8281526001602090815260408083206001600160a01b038516845290915281205460ff16610208575f8381526001602081815260408084206001600160a01b0387168086529252808420805460ff19169093179092559051339286917f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d9190a450600161020b565b505f5b92915050565b828054828255905f5260205f20908101928215610264579160200282015b8281111561026457825182546001600160a01b0319166001600160a01b0390911617825560209092019160019091019061022f565b50610270929150610274565b5090565b5b80821115610270575f8155600101610275565b612160806102955f395ff3fe608060405234801561000f575f80fd5b5060043610610208575f3560e01c806373f885131161011f578063a217fddf116100a9578063ea6c002311610079578063ea6c002314610517578063f2fde38b1461052a578063f5b541a61461053d578063fbfa77cf14610564578063fcb53d3614610577575f80fd5b8063a217fddf146104d0578063c486456c146104d7578063c6255626146104f1578063d547741f14610504575f80fd5b8063819aa007116100ef578063819aa0071461047957806389476069146104915780638da5cb5b146104a4578063900407bc146104b457806391d14854146104bd575f80fd5b806373f88513146103f9578063763191901461040c5780637b2b5f671461041f5780637c01800d1461043f575f80fd5b80633a40481d116101a05780635c413fa8116101705780635c413fa81461037957806361c3efb1146103a35780636817031b146103cb5780636d69fcaf146103de578063715018a6146103f1575f80fd5b80633a40481d146103105780633ccfd60b1461034b5780634eeea2f21461035357806356de96db14610366575f80fd5b80632f2ff15d116101db5780632f2ff15d146102b057806334461067146102c557806336568abe146102ea57806337d629a6146102fd575f80fd5b806301ffc9a71461020c57806303e9e60914610234578063248a9ca31461025457806329728c6e14610285575b5f80fd5b61021f61021a366004611abf565b61058c565b60405190151581526020015b60405180910390f35b610247610242366004611aed565b6105c2565b60405161022b9190611b32565b610277610262366004611aed565b5f908152600160208190526040909120015490565b60405190815260200161022b565b600454610298906001600160a01b031681565b6040516001600160a01b03909116815260200161022b565b6102c36102be366004611bb1565b6106fb565b005b6102d86102d3366004611aed565b610726565b60405161022b96959493929190611bdb565b6102c36102f8366004611bb1565b6107f0565b6102c361030b366004611c27565b610828565b61033661031e366004611aed565b600a6020525f90815260409020805460029091015482565b6040805192835260208301919091520161022b565b6102c3610b03565b6102c3610361366004611caa565b610bb4565b6102c3610374366004611cc3565b610be4565b610277610387366004611ce1565b600960209081525f928352604080842090915290825290205481565b6004546103b890600160b01b900461ffff1681565b60405161ffff909116815260200161022b565b6102c36103d9366004611caa565b610c12565b6102c36103ec366004611caa565b610c3c565b6102c3610c95565b6102c3610407366004611d1e565b610ca8565b6102c361041a366004611caa565b610ce0565b61043261042d366004611bb1565b610ddd565b60405161022b9190611d4e565b61046461044d366004611caa565b60086020525f908152604090205463ffffffff1681565b60405163ffffffff909116815260200161022b565b6002546102989061010090046001600160a01b031681565b6102c361049f366004611caa565b610ea4565b5f546001600160a01b0316610298565b61027760075481565b61021f6104cb366004611bb1565b611037565b6102775f81565b6002546104e49060ff1681565b60405161022b9190611dd2565b6102986104ff366004611aed565b611061565b6102c3610512366004611bb1565b611089565b6102c3610525366004611df8565b6110ae565b6102c3610538366004611caa565b611426565b6102777f97667070c54ef182b0f5858b034beac1b6f3089aa2d3188bb1e8929f4fa9b92981565b600354610298906001600160a01b031681565b6004546103b890600160a01b900461ffff1681565b5f6001600160e01b03198216637965db0b60e01b14806105bc57506301ffc9a760e01b6001600160e01b03198316145b92915050565b6106086040518060c001604052805f6001600160a01b031681526020015f81526020015f81526020015f81526020015f6001600160a01b03168152602001606081525090565b5f82815260066020908152604091829020825160c08101845281546001600160a01b039081168252600183015493820193909352600282015493810193909352600381015460608401526004810154909116608083015260058101805460a08401919061067490611e89565b80601f01602080910402602001604051908101604052809291908181526020018280546106a090611e89565b80156106eb5780601f106106c2576101008083540402835291602001916106eb565b820191905f5260205f20905b8154815290600101906020018083116106ce57829003601f168201915b5050505050815250509050919050565b5f828152600160208190526040909120015461071681611463565b610720838361146d565b50505050565b60066020525f90815260409020805460018201546002830154600384015460048501546005860180546001600160a01b0396871697959694959394909216929161076f90611e89565b80601f016020809104026020016040519081016040528092919081815260200182805461079b90611e89565b80156107e65780601f106107bd576101008083540402835291602001916107e6565b820191905f5260205f20905b8154815290600101906020018083116107c957829003601f168201915b5050505050905086565b6001600160a01b03811633146108195760405163334bd91960e11b815260040160405180910390fd5b61082382826114e3565b505050565b835f805b60055481101561087d57826001600160a01b03166005828154811061085357610853611ec1565b5f918252602090912001546001600160a01b031603610875576001915061087d565b60010161082c565b50806108c65760405162461bcd60e51b8152602060048201526013602482015272151bdad95b881b9bdd081cdd5c1c1bdc9d1959606a1b60448201526064015b60405180910390fd5b600160025460ff1660018111156108df576108df611dbe565b146109215760405162461bcd60e51b8152602060048201526012602482015271636f6e7472616374206e6f7420726561647960701b60448201526064016108bd565b5f61092d33888861154e565b90506040518060c00160405280886001600160a01b031681526020018781526020018281526020015f8152602001336001600160a01b0316815260200186868080601f0160208091040260200160405190810160405280939291908181526020018383808284375f920182905250939094525050600754815260066020908152604091829020845181546001600160a01b03199081166001600160a01b03928316178355928601516001830155928501516002820155606085015160038201556080850151600482018054909316931692909217905560a08301519091506005820190610a1a9082611f34565b5050335f9081526008602052604090205463ffffffff169050610a3e816001612003565b335f908152600860209081526040808320805463ffffffff191663ffffffff9590951694909417909355600754600990915291812090610a7f846001612003565b63ffffffff1663ffffffff1681526020019081526020015f2081905550336001600160a01b03167f5212e1a295c6c84c70054418822dc57308834e683568840f8282bd58f1d1913e600754898989604051610add949392919061201f565b60405180910390a260078054905f610af48361205b565b91905055505050505050505050565b610b0b611947565b476001610b1f5f546001600160a01b031690565b6001600160a01b0316826040515f6040518083038185875af1925050503d805f8114610b66576040519150601f19603f3d011682016040523d82523d5f602084013e610b6b565b606091505b50508091505080610bb05760405162461bcd60e51b815260206004820152600f60248201526e1dda5d1a191c985dc819985a5b1959608a1b60448201526064016108bd565b5050565b610bbc611947565b600280546001600160a01b0390921661010002610100600160a81b0319909216919091179055565b610bec611947565b6002805482919060ff191660018381811115610c0a57610c0a611dbe565b021790555050565b610c1a611947565b600380546001600160a01b0319166001600160a01b0392909216919091179055565b610c44611947565b600580546001810182555f919091527f036b6384b5eca791c62761152d0c79bb0604c104a5fb6f4eb0703f3154bb3db00180546001600160a01b0319166001600160a01b0392909216919091179055565b610c9d611947565b610ca65f611973565b565b610cb0611947565b6004805461ffff909216600160a01b026001600160b01b03199092166001600160a01b0390931692909217179055565b610ce8611947565b5f5b600554811015610bb057816001600160a01b031660058281548110610d1157610d11611ec1565b5f918252602090912001546001600160a01b031603610dd55760058054610d3a90600190612073565b81548110610d4a57610d4a611ec1565b5f91825260209091200154600580546001600160a01b039092169183908110610d7557610d75611ec1565b905f5260205f20015f6101000a8154816001600160a01b0302191690836001600160a01b031602179055506005805480610db157610db1612086565b5f8281526020902081015f1990810180546001600160a01b03191690550190555050565b600101610cea565b610dff60405180606001604052805f8152602001606081526020015f81525090565b5f610e0a84846119c2565b9050600a5f8281526020019081526020015f206040518060600160405290815f820154815260200160018201805480602002602001604051908101604052809291908181526020018280548015610e8857602002820191905f5260205f20905b81546001600160a01b03168152600190910190602001808311610e6a575b5050505050815260200160028201548152505091505092915050565b610eac611947565b6040516370a0823160e01b815230600482015281905f906001600160a01b038316906370a0823190602401602060405180830381865afa158015610ef2573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610f16919061209a565b90505f8111610f675760405162461bcd60e51b815260206004820152601a60248201527f6f7574206f662062616c616e636520696e20636f6e747261637400000000000060448201526064016108bd565b5f826001600160a01b031663a9059cbb610f885f546001600160a01b031690565b6040516001600160e01b031960e084901b1681526001600160a01b039091166004820152602481018590526044016020604051808303815f875af1158015610fd2573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190610ff691906120b1565b9050806107205760405162461bcd60e51b815260206004820152600f60248201526e1dda5d1a191c985dc819985a5b1959608a1b60448201526064016108bd565b5f9182526001602090815260408084206001600160a01b0393909316845291905290205460ff1690565b60058181548110611070575f80fd5b5f918252602090912001546001600160a01b0316905081565b5f82815260016020819052604090912001546110a481611463565b61072083836114e3565b6001600160a01b0384166110fc5760405162461bcd60e51b8152602060048201526015602482015274496e76616c696420746f6b656e206164647265737360581b60448201526064016108bd565b806111495760405162461bcd60e51b815260206004820152601760248201527f4e6f20726563697069656e74732073706563696669656400000000000000000060448201526064016108bd565b5f83116111985760405162461bcd60e51b815260206004820181905260248201527f416d6f756e74206d7573742062652067726561746572207468616e207a65726f60448201526064016108bd565b836111a382856120d0565b6040516370a0823160e01b81523060048201526001600160a01b038316906370a0823190602401602060405180830381865afa1580156111e5573d5f803e3d5ffd5b505050506040513d601f19601f82011682018060405250810190611209919061209a565b10156112575760405162461bcd60e51b815260206004820152601d60248201527f496e73756666696369656e7420636f6e74726163742062616c616e636500000060448201526064016108bd565b5f5b8281101561138c575f84848381811061127457611274611ec1565b90506020020160208101906112899190611caa565b6001600160a01b0316036112df5760405162461bcd60e51b815260206004820152601960248201527f496e76616c696420726563697069656e7420616464726573730000000000000060448201526064016108bd565b816001600160a01b031663a9059cbb85858481811061130057611300611ec1565b90506020020160208101906113159190611caa565b6040516001600160e01b031960e084901b1681526001600160a01b039091166004820152602481018890526044016020604051808303815f875af115801561135f573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061138391906120b1565b50600101611259565b505f61139887876119c2565b905060405180606001604052808681526020018585808060200260200160405190810160405280939291908181526020018383602002808284375f92018290525093855250504260209384015250838152600a82526040902082518155828201518051919261140f92600185019290910190611a48565b506040820151816002015590505050505050505050565b61142e611947565b6001600160a01b03811661145757604051631e4fbdf760e01b81525f60048201526024016108bd565b61146081611973565b50565b6114608133611a0f565b5f6114788383611037565b6114dc575f8381526001602081815260408084206001600160a01b0387168086529252808420805460ff19169093179092559051339286917f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d9190a45060016105bc565b505f6105bc565b5f6114ee8383611037565b156114dc575f8381526001602090815260408083206001600160a01b0386168085529252808320805460ff1916905551339286917ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b9190a45060016105bc565b604051636eb1769f60e11b81526001600160a01b0384811660048301523060248301525f91849184919083169063dd62ed3e90604401602060405180830381865afa15801561159f573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906115c3919061209a565b10156116115760405162461bcd60e51b815260206004820152601b60248201527f6861766520746f20617070726f7665207573616765206669727374000000000060448201526064016108bd565b6003545f906001600160a01b03161580159061163757506004546001600160a01b031615155b1561184f5760045461ffff600160b01b820481169161165f91600160a01b90910416866120d0565b61166991906120e7565b90505f6116768286612073565b6002549091506001600160a01b0361010090910481169087160361175d57600480546040516323b872dd60e01b81526001600160a01b03898116936323b872dd936116c9938d9390911691889101612106565b5f604051808303815f87803b1580156116e0575f80fd5b505af11580156116f2573d5f803e3d5ffd5b50506003546040516323b872dd60e01b81526001600160a01b03808b1694506323b872dd935061172b928c929116908690600401612106565b5f604051808303815f87803b158015611742575f80fd5b505af1158015611754573d5f803e3d5ffd5b50505050611849565b600480546040516323b872dd60e01b81526001600160a01b03868116936323b872dd93611792938d9390911691889101612106565b6020604051808303815f875af11580156117ae573d5f803e3d5ffd5b505050506040513d601f19601f820116820180604052508101906117d291906120b1565b506003546040516323b872dd60e01b81526001600160a01b03808616926323b872dd92611807928c9216908690600401612106565b6020604051808303815f875af1158015611823573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061184791906120b1565b505b5061193e565b6002546001600160a01b036101009091048116908616036118cc576040516323b872dd60e01b81526001600160a01b038616906323b872dd9061189a90899030908990600401612106565b5f604051808303815f87803b1580156118b1575f80fd5b505af11580156118c3573d5f803e3d5ffd5b5050505061193e565b6040516323b872dd60e01b81526001600160a01b038316906323b872dd906118fc90899030908990600401612106565b6020604051808303815f875af1158015611918573d5f803e3d5ffd5b505050506040513d601f19601f8201168201806040525081019061193c91906120b1565b505b95945050505050565b5f546001600160a01b03163314610ca65760405163118cdaa760e01b81523360048201526024016108bd565b5f80546001600160a01b038381166001600160a01b0319831681178455604051919092169283917f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e09190a35050565b5f82826040516020016119f192919091825260601b6bffffffffffffffffffffffff1916602082015260340190565b60405160208183030381529060405280519060200120905092915050565b611a198282611037565b610bb05760405163e2517d3f60e01b81526001600160a01b0382166004820152602481018390526044016108bd565b828054828255905f5260205f20908101928215611a9b579160200282015b82811115611a9b57825182546001600160a01b0319166001600160a01b03909116178255602090920191600190910190611a66565b50611aa7929150611aab565b5090565b5b80821115611aa7575f8155600101611aac565b5f60208284031215611acf575f80fd5b81356001600160e01b031981168114611ae6575f80fd5b9392505050565b5f60208284031215611afd575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b6020815260018060a01b03825116602082015260208201516040820152604082015160608201526060820151608082015260018060a01b0360808301511660a08201525f60a083015160c080840152611b8e60e0840182611b04565b949350505050565b80356001600160a01b0381168114611bac575f80fd5b919050565b5f8060408385031215611bc2575f80fd5b82359150611bd260208401611b96565b90509250929050565b6001600160a01b0387811682526020820187905260408201869052606082018590528316608082015260c060a082018190525f90611c1b90830184611b04565b98975050505050505050565b5f805f8060608587031215611c3a575f80fd5b611c4385611b96565b935060208501359250604085013567ffffffffffffffff811115611c65575f80fd5b8501601f81018713611c75575f80fd5b803567ffffffffffffffff811115611c8b575f80fd5b876020828401011115611c9c575f80fd5b949793965060200194505050565b5f60208284031215611cba575f80fd5b611ae682611b96565b5f60208284031215611cd3575f80fd5b813560028110611ae6575f80fd5b5f8060408385031215611cf2575f80fd5b611cfb83611b96565b9150602083013563ffffffff81168114611d13575f80fd5b809150509250929050565b5f8060408385031215611d2f575f80fd5b611d3883611b96565b9150602083013561ffff81168114611d13575f80fd5b602080825282518282015282810151606060408401528051608084018190525f929190910190829060a08501905b80831015611da75783516001600160a01b031682526020938401936001939093019290910190611d7c565b506040860151606086015280935050505092915050565b634e487b7160e01b5f52602160045260245ffd5b6020810160028310611df257634e487b7160e01b5f52602160045260245ffd5b91905290565b5f805f805f60808688031215611e0c575f80fd5b85359450611e1c60208701611b96565b935060408601359250606086013567ffffffffffffffff811115611e3e575f80fd5b8601601f81018813611e4e575f80fd5b803567ffffffffffffffff811115611e64575f80fd5b8860208260051b8401011115611e78575f80fd5b959894975092955050506020019190565b600181811c90821680611e9d57607f821691505b602082108103611ebb57634e487b7160e01b5f52602260045260245ffd5b50919050565b634e487b7160e01b5f52603260045260245ffd5b634e487b7160e01b5f52604160045260245ffd5b601f82111561082357805f5260205f20601f840160051c81016020851015611f0e5750805b601f840160051c820191505b81811015611f2d575f8155600101611f1a565b5050505050565b815167ffffffffffffffff811115611f4e57611f4e611ed5565b611f6281611f5c8454611e89565b84611ee9565b6020601f821160018114611f94575f8315611f7d5750848201515b5f19600385901b1c1916600184901b178455611f2d565b5f84815260208120601f198516915b82811015611fc35787850151825560209485019460019092019101611fa3565b5084821015611fe057868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b634e487b7160e01b5f52601160045260245ffd5b63ffffffff81811683821601908111156105bc576105bc611fef565b84815283602082015260606040820152816060820152818360808301375f818301608090810191909152601f909201601f191601019392505050565b5f6001820161206c5761206c611fef565b5060010190565b818103818111156105bc576105bc611fef565b634e487b7160e01b5f52603160045260245ffd5b5f602082840312156120aa575f80fd5b5051919050565b5f602082840312156120c1575f80fd5b81518015158114611ae6575f80fd5b80820281158282048414176105bc576105bc611fef565b5f8261210157634e487b7160e01b5f52601260045260245ffd5b500490565b6001600160a01b03938416815291909216602082015260408101919091526060019056fea2646970667358221220a20613a88103ab9efd8d3b36baf11438411d6b1ca24c99d873ea5f69cc58673464736f6c634300081a003397667070c54ef182b0f5858b034beac1b6f3089aa2d3188bb1e8929f4fa9b929";

    public static final String FUNC_MAX_PERCENTS = "MAX_PERCENTS";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_BURN = "burn";

    public static final String FUNC_BURNFROM = "burnFrom";

    public static final String FUNC_CURRENTMODE = "currentMode";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_FEEADDRESSES = "feeAddresses";

    public static final String FUNC_FEEPERCENTS = "feePercents";

    public static final String FUNC_FEERATE = "feeRate";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SETFEEDISTRIBUTION = "setFeeDistribution";

    public static final String FUNC_SETFEERATE = "setFeeRate";

    public static final String FUNC_SETMODE = "setMode";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPDATEWHITELIST = "updateWhitelist";

    public static final String FUNC_WHITELIST = "whitelist";

    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event MODECHANGED_EVENT = new Event("ModeChanged",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event WHITELISTUPDATED_EVENT = new Event("WhitelistUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
    ;

    @Deprecated
    protected MoonBase(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MoonBase(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MoonBase(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MoonBase(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
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

    public List<ModeChangedEventResponse> getModeChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(MODECHANGED_EVENT, transactionReceipt);
        ArrayList<ModeChangedEventResponse> responses = new ArrayList<ModeChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ModeChangedEventResponse typedResponse = new ModeChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.mode = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ModeChangedEventResponse> modeChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ModeChangedEventResponse>() {
            @Override
            public ModeChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(MODECHANGED_EVENT, log);
                ModeChangedEventResponse typedResponse = new ModeChangedEventResponse();
                typedResponse.log = log;
                typedResponse.mode = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ModeChangedEventResponse> modeChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MODECHANGED_EVENT));
        return modeChangedEventFlowable(filter);
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

    public List<WhitelistUpdatedEventResponse> getWhitelistUpdatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(WHITELISTUPDATED_EVENT, transactionReceipt);
        ArrayList<WhitelistUpdatedEventResponse> responses = new ArrayList<WhitelistUpdatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            WhitelistUpdatedEventResponse typedResponse = new WhitelistUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.status = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<WhitelistUpdatedEventResponse> whitelistUpdatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, WhitelistUpdatedEventResponse>() {
            @Override
            public WhitelistUpdatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(WHITELISTUPDATED_EVENT, log);
                WhitelistUpdatedEventResponse typedResponse = new WhitelistUpdatedEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.status = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<WhitelistUpdatedEventResponse> whitelistUpdatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(WHITELISTUPDATED_EVENT));
        return whitelistUpdatedEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> MAX_PERCENTS() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_MAX_PERCENTS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteFunctionCall<TransactionReceipt> burn(BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BURN,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> burnFrom(String account, BigInteger value) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BURNFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.generated.Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> currentMode() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CURRENTMODE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> feeAddresses(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FEEADDRESSES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> feePercents(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FEEPERCENTS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> feeRate() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FEERATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFeeDistribution(List<String> _feeAddresses, List<BigInteger> _feePercents) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETFEEDISTRIBUTION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.StaticArray4<org.web3j.abi.datatypes.Address>(
                                org.web3j.abi.datatypes.Address.class,
                                org.web3j.abi.Utils.typeMap(_feeAddresses, org.web3j.abi.datatypes.Address.class)),
                        new org.web3j.abi.datatypes.generated.StaticArray4<org.web3j.abi.datatypes.generated.Uint32>(
                                org.web3j.abi.datatypes.generated.Uint32.class,
                                org.web3j.abi.Utils.typeMap(_feePercents, org.web3j.abi.datatypes.generated.Uint32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFeeRate(BigInteger _feeRate) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETFEERATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint32(_feeRate)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setMode(BigInteger mode) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETMODE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint8(mode)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public RemoteFunctionCall<TransactionReceipt> transfer(String recipient, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(String sender, String recipient, BigInteger amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, sender),
                        new org.web3j.abi.datatypes.Address(160, recipient),
                        new org.web3j.abi.datatypes.generated.Uint256(amount)),
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

    public RemoteFunctionCall<TransactionReceipt> updateWhitelist(String account, Boolean status) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_UPDATEWHITELIST,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, account),
                        new org.web3j.abi.datatypes.Bool(status)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> whitelist(String param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_WHITELIST,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static MoonBase load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MoonBase(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MoonBase load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MoonBase(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MoonBase load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MoonBase(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MoonBase load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MoonBase(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MoonBase> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MoonBase.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<MoonBase> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MoonBase.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MoonBase> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MoonBase.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<MoonBase> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MoonBase.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public String owner;

        public String spender;

        public BigInteger value;
    }

    public static class ModeChangedEventResponse extends BaseEventResponse {
        public BigInteger mode;
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

    public static class WhitelistUpdatedEventResponse extends BaseEventResponse {
        public String account;

        public Boolean status;
    }
}
