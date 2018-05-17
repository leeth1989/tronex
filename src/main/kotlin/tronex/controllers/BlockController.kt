package tronex.controllers

import com.alibaba.fastjson.JSON
import org.apache.commons.lang3.math.NumberUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tronex.BLOCK_ID_REGEX
import tronex.services.DB
import java.util.regex.Pattern
import javax.servlet.http.HttpServletResponse


@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/v1", method = [RequestMethod.GET, RequestMethod.OPTIONS])
class BlockController {

    @Autowired
    lateinit var db: DB

    @RequestMapping("/block/{height:[0-9]+}", produces = ["application/json"])
    fun blockByHeight(@PathVariable height: Long, response: HttpServletResponse): String? {
        val block = db.findByHeight(height)
        if (block == null) {
            response.status = 404
            return null
        }
        return JSON.toJSONString(block)
    }

    @RequestMapping("/block/id/{id:$BLOCK_ID_REGEX}", produces = ["application/json"])
    fun blockById(@PathVariable id: String, response: HttpServletResponse): String? {
        val block = db.findById(id)
        if (block == null) {
            response.status = 404
            return null
        }
        return JSON.toJSONString(block)
    }

    @RequestMapping("/block/search", produces = ["application/json"])
    fun search(@RequestParam("q") q: String, response: HttpServletResponse): String? {
        return when {
            NumberUtils.isDigits(q) -> {
                val height = NumberUtils.toLong(q)
                val block = db.findByHeight(height)
                JSON.toJSONString(block)
            }
            Pattern.matches(BLOCK_ID_REGEX, q) -> {
                val id = q
                val block = db.findById(id)
                JSON.toJSONString(block)
            }
            else -> {
                response.status = 404
                null
            }
        }
    }

    @RequestMapping("/block/latest", produces = ["application/json"])
    fun latest(): String {
        val blocks = db.latestN(20)
        return JSON.toJSONString(blocks)
    }

    @RequestMapping("/blocks/page/{pageNum:[0-9]+}", produces = ["application/json"])
    fun pagedBlocks(@PathVariable pageNum: Int): String {
        val blocks = db.findPagedBlocks(pageNum, 20)
        return JSON.toJSONString(blocks)
    }
}
