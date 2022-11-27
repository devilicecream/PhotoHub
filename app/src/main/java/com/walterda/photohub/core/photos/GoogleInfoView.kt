package com.walterda.photohub.core.photos

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.SignInButton
import com.walterda.photohub.R
import com.walterda.photohub.core.utils.Constants.GOOG_RC_SIGN_IN


class GoogleInfoView(context: Context, attrs: AttributeSet?): ConstraintLayout(context, attrs) {
    private var mGoogleInfo: ConstraintLayout
    private var mSignInButton: SignInButton
    private var mLoggedName: TextView

    init {
        inflate(context, R.layout.google_info, this)
        mGoogleInfo = findViewById(R.id.google_info_view)
        mLoggedName = findViewById(R.id.logged_username)
        mSignInButton = findViewById(R.id.sign_in_button)
        mSignInButton.setSize(SignInButton.SIZE_STANDARD)

        setUpGoogle()
    }

    private fun setUpGoogle() {
        val loggedAccount = GoogleIdentity(context).getLastSignIn()

        if (loggedAccount != null) {
            mLoggedName.setText(loggedAccount.displayName)
            mGoogleInfo.visibility = VISIBLE
            mSignInButton.visibility = GONE
            mSignInButton.setOnClickListener {
                val signInIntent: Intent =
                    GoogleIdentity(context).getSignInClient().getSignInIntent()
                ActivityCompat.startActivityForResult(
                    context as Activity,
                    signInIntent,
                    GOOG_RC_SIGN_IN,
                    null
                )
            }
        } else {
            mGoogleInfo.visibility = GONE
            mSignInButton.visibility = VISIBLE
        }
    }
}