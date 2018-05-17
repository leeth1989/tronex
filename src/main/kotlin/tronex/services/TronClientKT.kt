package tronex.services

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.tron.api.DatabaseGrpc
import org.tron.api.WalletGrpc
import tronex.AppConfig

@Service
class TronClientKT {

    private var managedChannel: ManagedChannel? = null
    private var walletStub: WalletGrpc.WalletBlockingStub? = null

    @Autowired
    private lateinit var appConfig: AppConfig


    @Synchronized
    fun getManagedChannel(): ManagedChannel {
        if (this.managedChannel == null) {
            val host = appConfig.tronHost
            val port = appConfig.tronPort.toInt()
            this.managedChannel = ManagedChannelBuilder
                    .forAddress(host, port)
                    .usePlaintext()
                    .build()
        }
        return this.managedChannel!!
    }


    @Synchronized
    fun getWalletStub(): WalletGrpc.WalletBlockingStub {
        if (this.walletStub == null) {
            this.walletStub = WalletGrpc.newBlockingStub(getManagedChannel())
        }
        return walletStub!!
    }


}