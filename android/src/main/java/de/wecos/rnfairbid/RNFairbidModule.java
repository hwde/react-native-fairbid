package de.wecos.rnfairbid;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import com.fyber.FairBid;
import com.fyber.fairbid.ads.Banner;
import com.fyber.fairbid.ads.ImpressionData;
import com.fyber.fairbid.ads.Interstitial;
import com.fyber.fairbid.ads.Rewarded;
import com.fyber.fairbid.ads.banner.BannerError;
import com.fyber.fairbid.ads.banner.BannerListener;
import com.fyber.fairbid.ads.interstitial.InterstitialListener;
import com.fyber.fairbid.ads.rewarded.RewardedListener;
import com.fyber.fairbid.user.UserInfo;

public class RNFairbidModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private final BannerListener bannerListener = new BannerListener() {
        @Override
        public void onError(String placementId, BannerError error) {
            sendReactEvent("bannerDidFailToLoad", "placementId", placementId);
        }

        @Override
        public void onLoad(String placementId) {
            sendReactEvent("bannerDidLoad", "placementId", placementId);
        }

        @Override
        public void onShow(String placementId, ImpressionData impressionData) {
            sendReactEvent("bannerDidShow", "placementId", placementId);
        }

        @Override
        public void onClick(String placementId) {
            sendReactEvent("bannerDidClick", "placementId", placementId);
        }
    };

    private final InterstitialListener interstitialListener = new InterstitialListener() {
        @Override
        public void onShow(String placementId, ImpressionData impressionData) {
            sendReactEvent("interstitialDidShow", "placementId", placementId);
        }

        @Override
        public void onClick(String placementId) {
            sendReactEvent("interstitialDidClick", "placementId", placementId);
        }

        @Override
        public void onHide(String placementId) {
            sendReactEvent("interstitialDidDismiss", "placementId", placementId);
        }

        @Override
        public void onShowFailure(String placementId, ImpressionData impressionData) {
            sendReactEvent("interstitialDidFailToShow", "placementId", placementId);
        }

        @Override
        public void onAvailable(String placementId) {
            sendReactEvent("interstitialIsAvailable", "placementId", placementId);
        }

        @Override
        public void onUnavailable(String placementId) {
            sendReactEvent("interstitialIsUnavailable", "placementId", placementId);
        }
    };

    private final RewardedListener rewardedListener = new RewardedListener() {
        @Override
        public void onShow(String placementId, ImpressionData impressionData) {
            sendReactEvent("rewardedDidShow", "placementId", placementId);
        }

        @Override
        public void onClick(String placementId) {
            sendReactEvent("rewardedDidClick", "placementId", placementId);
        }

        @Override
        public void onHide(String placementId) {
            sendReactEvent("rewardedDidDismiss", "placementId", placementId);
        }

        @Override
        public void onShowFailure(String placementId, ImpressionData impressionData) {
            sendReactEvent("rewardedDidFailToShow", "placementId", placementId);
        }

        @Override
        public void onAvailable(String placementId) {
            sendReactEvent("rewardedIsAvailable", "placementId", placementId);
        }

        @Override
        public void onUnavailable(String placementId) {
            sendReactEvent("rewardedIsUnavailable", "placementId", placementId);
        }

        @Override
        public void onCompletion(@NonNull String placementId, boolean userRewarded) {
            sendReactEvent("rewardedDidComplete", "placementId", placementId);
        }
    };

    public RNFairbidModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    private void sendReactEvent(final String eventName, final String key, final String value) {
        final WritableMap params = new WritableNativeMap();

        params.putString("name", eventName);
        params.putString(key, value);

        sendReactEvent(eventName, params);
    }

    private void sendReactEvent(final String eventName, @Nullable WritableMap params) {
        if (params == null) {
            params = new WritableNativeMap();
            params.putString("name", eventName);
        }
        getReactApplicationContext()
                .getJSModule(RCTDeviceEventEmitter.class)
                .emit("RNFairbidEvent", params);
    }

    @Override
    public String getName() {
        return "RNFairbid";
    }

    @ReactMethod
    public void initWithAppID(final String appId, final String userId) {
        final Activity activity = getCurrentActivity();

        if (activity != null) {
            FairBid.start(appId, activity);
            UserInfo.setUserId(userId);
            Banner.setBannerListener(bannerListener);
            Interstitial.setInterstitialListener(interstitialListener);
            Rewarded.setRewardedListener(rewardedListener);
        }
    }

    @ReactMethod
    public void presentTestSuite() {
        FairBid.showTestSuite(getCurrentActivity());
    }

    @ReactMethod
    public void requestInterstitialForPlacementID(final String placementId) {
        Interstitial.request(placementId);
    }
    @ReactMethod
    public void isInterstitialAvailableForPlacementID(final String placementId, final Callback callback) {
        callback.invoke(Interstitial.isAvailable(placementId));
    }
    @ReactMethod
    public void showInterstitialForPlacementID(final String placementId, final Callback callback) {
        if (Interstitial.isAvailable(placementId)) {
            Interstitial.show(placementId, getCurrentActivity());
            callback.invoke(true);
        } else {
            callback.invoke(false);
        }
    }

    @ReactMethod
    public void requestVideoForPlacementID(final String placementId) {
        Rewarded.request(placementId);
    }
    @ReactMethod
    public void isVideoAvailableForPlacementID(final String placementId, final Callback callback) {
        callback.invoke(Rewarded.isAvailable(placementId));
    }
    @ReactMethod
    public void showVideoForPlacementID(final String placementId, final Callback callback) {
        if (Rewarded.isAvailable(placementId)) {
            Rewarded.show(placementId, getCurrentActivity());

            callback.invoke(true);
        } else {
            callback.invoke(false);
        }
    }

    @ReactMethod
    public void showBannerInView(final String placementId,final String info,final Callback callback) {
        callback.invoke((Object)null);
    }
}
