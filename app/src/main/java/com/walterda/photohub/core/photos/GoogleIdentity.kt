package com.walterda.photohub.core.photos

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.walterda.photohub.R


class GoogleIdentity(context: Context) {
    private val mContext: Context = context

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.google_id_token))
        .requestEmail()
        .build()

    fun getLastSignIn(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(mContext)
    }

    fun getSignInClient(): GoogleSignInClient {
        return GoogleSignIn.getClient(mContext, gso)
    }

    fun trySilentLogin() {
        Log.w("GOOGLE", "Trying silent login...")
        val task = getSignInClient().silentSignIn()
        if (task.isSuccessful()) {
            // There's immediate result available.
            val signInAccount: GoogleSignInAccount = task.getResult()
            Log.w("GOOGLE",String.format("Sign in account: %s", signInAccount.toString()))
        } else {
            // There's no immediate result ready, displays some progress indicator and waits for the
            // async callback.
            task.addOnCompleteListener { task ->
                try {
                    val signInAccount = task.getResult(ApiException::class.java)
                    Log.w(
                        "GOOGLE",
                        String.format("(ASYNC) Sign in account: %s", signInAccount.toString()),
                    )
                } catch (apiException: ApiException) {
                    Log.w("GOOGLE", String.format("Sign in failed %s", apiException.status))
                    // You can get from apiException.getStatusCode() the detailed error code
                    // e.g. GoogleSignInStatusCodes.SIGN_IN_REQUIRED means user needs to take
                    // explicit action to finish sign-in;
                    // Please refer to GoogleSignInStatusCodes Javadoc for details
                }
            }
        }

    }
}