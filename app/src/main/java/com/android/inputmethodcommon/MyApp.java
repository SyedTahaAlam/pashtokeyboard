package com.android.inputmethodcommon;

import android.app.Application;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/pashto.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/pashto.ttf");
    }
}
