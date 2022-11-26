package com.walterda.photohub.core.utils

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import com.walterda.photohub.R


class NamePopup : Activity(), OnEditorActionListener {
    private lateinit var mTextEdit: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.name_popup)

        mTextEdit = findViewById(R.id.name_editor)

        setUp()
    }

    private fun setUp() {
        val name = LocalStorage(this).getCurrentPreferenceName()
        mTextEdit.setText(name)
        mTextEdit.setOnEditorActionListener(this)
    }

    override fun onEditorAction(v: TextView?, k: Int, e: KeyEvent?): Boolean {
        LocalStorage(this).setCurrentPreferenceName(mTextEdit.text.toString())
        finish()
        return true
    }
}