package com.marwaeltayeb.multicolorthemes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marwaeltayeb.multicolorthemes.ThemeManager.Companion.setCustomizedThemes
import com.marwaeltayeb.multicolorthemes.ThemeStorage.Companion.getThemeColor

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_second)
    }
}