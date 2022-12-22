package com.hoo.network_listener

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

/** NetworkListenerPlugin */
class NetworkListenerPlugin : FlutterPlugin, EventChannel.StreamHandler, ActivityAware {
    private lateinit var activity: Activity
    var eventSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.e("NetworkListener:", "onAttachedToEngine")
        //1.创建Android端的EventChannel
        val eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "com.hoo.network_listener")
        //2.在StreamHandler#onLister回调中获取EventSink引用并保存
        eventChannel.setStreamHandler(this)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        initRegisterNetwork()
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        NetWorkUtils.unregisterNetwork(binding.activity.applicationContext)
    }

    override fun onDetachedFromActivity() {
    }

    private fun initRegisterNetwork() {
        NetWorkUtils.registerNetwork(activity.applicationContext, object : NetStateChangeObserver {
            override fun onDisconnect() {
                activity.runOnUiThread {
                    eventSink?.success(false)
                }

            }

            override fun onConnect() {
                activity.runOnUiThread {
                    eventSink?.success(true)
                }
            }
        })
    }


}
