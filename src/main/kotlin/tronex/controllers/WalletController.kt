package tronex.controllers

import com.alibaba.fastjson.JSON
import com.google.protobuf.ByteString
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import org.tron.api.GrpcAPI
import org.tron.protos.Protocol
import tronex.BLOCK_ID_REGEX
import tronex.services.TronClientKT
import tronex.utils.Base58
import tronex.utils.convertPBAccount2View
import tronex.utils.convertPBBlock2View
import tronex.utils.fromHex
import java.util.regex.Pattern
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/v0", method = [RequestMethod.GET])
class WalletController {
    @Autowired
    lateinit var tronClient: TronClientKT

    @RequestMapping("/block/{height:[0-9]+}", produces = ["application/json"])
    fun blockByHeight(@PathVariable height: Long, response: HttpServletResponse): String? {

        val stub = tronClient.getWalletStub()
        val pbblock = stub.getBlockByNum(GrpcAPI.NumberMessage.newBuilder().setNum(height).build())
        if (pbblock == null || !pbblock.hasBlockHeader()) {
            response.status = 404
            return null
        }
        val block = convertPBBlock2View(pbblock)

        return JSON.toJSONString(block)
    }

    @RequestMapping("/block/id/{id:${BLOCK_ID_REGEX}}", produces = ["application/json"])
    fun blockById(@PathVariable id: String, response: HttpServletResponse): String? {
        val stub = tronClient.getWalletStub()
        val idBytes = fromHex(id)
        val pbblock = stub.getBlockById(GrpcAPI.BytesMessage.newBuilder().setValue(ByteString.copyFrom(idBytes)).build())
        if (pbblock == null || !pbblock.hasBlockHeader()) {
            response.status = 404
            return null
        }

        val block = convertPBBlock2View(pbblock)

        return JSON.toJSONString(block)
    }

    @RequestMapping("/block/now", produces = ["application/json"])
    fun nowBlock(): String {
        val stub = tronClient.getWalletStub()
        val pbblock = stub.getNowBlock(null)
        val block = convertPBBlock2View(pbblock)
        return JSON.toJSONString(block)
    }

    @RequestMapping("/block/search", produces = ["application/json"])
    fun search(@RequestParam("q") q: String, response: HttpServletResponse): RedirectView? {
        return when {
            NumberUtils.isDigits(q) -> {
                val height = NumberUtils.toLong(q)
                RedirectView( "/v0/block/$height")
            }
            Pattern.matches(BLOCK_ID_REGEX, q) -> RedirectView("/v0/block/id/$q")
            else -> {
                response.status = 404
                null
            }
        }
    }

    @RequestMapping("/account/{address}", produces = ["application/json"])
    fun accountDetail(@PathVariable address: String, response: HttpServletResponse): String? {
        val stub = tronClient.getWalletStub()

        val addrRaw = Base58.decode58Check(address)

        val pbAccount = stub.getAccount(Protocol.Account.newBuilder().setAddress(ByteString.copyFrom(addrRaw)).build())

        if (pbAccount == null) {
            response.status = 404
            return null
        }

        val account = convertPBAccount2View(pbAccount)

        return JSON.toJSONString(account)
    }

}