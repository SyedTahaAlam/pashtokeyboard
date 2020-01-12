package com.umbrellaapps.urdu.keyboard;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.umbrellaapps.urdu.keyboard.utils.Helper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    InputMethodManager inputMethodManager;
    Button btnEnable;
    Button btnSelect;
    Button btnThemes;
    Button btnSettings;
    Button btntune;
    SessionManager sessionManager;
    TextView btnEnableIcon, btnSelectIcon,btnSelectLanguag,btnSelectTune;
    private InterstitialAd mInterstitialAd;
    int flag = 0;
    Boolean removeAds;
    // Volley
    Dialog d;
    boolean isInstalled = false;
    PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        removeAds = sessionManager.getRemoveAds();

        /*====================================================
        *               Volley
        * ====================================================*/

        pm = getPackageManager();

      /*  // Promotional Ads
        if (Helper.isNetworkAvailable(MainActivity.this)) {
            showAds();
        }*/

        /*====================================================*/

        final AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B28DDF9144B293265D3087628FD72536")
                .addTestDevice("7A379E9A65205DEEA2CBCA6D89C8B197")
                .addTestDevice("4D246EBD4E16355CC2316AD57E5CFA36")
                .build();
        if (mAdView != null && !removeAds) {
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
                if (flag == 1) {
                    showThemes();
                }
            }
        });

        requestNewInterstitial();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////
        inputMethodManager=(InputMethodManager)getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.otf");

        btnEnable = (Button) findViewById(R.id.btn_enable);
        btnEnable.setTypeface(tf);
        btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setTypeface(tf);
        btnThemes = (Button) findViewById(R.id.btn_themes);
        btnThemes.setTypeface(tf);
        btnSettings = (Button) findViewById(R.id.btn_settings);
        btnSettings.setTypeface(tf);
        btnSelectLanguag=(Button)findViewById(R.id.btn_select_language);
        btnSelectLanguag.setTypeface(tf);
        btnSelectTune=(Button)findViewById(R.id.btn_select_Tune);
        btnSelectTune.setTypeface(tf);
        btnSelectTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SelectTune.class);
                startActivity(intent);
            }
        });



        btnSelectLanguag.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                inputMethodManager.showInputMethodAndSubtypeEnabler("");
            }
        });

        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableInputMethod();
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputMethodPicker();
            }
        });

        btnThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!removeAds && mInterstitialAd.isLoaded()) {
                    flag = 1;
                    mInterstitialAd.show();
                } else {
                    showThemes();
                }
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettings();
            }
        });

        btnEnableIcon = (TextView) findViewById(R.id.btn_enable_icon);
        btnSelectIcon = (TextView) findViewById(R.id.btn_select_icon);

    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4D246EBD4E16355CC2316AD57E5CFA36").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void showThemes() {
        startActivity(new Intent(MainActivity.this, ThemeCategories.class));
    }

    private void showSettings() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }


    // Enable keyboard
    private void enableInputMethod() {
        this.startActivityForResult(new Intent("android.settings.INPUT_METHOD_SETTINGS"), 0);
    }

    // Select keyboard
    private void showInputMethodPicker() {
        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        String list = im.getEnabledInputMethodList().toString();
        if (list.contains(getPackageName())) {
            btnEnable.setEnabled(false);
            btnEnable.setTextColor(Color.parseColor("#3F2900"));
            btnSelect.setEnabled(true);
            btnSelect.setTextColor(getResources().getColor(R.color.candidate_normal));
        } else {
            btnEnable.setEnabled(true);
            btnEnable.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            btnSelect.setEnabled(false);
            btnSelect.setTextColor(Color.parseColor("#3F2900"));
        }
    }

    private void showCustomInterstitialDialog(final ArrayList<String> customInterstitial) {
        d = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        d.setContentView(R.layout.interstitial_dialog);
        d.setCancelable(true);

        isInstalled = Helper.isPackageInstalled(customInterstitial.get(0), pm);
        final ImageView ivInterstitial = (ImageView) d.findViewById(R.id.ivInterstitial);

        Button btnClose = (Button) d.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        String imageUrl = Helper.fileBaseUrl + customInterstitial.get(1);
        RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        ImageRequest ir = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (!removeAds && response != null && !customInterstitial.get(0).equals("") && !isInstalled) {
                    ivInterstitial.setImageBitmap(response);
                    ivInterstitial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Helper.rate(MainActivity.this, customInterstitial.get(0));
                        }
                    });
                    d.show();
                }
            }
        }, 0, 0, null, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(ir);
    }

   /* public void showAds() {
        Promotions promotions = new Promotions(MainActivity.this);
        if (Helper.isNetworkAvailable(MainActivity.this) && promotions.getCustomInterstitialAds()) {
            ArrayList<String> customInterstitial = sessionManager.loadArray("CUSTOM_INTERSTITIAL");
            if (customInterstitial.size() > 0) {
                showCustomInterstitialDialog(customInterstitial);
            }
        }
    }*/
}
