package tronex.utils

import com.alibaba.fastjson.JSON
import org.apache.commons.lang3.time.DateFormatUtils
import org.tron.protos.Protocol
import tronex.Account
import tronex.Vote

typealias PBAccount = Protocol.Account
typealias PBVote = Protocol.Account.Vote

fun convertPBAccount2View(pbAccount: PBAccount, convertAssetMap: Boolean = true): Account {

    val voteList = pbAccount.votesList.map { convertVote2View(it) }

    return Account(accountName =  String(pbAccount.accountName.toByteArray()),
            address = base58check(pbAccount.address.toByteArray()),
            balance = pbAccount.balance.toString(),
            asset = if (convertAssetMap) convertAssetMap( pbAccount.assetMap ) else pbAccount.assetMap,
            accountType = pbAccount.type.name,
            votes = voteList,
            bandwidth = pbAccount.bandwidth.toString(),
            createTime = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(pbAccount.createTime),
            latestOperationTime = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(pbAccount.latestOprationTime),
            allowance = pbAccount.allowance,
            latestWithdrawTime = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(pbAccount.latestWithdrawTime))
}

fun convertAssetMap(assetMap: Map<String, Long>): Map<String, Long> {
    return assetMap.mapKeys { hex(it.key.toByteArray()) }.toMap()
}

fun convertVote2View(vote: PBVote): Vote {
    return Vote(voteAddress = base58check(vote.voteAddress.toByteArray()),
            voteCount = vote.voteCount)
}
