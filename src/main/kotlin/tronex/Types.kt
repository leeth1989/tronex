package tronex

import org.tron.protos.Contract
import org.tron.protos.Protocol
import java.util.*

typealias ContractType = Protocol.Transaction.Contract.ContractType

enum class AccountType {
    Normal,
    AssetIssue,
    Contract,
}

data class Vote(val voteAddress: String,
                val voteCount: String)

data class Frozen(val frozenBalance: String,
                  val expireTime: String)

data class Account(val accountName: String,
                   val accountType: String,
                   val address: String,
                   val balance: String,
                   val votes: List<Vote>,
                   var asset: Map<String, Long>,
                   val frozen: List<Frozen>,
                   val netUsage: String,
                   val createTime: String,
                   val latestOperationTime: String,

                   val allowance: Long,
                   val latestWithdrawTime: String,
                   val isWitness: Boolean,
                   val isCommittee: Boolean
                   )

data class Header(val height: Long,
                  val time: String,
                  val parentID: String,
                  val witness: String,
                  val size: Int,
                  val txTrieRoot: String,
                  val sig: String,
                  val txCount: Int)



data class Contract(val type: Int,
                    val provider: String,
                    val name: String)

data class Result(val fee: Long,
                  val ret: Int)




data class Transaction(val contractList: List<Any?>)


data class Block(val header: Header,
                 val txs: List<Transaction>,
                 val hash: String,
                 val id: String)

data class Witness(val address: String,
                   val voteCount: String,
                   val pubKey: String,
                   val url: String,
                   val totalProduced: String,
                   val totalMissed: String,
                   val latestBlockNum: String,
                   val latestSlotNum: String,
                   val isJobs: Boolean)

data class AccountCreateContract(val accountType: String,
                                 val accountName: String,
                                 val ownerAddress: String,
                                 val contractType: String = ContractType.AccountCreateContract.name)

data class TransferContract(val from: String,
                            val to: String,
                            val amount: String,
                            val contractType: String = ContractType.TransferContract.name
)

data class TransferAssetContract(val assetName: String,
                                 val ownerAddress: String,
                                 val toAddress: String,
                                 val amount: String,
                                 val contractType:String = ContractType.TransferAssetContract.name)

data class VoteAssetContract(val ownerAddress: String,
                             val voteAddress: List<String>,
                             val support: Boolean,
                             val count: String,
                             val contractType: String = ContractType.VoteAssetContract.name)

data class VoteWitnessContract(val ownerAddress: String,
                               val support: Boolean,
                               val votes: List<Vote>,
                               val contractType: String = ContractType.VoteWitnessContract.name)

data class WitnessCreateContract(val ownerAddress: String,
                                 val url: String,
                                 val contractType: String = ContractType.WitnessCreateContract.name)

data class AssetIssueContract(val contractType: String = ContractType.AssetIssueContract.name)

data class DeployContract(val ownerAddress: String,
                          val script: String,
                          val contractType: String = ContractType.DeployContract.name)

data class WitnessUpdateContract(val ownerAddress: String,
                                 val updateUrl: String,
                                 val contractType: String = ContractType.WitnessUpdateContract.name)

data class ParticipateAssetIssueContract(val ownerAddress: String,
                                         val toAddress: String,
                                         val assetName: String,
                                         val amount: String,
                                         val contractType: String = ContractType.ParticipateAssetIssueContract.name)

data class AccountUpdateContract(val accountName: String,
                                 val ownerAddress: String,
                                 val contractType: String = ContractType.AccountUpdateContract.name)

data class FreezeBalanceContract(val ownerAddress: String,
                                 val frozenBalance: String,
                                 val frozenDuration: String)

data class UnfreezeBalanceContract(val ownerAddress: String,
                                   val contractType: String = ContractType.UnfreezeBalanceContract.name)

data class WithdrawBalanceContract(val ownerAddress: String,
                                   val contractType: String = ContractType.WithdrawBalanceContract.name)

data class UnfreezeAssetContract(val ownerAddress: String,
                                 val contractType: String = ContractType.UnfreezeAssetContract.name)

data class UpdateAssetContract(val ownerAddress: String,
                               val description: String,
                               val url: String,
                               val newLimit: String,
                               val newPublicLimit: String,
                               val contractType: String = ContractType.UpdateAssetContract.name)

