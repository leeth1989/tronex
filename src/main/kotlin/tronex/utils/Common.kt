package tronex.utils

import javax.xml.bind.DatatypeConverter

fun hex(input: ByteArray): String = DatatypeConverter.printHexBinary(input)
fun fromHex(input: String): ByteArray = DatatypeConverter.parseHexBinary(input)
fun base58check(input: ByteArray): String = Base58.encode58Check(input)