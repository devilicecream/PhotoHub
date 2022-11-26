package com.walterda.photohub.core.photos

import android.os.SystemClock
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object AccessTokenFactory {
    private var mAccessToken: String? = null
    private var mTokenExpired: Long = 0

    fun requestAccessToken(googleAccount: GoogleSignInAccount, googleId: String, googleSecret: String): String? {
        if (mAccessToken != null && SystemClock.elapsedRealtime() < mTokenExpired) {
            return mAccessToken
        }
        mTokenExpired = 0
        mAccessToken = null

        var conn: HttpURLConnection? = null
        var os: OutputStream? = null
        var `is`: InputStream? = null
        var isr: InputStreamReader? = null
        var br: BufferedReader? = null

        try {
            val url = URL("https://www.googleapis.com/oauth2/v4/token")
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.useCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.connectTimeout = 3000
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

            val b = StringBuilder()
            b.append("code=").append(googleAccount.serverAuthCode).append('&')
                .append("client_id=").append(googleId).append('&')
                .append("client_secret=").append(googleSecret).append('&')
                .append("redirect_uri=").append("").append('&')
                .append("grant_type=").append("authorization_code")

            val postData = b.toString().toByteArray(charset("UTF-8"))

            os = conn.outputStream
            os!!.write(postData)

            val responseCode = conn.responseCode
            if (200 <= responseCode && responseCode <= 299) {
                `is` = conn.inputStream
                isr = InputStreamReader(`is`!!)
                br = BufferedReader(isr)
            } else {
                Log.d("Error:", conn.responseMessage)
                return null
            }

            val output = StringBuilder()
            var nextBuf = br.readLine()
            while ( nextBuf != null  ) {
                output.append(nextBuf)
                nextBuf = br.readLine()
            }

            val jsonResponse = JSONObject(output.toString())

            mAccessToken = jsonResponse.getString("access_token")
            mTokenExpired = SystemClock.elapsedRealtime() + jsonResponse.getLong("expires_in") * 1000
            return mAccessToken
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                }

            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                }

            }
            if (isr != null) {
                try {
                    isr.close()
                } catch (e: IOException) {
                }

            }
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                }

            }
            if (conn != null) {
                conn.disconnect()
            }
        }
        return null
    }
}
