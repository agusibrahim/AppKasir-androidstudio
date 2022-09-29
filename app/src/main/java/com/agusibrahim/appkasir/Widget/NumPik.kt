package com.agusibrahim.appkasir.Widget

import android.widget.NumberPicker
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.graphics.Typeface
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class NumPik(context: Context?, attrs: AttributeSet?) : NumberPicker(context, attrs) {
    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (view is EditText) {
            val txt: TextView = view
            txt.textSize = 25f
            txt.setTypeface(null, Typeface.BOLD)
            txt.setTextColor(Color.parseColor("#333333"))
        }
    }
}