package tronex.utils

import java.security.MessageDigest

class Hash {

    companion object {
        val sha256Digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        fun sha256(input: ByteArray): ByteArray {
            return sha256Digest.digest(input)
        }
    }



}