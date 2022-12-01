package com.walterda.photohub.features.full_screen_view.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.walterda.photohub.core.utils.*
import com.walterda.photohub.databinding.FragmentFullScreenImageBinding
import com.walterda.photohub.features.DPadEvent
import com.walterda.photohub.features.NavigationDirection
import com.walterda.photohub.features.NavigationEvent
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class FullScreenImageFragment : Fragment() {

    private lateinit var mBinding: FragmentFullScreenImageBinding
    private val args: FullScreenImageFragmentArgs by navArgs()

    @Inject
    lateinit var connectivity: IConnectivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Register for events
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        mBinding = FragmentFullScreenImageBinding.inflate(inflater)
        return mBinding.root
    }

    @Subscribe
    fun onEvent(event : DPadEvent) {
        // Called by eventBus when an event occurs
        if (event.keyCode == 22) {
            EventBus.getDefault().post(NavigationEvent(NavigationDirection.FORWARD))
        } else if (event.keyCode == 21) {
            EventBus.getDefault().post(NavigationEvent(NavigationDirection.BACKWARDS))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun setView() {
        try {
            if (connectivity.hasInternetConnection()) {
                val context = requireContext()
                val screenSize = context.screenSize()
                val photoData = args.photoData!!
                var crop = "-c"
                if (photoData.mediaMetadata.width < photoData.mediaMetadata.height) {
                    crop = ""
                }
                context.loadImage(
                    mBinding.fullScreenIV,
                    photoData.baseUrl.toString() + "=w${screenSize.width}-h${screenSize.height}$crop"
                )
            } else showSnackBar(mBinding, Constants.NO_NETWORK_CONNECTION)
        } catch (e: Exception) {
            showSnackBar(mBinding, Constants.ERROR_MESSAGE)
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        EventBus.getDefault().unregister(this)
        super.onDetach()
    }

}