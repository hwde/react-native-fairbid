# react-native-fairbid

React Native wrapper for the Fyber Fairbid SDK.

Current status:

1. Rewarded Video
2. Interstitial
3. Banner (iOS)

## Getting started
### iOS

**Verify pod is installed in ios/Podfile, should be something like:**

```text
pod 'RNFairbid', :path => '../node_modules/react-native-fairbid'
```

### Android

tbd.

### Usage

```js
import RNFairbid from 'react-native-fairbid';
```

### Initialization

```js
const APP_ID = '123456';
const userId = ... // pass in a userId
RNFairbid.initWithAppID(APP_ID, userid);
```

### Rewarded Video

```js
const PLACEMENT_ID = '456789';

RNFairbid.on('rewardedIsAvailable', () => {
	console.log('RNFairbid.rewardedIsAvailable');
	RNFairbid.showVideoForPlacementID(PLACEMENT_ID, (result) => {
		console.log('RNFairbid.showVideoForPlacementID: result=', result);
	});
});
RNFairbid.on('rewardedIsUnavailable', () => {
	console.log('RNFairbid.rewardedIsUnavailable');
});
RNFairbid.on('rewardedDidShow', () => {
	console.log('RNFairbid.rewardedDidShow');
});
RNFairbid.on('rewardedDidDismiss', () => {
	console.log('RNFairbid.rewardedDidDismiss');
});
RNFairbid.on('rewardedDidComplete', () => {
	console.log('RNFairbid.rewardedDidComplete');
});
RNFairbid.on('rewardedDidFailToShow', () => {
	console.log('RNFairbid.rewardedDidFailToShow');
});

RNFairbid.requestVideoForPlacementID(PLACEMENT_ID);
```

### Interstitial

```js
const PLACEMENT_ID = '456789';

RNFairbid.on('interstitialIsAvailable', () => {
	console.log('RNFairbid.interstitialIsAvailable');
	RNFairbid.showInterstitialForPlacementID(PLACEMENT_ID, (result) => {
		console.log('RNFairbid.showInterstitialForPlacementID: result=', result);
	});
});
RNFairbid.on('interstitialIsUnavailable', () => {
	console.log('RNFairbid.interstitialIsUnavailable');
});
RNFairbid.on('interstitialDidShow', () => {
	console.log('RNFairbid.interstitialDidShow');
});
RNFairbid.on('interstitialDidDismiss', () => {
	console.log('RNFairbid.interstitialDidDismiss');
});
RNFairbid.on('interstitialDidClick', () => {
	console.log('RNFairbid.interstitialDidClick');
});
RNFairbid.on('interstitialDidFailToShow', () => {
	console.log('RNFairbid.interstitialDidFailToShow');
});

RNFairbid.requestInterstitialForPlacementID(PLACEMENT_ID);
```

### Banner

```js
const PLACEMENT_ID = '456789';
const VIEW_COMPONENT = ReactNative.findNodeHandle(this.myRef.current)

RNFairbid.on('bannerDidLoad', () => {
	console.log('RNFairbid.bannerDidLoad');
});
RNFairbid.on('bannerDidShow', () => {
	console.log('RNFairbid.bannerDidShow');
});
RNFairbid.on('bannerDidClick', () => {
	console.log('RNFairbid.bannerDidClick');
});
RNFairbid.on('bannerDidFailToLoad', () => {
	console.log('RNFairbid.bannerDidFailToLoad');
});

RNFairbid.showBannerInView(PLACEMENT_ID, VIEW_COMPONENT, () => {
  console.log('RNFairbid.showBannerInView');
});
```

### GDPR

```js
RNFairbid.GDPRConsent(true); // got user consent
RNFairbid.GDPRConsent(false); // user consent not given
RNFairbid.clearGDPRConsent(); // clear user consent and data
RNFairbid.GDPRConsentString('BOEFEAyOEFEAyAHABDENAI4AAAB9vABAASA');
```
