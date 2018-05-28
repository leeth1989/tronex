package tronex.tasks

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.tron.api.GrpcAPI
import tronex.services.DB
import tronex.services.TronClientKT
import tronex.utils.convertPBBlock2View

@Component
class SyncBlockTask {
    companion object {
        private val logger = LoggerFactory.getLogger(SyncBlockTask::class.java)
    }
    @Autowired
    private lateinit var db: DB

    @Autowired
    private lateinit var tronClient: TronClientKT

    @Autowired
    private lateinit var syncHistoryBlockTask: SyncHistoryBlockTask

    @Scheduled(fixedDelay = 1000)
    fun syncBlock() {

        if (syncHistoryBlockTask.isRunning()) {
            return
        }

        val stub = tronClient.getWalletStub()
        val nowBlock = stub.getNowBlock(null)
        val nowHeight = nowBlock.blockHeader.rawData.number
        val latestBlockHeight = db.largestHeight()
        if (nowHeight == latestBlockHeight) {
            return
        }
        for (i in latestBlockHeight + 1..nowHeight) {
            logger.info("Sync block $i ...")

            val existBlock = db.findByHeight(i)
            if (existBlock == null) {
                val iBlock = stub.getBlockByNum(GrpcAPI.NumberMessage.newBuilder().setNum(i).build())
                val b = convertPBBlock2View(iBlock)
                db.saveBlock(b)
                logger.info("Finished sync block $i ...")
            } else {
                logger.info("Block $i exists.")
            }
        }
    }

}