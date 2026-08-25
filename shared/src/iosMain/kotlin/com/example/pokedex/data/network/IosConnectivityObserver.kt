package com.example.pokedex.data.network

import com.example.pokedex.domain.network.ConnectivityObserver
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityFlagsVar
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable

@OptIn(ExperimentalForeignApi::class)
class IosConnectivityObserver : ConnectivityObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val isConnected: StateFlow<Boolean> = flow {
        while (true) {
            emit(checkReachable())
            delay(3_000)
        }
    }
        .distinctUntilChanged()
        .stateIn(scope, SharingStarted.Eagerly, true)

    private fun checkReachable(): Boolean = memScoped {
        val reachability = SCNetworkReachabilityCreateWithName(null, "pokeapi.co") ?: return false
        val flags = alloc<SCNetworkReachabilityFlagsVar>()
        val success = SCNetworkReachabilityGetFlags(reachability, flags.ptr)
        if (!success) return false
        val reachable = flags.value.toInt() and kSCNetworkReachabilityFlagsReachable.toInt() != 0
        val needsConnection = flags.value.toInt() and kSCNetworkReachabilityFlagsConnectionRequired.toInt() != 0
        reachable && !needsConnection
    }
}