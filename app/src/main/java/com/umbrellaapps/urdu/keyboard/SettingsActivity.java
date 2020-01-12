package com.umbrellaapps.urdu.keyboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;
    Boolean removeAds;
    SessionManager sessionManager;
    int flag = 0;
    private TextView lblKeyPress, lblKeySound, lblKeyVibrate;
    Animation anim;
    boolean isInstalled = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sessionManager = new SessionManager(this);

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
                if (flag == 1) {
                    showThemes();
                } else {
                    finish();
                }
            }
        });

        requestNewInterstitial();


        lblKeyPress = (TextView) findViewById(R.id.lblKeyPress);
        lblKeySound = (TextView) findViewById(R.id.lblKeySound);
        lblKeyVibrate = (TextView) findViewById(R.id.lblKeyVibrate);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.otf");

        final CheckBox sound = (CheckBox) findViewById(R.id.sound_on);
        final CheckBox vibration = (CheckBox) findViewById(R.id.vibrate_on);
        final CheckBox popUp = (CheckBox) findViewById(R.id.popup_on);

        if (sessionManager.getPopup()) {
            lblKeyPress.setTextColor(Color.parseColor("#e728a5"));
        }

        if (sessionManager.getSound()) {
            lblKeySound.setTextColor(Color.parseColor("#e728a5"));
        }

        if (sessionManager.getVibration()) {
            lblKeyVibrate.setTextColor(Color.parseColor("#e728a5"));
        }

        sound.setChecked(sessionManager.getSound());
        vibration.setChecked(sessionManager.getVibration());
        popUp.setChecked(sessionManager.getPopup());

        sound.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Boolean isChecked = false;
                if (((CheckBox) view).isChecked()) {
                    isChecked = true;


vibration.setChecked(false);
lblKeyVibrate.setTextColor(Color.parseColor("#040404"));
sessionManager.setVibration(false);
popUp.setChecked(false);
lblKeyPress.setTextColor(Color.parseColor("#040404"));
sessionManager.setPopup(false);


                    lblKeySound.setTextColor(Color.parseColor("#e728a5"));
                } else {
                    lblKeySound.setTextColor(Color.parseColor("#040404"));
                }
                // Save it to the shared preferences
                sessionManager.setSound(isChecked);
            }
        });

        vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isChecked = false;
                if (((CheckBox) view).isChecked()) {
                    isChecked = true;


                    popUp.setChecked(false);
                    lblKeyPress.setTextColor(Color.parseColor("#040404"));
                    sessionManager.setPopup(false);
                    sound.setChecked(false);
                    lblKeySound.setTextColor(Color.parseColor("#040404"));
                    sessionManager.setSound(false);


                    lblKeyVibrate.setTextColor(Color.parseColor("#e728a5"));
                } else {
                    lblKeyVibrate.setTextColor(Color.parseColor("#040404"));
                }
                // Save it to the shared preferences
                sessionManager.setVibration(isChecked);
            }
        });

        popUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isChecked = false;
                if (((CheckBox) view).isChecked()) {
                    isChecked = true;

                    sound.setChecked(false);
                    lblKeySound.setTextColor(Color.parseColor("#040404"));
                    sessionManager.setSound(false);
                    vibration.setChecked(false);
                    lblKeyVibrate.setTextColor(Color.parseColor("#040404"));
                    sessionManager.setVibration(false);



                    lblKeyPress.setTextColor(Color.parseColor("#e728a5"));
                } else {
                    lblKeyPress.setTextColor(Color.parseColor("#040404"));
                }
                // Save it to the shared preferences
                sessionManager.setPopup(isChecked);
            }
        });

        Button btnThemes = (Button) findViewById(R.id.btn_themes);
        btnThemes.setTypeface(tf);
        btnThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    flag = 1;
                    mInterstitialAd.show();
                } else {
                    showThemes();
                }
            }
        });

        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);
        anim.setDuration(1500);

        final LinearLayout promotionalAdsContainer = (LinearLayout) findViewById(R.id.promotionalAdsContainer);
//       Promotions promotions = new Promotions(SettingsActivity.this);
       /* if (Helper.isNetworkAvailable(SettingsActivity.this) && promotions.getPromotionalAds()) {
            HashMap<String, String> promotionalAds = (HashMap<String, String>) sessionManager.loadMap("PROMOTIONAL_ADS");
            if (promotionalAds.size() > 0) {
                Iterator iterator = promotionalAds.keySet().iterator();
                while (iterator.hasNext()) {
                    final String key = (String) iterator.next();
                    String value = promotionalAds.get(key);
                    final ImageView iv = new ImageView(SettingsActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            getResources().getDimensionPixelSize(R.dimen.dp60),
                            getResources().getDimensionPixelSize(R.dimen.dp60)
                    );
                    params.setMargins(10, 10, 10, 10);
                    iv.setLayoutParams(params);
                    final PackageManager pm = getPackageManager();
                    String imageUrl = Helper.fileBaseUrl + value;
                    RequestQueue mRequestQueue = Volley.newRequestQueue(SettingsActivity.this);
                    ImageRequest ir = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            if (response != null && !Helper.isPackageInstalled(key, pm)) {
                                iv.setImageBitmap(response);
                                iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Helper.rate(SettingsActivity.this, key);
                                    }
                                });
                                iv.setAnimation(anim);
                                promotionalAdsContainer.addView(iv);
                            } else {
                                Log.d(TAG, "onCreate: bitmap is null");
                            }
                        }
                    }, 0, 0, null, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    mRequestQueue.add(ir);
                }*/

    }
//        }

//    }






    private void showThemes() {
        startActivity(new Intent(SettingsActivity.this, ThemesActivity.class));
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4D246EBD4E16355CC2316AD57E5CFA36").build();
        mInterstitialAd.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();

        return super.onOptionsItemSelected(item);
    }

}
