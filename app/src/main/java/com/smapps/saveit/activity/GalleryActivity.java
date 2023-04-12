package com.smapps.saveit.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.smapps.saveit.R;
import com.smapps.saveit.adapter.TabAdapter;
import com.smapps.saveit.databinding.ActivityGalleryBinding;
import com.smapps.saveit.fragment.AllinOneGalleryFragment;
import com.smapps.saveit.util.AppLangSessionManager;
import com.smapps.saveit.util.Utils;

import java.util.Locale;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.smapps.saveit.util.Utils.createFileFolder;

public class GalleryActivity  extends AppCompatActivity {
    GalleryActivity activity;
    ActivityGalleryBinding binding;

    AppLangSessionManager appLangSessionManager;
    private int[] tabIcons = {
            R.drawable.sharechat,
            R.drawable.ic_roposo,
            R.drawable.insta_logo,
            R.drawable.whatsapp_logo,
            R.drawable.tiktok_logo,
            R.drawable.fb,
            R.drawable.ic_twitter,
            R.drawable.likee_logo,
            R.drawable.josh_logo,
            R.drawable.chingari,
            R.drawable.mitron,
            R.drawable.moj,
            R.drawable.mxtakatak,
            R.drawable.snackvideo

    };
    int PositionIntent = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        activity = this;

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        initViews();
    }

    public void initViews() {
        try {
            PositionIntent = Integer.parseInt(getIntent().getStringExtra("Value"));
        }catch (Exception e){
            e.printStackTrace();
        }
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
        setupTabIcons();
        binding.viewpager.setCurrentItem(PositionIntent);
        binding.head.tvTitle.setText(activity.getResources().getString(R.string.gallery_name));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TextView tv=(TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab,null);
            binding.tabs.getTabAt(i).setCustomView(tv);
        }*/

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {

        TabAdapter adapter = new TabAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryShareChatShow), "ShareChat");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryRoposoShow), "Roposo");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryInstaShow), "Instagram");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryWhatsappShow), "Whatsapp");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryTikTokShow), "TikTok");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryFacebookShow), "Facebook");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryTwitterShow), "Twitter");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectoryLikeeShow), "Likee");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYJOSHSHOW), "Josh");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYCHINGARISHOW), "Chingari");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYMITRONSHOW), "Mitron");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYMOJSHOW), "Moj");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.ROOTDIRECTORYMXSHOW), "MXTakaTak");
        adapter.addFragment(new AllinOneGalleryFragment(Utils.RootDirectorySnackVideoShow), "Snack Video");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);

    }
    private void setupTabIcons() {
        binding.tabs.getTabAt(0).setIcon(tabIcons[0]);
        binding.tabs.getTabAt(1).setIcon(tabIcons[1]);
        binding.tabs.getTabAt(2).setIcon(tabIcons[2]);
        binding.tabs.getTabAt(3).setIcon(tabIcons[3]);
        binding.tabs.getTabAt(4).setIcon(tabIcons[4]);
        binding.tabs.getTabAt(5).setIcon(tabIcons[5]);
        binding.tabs.getTabAt(6).setIcon(tabIcons[6]);
        binding.tabs.getTabAt(7).setIcon(tabIcons[7]);
        binding.tabs.getTabAt(8).setIcon(tabIcons[8]);
        binding.tabs.getTabAt(9).setIcon(tabIcons[9]);
        binding.tabs.getTabAt(10).setIcon(tabIcons[10]);
        binding.tabs.getTabAt(11).setIcon(tabIcons[11]);
        binding.tabs.getTabAt(12).setIcon(tabIcons[12]);
        binding.tabs.getTabAt(13).setIcon(tabIcons[13]);

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
