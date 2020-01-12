package com.umbrellaapps.urdu.keyboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class FooterFragment extends Fragment {

    LinearLayout btnRemoveAds;
    private InterstitialAd mInterstitialAd;
    static final String ITEM_SKU = "com.English.type.keyboard.free";
    private boolean removeAds = false;

    // Library

    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.footer, container, false);
        sessionManager = new SessionManager(getActivity());
        removeAds = sessionManager.getRemoveAds();

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();


        LinearLayout btnShare = (LinearLayout) view.findViewById(R.id.btn_share);
        LinearLayout btnRate = (LinearLayout) view.findViewById(R.id.btn_rate);
        LinearLayout btnMoreApps = (LinearLayout) view.findViewById(R.id.btn_more_apps);
        btnRemoveAds = (LinearLayout) view.findViewById(R.id.btn_remove_ads);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate("com.English.type.keyboard.free");
            }
        });

        btnMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreApps();
            }
        });



        if (removeAds) {
            btnRemoveAds.setVisibility(View.GONE);
        }
        return view;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void share() {
        String shareBody = "https://play.google.com/store/apps/details?id=com.English.type.keyboard.free";
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "English  Keyboard");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void rate(String app_link) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_link)));
        } catch (android.content.ActivityNotFoundException ex) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + app_link)));
        }
    }

    private void moreApps() {
        String str = "https://play.google.com/store/apps/developer?id=Softcrust+Solution";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    }

}