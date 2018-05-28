package tronex.utils

import com.alibaba.fastjson.JSON
import org.apache.commons.lang3.time.DateFormatUtils
import org.tron.protos.Protocol
import tronex.Account
import tronex.Frozen
import tronex.Vote

typealias PBAccount = Protocol.Account
typealias PBVote = Protocol.Vote
typealias PBFrozen = Protocol.Account.Frozen

fun dateFormat(timeMilli: Long): String = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(timeMilli)

fun convertPBAccount2View(pbAccount: PBAccount, convertAssetMap: Boolean = true): Account {

    val voteList = pbAccount.votesList.map { convertVote2View(it) }

    val frozen = pbAccount.frozenList.map { convertFrozen(it) }

    return Account(accountName =  String(pbAccount.accountName.toByteArray()),
            address = base58check(pbAccount.address.toByteArray()),
            balance = pbAccount.balance.toString(),
            asset = if (convertAssetMap) convertAssetMap( pbAccount.assetMap ) else pbAccount.assetMap,
            accountType = pbAccount.type.name,
            votes = voteList,
            netUsage = pbAccount.netUsage.toString(),
            createTime = dateFormat(pbAccount.createTime),
            latestOperationTime = dateFormat(pbAccount.latestOprationTime),
            allowance = pbAccount.allowance,
            latestWithdrawTime = dateFormat(pbAccount.latestWithdrawTime),
            isCommittee = pbAccount.isCommittee,
            isWitness = pbAccount.isWitness,
            frozen = frozen)
}

fun convertFrozen(pbFrozen: PBFrozen): Frozen {
    return Frozen(frozenBalance = pbFrozen.frozenBalance.toString(),
            expireTime = dateFormat(pbFrozen.expireTime))
}

fun convertAssetMap(assetMap: Map<String, Long>): Map<String, Long> {
    return assetMap.mapKeys { hex(it.key.toByteArray()) }.toMap()
}

fun convertVote2View(vote: PBVote): Vote {
    return Vote(voteAddress = base58check(vote.voteAddress.toByteArray()),
            voteCount = vote.voteCount.toString())
}
