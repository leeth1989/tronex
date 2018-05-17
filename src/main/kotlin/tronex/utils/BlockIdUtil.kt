package tronex.utils

import com.google.common.primitives.Longs

fun generateBlockId(blockNum: Long, blockHash: ByteArray): ByteArray {
    val blockHashCp = blockHash.copyOf()
    val numBytes = Longs.toByteArray(blockNum)
    System.arraycopy(numBytes, 0, blockHashCp, 0, 8)
    return blockHashCp
}