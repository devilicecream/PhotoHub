package com.walterda.photohub.core.photos

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.api.gax.rpc.UnauthenticatedException
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.UserCredentials
import com.google.photos.library.v1.PhotosLibraryClient
import com.google.photos.library.v1.PhotosLibrarySettings
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient.ListSharedAlbumsPagedResponse
import com.walterda.photohub.R
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch


class GoogleIdentity(context: Context) {

    enum class GoogleError {
        NOT_LOGGED_IN,
        AUTHORIZATION_ERROR,
        CONNECTION_ERROR
    }

    private val mContext: Context = context

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(context.getString(R.string.google_id_token))
        .requestServerAuthCode(
            "491926964381-jc7t5mmod6dhu6bfij90dji2ieuh4vpb.apps.googleusercontent.com",
            false
        )
        .requestScopes(Scope("https://www.googleapis.com/auth/photoslibrary.readonly"))
        .requestEmail()
        .build()

    fun getLastSignIn(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(mContext)
    }

    fun getSignInClient(): GoogleSignInClient {
        return GoogleSignIn.getClient(mContext, gso)
    }

    //    fun loadAlbums(callback: (result: ListSharedAlbumsPagedResponse?, error: GoogleError?) -> Void) {
    suspend fun loadAlbums(): ListSharedAlbumsPagedResponse? {
        val account = getLastSignIn()
        if (account == null) {
//            callback(null, GoogleError.NOT_LOGGED_IN)
//            return
            return null
        }
//        GlobalScope.launch(context = Dispatchers.IO) {
        val tok = AccessTokenFactory.requestAccessToken(
            mContext,
            account
        )
        val token = AccessToken(tok, null)
        val credentials = UserCredentials.newBuilder()
            .setClientId(mContext.getString(R.string.google_id_token))
            .setClientSecret(mContext.getString(R.string.google_secret))
            .setAccessToken(token)
            .build()
        Log.w("CREDS", credentials.toString())
        val settings = PhotosLibrarySettings.newBuilder()
            .setCredentialsProvider(
                FixedCredentialsProvider.create(credentials)
            )
            .build()

        val client = account.let { _ -> PhotosLibraryClient.initialize(settings) }
//            var response: ListSharedAlbumsPagedResponse? = null
//            var error: GoogleError? = null
//            try {
//                response = client?.listSharedAlbums()
//            } catch (exc: UnauthenticatedException) {
//                Log.e("PHOTOS", exc.message.toString())
//                error = GoogleError.AUTHORIZATION_ERROR
//            }
        try {
            return client?.listSharedAlbums()
        } catch (exc: UnauthenticatedException) {
            Log.e("PHOTOS", exc.message.toString())
            return null
        }
//            GlobalScope.launch(context = Dispatchers.Main) {
//                callback(response, error)
//            }
    }
//            if (response != null) {
//                for (album in response.iterateAll()) {
//                    albums.add(album)
//                }
//                if (albums.size > 0) {
////                    Toast.makeText(mContext, "Found Albums!", 1000).show()
//                    Log.w("ALBUMS", albums.toString())
//                    for (album in albums) {
//                        val mediaResponse = client?.searchMediaItems(album.id)
//                        for (mediaItem in mediaResponse!!.iterateAll()) {
//                            Log.w("MEDIA ITEMS", mediaItem.toString())
//                        }
//                    }
//                } else {
////                    Toast.makeText(mContext, "No Albums Found", 1000).show()
//                    Log.e("ALBUMS", "SHIT")
//                }
//            } else {
////                Toast.makeText(mContext, "Empty response!", 1000).show()
//                Log.e("ALBUMS", "BIG SHIT")
//            }
//        }
//  }

    fun trySilentLogin() {
        Log.w("GOOGLE", "Trying silent login...")
        val task = getSignInClient().silentSignIn()
        if (task.isSuccessful()) {
            // There's immediate result available.
            val signInAccount: GoogleSignInAccount = task.getResult()
            Log.w("GOOGLE", String.format("Sign in account: %s", signInAccount.toString()))
//            loadAlbums(signInAccount)
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
//                    loadAlbums(signInAccount)
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