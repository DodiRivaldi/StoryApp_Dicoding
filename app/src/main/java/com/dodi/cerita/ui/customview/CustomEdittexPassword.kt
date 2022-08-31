package com.dodi.cerita.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dodi.cerita.R

class CustomEdittexPassword : AppCompatEditText {
    constructor(context: Context) : super(context) {init() }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init() }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init() }

    private lateinit var drawable: Drawable

    private fun setDrawable(
        start : Drawable? = null,
        top : Drawable? = null,
        end : Drawable? = null,
        bottom : Drawable? = null
    ){
        setCompoundDrawablesRelativeWithIntrinsicBounds(start,top,end,bottom)
    }

    private fun init(){
        drawable = ContextCompat.getDrawable(context,android.R.drawable.ic_lock_lock) as Drawable
        setDrawable(drawable)
        compoundDrawablePadding = 24
        hint = "*****"
        addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                error = if (!p0.isNullOrEmpty() && p0.length<6) context.getString(R.string.warning_password) else null
            }

            override fun afterTextChanged(p0: Editable?) { }

        })
    }
}