package tronex.utils

import org.tron.protos.Protocol
import tronex.Account
import tronex.Vote

typealias PBAccount = Protocol.Account
typealias PBVote = Protocol.Account.Vote

fun convertPBAccount2View(pbAccount: PBAccount): Account {

    val voteList = pbAccount.votesList.map { convertVote2View(it) }

    return Account(accountName =  String(pbAccount.accountName.toByteArray()),
            address = base58check(pbAccount.address.toByteArray()),
            balance = pbAccount.balance,
            asset = pbAccount.assetMap,
            accountType = pbAccount.type.name,
            votes = voteList)
}

fun convertVote2View(vote: PBVote): Vote {
    return Vote(voteAddress = base58check(vote.voteAddress.toByteArray()),
            voteCount = vote.voteCount)
}
