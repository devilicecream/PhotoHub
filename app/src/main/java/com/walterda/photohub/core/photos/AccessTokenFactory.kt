package com.walterda.photohub.core.photos

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.walterda.photohub.R
import com.walterda.photohub.core.utils.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType
import org.json.JSONObject
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

const val EXPIRATION_HOURS: Long = 1


object AccessTokenFactory {
    suspend fun getExisting(context: Context): String? {
        val token = LocalStorage(context).getCurrentPreferenceToken()
        val expiration = LocalStorage(context).getCurrentPreferenceTokenExpiration()
        if (token != null && expiration?.compareTo(LocalDateTime.now())!! >0 ) {
            Log.w("TOKEN", "token found!")
            return token
        }
        Log.e("TOKEN", "token NOT found!" + token + expiration.toString())
        return null
    }

    suspend fun setExisting(context: Context, token: String) {
        LocalStorage(context).setCurrentPreferenceToken(token, LocalDateTime.now().plusHours(EXPIRATION_HOURS))
    }

    suspend fun requestAccessToken(context: Context, googleAccount: GoogleSignInAccount): String? {
        val existing = getExisting(context)
        if (existing != null)
            return existing
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .build()

        val googleId = context.getString(R.string.google_id_token)
        val googleSecret = context.getString(R.string.google_secret)
        val bodyJSON = "{\"code\": \"${googleAccount.serverAuthCode}\", " +
                "\"client_id\": \"$googleId\", \"client_secret\": \"$googleSecret\", " +
                "\"redirect_uri\": \"\", \"grant_type\": \"authorization_code\"}"

        val body = RequestBody.create(MediaType.parse("application/json"), bodyJSON)
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .method("POST", body)
            .build()

        val asyncRequest = GlobalScope.async(Dispatchers.IO) { client.newCall(request).execute() }
        val response = asyncRequest.await()
        if (!response.isSuccessful) {
            Log.e("ACCESS_TOKEN", "ERROR: $response")
            return null
        }

        val jsonResponse = JSONObject(response.body()!!.string())

        val token = jsonResponse.getString("access_token")
        setExisting(context, token)
        return token
    }
}
