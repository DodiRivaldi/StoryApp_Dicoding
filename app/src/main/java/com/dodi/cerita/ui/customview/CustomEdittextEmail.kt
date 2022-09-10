package com.dodi.cerita.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dodi.cerita.R

class CustomEdittextEmail : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private lateinit var drawable: Drawable

    private fun setDrawable(
        start: Drawable? = null,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
    }

    private fun init() {
        drawable =
            ContextCompat.getDrawable(context, android.R.drawable.ic_dialog_email) as Drawable
        setDrawable(drawable)
        hint = context.getString(R.string.sample_email)
        compoundDrawablePadding = 24
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val isValid = Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()
                error =
                    if (!isValid && !p0.isNullOrEmpty()) context.getString(R.string.warning_email) else null
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }
}