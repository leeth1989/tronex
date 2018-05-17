import org.tron.api.GrpcAPI
import tronex.services.TronClientKT
import tronex.utils.Base58
import tronex.utils.hex

fun main(args: Array<String>) {
    val client = TronClientKT()
    val stub = client.getWalletStub()

    val block = stub.getBlockByNum(GrpcAPI.NumberMessage.newBuilder().setNum(0).build())
    val addr = "27PpjEMkPg2aSS2DrqkbE8rY8oKABkCgwZk"

    val bs = Base58.decode58Check(addr)
    println(hex(bs))

    val b5858 = Base58.encode58Check(bs)

    println(b5858)

}