package com.hoo.network_listener

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import java.util.Timer
import kotlin.concurrent.schedule

interface NetStateChangeObserver {
    fun onDisconnect()
    fun onConnect()
}

class NetworkListener(context: Context, netStateChangeObserver: NetStateChangeObserver?) :
    ConnectivityManager.NetworkCallback() {
    private val mContext: Context = context
    private val mNetStateChangeObserver = netStateChangeObserver
    private val TAG = "NetworkListener:"

    //网络连接成功
    override fun onAvailable(network: Network) {
        Log.e(TAG, "网络连接成功:onAvailable:${isNetworkConnected()}")
        netStateHandle()
        super.onAvailable(network)
    }

    //网络已断开连接
    override fun onLost(network: Network) {
        Log.e(TAG, "网络已断开连接:onLost:${isNetworkConnected()}")
        Timer().schedule(1000){
            netStateHandle()
        }
        super.onLost(network)
    }

    //当网络状态修改（网络依然可用）时调用
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        netStateHandle()
        Log.e(
            TAG,
            "当网络状态修改（网络依然可用）时调用:onCapabilitiesChanged:${isNetworkConnected()}"
        )
    }

    private fun netStateHandle() {
        mNetStateChangeObserver?.let {
            if (isNetworkConnected())
                it.onConnect()
            else
                it.onDisconnect()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            mContext.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val mNetworkInfo = connectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        } else {
            val network = connectivityManager.activeNetwork ?: return false
            val status = connectivityManager.getNetworkCapabilities(network)
                ?: return false
            if (status.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                return true
            }
        }
        return false
    }
}

object NetWorkUtils {
    lateinit var networkListener: NetworkListener
    fun registerNetwork(context: Context, netStateChangeObserver: NetStateChangeObserver?) {
        networkListener = NetworkListener(context, netStateChangeObserver)
        var request = NetworkRequest.Builder().build()
        var connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connMgr.registerNetworkCallback(request, networkListener)
    }

    fun unregisterNetwork(context: Context) {
        var connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkListener?.let {
            connMgr.unregisterNetworkCallback(it)
        }
    }
}
