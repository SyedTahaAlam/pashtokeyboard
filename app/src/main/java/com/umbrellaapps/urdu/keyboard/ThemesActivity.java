package com.umbrellaapps.urdu.keyboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umbrellaapps.urdu.keyboard.adapters.MyAdapter;
import com.umbrellaapps.urdu.keyboard.models.Theme;

import java.util.ArrayList;
import java.util.List;

public class ThemesActivity extends AppCompatActivity {

    private List<Theme> themesList;
    private MyAdapter myAdapter;
    Integer[] images;
    String[] bgImages;
    SessionManager sessionManager;
    Context mContext;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        initImageLoader(getApplicationContext());
        mContext = this;
        images = new Integer[]{
                R.drawable.keyboard_1,
                R.drawable.keyboard_2,
                R.drawable.keyboard_3,
                R.drawable.keyboard_4,
                R.drawable.keyboard_5,
                R.drawable.keyboard_6,
                R.drawable.keyboard_7,
                R.drawable.keyboard_8,
                R.drawable.keyboard_9,
                R.drawable.mainimage,
                R.drawable.kbg
        };

        bgImages = new String[]{
                "keyboard_bg1",//0bl
                "keyboard_bg2",//1
                "keyboard_bg3",//2bl
                "keyboard_bg4",//3
                "keyboard_bg5",//4
                "keyboard_bg6",//5
                "keyboard_bg7",//6bl
                "keyboard_bg8",//7bl
                "keyboard_bg9",//8bl
                "keybg",//9
                "kbg"

        };
        final AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B28DDF9144B293265D3087628FD72536")
                .addTestDevice("7A379E9A65205DEEA2CBCA6D89C8B197")
                .addTestDevice("4D246EBD4E16355CC2316AD57E5CFA36")
                .build();
        if (mAdView != null ) {
            mAdView.loadAd(adRequest);
        }
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

        requestNewInterstitial();
        sessionManager = new SessionManager(this);
        ListView list = (ListView) findViewById(R.id.list);
        themesList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            Theme theme = new Theme();
            theme.setImageId(images[i]);
            theme.setBgImageId(bgImages[i]);
            if (i == sessionManager.getPosition()) {
                theme.setSelected(true);
                sessionManager.setKeyboardBackground(bgImages[i]);
            } else {
                theme.setSelected(false);
            }

            themesList.add(theme);
        }
        myAdapter = new MyAdapter(themesList, mContext);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Theme theme = themesList.get(i);
                theme.setSelected(true);
                for (int k = 0; k < themesList.size(); k++) {
                    if (k != i) {
                        themesList.get(k).setSelected(false);
                    }
                }
                // Save it the shared preferences
                sessionManager.setKeyboardBackground(theme.getBgImageId());
                sessionManager.setPosition(i);
                myAdapter.notifyDataSetChanged();
            }
        });


    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4D246EBD4E16355CC2316AD57E5CFA36").build();
        mInterstitialAd.loadAd(adRequest);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).
                threadPriority(3).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build());
    }
}
