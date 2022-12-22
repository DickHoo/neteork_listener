import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class NetworkListener {
  static const EventChannel _eventChannel = EventChannel('com.hoo.network_listener');
  StreamSubscription? streamSubscription;
  static NetworkListener networkListener = NetworkListener();

  static NetworkListener getInstance() {
    return networkListener;
  }

  registerEvent(ValueChanged<bool> status) {
    streamSubscription = _eventChannel.receiveBroadcastStream().listen((event) {
      status.call(event);
      print("网络状态：$event");
    });
  }

}
