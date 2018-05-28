package tronex.tasks

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.tron.api.GrpcAPI
import tronex.services.DB
import tronex.services.TronClientKT
import tronex.utils.convertPBBlock2View

@Component
class SyncHistoryBlockTask: Runnable {
    companion object {
        private val logger = LoggerFactory.getLogger(SyncHistoryBlockTask::class.java)
    }

    @Autowired
    private lateinit var db: DB

    @Autowired
    private lateinit var tronClientKT: TronClientKT


    private var isRunning: Boolean = false
    @Synchronized
    fun isRunning(): Boolean {
        return isRunning
    }

    fun setRunning(r: Boolean) {
        this.isRunning = r
    }


    override fun run() {

        setRunning(true)

        val stub = tronClientKT.getWalletStub()
        val block = stub.getNowBlock(null)

        val nowHeight = block.blockHeader.rawData.number

        val existLatestBlockHeight = db.largestHeight()

        if (existLatestBlockHeight == nowHeight){
            return
        }
        for (i in existLatestBlockHeight..nowHeight) {
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

        setRunning(false)
    }


}