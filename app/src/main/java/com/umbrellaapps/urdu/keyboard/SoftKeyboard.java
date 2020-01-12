package com.umbrellaapps.urdu.keyboard;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.util.Base64;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

public class SoftKeyboard extends InputMethodService implements
        KeyboardView.OnKeyboardActionListener {
    static final boolean DEBUG = false;
    /**
     * This boolean indicates the optional example code for performing
     * processing of hard keys in addition to regular text generation from
     * on-screen interaction. It would be used for input methods that perform
     * language translations (such as converting text entered on a QWERTY
     * keyboard to Chinese), but may not be used for input methods that are
     * primarily intended to be used for on-screen text entry.
     */
    static final boolean PROCESS_HARD_KEYS = true;

    private KeyboardView mInputView;
    private CandidateView mCandidateView;
    private CompletionInfo[] mCompletions;

    private StringBuilder mComposing = new StringBuilder();
    private boolean mPredictionOn;
    private boolean mCompletionOn;
    private int mLastDisplayWidth;
    private boolean mCapsLock;
    private long mMetaState;
    private long mLastShiftTime;
//    private LatinKeyboard mSymbolsKeyboard;
//    private LatinKeyboard mSymbolsShiftedKeyboard;
//    private LatinKeyboard mQwertyKeyboard;
//    private LatinKeyboard urduQwertyKeyboard;
//    private LatinKeyboard urduShiftKeyboard;
//
//
//    private LatinKeyboard mPersianKeyboard;
//    private LatinKeyboard mPersianSymbolsKeyboard;
//    private LatinKeyboard mPersianSymbolsShiftedKeyboard;
//
//    private LatinKeyboard abkhazKeyboard;
//    private LatinKeyboard abkhazSymbolsKeyboard;
//    private LatinKeyboard abkhazSymbolsShiftedKeyboard;

    private LatinKeyboard pashto,pashto_shift,pashto_numeric,english,english_shift,english_numeric;

    private LatinKeyboard mCurKeyboard;

    private String mWordSeparators;
    LinearLayout inputViewAdds = null;
    AdView mAdView;

    SessionManager sessionManager;
    private Boolean removeAds;
    EmojiconsPopup popup;
    private InputMethodManager mInputMethodManager;
    MediaPlayer mMediaPlayer;
    MediaPlayer mMediaplayer1;
    SharedPreferences sharedPreferences;
    String encodedImage;
    /**
     * Main initialization of the input method component. Be sure to call to
     * super class.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        super.onCreate();
        mWordSeparators = getResources().getString(R.string.word_separators);
        sessionManager = new SessionManager(getApplicationContext());
        removeAds = sessionManager.getRemoveAds();
        mMediaPlayer = MediaPlayer.create(SoftKeyboard.this, R.raw.sound);
        mMediaplayer1=MediaPlayer.create(SoftKeyboard.this,R.raw.sound);
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * This is the point where you can do all of your UI initialization. It is
     * called after creation and any configuration change.
     */
    @Override
    public void onInitializeInterface() {
        if (pashto != null) {
            // Configuration changes can happen after the keyboard gets
            // recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth)
                return;
            mLastDisplayWidth = displayWidth;
        }

        mCapsLock = false;
//        mQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
//        mSymbolsKeyboard = new LatinKeyboard(this, R.xml.english_numeric);
//        mSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.symbols_shift);
//        urduQwertyKeyboard = new LatinKeyboard(this, R.xml.qwerty);
//        urduShiftKeyboard = new LatinKeyboard(this, R.xml.qwerty);
//
//        mPersianKeyboard = new LatinKeyboard(this, R.xml.beolsik);
//        mPersianSymbolsKeyboard = new LatinKeyboard(this, R.xml.beolsik_shift);
//        mPersianSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.beolsik_shift);
//
//        abkhazKeyboard = new LatinKeyboard(this, R.xml.abkhaz_qwerty);
//        abkhazSymbolsKeyboard = new LatinKeyboard(this, R.xml.english_numeric);
//        abkhazSymbolsShiftedKeyboard = new LatinKeyboard(this, R.xml.abkhaz_shift);

        pashto = new LatinKeyboard(this, R.xml.bashkir_urduqwerty);
        pashto_shift= new LatinKeyboard(this, R.xml.bashkir_urdu_shift);
        pashto_numeric = new LatinKeyboard(this, R.xml.pashto_numeric);
        english = new LatinKeyboard(this,R.xml.english_qwerty);
        english_shift = new LatinKeyboard(this,R.xml.english_qwerty_shifted);

        english_numeric = new LatinKeyboard(this,R.xml.english_numeric);

    }

    /**
     * Called by the framework when your view for creating input needs to be
     * generated. This will be called the first time your input method is
     * displayed, and every time it needs to be re-created such as due to a
     * configuration change.
     */
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateInputView() {
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        InputMethodSubtype subtype = mInputMethodManager.getCurrentInputMethodSubtype();
        switch (subtype.getLanguageTag()) {
//            case "fa_IR":
//               // setLatinKeyboard(mSymbolsKeyboard);
//                break;
//            case "en_US":
//                setLatinKeyboard(english);
//                break;
//
//            case "key_abhaz":
//                //setLatinKeyboard(abkhazSymbolsKeyboard);


            case "key_Bashkir":
                setLatinKeyboard(pashto);
                break;

        }


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (this.inputViewAdds != null) {
            this.inputViewAdds.removeAllViews();
        }
        LayoutInflater layoutInflater = this.getLayoutInflater();
        if (sessionManager.getKeyboardBackground().equals("white")) {
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_white, null);
        } else if (sessionManager.getKeyboardBackground().equals("keyboard_bg1")) {
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_black, null);

        }else if( sessionManager.getKeyboardBackground().equals("keyboard_bg2")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_back, null);

        }else if(sessionManager.getKeyboardBackground().equals("keyboard_bg3")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_black, null);

        }else if(sessionManager.getKeyboardBackground().equals("keyboard_bg4")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_back, null);

        }else if(sessionManager.getKeyboardBackground().equals("keyboard_bg5")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_back, null);

        }else if(sessionManager.getKeyboardBackground().equals("keyboard_bg6")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_back, null);

        }else if(sessionManager.getKeyboardBackground().equals("keyboard_bg7")
               ){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_black, null);

        }
        else if( sessionManager.getKeyboardBackground().equals("keyboard_bg8")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_black, null);

        }
        else if( sessionManager.getKeyboardBackground().equals("keyboard_bg9")||sessionManager.getKeyboardBackground().equals("kbg")){
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_black, null);

        }

        else if (sessionManager.getKeyboardBackground().equals("black")) {
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input_default_black, null);
        }

        else if (sessionManager.getKeyboardBackground().equals("personalize_theme")){
            this.inputViewAdds=(LinearLayout)layoutInflater.inflate(R.layout.input,null);
        }

        else {
            this.inputViewAdds = (LinearLayout) layoutInflater.inflate(R.layout.input, null);
        }
        mInputView = (KeyboardView) this.inputViewAdds.findViewById(R.id.keyboard);
        mAdView = (AdView) this.inputViewAdds.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B28DDF9144B293265D3087628FD72536")
                .addTestDevice("7A379E9A65205DEEA2CBCA6D89C8B197")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (!removeAds) {
                    mAdView.setVisibility(View.VISIBLE);
                }
            }
        });

        mInputView.setOnKeyboardActionListener(this);
        mInputView.setKeyboard(pashto);
        // Give the topmost view of your activity layout hierarchy. This will be used to measure soft keyboard height
        popup = new EmojiconsPopup(mInputView, this);

        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();
        return this.inputViewAdds;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setLatinKeyboard(LatinKeyboard nextKeyboard) {
        final boolean shouldSupportLanguageSwitchKey =
                mInputMethodManager.shouldOfferSwitchingToNextInputMethod(getToken());
//        nextKeyboard.setLanguageSwitchKeyVisibility(shouldSupportLanguageSwitchKey);
        mInputView.setKeyboard(nextKeyboard);

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public View onCreateCandidatesView() {
        mCandidateView = new CandidateView(this);
        mCandidateView.setService(this);
        return mCandidateView;
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        // Reset our state. We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any
        // way.
        mComposing.setLength(0);
        updateCandidates();

        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }

        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
            case EditorInfo.TYPE_CLASS_DATETIME:

                mCurKeyboard = pashto;
                break;

            case EditorInfo.TYPE_CLASS_PHONE:

                mCurKeyboard = pashto;
                break;

            case EditorInfo.TYPE_CLASS_TEXT:

                mCurKeyboard = pashto;

                int variation = attribute.inputType
                        & EditorInfo.TYPE_MASK_VARIATION;
                if (variation == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mPredictionOn = false;
                }

                if (variation == EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_URI
                        || variation == EditorInfo.TYPE_TEXT_VARIATION_FILTER) {
                    mPredictionOn = false;
                }

                if ((attribute.inputType & EditorInfo.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    mPredictionOn = false;
                    mCompletionOn = isFullscreenMode();
                }

                updateShiftKeyState(attribute);
                break;

            default:
                mCurKeyboard = pashto;
                updateShiftKeyState(attribute);
        }

        mCurKeyboard.setImeOptions(getResources(), attribute.imeOptions);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.
        mComposing.setLength(0);
        updateCandidates();

        // We only hide the candidates window when finishing input on
        // a particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        mCurKeyboard = pashto;
        if (mInputView != null) {
            mInputView.closing();
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        setInputView(onCreateInputView());

        List<Keyboard.Key> keys = mInputView.getKeyboard().getKeys();
        if (sessionManager.getKeyboardBackground().equals("white") ||
                sessionManager.getKeyboardBackground().equals("keyboard_bg1") ||
                sessionManager.getKeyboardBackground().equals("keyboard_bg3") ||
                sessionManager.getKeyboardBackground().equals("keyboard_bg7") ||
                sessionManager.getKeyboardBackground().equals("keyboard_bg8") ||
                sessionManager.getKeyboardBackground().equals("keyboard_bg9")
                ) {
            for (Keyboard.Key key : keys) {
                if (key.codes[0] == 10) {
                    key.icon = getResources().getDrawable(R.drawable.ic_return_black_24dp);
                }
                if (key.codes[0] == -1) {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_black_24dp);
                }

                if (key.codes[0] == -3) {
                    key.icon = getResources().getDrawable(R.drawable.ic_keyboard_hide_black_24dp);
                }
                if (key.codes[0] == -5) {
                    key.icon = getResources().getDrawable(R.drawable.ic_backspace_black_24dp);
                }
                if (key.codes[0] == 400000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_settings_black_24dp);
                }
                if (key.codes[0] == 500000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_emoticon_black);
                }
            }
        } else {
                for (Keyboard.Key key : keys) {

                if (key.codes[0] == 10) {
                    key.icon = getResources().getDrawable(R.drawable.ic_return_white_24dp);
                }
                else
                if (key.codes[0] == -1) {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_white_24dp);
                }
                else
                if (key.codes[0] == -3) {
                    key.icon = getResources().getDrawable(R.drawable.ic_keyboard_hide_white_24dp);
                }
                else
                if (key.codes[0] == -5) {
                    key.icon = getResources().getDrawable(R.drawable.ic_backspace_white_24dp);
                }
                else

                if (key.codes[0] == 400000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_settings_white_24dp);
                }
                else
                if (key.codes[0] == 500000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_emoticon_white);
                }
                else
                {

                }
            }
        }
        // Apply the selected keyboard to the input view.
        if (sessionManager.getPopup()) {
            mInputView.setPreviewEnabled(true);
        } else {
            mInputView.setPreviewEnabled(false);
        }
        removeAds = sessionManager.getRemoveAds();
        if (removeAds) {
            disableAds();
        }

        String keyboardBg = sessionManager.getKeyboardBackground();
        if (!keyboardBg.equals("white") && !keyboardBg.equals("black") && !keyboardBg.equals("personalize_theme")) {
            int keyboardBgId = getResources().getIdentifier(keyboardBg, "drawable", this.getPackageName());
            mInputView.setBackgroundResource(keyboardBgId);
        }else if (keyboardBg.equals("personalize_theme")){

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            encodedImage = sharedPreferences.getString("my_image", "");
            if (!encodedImage.equalsIgnoreCase("")) {
                //Decoding the Image and display in ImageView
                byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                //Convert bitmap to drawable
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawable = new BitmapDrawable(bitmap);
                mInputView.setBackground(drawable);
            }
        }

        mInputView.setKeyboard(mCurKeyboard);
        mInputView.closing();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
    //    mInputView.setSubtypeOnSpaceKey(subtype);

        switch(subtype.getLocale()) {
//            case "fa_IR":
//                setLatinKeyboard(mPersianKeyboard);
//                Toast.makeText(this, "3-Beolsik-Keyboard", Toast.LENGTH_SHORT).show();
//                break;
//            case "en_US":
//                setLatinKeyboard(mQwertyKeyboard);
//                Toast.makeText(this, "English-Keyboard", Toast.LENGTH_SHORT).show();
//                break;
//            case "key_abhaz":
//                setLatinKeyboard(abkhazKeyboard);
//                Toast.makeText(this, "Abhaz-Keyboard", Toast.LENGTH_SHORT).show();
//                break;
            case "key_Bashkir":
                setLatinKeyboard(pashto);
                Toast.makeText(this, "Bashkir-Keyboard", Toast.LENGTH_SHORT).show();
                break;

        };
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void disableAds() {
        if (removeAds) {
            mAdView.setVisibility(View.GONE);
        }
    }

    public void sound_vibrate() {
//
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (sessionManager.getSound()) {
            float vol = (float) 5.5;
           // am.playSoundEffect(SoundEffectConstants.CLICK, vol);
        }

        if ((sessionManager.getBeep())){
           // mMediaPlayer.start();
        }
        if ((sessionManager.getBeep1())){
          //  mMediaplayer1.start();
        }

        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (sessionManager.getVibration()) {
           // vb.vibrate(50);
        }
    }
    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        // If the current selection in the text view changes, we should
        // clear whatever candidate text we have.
        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            updateCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    @Override
    public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setSuggestions(null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < completions.length; i++) {
                CompletionInfo ci = completions[i];
                if (ci != null)
                    stringList.add(ci.getText().toString());
            }
            setSuggestions(stringList, true, true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean translateKeyDown(int keyCode, KeyEvent event) {
        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        boolean dead = false;

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (mComposing.length() > 0){

            char accent = mComposing.charAt(mComposing.length() -1 );
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length()-1);
            }
        }

        onKey(c, null);

        return true;
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing a pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState()&KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeyDown(keyCode, event)) {
                        return true;
                    }
                }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with a hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
        }

        return super.onKeyUp(keyCode, event);
    }
    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
            updateCandidates();
        }
    }

    /**
     * Helper to update the shift state of our keyboard based on the initial
     * editor state.
     */

    private void updateShiftKeyState(EditorInfo attr) {
        if (attr != null
                && mInputView != null && pashto == mInputView.getKeyboard()) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if (ei != null && ei.inputType != InputType.TYPE_NULL) {
                caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
        }
    }

    /**
     * Helper to determine if a given character code is alphabetic.
     */

    private boolean isAlphabet(int code) {
        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper to send a key down / key up pair to the current editor.
     */

    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    /**
     * Helper to send a character to the editor as raw key events.
     */

    private void sendKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    // Implementation of KeyboardViewListener

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onKey(int primaryCode, int[] keyCodes){

        sound_vibrate();
        if (isWordSeparator(primaryCode)) {
            if (mComposing.length() > 0) {
                commitTyped(getCurrentInputConnection());
            }
            sendKey(primaryCode);
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            handleBackspace();
            /*
    -3 abc
    -33 alif b pe

    -2 pashto123
    -22 eng123

    -1 pashtoshift
    -11 englishshift
    */

        } else if (primaryCode == -1) {
            handleShift();
        }
        else if(primaryCode==-3){
            handleEnglish();
        }
        else if(primaryCode==-33){
            handlePushto();
        }
        else if(primaryCode == -11){
            handleShift2();
        }
        else if(primaryCode==-22){
            numberKeyboard();
        }
        else if(primaryCode==-2){
            numberKeyboard2();
        }

        else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            handleClose();
            return;
        }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (primaryCode == LatinKeyboardView.KEYCODE_LANGUAGE_SWITCH) {
            handleLanguageSwitch();
            return;
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (primaryCode == LatinKeyboardView.KEYCODE_OPTIONS) {
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE
                && mInputView != null) {
            Keyboard current = mInputView.getKeyboard();
//            if (current == mSymbolsKeyboard
//                    || current == mSymbolsShiftedKeyboard) {
//                current = mQwertyKeyboard;
//            } else {
//                current = mSymbolsKeyboard;
//            }
//            mInputView.setKeyboard(current);
//            if (current == mSymbolsKeyboard) {
//                current.setShifted(false);
//            }
//        } else if (primaryCode == 100000) {
//            mInputView.setKeyboard(mQwertyKeyboard);
//        } else if (primaryCode == 200000) {
//            mInputView.setKeyboard(mSymbolsKeyboard);
//        }else if (primaryCode == 300000) {
//            mInputView.setKeyboard(mQwertyKeyboard);
        } else if (primaryCode == 400000) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (primaryCode == 500000) {
            this.popup.showAtBottom();
            this.popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {
                public void onEmojiconClicked(Emojicon emojicon) {
                    if (emojicon != null) {
                        getCurrentInputConnection().commitText(emojicon.getEmoji(), 1);
                    }
                }
            });
            this.popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {
                public void onEmojiconBackspaceClicked(View v) {
                    getCurrentInputConnection().sendKeyEvent(new KeyEvent(0, 0, 0, 67, 0, 0, 0, 0, 6));
                }
            });
            this.popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
                public void onKeyboardOpen(int keyBoardHeight) {
                }

                public void onKeyboardClose() {
                    if (SoftKeyboard.this.popup.isShowing()) {
                        SoftKeyboard.this.popup.dismiss();
                    }
                }
            });
        } else {
            handleCharacter(primaryCode, keyCodes);
        }
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null)
            return;
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
        updateShiftKeyState(getCurrentInputEditorInfo());
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void numberKeyboard2(){
        Keyboard currentKeyboard = mInputView.getKeyboard();
        if(currentKeyboard!=pashto && currentKeyboard!=pashto_shift) {
            mCapsLock = false;
            setLatinKeyboard(pashto);
        }
        else{
            mCapsLock = false;
            setLatinKeyboard(pashto_numeric);
        }
    }

    private void handleEnglish(){

        mCapsLock = false;
        setLatinKeyboard(english);
    }
    private void handlePushto(){

        mCapsLock = false;
        setLatinKeyboard(pashto);
    }
    private void numberKeyboard(){
        Keyboard currentKeyboard = mInputView.getKeyboard();
        if(currentKeyboard!=english && currentKeyboard!=english_shift) {
            mCapsLock = false;
            setLatinKeyboard(english);
        }
        else{
            mCapsLock = false;
            setLatinKeyboard(english_numeric);
        }
    }
    private void updateCandidates() {
        if (!mCompletionOn) {
            if (mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(mComposing.toString());
                setSuggestions(list, true, true);
            } else {
                setSuggestions(null, false, false);
            }
        }
    }

    public void setSuggestions(List<String> suggestions, boolean completions,
                               boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mCandidateView != null) {
            mCandidateView.setSuggestions(suggestions, completions, typedWordValid);
        }
    }


    private void handleBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            updateCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
        updateShiftKeyState(getCurrentInputEditorInfo());
    }





    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleShift(){
        if (mInputView == null) {
            return;
        }

        Keyboard currentKeyboard = mInputView.getKeyboard();


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        if (mPersianKeyboard==currentKeyboard){
//            //Alphabet Keyboard
//            checkToggleCapsLock();
//            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
//            setLatinKeyboard(mPersianSymbolsKeyboard);
//
//        }
//        else if (mPersianKeyboard == currentKeyboard) {
//            // Alphabet keyboard
//            checkToggleCapsLock();
//            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
//
//        }
//        else if (currentKeyboard == mPersianSymbolsKeyboard) {
//            mPersianSymbolsKeyboard.setShifted(true);
//            setLatinKeyboard(mPersianKeyboard);
//            mPersianKeyboard.setShifted(true);
//
//        } else if (currentKeyboard == mPersianSymbolsShiftedKeyboard) {
//            mPersianSymbolsShiftedKeyboard.setShifted(false);
//            setLatinKeyboard(mPersianSymbolsKeyboard);
//            mPersianSymbolsKeyboard.setShifted(false);
//
//        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
         if (!mCapsLock) {
            // Alphabet keyboard
             mCapsLock = !mCapsLock;
             setLatinKeyboard(pashto_shift);


        }
        else {
             mCapsLock = !mCapsLock;

             setLatinKeyboard(pashto);
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        else if (abkhazKeyboard == currentKeyboard) {
//            // Alphabet keyboard
//            checkToggleCapsLock();
//            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
//        }
//        else if (currentKeyboard == abkhazSymbolsKeyboard) {
//            abkhazSymbolsKeyboard.setShifted(true);
//            setLatinKeyboard(abkhazSymbolsShiftedKeyboard);
//            abkhazSymbolsShiftedKeyboard.setShifted(true);
//        } else if (currentKeyboard == abkhazSymbolsShiftedKeyboard) {
//            abkhazSymbolsShiftedKeyboard.setShifted(false);
//            setLatinKeyboard(abkhazSymbolsKeyboard);
//            abkhazSymbolsKeyboard.setShifted(false);
//        }


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        else if (mQwertyKeyboard == currentKeyboard) {
//            // Alphabet keyboard
//            checkToggleCapsLock();
//            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
//        }
//        else if (currentKeyboard == mSymbolsKeyboard) {
//
//            mSymbolsKeyboard.setShifted(true);
//            setLatinKeyboard(mSymbolsShiftedKeyboard);
//            mSymbolsShiftedKeyboard.setShifted(true);
//        } else if (currentKeyboard == mSymbolsShiftedKeyboard) {
//            mSymbolsShiftedKeyboard.setShifted(false);
//            setLatinKeyboard(mSymbolsKeyboard);
//            mSymbolsKeyboard.setShifted(false);
//        }

    }
    private void handleShift2(){
        if (!mCapsLock) {
            // Alphabet keyboard
            mCapsLock = !mCapsLock;
            setLatinKeyboard(english_shift);
        }
        else {
            mCapsLock = !mCapsLock;
            setLatinKeyboard(english);
        }
    }

    private void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isInputViewShown()) {
            if (mInputView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
        }
        if (isAlphabet(primaryCode) && mPredictionOn) {
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            updateShiftKeyState(getCurrentInputEditorInfo());
            updateCandidates();
        } else {
            getCurrentInputConnection().commitText(
                    String.valueOf((char) primaryCode), 1);
        }
    }

    private void handleClose() {
        commitTyped(getCurrentInputConnection());
        requestHideSelf(0);
        mInputView.closing();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void handleLanguageSwitch() {
        mInputMethodManager.switchToNextInputMethod(getToken(), false /* onlyCurrentIme */);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private String getWordSeparators() {
        return mWordSeparators;
    }

    public boolean isWordSeparator(int code) {
        String separators = getWordSeparators();
        return separators.contains(String.valueOf((char) code));
    }

    public void pickDefaultCandidate() {
        pickSuggestionManually(0);
    }

    public void pickSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mCandidateView != null) {
                mCandidateView.clear();
            }
            updateShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitTyped(getCurrentInputConnection());
        }
    }


    public void swipeRight() {
        if (mCompletionOn) {
            pickDefaultCandidate();
        }
    }

    public void swipeLeft() {
        handleBackspace();
    }

    public void swipeDown() {
        handleClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
        if (primaryCode == -2 || primaryCode == 10 || primaryCode == 300000 || primaryCode == 400000 || primaryCode == 500000 || primaryCode == 100000 || primaryCode == 32 ||
                primaryCode == 200000 || primaryCode == -3 || primaryCode == -1 || primaryCode == -5) {
            mInputView.setPreviewEnabled(false);
        } else {
            if (sessionManager.getPopup()) {
                mInputView.setPreviewEnabled(true);
            }
        }
    }

    public void onRelease(int primaryCode) {
        List<Keyboard.Key> keys = mInputView.getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if ((sessionManager.getKeyboardBackground().equals("white") ||
                    sessionManager.getKeyboardBackground().equals("keyboard_bg1") ||
                    sessionManager.getKeyboardBackground().equals("keyboard_bg3") ||
                    sessionManager.getKeyboardBackground().equals("keyboard_bg7") ||
                    sessionManager.getKeyboardBackground().equals("keyboard_bg8") ||
                    sessionManager.getKeyboardBackground().equals("keyboard_bg9")) && (primaryCode == -2 || primaryCode == -1 || primaryCode == 100000 || primaryCode == 200000)) {
                if (key.codes[0] == 10) {
                    key.icon = getResources().getDrawable(R.drawable.ic_return_black_24dp);
                }
                if (key.codes[0] == -1) {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_black_24dp);
                }
                if (key.codes[0] == 32) {
                    key.icon = getResources().getDrawable(R.drawable.sym_keyboard_space_black);
                }
                if (key.codes[0] == -3) {
                    key.icon = getResources().getDrawable(R.drawable.ic_keyboard_hide_black_24dp);
                }
                if (key.codes[0] == -5) {
                    key.icon = getResources().getDrawable(R.drawable.ic_backspace_black_24dp);
                }
                if (key.codes[0] == 400000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_settings_black_24dp);
                }
                if (key.codes[0] == 500000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_emoticon_black);
                }
            } else if (!sessionManager.getKeyboardBackground().equals("white") && (primaryCode == -2 || primaryCode == -1 || primaryCode == 100000 || primaryCode == 200000)) {
                if (key.codes[0] == 10) {
                    key.icon = getResources().getDrawable(R.drawable.ic_return_white_24dp);
                }
                if (key.codes[0] == -1) {
                    key.icon = getResources().getDrawable(R.drawable.ic_shift_white_24dp);
                }
                if (key.codes[0] == 32) {
                    key.icon = getResources().getDrawable(R.drawable.sym_keyboard_space);
                }
                if (key.codes[0] == -3) {
                    key.icon = getResources().getDrawable(R.drawable.ic_keyboard_hide_white_24dp);
                }
                if (key.codes[0] == -5) {
                    key.icon = getResources().getDrawable(R.drawable.ic_backspace_white_24dp);
                }
                if (key.codes[0] == 400000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_settings_white_24dp);
                }
                if (key.codes[0] == 500000) {
                    key.icon = getResources().getDrawable(R.drawable.ic_emoticon_white);
                }
            }
        }
        if (sessionManager.getPopup() && primaryCode != -5 && primaryCode != 32) {
            mInputView.setPreviewEnabled(true);
        }
    }
}
