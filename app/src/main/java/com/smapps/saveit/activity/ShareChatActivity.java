package com.smapps.saveit.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.smapps.saveit.R;
import com.smapps.saveit.api.CommonClassForAPI;
import com.smapps.saveit.databinding.LayoutHowToBinding;
import com.smapps.saveit.databinding.LayoutNewUiBinding;
import com.smapps.saveit.util.AppLangSessionManager;
import com.smapps.saveit.util.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.smapps.saveit.util.Utils.RootDirectoryShareChat;
import static com.smapps.saveit.util.Utils.createFileFolder;
import static com.smapps.saveit.util.Utils.startDownload;

public class ShareChatActivity extends AppCompatActivity {
    LayoutNewUiBinding binding;
    ShareChatActivity activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;
    private UnifiedNativeAd nativeAd;



    LayoutHowToBinding layoutHowToBinding;
    BottomSheetDialog dialogHowTo;


    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.layout_new_ui);

        activity = this;

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
        initViews();
        InterstitialAdsINIT();
        LoadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        binding.head.tvTitle.setText(activity.getResources().getString(R.string.share_chat_app_name));
        binding.head.imAppLogo.setVisibility(View.VISIBLE);
        binding.head.imAppLogo.setImageResource(R.drawable.sharechat);
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        dialogHowTo = new BottomSheetDialog(activity, R.style.SheetDialog);
        dialogHowTo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutHowToBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout. layout_how_to, null, false);
        dialogHowTo.setContentView(layoutHowToBinding.getRoot());

        binding.tvHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogHowTo.show();
            }
        });


        binding.tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, GalleryActivity.class);
                i.putExtra("Value","0");
                startActivity(i);
            }
        });

        binding.tvOptionName.setText(activity.getResources().getString(R.string.share_chat_app_name));

        Glide.with(activity)
                .load(R.drawable.sc1)
                .into(layoutHowToBinding.imHowto1);

        Glide.with(activity)
                .load(R.drawable.app2)
                .into(layoutHowToBinding.imHowto2);

        Glide.with(activity)
                .load(R.drawable.sc1)
                .into(layoutHowToBinding.imHowto3);

        Glide.with(activity)
                .load(R.drawable.sc4)
                .into(layoutHowToBinding.imHowto4);

        layoutHowToBinding.tvHowToHeadOne.setVisibility(View.GONE);
        layoutHowToBinding.LLHowToOne.setVisibility(View.GONE);
        layoutHowToBinding.tvHowToHeadTwo.setText(getResources().getString(R.string.how_to_download));

        layoutHowToBinding.tvHowTo1.setText(getResources().getString(R.string.opn_sharechat));
        layoutHowToBinding.tvHowTo3.setText(getResources().getString(R.string.copy_video_link_frm_sharechat));

        binding.imDownload.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString().trim();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetShareChatData();
            }
        });


        binding.head.imAppLogo.setOnClickListener(v -> {
            Utils.OpenApp(activity, "in.mohalla.sharechat");
        });


    }

    private void GetShareChatData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);

            if (host.contains("sharechat")) {
                Utils.showProgressDialog(activity);
                new callGetShareChatData().execute(binding.etText.getText().toString());
                showInterstitial();
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("sharechat")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("sharechat")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("sharechat")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetShareChatData extends AsyncTask<String, Void, Document> {
        Document ShareChatDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                ShareChatDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return ShareChatDoc;
        }

        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog(activity);
            try {

                VideoUrl = result.select("meta[property=\"og:video:secure_url\"]").last().attr("content");
                Log.e("onPostExecute: ", VideoUrl);
                if (!VideoUrl.equals("")) {
                    try {
                        startDownload(VideoUrl, RootDirectoryShareChat, activity, "sharechat_"+System.currentTimeMillis() + ".mp4");
                        VideoUrl = "";
                        binding.etText.setText("");

                        showInterstitial();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName() + ".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void LoadAd() {
        binding.myTemplate.setVisibility(View.GONE);
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdLoader adLoader = new AdLoader.Builder(activity, activity.getResources().getString(R.string.admob_native_ad))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;

                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder()
                                .withCallToActionBackgroundColor
                                        (new ColorDrawable(ContextCompat.getColor(activity, R.color.colorAccent)))
                                .build();
                        binding.myTemplate.setStyles(styles);
                        binding.myTemplate.setNativeAd(unifiedNativeAd);
                        binding.myTemplate.setVisibility(View.VISIBLE);
                    }
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }


    //InterstitialAd : Start

    public void InterstitialAdsINIT(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });


        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_interstitial_ad));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });


    }


    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    //InterstitialAd : End

}