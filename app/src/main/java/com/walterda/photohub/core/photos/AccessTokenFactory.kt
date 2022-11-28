package com.walterda.photohub.core.photos

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object AccessTokenFactory {

    suspend fun requestAccessToken(googleAccount: GoogleSignInAccount, googleId: String, googleSecret: String): String? {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .build()

        val bodyJSON = "{\"code\": \"${googleAccount.serverAuthCode}\", " +
                "\"client_id\": \"$googleId\", \"client_secret\": \"$googleSecret\", " +
                "\"redirect_uri\": \"\", \"grant_type\": \"authorization_code\"}"

        val body = RequestBody.create(MediaType.parse("application/json"), bodyJSON)
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v4/token")
            .method("POST", body)
            .build()

        val asyncRequest = GlobalScope.async(Dispatchers.IO) { client.newCall(request).execute() }
        val response = runBlocking(Dispatchers.IO) { asyncRequest.await() }
//        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            Log.e("ACCESS_TOKEN", "ERROR: $response")
            return null
        }

        val jsonResponse = JSONObject(response.body()!!.string())

        return jsonResponse.getString("access_token")
    }
}
