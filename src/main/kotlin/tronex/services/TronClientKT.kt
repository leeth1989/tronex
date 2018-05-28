package tronex.services

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.tron.api.WalletGrpc
import tronex.TRON_HOST
import tronex.TRON_PORT

@Service
class TronClientKT(@Value(TRON_HOST) host: String?,
                   @Value(TRON_PORT) port: Int?) {

    private var managedChannel: ManagedChannel? = null
    private var walletStub: WalletGrpc.WalletBlockingStub? = null

    init {
        this.managedChannel = ManagedChannelBuilder
                .forAddress(host, port!!)
                .usePlaintext()
                .build()

        this.walletStub = WalletGrpc.newBlockingStub(this.managedChannel)
    }

    fun getWalletStub(): WalletGrpc.WalletBlockingStub {
        return walletStub!!
    }

}