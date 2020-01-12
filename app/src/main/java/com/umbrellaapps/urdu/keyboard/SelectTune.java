package com.umbrellaapps.urdu.keyboard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SelectTune extends AppCompatActivity {
    private static final String TAG = "TAG";
    private InterstitialAd mInterstitialAd;
    Boolean removeAds;
    SessionManager sessionManager;
    int flag = 0;
    private TextView lblKeyBeep,lblKeyBeep1;
    Animation anim;
    boolean isInstalled = false;
    ImageView imgbeep,imgbeep1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tune);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(this);

        final AdView mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B28DDF9144B293265D3087628FD72536")
                .addTestDevice("7A379E9A65205DEEA2CBCA6D89C8B197")
                .addTestDevice("4D246EBD4E16355CC2316AD57E5CFA36")
                .build();
        if (mAdView != null) {
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

        imgbeep=(ImageView)findViewById(R.id.imagebeep);
        imgbeep1=(ImageView)findViewById(R.id.imagebeep1);

        imgbeep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mMediaPlayer = MediaPlayer.create(SelectTune.this, R.raw.beep1);
                mMediaPlayer.start();
            }
        });

        imgbeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mMediaPlayer = MediaPlayer.create(SelectTune.this, R.raw.beep);
                mMediaPlayer.start();
            }
        });

        lblKeyBeep = (TextView) findViewById(R.id.lblKeyPress);
        lblKeyBeep1 = (TextView) findViewById(R.id.lblKeybeep1);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.otf");


        final CheckBox beep = (CheckBox) findViewById(R.id.popup_on);
        final CheckBox beep1 = (CheckBox) findViewById(R.id.beep1_on);

        if (sessionManager.getBeep()) {
            lblKeyBeep.setTextColor(Color.parseColor("#e728a5"));
        }
        if (sessionManager.getBeep1()) {
            lblKeyBeep1.setTextColor(Color.parseColor("#e728a5"));
        }

beep1.setChecked(sessionManager.getBeep1());
        beep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isChecked = false;
                if (((CheckBox) v).isChecked()) {
                    isChecked = true;
                    sessionManager.setBeep(false);
                    beep.setChecked(false);
                    lblKeyBeep.setTextColor(Color.parseColor("#040404"));

                    lblKeyBeep1.setTextColor(Color.parseColor("#e728a5"));
                } else {
                    lblKeyBeep1.setTextColor(Color.parseColor("#040404"));
                }
                // Save it to the shared preferences
                sessionManager.setBeep1(isChecked);
            }
        });

        beep.setChecked(sessionManager.getBeep());


        beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isChecked = false;
                if (((CheckBox) view).isChecked()) {
                    isChecked = true;
                    sessionManager.setBeep1(false);
                    beep1.setChecked(false);
                    lblKeyBeep1.setTextColor(Color.parseColor("#040404"));
                    lblKeyBeep.setTextColor(Color.parseColor("#e728a5"));
                } else {
                    lblKeyBeep.setTextColor(Color.parseColor("#040404"));
                }
                // Save it to the shared preferences
                sessionManager.setBeep(isChecked);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();

        return super.onOptionsItemSelected(item);
    }

}
