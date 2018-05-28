package tronex.utils

import org.apache.commons.lang3.time.DateFormatUtils
import org.tron.protos.Contract
import org.tron.protos.Protocol
import tronex.*
typealias ContractType = Protocol.Transaction.Contract.ContractType

typealias PBBlock = Protocol.Block
typealias PBTransaction = Protocol.Transaction
typealias PBContract = Protocol.Transaction.Contract
typealias PBTransferContract = org.tron.protos.Contract.TransferContract



fun convertPBBlock2View(block: PBBlock): Block {
    val rawHeader = block.blockHeader.rawData

    val header = Header(
            height = rawHeader.number,
            time = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(rawHeader.timestamp),
            parentID = hex(rawHeader.parentHash.toByteArray()),
            witness = base58check(rawHeader.witnessAddress.toByteArray()),
            size = rawHeader.serializedSize,
            txTrieRoot = hex(Hash.sha256(rawHeader.txTrieRoot.toByteArray())),
            sig = hex(block.blockHeader.witnessSignature.toByteArray()),
            txCount = block.transactionsCount)



    val txs = block.transactionsList.map {
        convertPBTransaction2View(it)
    }

    val hash = Hash.sha256(block.blockHeader.toByteArray())
    val id = generateBlockId(rawHeader.number, hash)
    return Block(header, txs, hash=hex(hash), id=hex(id))

}

fun convertPBTransaction2View(transaction: PBTransaction): Transaction {
    val clist = transaction.rawData.contractList.map { convertPBContract2View(it) }
    return Transaction(clist)
}

fun convertPBContract2View(contract: PBContract): Any? {
    return when (contract.typeValue) {
        ContractType.TransferContract_VALUE -> {
            convertData2TransferContract(contract.parameter.value.toByteArray())
        }
        else -> {
            null
        }
    }
}

fun convertData2TransferContract(input: ByteArray): TransferContract {
    val pbtc = PBTransferContract.parseFrom(input)
    return TransferContract(from = base58check(pbtc.ownerAddress.toByteArray()),
            to = base58check(pbtc.toAddress.toByteArray()),
            amount = pbtc.amount.toString(),
            contractType = ContractType.TransferContract.name)
}
//
//fun convertData2AccountCreateContract(input: ByteArray): Contract.AccountCreateContract {
//
//}