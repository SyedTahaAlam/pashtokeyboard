/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.umbrellaapps.urdu.keyboard;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umbrellaapps.urdu.keyboard.custom.ExTextView;

import java.util.List;

public class LatinKeyboardView extends KeyboardView {

    static final int KEYCODE_OPTIONS = -100;
    static final int KEYCODE_LANGUAGE_SWITCH = -101;
    Context context;
    SessionManager sessionManager;
    String[]  bgImages = new String[]{
            "keyboard_bg1",
            "keyboard_bg2",
            "keyboard_bg3",
            "keyboard_bg4",
            "keyboard_bg5",
            "keyboard_bg6",
            "keyboard_bg7",
            "keyboard_bg8",
            "keyboard_bg9",
            "keybg"
    };
    public LatinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sessionManager = new SessionManager(context.getApplicationContext());
        this.context = context;
    }

    public LatinKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean onLongPress(Key key) {
        if (key.codes[0] == Keyboard.KEYCODE_CANCEL) {
            getOnKeyboardActionListener().onKey(KEYCODE_OPTIONS, null);
            return true;
        } else {
            return super.onLongPress(key);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Typeface mFace;
        mFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/pashto.ttf");
        Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(46);
        paint.setTypeface(mFace);
        paint.setColor(Color.BLACK);
        Paint PAINT2 = new Paint();
        PAINT2.setColor(Color.BLACK);

        PAINT2.setTextSize(16);
        ColorFilter filter = new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        PAINT2.setColorFilter(filter);
        PAINT2.setColor(Color.BLACK);
        //FOR THE FULL STOP TO APPEAR LARGE
        Paint paint1 =  new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setTextSize(130);

        paint1.setTypeface(mFace);
        paint1.setColor(Color.BLACK);
        Bitmap mBitmap =null;

        Log.e("background",sessionManager.getKeyboardBackground());
        String background=sessionManager.getKeyboardBackground();

        //this will change the color of the character that will be drawn on to the button of the keyboard

        if(background.equalsIgnoreCase(bgImages[1])  ||  background.equalsIgnoreCase(bgImages[5])   ||  background.equalsIgnoreCase(bgImages[3])||   background.equalsIgnoreCase(bgImages[4])  ||   background.equalsIgnoreCase(bgImages[9])){
            paint1.setColor(Color.WHITE);
            ColorFilter filters = new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            PAINT2.setColorFilter(filters);

            paint.setColor(Color.WHITE);
        }

        List<Key> keys = getKeyboard().getKeys();
        Key previous=null;
        for(Key key: keys) {

            if(String.valueOf((char)key.codes[0]).equals("ه")){
                canvas.drawText("ھ", key.x + (key.width-(key.width*0.3f)), key.y + key.height*0.4f, paint);
            }

            if(String.valueOf((char)key.codes[0]).equals("ی")){
                canvas.drawText("ئ", key.x +(key.width-(key.width*.3f)), key.y + key.height*0.4f, paint);
            }
            if(key.codes[0]==412){
                Log.e("no",Integer.valueOf(key.codes[0])+"");
                Log.e("name","here");
                canvas.drawText("ړ", key.x + (key.width-(key.width*0.3f)), key.y + key.height*.4f, paint);
            }
            if(key.codes[0]==689){
                canvas.drawText("\u06C7", key.x + (key.width-(key.width*0.4f)), key.y + key.height*0.55f, paint1);
            }
            if(String.valueOf((char)key.codes[0]).equals("ʮ")){
            }


            if(String.valueOf((char)key.codes[0]).equals("ن")){
                Log.e("ن","agaya hai");
                canvas.drawText("ڼ", key.x + (key.width-(key.width*0.3f)), key.y + key.height*0.4f, paint);
            }
            if(String.valueOf((char)key.codes[0]).equals("ش")){
                canvas.drawText("\u069A", key.x + (key.width-(key.width*0.4f)), key.y + key.height*0.4f, paint);
            }

            //THIS IS TO SHOW ALIF AT THE TOP OF ADHA ALIF
            if(String.valueOf((char)key.codes[0]).equals("ل")){
                previous=key;
                continue;

            }

            if(key.codes[0]==627){

                canvas.drawText("\u06C9", key.x + (key.width-(key.width*0.3f)), key.y + key.height*0.4f, paint);


            }
            if(key.codes[0]==698){

                canvas.drawText("\u0696", key.x + (key.width-(key.width*0.2f)), key.y + key.height*0.4f, paint);


            }
            if(String.valueOf((char)key.codes[0]).equals("\u06D2")){
                canvas.drawText("\u06D0", key.x + (key.width-(key.width*0.2f)), key.y + key.height*0.4f, paint);
            }


            if(String.valueOf((char)key.codes[0]).equals("و")){

//
//
//                        mBitmap= BitmapFactory.decodeResource(context.getResources(),
//                        R.drawable.wao);
//
//                        canvas.drawBitmap(mBitmap, key.x + (key.width-(key.width*0.2f)),key.y + key.height*0.3f,paint);
                canvas.drawText("\u06C4", key.x + (key.width-(key.width*0.2f)), key.y + key.height*0.4f, paint);



            }

            if(key.codes[0]==6714){



                mBitmap= BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.h);

                canvas.drawBitmap(mBitmap, key.x + (key.width-(key.width*0.3f)),key.y + key.height*0.25f,PAINT2);



            }

            if(key.codes[0]==628){



                mBitmap= BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.v);

                canvas.drawBitmap(mBitmap, key.x + (key.width-(key.width*0.3f)),key.y + key.height*0.25f,PAINT2);



            }

            if(key.codes[0]==633){



                mBitmap= BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.shada);

                canvas.drawBitmap(mBitmap, key.x + (key.width-(key.width*0.3f)),key.y + key.height*0.25f,PAINT2);



            }
            if(key.codes[0]==652){



                mBitmap= BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.sukoon);

                canvas.drawBitmap(mBitmap, key.x + (key.width-(key.width*0.5f)),key.y + key.height*0.4f,PAINT2);



            }
            if(key.codes[0]==674){



                mBitmap= BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.hamza);

                canvas.drawBitmap(mBitmap, key.x + (key.width-(key.width*0.5f)),key.y + key.height*0.4f,PAINT2);



            }








        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    void setSubtypeOnSpaceKey(final InputMethodSubtype subtype) {
        final LatinKeyboard keyboard = (LatinKeyboard)getKeyboard();
//        keyboard.setSpaceIcon(getResources().getDrawable(subtype.getIconResId()));
        invalidateAllKeys();
    }
}