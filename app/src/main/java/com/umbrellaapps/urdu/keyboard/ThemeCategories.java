package com.umbrellaapps.urdu.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ThemeCategories extends AppCompatActivity {
Button select,gallery,defalt;
    SharedPreferences sharedPreferences;
    String encodedImage;
    static final int REQUEST_CODE_TO_BROWSE_IMAGE = 1;

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
        setContentView(R.layout.activity_theme_categories);


        initImageLoader(getApplicationContext());
        mContext = this;
        images = new Integer[]{
                R.drawable.personalize_theme,

        };

        bgImages = new String[]{
                "personalize_theme"
        };
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
                theme.setSelected(false);
                sessionManager.setKeyboardBackground(bgImages[i]);
            } else {
                theme.setSelected(false);
            }

            themesList.add(theme);
        }
        myAdapter = new MyAdapter(themesList, mContext);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Theme theme = themesList.get(i);
                theme.setSelected(false);
                themesList.get(i).setSelected(false);

                SharedPreferences sharedPreferences;
                String encodedImage;
                final int REQUEST_CODE_TO_BROWSE_IMAGE = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE_TO_BROWSE_IMAGE);//Storing Image in SharedPreferences


                // Save it the shared preferences
                sessionManager.setKeyboardBackground(theme.getBgImageId());
                sessionManager.setPosition(i);
                myAdapter.notifyDataSetChanged();

            }


        });


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.otf");
        defalt = (Button) findViewById(R.id.default_theme);
        defalt.setTypeface(tf);
//        gallery=(Button)findViewById(R.id.customkey_board);
//        gallery.setTypeface(tf);
        select = (Button) findViewById(R.id.select_them);
        select.setTypeface(tf);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemeCategories.this, ThemesActivity.class);
                startActivity(intent);
            }
        });


        defalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThemeCategories.this, Defaulttheme.class);
                startActivity(intent);
            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            InputStream stream;
            try {
                stream = getContentResolver().openInputStream(intent.getData());
                // Encoding Image into Base64
                Bitmap realImage = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                //Converting Base64 into String to Store in SharedPreferences
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                //NOw storing String to SharedPreferences
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("my_image", encodedImage);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Theme is Changed",Toast.LENGTH_LONG).show();
                Intent intent2=new Intent(ThemeCategories.this, MainActivity.class);
                startActivity(intent2);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
