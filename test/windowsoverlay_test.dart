import 'package:flutter_test/flutter_test.dart';
import 'package:windowsoverlay/windowsoverlay.dart';
import 'package:windowsoverlay/windowsoverlay_platform_interface.dart';
import 'package:windowsoverlay/windowsoverlay_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockWindowsoverlayPlatform
    with MockPlatformInterfaceMixin
    implements WindowsoverlayPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final WindowsoverlayPlatform initialPlatform =
      WindowsoverlayPlatform.instance;

  test('$MethodChannelWindowsoverlay is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelWindowsoverlay>());
  });

  test('getPlatformVersion', () async {
    Windowsoverlay windowsoverlayPlugin = Windowsoverlay();
    MockWindowsoverlayPlatform fakePlatform = MockWindowsoverlayPlatform();
    WindowsoverlayPlatform.instance = fakePlatform;

    expect(await windowsoverlayPlugin.startservice(), '42');
  });
}
