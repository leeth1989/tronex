package tronex.controllers

import com.alibaba.fastjson.JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import tronex.APPLICATION_JSON
import tronex.services.TronClientKT
import tronex.utils.convertPBWitness2View

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/v1/nodes", method = [RequestMethod.GET, RequestMethod.OPTIONS])
class NodesController {

    @Autowired
    private lateinit var tronClientKT: TronClientKT

    @RequestMapping("/", produces = [APPLICATION_JSON])
    fun nodesList(): String {
        val stub = tronClientKT.getWalletStub()
        val pbNodesList = stub.listNodes(null).nodesList

        return JSON.toJSONString(pbNodesList.size)
    }

    @RequestMapping("/witness", produces = [APPLICATION_JSON])
    fun witnessList(): String {
        val stub = tronClientKT.getWalletStub()
        val wl = stub.listWitnesses(null).witnessesList

        val witnesses = wl.map { convertPBWitness2View(it) }.sortedBy { it.totalProduced }.reversed()

        return JSON.toJSONString(witnesses)
    }
}