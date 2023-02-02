import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:windowsoverlay/windowsoverlay.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _windowsoverlayPlugin = Windowsoverlay();

  @override
  void initState() {
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState({required int matchid}) async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion = await _windowsoverlayPlugin.startservice(
              matchid: matchid, token: "live-soccer-tv-footballl-live-tv") ??
          'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            MaterialButton(
              onPressed: () async {
                initPlatformState(matchid: 980339);
                // await Permission.systemAlertWindow.request();
              },
              child: Text("start Service"),
            ),
            MaterialButton(
              onPressed: () async {
                Windowsoverlay().stopservice();
              },
              child: Text("Stop Service"),
            ),
            MaterialButton(
              onPressed: () async {
                await Permission.systemAlertWindow.request();
              },
              child: Text("Permission Check"),
            ),
          ],
        ),
      ),
    );
  }
}
