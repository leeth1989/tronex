package tronex.utils

import org.tron.protos.Protocol
import tronex.Witness


typealias PBWitness = Protocol.Witness

fun convertPBWitness2View(pbw: PBWitness): Witness {
    return Witness(
            address = base58check(pbw.address.toByteArray()),
            voteCount = pbw.voteCount.toString(),
            pubKey = hex(pbw.pubKey.toByteArray()),
            url = pbw.url,
            totalProduced = pbw.totalProduced.toString(),
            totalMissed =  pbw.totalMissed.toString(),
            latestSlotNum = pbw.latestBlockNum.toString(),
            latestBlockNum = pbw.latestBlockNum.toString(),
            isJobs = pbw.isJobs
    )
}