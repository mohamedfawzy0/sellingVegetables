package com.sellingvegetables.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.sellingvegetables.language.Language;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        com.sellingvegetables.share.TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/ar_font.ttf");
        com.sellingvegetables.share.TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/ar_font.ttf");
        com.sellingvegetables.share.TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/ar_font.ttf");
        com.sellingvegetables.share.TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/ar_font.ttf");

    }
}

