package tronex.controllers

import com.alibaba.fastjson.JSON
import org.apache.commons.lang3.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.tron.api.GrpcAPI
import tronex.APPLICATION_JSON
import tronex.services.DB
import tronex.services.TronClientKT
import tronex.utils.dateFormat
import java.util.*
import com.mongodb.client.model.Filters.*
import tronex.Block

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/v1/stats", method = [RequestMethod.GET, RequestMethod.OPTIONS])
class StatsController {

    @Autowired
    private lateinit var tronClient: TronClientKT

    @Autowired
    private lateinit var db: DB

    @RequestMapping("/height", produces = [APPLICATION_JSON])
    fun height(): String {
        val stub = tronClient.getWalletStub()
        val block = stub.getNowBlock(null)
        val height = block.blockHeader.rawData.number
        return JSON.toJSONString(mapOf("height" to height))
    }

    @RequestMapping("/totaltxcount", produces = [APPLICATION_JSON])
    fun indexStats(): String {
        val stub = tronClient.getWalletStub()
        val totalCount = stub.totalTransaction(null).num
        return JSON.toJSONString(mapOf("totalTxCount" to totalCount))
    }

    @RequestMapping("/txcount", produces = [APPLICATION_JSON])
    fun transactionCount(): String {

        val current = DateUtils.truncate(Date(), Calendar.HOUR)

        val timeRangeList = mutableListOf<Pair<String, String>>()
        for (i in 0..10) {
            val to = DateUtils.addHours(current, -i)
            val toStr = dateFormat(to.time)
            val from = DateUtils.addHours(current, -i-1)
            val fromStr = dateFormat(from.time)
            timeRangeList.add(fromStr to toStr)
        }

        val res = timeRangeList.reversed().map {
            val blockDocs = db.blocks.find(and(lt("header.time", it.second), gt("header.time", it.first))).toList()
            val blockList = blockDocs.map {
                JSON.parseObject(it.toJson(), Block::class.java)
            }

            val count = blockList.sumBy {
                it.txs.count()
            }

            mapOf("time" to it.first, "txcount" to count)
        }

        return JSON.toJSONString(res)
    }

    @RequestMapping("/txvolume", produces = [APPLICATION_JSON])
    fun transactionVolume(): String {
        val stub = tronClient.getWalletExtensionStub()

        val current = Date()
        val timeSeq = mutableListOf<Long>()
        for (i in 0..10) {
            val milli = DateUtils.addHours(current, -i).time
            timeSeq.add(milli)
        }
        val tmParam = timeSeq.reversed().map {
            val tm = GrpcAPI.TimeMessage.newBuilder()
                    .setBeginInMilliseconds(it - 60*60*1000)
                    .setEndInMilliseconds(it)
                    .build()
            tm
        }

        val count = tmParam.map {
            val cnt = stub.getTransactionsByTimestampCount(it)
            mapOf("datetime" to dateFormat(it.endInMilliseconds), "count" to cnt)
        }

        return JSON.toJSONString(count)
    }

}