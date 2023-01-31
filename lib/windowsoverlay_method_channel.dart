import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'windowsoverlay_platform_interface.dart';

/// An implementation of [WindowsoverlayPlatform] that uses method channels.
class MethodChannelWindowsoverlay extends WindowsoverlayPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('windowsoverlay');

  @override
  Future getPlatformVersion({required int matchid, required String token}) async {
    await methodChannel
        .invokeMethod('getPlatformVersion', {"matchid": matchid, "token": token});
    return "";
  }

  @override
  Future stopservice() async {
    await methodChannel.invokeMethod('stopservice');
    return "";
  }
}
