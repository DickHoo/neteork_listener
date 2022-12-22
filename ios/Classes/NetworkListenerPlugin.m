#import "NetworkListenerPlugin.h"
#if __has_include(<network_listener/network_listener-Swift.h>)
#import <network_listener/network_listener-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "network_listener-Swift.h"
#endif

@implementation NetworkListenerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftNetworkListenerPlugin registerWithRegistrar:registrar];
}
@end
