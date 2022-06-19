package com.soumik.photohub.features.full_screen_view.presentation

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.soumik.photohub.core.utils.*
import com.soumik.photohub.databinding.FragmentFullScreenImageBinding


@Suppress("DEPRECATION")
class FullScreenImageFragment : Fragment() {

    private lateinit var mBinding: FragmentFullScreenImageBinding
    private val args: FullScreenImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFullScreenImageBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().transparentStatusBar(true)

        setView()
        viewListener()
    }

    private fun viewListener() {
        mBinding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            shareBtn.setOnClickListener { requireContext().share(body = args.photoData?.urls?.full) }
            downloadBtn.setOnClickListener { downloadImageAndSave() }
        }
    }

    private fun downloadImageAndSave() {
        val imageDownloader = ImageDownloader(requireContext())

        mBinding.apply {
            progressCircular.visible()
            downloadBtn.gone()
        }


        lifecycleScope.executeAsyncTask(
            params = args.photoData?.urls?.full,
            onPreExecute = {},
            doInBackground = {
                return@executeAsyncTask imageDownloader.saveImageToStorage(it)
            },
            onPostExecute = {
                mBinding.apply {
                    progressCircular.gone()
                    downloadBtn.visible()
                }
                if (it == ImageDownloader.DownloadStatus.SUCCESS) {
                    showSnackBar(mBinding, "Photo saved to gallery")
                } else showSnackBar(mBinding, "Photo saving failed")
            })


    }


    private fun setView() {
        try {
            requireContext().loadImage(mBinding.fullScreenIV, args.photoData?.urls?.regular)
        } catch (e: Exception) {
            showSnackBar(mBinding, Constants.ERROR_MESSAGE)
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        requireActivity().transparentStatusBar(false)
        super.onDetach()
    }

    private fun Activity.transparentStatusBar(it: Boolean) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor =
            if (it) ContextCompat.getColor(
                this,
                android.R.color.transparent
            ) else ContextCompat.getColor(
                this,
                com.soumik.photohub.R.color.white_200
            )
    }

}