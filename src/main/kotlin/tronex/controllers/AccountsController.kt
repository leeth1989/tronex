package tronex.controllers

import com.alibaba.fastjson.JSON
import com.google.protobuf.ByteString
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import tronex.APPLICATION_JSON
import tronex.Account
import tronex.services.DB
import tronex.services.TronClientKT
import tronex.utils.Base58
import tronex.utils.PBAccount
import tronex.utils.convertPBAccount2View
import tronex.utils.fromHex

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/v1/accounts", method = [RequestMethod.GET, RequestMethod.OPTIONS])
class AccountsController {

    @Autowired
    private lateinit var tronClient: TronClientKT

    @Autowired
    private lateinit var db: DB


    @RequestMapping("/", produces = [APPLICATION_JSON])
    fun accountsList(): String {
        return accountsList(1)
    }

    @RequestMapping("/page/{pageNum:[0-9]+}", produces = [APPLICATION_JSON])
    fun accountsList(@PathVariable pageNum: Int=1): String {
        val pageSize = 20
        val pageN = if (pageNum <= 1) 1 else pageNum

        val skip = (pageN - 1) * pageSize

        val docs = db.accounts.find().sort(Sorts.descending("balance")).skip(skip).limit(pageSize)

        val accounts = docs.map {
            JSON.parseObject(it.toJson(), Account::class.java)
        }.toList()
        accounts.forEach {
            it.asset = it.asset.mapKeys {
                String(fromHex(it.key))
            }
        }
        return JSON.toJSONString(accounts)
    }

    @RequestMapping("/{address}", produces = [APPLICATION_JSON])
    fun accountDetail(@PathVariable address: String): String {
        val addrRaw = Base58.decode58Check(address)
        val pbAccount = tronClient.getWalletStub().getAccount(PBAccount.newBuilder().setAddress(ByteString.copyFrom(addrRaw)).build())
        val account = convertPBAccount2View(pbAccount, convertAssetMap = false)
        return JSON.toJSONString(account)
    }

}