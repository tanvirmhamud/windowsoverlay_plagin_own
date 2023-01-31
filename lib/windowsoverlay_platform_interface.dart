import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'windowsoverlay_method_channel.dart';

abstract class WindowsoverlayPlatform extends PlatformInterface {
  /// Constructs a WindowsoverlayPlatform.
  WindowsoverlayPlatform() : super(token: _token);

  static final Object _token = Object();

  static WindowsoverlayPlatform _instance = MethodChannelWindowsoverlay();

  /// The default instance of [WindowsoverlayPlatform] to use.
  ///
  /// Defaults to [MethodChannelWindowsoverlay].
  static WindowsoverlayPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [WindowsoverlayPlatform] when
  /// they register themselves.
  static set instance(WindowsoverlayPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future getPlatformVersion({required int matchid,required String token}) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
    Future stopservice() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
