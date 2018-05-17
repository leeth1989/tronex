package tronex.services

import com.alibaba.fastjson.JSON
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.springframework.stereotype.Service
import tronex.Block
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Sorts
import org.springframework.beans.factory.annotation.Autowired
import tronex.AppConfig

@Service
class DB {
    private var mongoClient: MongoClient? = null
    private var blockCollection: MongoCollection<Document>? = null

    @Autowired
    private lateinit var appConfig: AppConfig

    @Synchronized
    fun getMongoClient(): MongoClient {
        if (mongoClient == null) {
            val host = appConfig.mongodbHost
            val port = appConfig.mongodbPort.toInt()
            mongoClient = MongoClient(host, port)
        }
        return mongoClient!!
    }

    @Synchronized
    fun initBlocksCollection() {
        if (blockCollection == null) {
            blockCollection = getMongoClient().getDatabase("tronex")!!.getCollection("blocks")
            blockCollection!!.createIndex(Document("height", -1))
            blockCollection!!.createIndex(Document("id", -1))
        }
    }

    fun blocks(): MongoCollection<Document> {
        if (blockCollection == null) {
            initBlocksCollection()
        }
        return blockCollection!!
    }


    fun saveBlock(block: Block) {
        val json = JSON.toJSONString(block)
        val doc = Document.parse(json)
        doc.append("height", block.header.height)
        blocks().insertOne(doc)
    }

    fun findByHeight(height: Long): Block? {
        val doc = blocks().find(eq("height", height)).first()
        if (doc != null) {
            val json = doc.toJson()
            val block = JSON.parseObject(json, Block::class.java)
            return block
        } else {
            return null
        }
    }

    fun findById(id: String): Block? {
        val doc = blocks().find(eq("id", id)).first()
        if (doc != null) {
            val json = doc.toJson()
            val block = JSON.parseObject(json, Block::class.java)
            return block
        } else {
            return null
        }
    }

    fun largestHeight(): Long {
        val doc = blocks().find().sort(Sorts.descending("height")).first() ?: return -1
        return doc.getLong("height")
    }

    fun latestN(n: Int = 10): List<Block> {
        val docs = blocks().find().sort(Sorts.descending("height")).limit(n)
        val blocks = docs.map {
            val json = it.toJson()
            val block = JSON.parseObject(json, Block::class.java)
            block
        }.toList()
        return blocks
    }

    fun findPagedBlocks(pageNum: Int = 1, pageSize: Int = 100): List<Block> {
        val pageN = if (pageNum <= 1) 1 else pageNum
        val largestHeight = largestHeight()
        val maxHeight = largestHeight - (pageN - 1) * pageSize
        val minHeight = maxHeight - pageSize

        val docs = blocks().find(
                and(lte("height", maxHeight),
                        gt("height", minHeight))
        ).sort(Sorts.descending("height"))

        val blocks = docs.map {
            val json = it.toJson()
            val block = JSON.parseObject(json, Block::class.java)
            block
        }.toList()
        return blocks

    }
}