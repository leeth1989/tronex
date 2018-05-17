package tronex

import java.util.*

enum class AccountType {
    Normal,
    AssetIssue,
    Contract,
}

data class Vote(val voteAddress: String,
                val voteCount: Long)

data class Frozen(val frozenBalance: Long,
                  val expireTime: Long)


data class Account(val accountName: String,
                   val accountType: String,
                   val address: String,
                   val balance: Long,
                   val votes: List<Vote>,
                   val asset: Map<String, Long>
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


data class TransferContract(val from: String,
                            val to: String,
                            val amount: String,
                            val contractType: String)

data class Transaction(val contractList: List<Any?>)


data class Block(val header: Header,
                 val txs: List<Transaction>,
                 val hash: String,
                 val id: String)