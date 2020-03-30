'use strict';

import ReactNative from 'react-native';
import EventEmitter from 'events';

const {
  Platform,
  DeviceEventEmitter,
  NativeEventEmitter,
  NativeModules,
} = ReactNative;
const { RNFairbid } = NativeModules;

const g_eventEmitter = new EventEmitter();
if (Platform.OS === 'ios') {
  const g_iosEventEmitter = new NativeEventEmitter(RNFairbid);

  g_iosEventEmitter.addListener('RNFairbidEvent',e => {
    g_eventEmitter.emit(e.name,e.body);
  });
} else {
  DeviceEventEmitter.addListener('RNFairbidEvent',e => {
    g_eventEmitter.emit(e.name,e);
  });
}

function once(event,callback) {
  g_eventEmitter.once(event,callback);
}
function on(event,callback) {
  g_eventEmitter.on(event,callback);
}
function removeListener(event,callback) {
  g_eventEmitter.removeListener(event,callback);
}

const initWithAppID = RNFairbid.initWithAppID;
const presentTestSuite = RNFairbid.presentTestSuite;

function wrapArgMethod(method) {
    return (arg, done) => {
        if (!done) {
            done = function() {};
        }
        method(arg, done);
    }
}

const requestInterstitialForPlacementID = RNFairbid.requestInterstitialForPlacementID;
const isInterstitialAvailableForPlacementID = wrapArgMethod(RNFairbid.isInterstitialAvailableForPlacementID);
const showInterstitialForPlacementID = wrapArgMethod(RNFairbid.showInterstitialForPlacementID);

const requestVideoForPlacementID = RNFairbid.requestVideoForPlacementID;
const isVideoAvailableForPlacementID = wrapArgMethod(RNFairbid.isVideoAvailableForPlacementID);
const showVideoForPlacementID = wrapArgMethod(RNFairbid.showVideoForPlacementID);

const showBannerInView = wrapArgMethod(RNFairbid.showBannerInView);

const GDPRConsent = RNFairbid.GDPRConsent;
const GDPRConsentString = RNFairbid.GDPRConsentString;
const clearGDPRConsent = RNFairbid.clearGDPRConsent;

export default {
  once,
  on,
  removeListener,

  initWithAppID,

  presentTestSuite,

  requestInterstitialForPlacementID,
  isInterstitialAvailableForPlacementID,
  showInterstitialForPlacementID,

  requestVideoForPlacementID,
  isVideoAvailableForPlacementID,
  showVideoForPlacementID,

  showBannerInView,

  GDPRConsent,
  GDPRConsentString,
  clearGDPRConsent
};
