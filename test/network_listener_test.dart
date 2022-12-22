import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:network_listener/network_listener.dart';

void main() {
  const MethodChannel channel = MethodChannel('network_listener');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  // test('getPlatformVersion', () async {
  //   expect(await NetworkListener.platformVersion, '42');
  // });
}
