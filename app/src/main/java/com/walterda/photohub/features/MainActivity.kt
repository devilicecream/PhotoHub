package com.walterda.photohub.features

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.walterda.photohub.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

enum class NavigationDirection {
    FORWARD, BACKWARDS
}

data class DPadEvent(val keyCode: Int)
data class NavigationEvent(val direction: NavigationDirection)


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        EventBus.builder()
        EventBus.getDefault().post(DPadEvent(keyCode))
        return super.onKeyDown(keyCode, event)
    }
}