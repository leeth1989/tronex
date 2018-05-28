package tronex.tasks

import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import tronex.services.DB
import tronex.services.TronClientKT
import tronex.utils.convertPBAccount2View
import java.util.concurrent.TimeUnit
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates.*
import org.bson.Document
import java.lang.IllegalArgumentException

@Component
class SyncAccountTask {
    companion object {
        private val logger = LoggerFactory.getLogger(SyncAccountTask::class.java)

    }

    @Autowired
    private lateinit var db: DB

    @Autowired
    private lateinit var tronClient: TronClientKT

//    @Scheduled(fixedDelay = 60000)
    fun syncAccounts() {
//        val stub = tronClient.getWalletStub()
//
//        val pbAccounts = stub.listAccounts(null).accountsList
//        val accountList = pbAccounts.map {
//            convertPBAccount2View(it)
//        }
//
//        accountList.forEach {
//
//            val doc = Document.parse(JSON.toJSONString(it))
//            db.accounts.replaceOne(eq("address", it.address), doc, UpdateOptions().upsert(true))
//
//        }
    }
}