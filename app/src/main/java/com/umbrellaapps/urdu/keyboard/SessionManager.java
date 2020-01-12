package com.umbrellaapps.urdu.keyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class SessionManager {

    private static final String PREFS_NAME = "keyboard_prefs";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private String removeAdsKey = "REMOVE_ADS";
    private Boolean removeAds;
    private Boolean sound, vibration, popup,beep,beep1;
    private String keyboardBackground;
    private String soundKey = "SOUND";
    private String beepKey = "BEEP";
    private String beep1Key = "BEEP1";
    private String vibrationKey = "VIBRATION";
    private String popupKey = "POPUP";
    private String dialKey="DIAL";
    private String keyboardBgKey = "BACKGROUND";
    private int position = 0;
    private String positionKey = "POSITION";
    private boolean isRequestCompleted = false;

    private ArrayList<String> custom;

    Context mContext;

    public SessionManager(Context context)

    {
        this.mContext = context;
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = mPrefs.edit();
    }

    Boolean getRemoveAds() {
        removeAds = mPrefs.getBoolean(removeAdsKey, false);
        return removeAds;
    }

    void setRemoveAds(Boolean removeAds) {
        this.removeAds = removeAds;
        editor.putBoolean(removeAdsKey, removeAds);
        editor.apply();
    }

 //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//public Boolean getDial(){
//        dial=mPrefs.getBoolean(dialKey,false);
//        return dial;
//}
//public void setDial( Boolean dial){
//    this.dial=dial;
//    editor.putBoolean(dialKey,dial);
//    editor.apply();
//}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public Boolean getSound() {
        sound = mPrefs.getBoolean(soundKey, false);
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
        editor.putBoolean(soundKey, sound);
        editor.apply();
    }

    public Boolean getVibration() {
        vibration = mPrefs.getBoolean(vibrationKey, false);
        return vibration;
    }

    public void setVibration(Boolean vibration) {
        this.vibration = vibration;
        editor.putBoolean(vibrationKey, vibration);
        editor.apply();
    }

    public Boolean getPopup() {
        popup = mPrefs.getBoolean(popupKey, true);
        return popup;
    }

    public void setPopup(Boolean popup) {
        this.popup = popup;
        editor.putBoolean(popupKey, popup);
        editor.apply();
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getBeep() {
        beep = mPrefs.getBoolean(beepKey, true);
        return beep;
    }

    public void setBeep(Boolean beep) {
        this.beep = beep;
        editor.putBoolean(beepKey, beep);
        editor.apply();
    }

    public Boolean getBeep1() {
        beep1 = mPrefs.getBoolean(beep1Key, true);
        return beep1;
    }

    public void setBeep1(Boolean beep1) {
        this.beep1 = beep1;
        editor.putBoolean(beep1Key, beep1);
        editor.apply();
    }

    public String getKeyboardBackground() {
        keyboardBackground = mPrefs.getString(keyboardBgKey, "black");
        return keyboardBackground;
    }

    public void setKeyboardBackground(String keyboardBackground) {
        this.keyboardBackground = keyboardBackground;
        editor.putString(keyboardBgKey, keyboardBackground);
        editor.apply();
    }

    public int getPosition() {
        position = mPrefs.getInt(positionKey, 0);
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        editor.putInt(positionKey, position);
        editor.apply();
    }

    public boolean saveArray(ArrayList<String> array, String arrayName) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(arrayName + "_size", array.size());
        for (int i = 0; i < array.size(); i++)
            editor.putString(arrayName + "_" + i, array.get(i));
        return editor.commit();
    }

    public ArrayList<String> loadArray(String arrayName) {
        int size = mPrefs.getInt(arrayName + "_size", 0);
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 0; i < size; i++)
            arr.add(mPrefs.getString(arrayName + "_" + i, null));
        return arr;
    }

/*    public void saveMap(Map<String, String> inputMap, String name) {
        try {
            if (mPrefs != null) {
                JSONObject jsonObject = new JSONObject(inputMap);
                String jsonString = jsonObject.toString();

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putString(name, jsonString);
                editor.apply();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Map<String, String> loadMap(String name) {
        Map<String, String> outputMap = new HashMap<>();
        try {
            if (mPrefs != null) {
                String jsonString = mPrefs.getString(name, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    String value = (String) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

    public boolean isRequestCompleted() {
        return mPrefs.getBoolean("IS_REQUEST_COMPLETED", false);
    }

    public void setRequestCompleted(boolean requestCompleted) {
        isRequestCompleted = requestCompleted;
        editor.putBoolean("IS_REQUEST_COMPLETED", isRequestCompleted);
        editor.apply();
    }*/

}
