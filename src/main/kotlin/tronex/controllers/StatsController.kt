package tronex.controllers

import com.alibaba.fastjson.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tronex.APPLICATION_JSON
import tronex.services.TronClientKT

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/v1/stats", method = [RequestMethod.GET, RequestMethod.OPTIONS])
class StatsController {

    @Autowired
    private lateinit var tronClient: TronClientKT

    @RequestMapping("/height", produces = [APPLICATION_JSON])
    fun height(): String {
        val stub = tronClient.getWalletStub()
        val block = stub.getNowBlock(null)
        val height = block.blockHeader.rawData.number
        return JSON.toJSONString(mapOf("height" to height))
    }

}