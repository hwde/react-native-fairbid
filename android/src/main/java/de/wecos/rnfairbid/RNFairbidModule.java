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
import com.facebook.react.bridge.ReadableMap;
import com.fyber.FairBid;
import com.fyber.fairbid.ads.Banner;
import com.fyber.fairbid.ads.ImpressionData;
import com.fyber.fairbid.ads.Interstitial;
import com.fyber.fairbid.ads.Rewarded;
import com.fyber.fairbid.ads.banner.BannerError;
import com.fyber.fairbid.ads.banner.BannerListener;
import com.fyber.fairbid.ads.banner.BannerOptions;
import com.fyber.fairbid.ads.banner.SupportedCreativeSizes;
import com.fyber.fairbid.ads.interstitial.InterstitialListener;
import com.fyber.fairbid.ads.rewarded.RewardedListener;
import com.fyber.fairbid.user.UserInfo;

import java.util.Iterator;
import java.util.Map;

public class RNFairbidModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private final BannerListener bannerListener = new BannerListener() {
        @Override
        public void onError(@NonNull String placementId, BannerError error) {
            sendReactEvent("bannerDidFailToLoad", "placementId", placementId);
        }

        @Override
        public void onLoad(@NonNull String placementId) {
            sendReactEvent("bannerDidLoad", "placementId", placementId);
        }

        @Override
        public void onShow(@NonNull String placementId, ImpressionData impressionData) {
            sendReactEvent("bannerDidShow", "placementId", placementId);
        }

        @Override
        public void onClick(@NonNull String placementId) {
            sendReactEvent("bannerDidClick", "placementId", placementId);
        }

        @Override
        public void onRequestStart(@NonNull String placementId) {
            sendReactEvent("requestStart", "placementId", placementId);
        }
    };

    private final InterstitialListener interstitialListener = new InterstitialListener() {
        @Override
        public void onShow(@NonNull String placementId, ImpressionData impressionData) {
            sendReactEvent("interstitialDidShow", "placementId", placementId);
        }

        @Override
        public void onClick(@NonNull String placementId) {
            sendReactEvent("interstitialDidClick", "placementId", placementId);
        }

        @Override
        public void onHide(@NonNull String placementId) {
            sendReactEvent("interstitialDidDismiss", "placementId", placementId);
        }

        @Override
        public void onShowFailure(@NonNull String placementId, ImpressionData impressionData) {
            sendReactEvent("interstitialDidFailToShow", "placementId", placementId);
        }

        @Override
        public void onAvailable(@NonNull String placementId) {
            sendReactEvent("interstitialIsAvailable", "placementId", placementId);
        }

        @Override
        public void onUnavailable(@NonNull String placementId) {
            sendReactEvent("interstitialIsUnavailable", "placementId", placementId);
        }
        
        @Override
        public void onRequestStart(@NonNull String placementId) {
            sendReactEvent("requestStart", "placementId", placementId);
        }
    };

    private final RewardedListener rewardedListener = new RewardedListener() {
        @Override
        public void onShow(@NonNull String placementId, ImpressionData impressionData) {
            sendReactEvent("rewardedDidShow", "placementId", placementId);
        }

        @Override
        public void onClick(@NonNull String placementId) {
            sendReactEvent("rewardedDidClick", "placementId", placementId);
        }

        @Override
        public void onHide(@NonNull String placementId) {
            sendReactEvent("rewardedDidDismiss", "placementId", placementId);
        }

        @Override
        public void onShowFailure(@NonNull String placementId, ImpressionData impressionData) {
            sendReactEvent("rewardedDidFailToShow", "placementId", placementId);
        }

        @Override
        public void onAvailable(@NonNull String placementId) {
            sendReactEvent("rewardedIsAvailable", "placementId", placementId);
        }

        @Override
        public void onUnavailable(@NonNull String placementId) {
            sendReactEvent("rewardedIsUnavailable", "placementId", placementId);
        }

        @Override
        public void onCompletion(@NonNull @NonNull String placementId, boolean userRewarded) {
            sendReactEvent("rewardedDidComplete", "placementId", placementId);
        }

        @Override
        public void onRequestStart(@NonNull String placementId) {
            sendReactEvent("requestStart", "placementId", placementId);
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
    public void showBannerInView(final @Nullable ReadableMap options, final Callback callback) {
        BannerOptions bannerOptions = new BannerOptions();
        String placementId = options.getString("placementId");
        Iterator<Map.Entry<String, Object>> n = options.getEntryIterator();

        while (n.hasNext()) {
            Map.Entry<String, Object> key = n.next();

            switch(key.getKey()) {
                case "view": //TODO...
                    break;
                case "admobBannerSize":
                    bannerOptions = setAdmobBannersize(bannerOptions, key.getValue());
                    break;
                case "facebookBannerSize":
                    bannerOptions = setFacebookBannersize(bannerOptions, key.getValue());
                    break;
                case "fallbackSize":
                    bannerOptions = setFacebookBannersize(setAdmobBannersize(bannerOptions, key.getValue()), key.getValue());
                    break;
                case "placeAtTheBottom":
                    bannerOptions = bannerOptions.placeAtTheBottom();
                    break;
                case "placeAtTheTop":
                    bannerOptions = bannerOptions.placeAtTheTop();
                    break;
            }
        }

        if (placementId != null) {
            Banner.show(placementId, bannerOptions, getCurrentActivity());
            
            callback.invoke(true);
        } else {
            callback.invoke(false);
        }

    }

    private BannerOptions setAdmobBannersize(BannerOptions bannerOptions, Object value) {
        if (value instanceof String) {
            switch ((String) value) {
                case "banner": return bannerOptions.withNetworkSize(SupportedCreativeSizes.ADMOB_BANNER);
                case "largeBanner": return bannerOptions.withNetworkSize(SupportedCreativeSizes.ADMOB_LARGE_BANNER);
                case "leaderboard": return bannerOptions.withNetworkSize(SupportedCreativeSizes.ADMOB_LEADERBOARD);
                case "fullBanner": return bannerOptions.withNetworkSize(SupportedCreativeSizes.ADMOB_FULL_BANNER);
                case "flexibleWidthPortrait": return bannerOptions.withNetworkSize(SupportedCreativeSizes.ADMOB_SMART_BANNER); //UNSURE
                case "flexibleWidthLandscape": return bannerOptions.withNetworkSize(SupportedCreativeSizes.ADMOB_LARGE_BANNER); //UNSURE
            }
        }

        return bannerOptions;
    }

    private BannerOptions setFacebookBannersize(BannerOptions bannerOptions, Object value) {
        if (value instanceof String) {
            switch ((String) value) {
                case "flexibleWidthHeight50": return bannerOptions.withNetworkSize(SupportedCreativeSizes.FACEBOOK_BANNER_HEIGHT_50);
                case "flexibleWidthHeight90": return bannerOptions.withNetworkSize(SupportedCreativeSizes.FACEBOOK_BANNER_HEIGHT_90);
                case "fb320x50": return bannerOptions.withNetworkSize(SupportedCreativeSizes.FACEBOOK_BANNER_320_50);
                case "fbRectangle250": return bannerOptions.withNetworkSize(SupportedCreativeSizes.FACEBOOK_BANNER_RECTANGLE_250);
            }
        }

        return bannerOptions;
    }

    @ReactMethod
    public void GDPRConsent(final boolean consentFlag) {
        UserInfo.setGdprConsent(consentFlag, getCurrentActivity());
    }
    @ReactMethod
    public void GDPRConsentString(final String consentString) {
        UserInfo.setGdprConsentString(consentString, getCurrentActivity());
    }
    @ReactMethod
    public void clearGDPRConsent() {
        UserInfo.clearGdprConsent(getCurrentActivity());
    }
}
