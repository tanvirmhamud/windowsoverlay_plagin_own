package com.example.windowsoverlay

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** WindowsoverlayPlugin */
class WindowsoverlayPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, BasicMessageChannel.MessageHandler<Any?> {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private lateinit var activity: Activity

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "windowsoverlay")
    channel.setMethodCallHandler(this)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      val matchid: Int? = call.argument("matchid")
      var token : String? = call.argument("token");
      var candraw = false;
      var intent: Intent = Intent();
      context = activity.applicationContext;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        candraw = Settings.canDrawOverlays(context);
      }
      var service = Intent(context, overlayservice::class.java)
      service.putExtra("matchid",matchid);
      service.putExtra("token",token);
      activity.startService(service);
    } else if(call.method == "stopservice"){
      var service = Intent(context, overlayservice::class.java);
      activity.stopService(service);
    }
    else {
      result.notImplemented()
    }
  }



  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    this.context = binding.applicationContext
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity;
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")

  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  override fun onMessage(message: Any?, reply: BasicMessageChannel.Reply<Any?>) {
    TODO("Not yet implemented")
  }


}
