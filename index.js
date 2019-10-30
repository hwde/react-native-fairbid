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

function wrapArg1Method(method) {
    return (arg, done) => {
        if (!done) {
            done = function() {};
        }
        method(arg, done);
    }
}
function wrapArg2Method(method) {
    return (arg1, arg2, done) => {
        if (!done) {
            done = function() {};
        }
        method(arg1, arg2, done);
    }
}
const requestInterstitialForPlacementID = RNFairbid.requestInterstitialForPlacementID;
const isInterstitialAvailableForPlacementID = wrapArg1Method(RNFairbid.isInterstitialAvailableForPlacementID);
const showInterstitialForPlacementID = wrapArg1Method(RNFairbid.showInterstitialForPlacementID);

const requestVideoForPlacementID = RNFairbid.requestVideoForPlacementID;
const isVideoAvailableForPlacementID = wrapArg1Method(RNFairbid.isVideoAvailableForPlacementID);
const showVideoForPlacementID = wrapArg1Method(RNFairbid.showVideoForPlacementID);

const showBannerInView = wrapArg2Method(RNFairbid.showBannerInView);

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
};
