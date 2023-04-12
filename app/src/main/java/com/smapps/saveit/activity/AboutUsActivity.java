package com.smapps.saveit.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.smapps.saveit.BuildConfig;
import com.smapps.saveit.R;
import com.smapps.saveit.databinding.ActivityAboutUsBinding;
import com.smapps.saveit.util.AdsUtils;
import com.smapps.saveit.util.AppLangSessionManager;

import java.util.Locale;

public class AboutUsActivity extends AppCompatActivity {
    AboutUsActivity activity;
    ActivityAboutUsBinding binding;
    AppLangSessionManager appLangSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(AboutUsActivity.this);
        setLocale(appLangSessionManager.getLanguage());

        AdsUtils.showGoogleBannerAd(activity,binding.adView);

        binding.head.tvTitle.setText(activity.getResources().getString(R.string.aboutus_name));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tvVersion.setText(activity.getResources().getString(R.string.version_name)+" "+ BuildConfig.VERSION_NAME);

    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}
