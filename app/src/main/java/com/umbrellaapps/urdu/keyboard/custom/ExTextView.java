package com.umbrellaapps.urdu.keyboard.custom;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.umbrellaapps.urdu.keyboard.R;

public class ExTextView extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public ExTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public ExTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public ExTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExTextView);

            String fontName = a.getString(R.styleable.ExTextView_font);

            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/pashto.ttf" + fontName);
                    setTypeface(myTypeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            a.recycle();
        }else{
            try {
                if (isInEditMode()){
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/pashto.ttf" );
                setTypeface(myTypeface);
                }
            }catch (Exception e){
                Log.e("error",e.getLocalizedMessage());
            }
        }
    }
}
