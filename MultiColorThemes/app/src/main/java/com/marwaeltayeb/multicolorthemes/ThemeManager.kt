package com.marwaeltayeb.multicolorthemes

import android.content.Context

class ThemeManager {

    companion object {
        fun setCustomizedThemes(context: Context, theme: String?) {
            when (theme) {
                "blue" -> context.setTheme(R.style.AppTheme)
                "black" -> context.setTheme(R.style.Theme1)
                "red" -> context.setTheme(R.style.Theme2)
                "purple" -> context.setTheme(R.style.Theme3)
                "green" -> context.setTheme(R.style.Theme4)
                "grey" -> context.setTheme(R.style.Theme5)
                "orange" -> context.setTheme(R.style.Theme6)
                "pink" -> context.setTheme(R.style.Theme7)
            }
        }
    }
}