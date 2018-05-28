package tronex.services

import com.alibaba.fastjson.JSON
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.springframework.stereotype.Service
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Sorts
import org.springframework.beans.factory.annotation.Value
import tronex.*
import javax.print.Doc
import com.mongodb.client.model.IndexOptions



@Service
class DB(@Value(MONGODB_HOST) host: String?,
         @Value(MONGODB_PORT) port: Int?) {

    private var mongoClient: MongoClient = MongoClient(host, port!!)


    lateinit var blocks: MongoCollection<Document>
    lateinit var transactions: MongoCollection<Document>
    lateinit var accounts: MongoCollection<Document>

    init {

        val tronexDb = mongoClient.getDatabase("tronex")

        blocks = tronexDb.getCollection("blocks")
        blocks.createIndex(Indexes.descending("height"))
        blocks.createIndex(Indexes.ascending("id"))

        transactions = tronexDb.getCollection("transactions")
        transactions.createIndex(Document("from", 1))
        transactions.createIndex(Document("to", 1))

        accounts = tronexDb.getCollection("accounts")
        accounts.createIndex(Indexes.ascending("address"), IndexOptions().unique(true))
        accounts.createIndex(Indexes.descending("balance"))
    }

    fun insertOrUpdate(account: Account) {
        accounts.find()
    }

    fun saveTransaction(tx: Transaction) {
        val json = JSON.toJSONString(tx)
        val doc = Document.parse(json)

        transactions.insertOne(doc)
    }

    fun saveBlock(block: Block) {
        val json = JSON.toJSONString(block)
        val doc = Document.parse(json)
        doc.append("height", block.header.height)
        blocks.insertOne(doc)
    }

    fun findByHeight(height: Long): Block? {
        val doc = blocks.find(eq("height", height)).first()
        if (doc != null) {
            val json = doc.toJson()
            val block = JSON.parseObject(json, Block::class.java)
            return block
        } else {
            return null
        }
    }

    fun findById(id: String): Block? {
        val doc = blocks.find(eq("id", id)).first()
        if (doc != null) {
            val json = doc.toJson()
            val block = JSON.parseObject(json, Block::class.java)
            return block
        } else {
            return null
        }
    }

    fun largestHeight(): Long {
        val doc = blocks.find().sort(Sorts.descending("height")).first() ?: return -1
        return doc.getLong("height")
    }

    fun latestN(n: Int = 10): List<Block> {
        val docs = blocks.find().sort(Sorts.descending("height")).limit(n)
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

        val docs = blocks.find(
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