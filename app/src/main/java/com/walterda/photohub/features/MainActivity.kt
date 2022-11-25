package com.walterda.photohub.features

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.walterda.photohub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.util.*

enum class NavigationDirection {
    FORWARD, BACKWARDS
}

data class DPadEvent(val keyCode: Int)
data class NavigationEvent(val direction: NavigationDirection)


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mSettingsPasswordTimer: Timer
    private val mPassword = listOf<Int>(21, 22, 21, 22)
    private var mCurrentPasswordStep: Int = 0
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.w("PRESS", "Pressed: " + keyCode.toString())
        val passwordOverAndCorrect = _checkPassword(keyCode)
        if (!passwordOverAndCorrect) {
            EventBus.builder()
            EventBus.getDefault().post(DPadEvent(keyCode))
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun _checkPassword(keyCode: Int): Boolean {
        if (keyCode == mPassword[mCurrentPasswordStep]) {
            if (mCurrentPasswordStep == 0) {
                val settingsPasswordExpired: TimerTask = object : TimerTask() {
                    override fun run() {
                        Log.e("TIMER", "TIMER OVER!")
                        mCurrentPasswordStep = 0
                    }
                }
                mSettingsPasswordTimer = Timer()
                mSettingsPasswordTimer.schedule(settingsPasswordExpired, 2000)
            }
            mCurrentPasswordStep++
            if (mCurrentPasswordStep == mPassword.size) {
                Log.w("PRESS", "Password correct!")
                mSettingsPasswordTimer.cancel()
                mCurrentPasswordStep = 0
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        } else if (mCurrentPasswordStep != 0) {
            Log.e("PRESS", "Password incorrect!")
            mSettingsPasswordTimer.cancel()
            mCurrentPasswordStep = 0
        }
        return false
    }
}