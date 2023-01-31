
import 'windowsoverlay_platform_interface.dart';

class Windowsoverlay {
  Future startservice({required int matchid,required String token}) {
    return WindowsoverlayPlatform.instance.getPlatformVersion(matchid: matchid,token: token);
  }

    Future stopservice() {
    return WindowsoverlayPlatform.instance.stopservice();
  }
}
