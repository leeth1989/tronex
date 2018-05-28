package tronex.services

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.tron.api.WalletExtensionGrpc
import org.tron.api.WalletGrpc
import org.tron.api.WalletSolidityGrpc
import tronex.TRON_HOST
import tronex.TRON_PORT

@Service
class TronClientKT(@Value(TRON_HOST) host: String?,
                   @Value(TRON_PORT) port: Int?) {

    private var managedChannel: ManagedChannel? = null
    private var walletStub: WalletGrpc.WalletBlockingStub? = null
    private var walletExtensionStub: WalletExtensionGrpc.WalletExtensionBlockingStub? = null
    private var walletSolidityStub: WalletSolidityGrpc.WalletSolidityBlockingStub? = null

    init {
        this.managedChannel = ManagedChannelBuilder
                .forAddress(host, port!!)
                .usePlaintext()
                .build()

        this.walletStub = WalletGrpc.newBlockingStub(this.managedChannel)
        this.walletExtensionStub = WalletExtensionGrpc.newBlockingStub(this.managedChannel)
        this.walletSolidityStub = WalletSolidityGrpc.newBlockingStub(this.managedChannel)
    }

    fun getWalletStub(): WalletGrpc.WalletBlockingStub {
        return walletStub!!
    }

    fun getWalletSolidityStub(): WalletSolidityGrpc.WalletSolidityBlockingStub {
        return walletSolidityStub!!
    }

    fun getWalletExtensionStub(): WalletExtensionGrpc.WalletExtensionBlockingStub {
        return walletExtensionStub!!
    }

}