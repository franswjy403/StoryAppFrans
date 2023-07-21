package com.example.storyappfrans.ui.customViews

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var isEmailValid = true

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        isFocusableInTouchMode = true
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()

                if (isEmailValid) {
                    error = null
                } else {
                    error = "Invalid email format"
                }
            }
        })
    }
}