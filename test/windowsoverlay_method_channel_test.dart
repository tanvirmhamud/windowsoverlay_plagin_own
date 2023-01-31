import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:windowsoverlay/windowsoverlay_method_channel.dart';

void main() {
  MethodChannelWindowsoverlay platform = MethodChannelWindowsoverlay();
  const MethodChannel channel = MethodChannel('windowsoverlay');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
