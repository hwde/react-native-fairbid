
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
@import AdSupport;
@import CoreGraphics;
@import CoreLocation;
@import CoreTelephony;
@import MediaPlayer;
@import MessageUI;
@import MobileCoreServices;
@import QuartzCore;
@import Security;
@import StoreKit;
@import SystemConfiguration;
@import WebKit;
#import <FairBidSDK/FairBid.h>
#import <React/RCTUtils.h>

@interface RNFairbid : RCTEventEmitter <RCTBridgeModule,FYBBannerDelegate,FYBRewardedDelegate,FYBInterstitialDelegate>

@end

@implementation RNFairbid
{
  bool _hasListeners;
}
RCT_EXPORT_MODULE();

+ (BOOL)requiresMainQueueSetup {
  return YES;
}

- (dispatch_queue_t)methodQueue {
  return dispatch_get_main_queue();
}

- (void)startObserving {
  _hasListeners = true;
}

- (void)stopObserving {
  _hasListeners = false;
}

- (NSArray<NSString *> *)supportedEvents {
  return @[@"RNFairbidEvent"];
}

- (void)sendEvent:(NSString *)name body:(NSDictionary *)body {
  if (_hasListeners && super.bridge != nil) {
    [self sendEventWithName:@"RNFairbidEvent" body:@{@"name": name, @"body": body}];
  }
}

RCT_EXPORT_METHOD(initWithAppID:(NSString *)appId userID:(NSString *)userID) {

  [FairBid startWithAppId:appId];
  [FairBid user].userId = userID;
  FYBBanner.delegate = self;
  FYBInterstitial.delegate = self;
  FYBRewarded.delegate = self;
}

RCT_EXPORT_METHOD(presentTestSuite) {
  [FairBid presentTestSuite];
}

RCT_EXPORT_METHOD(requestInterstitialForPlacementID:(NSString *)placementId) {
  if (![FYBInterstitial isAvailable:placementId]) {
    [FYBInterstitial request:placementId];
  } else {
    [self sendEvent:@"interstitialIsAvailable" body:@{ @"placementId": placementId, }];
  }
}
RCT_EXPORT_METHOD(isInterstitialAvailableForPlacementID:(NSString *)placementId callback:(RCTResponseSenderBlock)callback) {
  callback(@[@([FYBInterstitial isAvailable:placementId])]);
}
RCT_EXPORT_METHOD(showInterstitialForPlacementID:(NSString *)placementId callback:(RCTResponseSenderBlock)callback) {
  if ([FYBInterstitial isAvailable:placementId]) {
    FYBShowOptions *showOptions = [FYBShowOptions new];
    UIViewController *controller = RCTPresentedViewController();
    if (controller != NULL) {
        showOptions.viewController = controller;
    }
    [FYBInterstitial show:placementId options:showOptions];
    callback(@[@true]);
  } else {
    callback(@[@false]);
  }
}

RCT_EXPORT_METHOD(requestVideoForPlacementID:(NSString *)placementId) {
  [FYBRewarded request:placementId];
}
RCT_EXPORT_METHOD(isVideoAvailableForPlacementID:(NSString *)placementId callback:(RCTResponseSenderBlock)callback) {
  callback(@[@([FYBRewarded isAvailable:placementId])]);
}
RCT_EXPORT_METHOD(showVideoForPlacementID:(NSString *)placementId callback:(RCTResponseSenderBlock)callback) {
  if ([FYBRewarded isAvailable:placementId]) {
    [FYBRewarded show:placementId];;
    callback(@[@true]);
  } else {
    callback(@[@false]);
  }
}

RCT_EXPORT_METHOD(showBannerInView:(NSString *)placementId incentivizedInfo:(NSString *)info callback:(RCTResponseSenderBlock)callback) {
  FYBBannerOptions *bannerOptions = [[FYBBannerOptions alloc] init];
  bannerOptions.placementId = placementId;
  UIViewController *controller = RCTPresentedViewController();

  if (controller != NULL) {
    [FYBBanner showBannerInView:controller.view
                       position:FYBBannerAdViewPositionBottom
                        options:bannerOptions];
    callback(@[@true]);
  } else {
    callback(@[@false]);
  }
}

RCT_EXPORT_METHOD(gdprConsent:(BOOL)consentFlag) {
  [FairBid user].gdprConsent = consentFlag;
}

RCT_EXPORT_METHOD(gdprConsentData:(NSDictionary *)consentData) {
  [FairBid user].gdprConsentData = consentData;
}

RCT_EXPORT_METHOD(clearGDPRConsent) {
    [[FairBid user] clearGDPRConsent];
}

- (void)interstitialIsAvailable:(NSString *)placementId {
  [self sendEvent:@"interstitialIsAvailable" body:@{ @"placementId": placementId, }];
}

- (void)interstitialIsUnavailable:(NSString *)placementId {
  [self sendEvent:@"interstitialIsUnavailable" body:@{ @"placementId": placementId, }];
}

- (void)interstitialDidShow:(NSString *)placementId impressionData:(FYBImpressionData *)impressionData {
  [self sendEvent:@"interstitialDidShow" body:@{ @"placementId": placementId, }];
}

- (void)interstitialDidFailToShow:(NSString *)placementId withError:(NSError *)error impressionData:(FYBImpressionData *)impressionData {
  [self sendEvent:@"interstitialDidFailToShow" body:@{ @"placementId": placementId, }];
}

- (void)interstitialDidClick:(NSString *)placementId {
  [self sendEvent:@"interstitialDidClick" body:@{ @"placementId": placementId, }];
}

- (void)interstitialDidDismiss:(NSString *)placementId {
  [self sendEvent:@"interstitialDidDismiss" body:@{ @"placementId": placementId, }];
}


- (void)rewardedIsAvailable:(NSString *)placementId {
  [self sendEvent:@"rewardedIsAvailable" body:@{ @"placementId": placementId, }];
}
- (void)rewardedIsUnavailable:(NSString *)placementId {
  [self sendEvent:@"rewardedIsUnavailable" body:@{ @"placementId": placementId, }];
}
- (void)rewardedDidShow:(NSString *)placementId impressionData:(FYBImpressionData *)impressionData {
  [self sendEvent:@"rewardedDidShow" body:@{ @"placementId": placementId, }];
}
- (void)rewardedDidFailToShow:(NSString *)placementId withError:(NSError *)error impressionData:(FYBImpressionData *)impressionData {
  [self sendEvent:@"rewardedDidFailToShow" body:@{ @"placementId": placementId, }];
}
- (void)rewardedDidClick:(NSString *)placementId {
  [self sendEvent:@"rewardedDidClick" body:@{ @"placementId": placementId, }];
}
- (void)rewardedDidDismiss:(NSString *)placementId {
  [self sendEvent:@"rewardedDidDismiss" body:@{ @"placementId": placementId, }];
}
- (void)rewardedDidComplete:(NSString *)placementId userRewarded:(BOOL)userRewarded {
  [self sendEvent:@"rewardedDidComplete" body:@{ @"placementId": placementId, }];
}


- (void)bannerDidLoad:(FYBBannerAdView *)banner {
  [self sendEvent:@"bannerDidLoad" body:@{ }];
}

- (void)bannerDidFailToLoad:(NSString *)placementId withError:(NSError *)error {
  [self sendEvent:@"bannerDidFailToLoad" body:@{ @"placementId": placementId, @"error": error.localizedDescription, }];
}

- (void)bannerDidShow:(FYBBannerAdView *)banner impressionData:(FYBImpressionData *)impressionData {
  [self sendEvent:@"bannerDidShow" body:@{ }];
}

- (void)bannerDidClick:(FYBBannerAdView *)banner {
  [self sendEvent:@"bannerDidClick" body:@{ }];
}

- (void)bannerWillPresentModalView:(FYBBannerAdView *)banner {
  [self sendEvent:@"bannerWillPresentModalView" body:@{ }];
}

- (void)bannerDidDismissModalView:(FYBBannerAdView *)banner {
  [self sendEvent:@"bannerWillPresentModalView" body:@{ }];
}

- (void)bannerWillLeaveApplication:(FYBBannerAdView *)banner {
  [self sendEvent:@"bannerWillLeaveApplication" body:@{ }];
}

- (void)banner:(FYBBannerAdView *)banner didResizeToFrame:(CGRect)frame {
  [self sendEvent:@"bannerDidResizeToFrame" body:@{ }];
}

@end
