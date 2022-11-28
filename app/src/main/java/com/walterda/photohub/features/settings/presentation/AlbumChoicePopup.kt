package com.walterda.photohub.features.settings.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.walterda.photohub.core.utils.*
import com.walterda.photohub.databinding.AlbumChoicePopupBinding
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import com.walterda.photohub.features.gallery.presentation.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.album_choice_popup.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumChoicePopup : FragmentActivity() {
    private lateinit var mBinding : AlbumChoicePopupBinding
    @Inject lateinit var mViewModel: AlbumsViewModel
    @Inject lateinit var mAlbumListAdapter: AlbumListAdapter
    private lateinit var mCurrentItem: AlbumListItem

    companion object {
        private const val TAG = "AlbumChoicePopup"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = AlbumChoicePopupBinding.inflate(layoutInflater)
//        mViewModel = AlbumsViewModel()
//        mAlbumListAdapter = AlbumListAdapter(this@AlbumChoicePopup as Context)
        val view = mBinding.root
        setContentView(view)

        setUp()
    }


    private fun setUp() {

        val savedAlbum = LocalStorage(this).getCurrentPreferenceAlbum()
        Log.w(TAG, String.format("Saved Album: %s", savedAlbum))
        mBinding.apply {
            albumRV.apply {
                layoutManager = StaggeredGridLayoutManager(1, RecyclerView.VERTICAL)
                adapter = mAlbumListAdapter
            }
        }

        mAlbumListAdapter.apply {
            onItemClicked {
                mCurrentItem = it
//                findNavController().navigate(GalleryFragmentDirections.actionDestGalleryToDestFullScreenImage(it))
            }
        }
        mViewModel.getAlbums()
        setObservers()
        observeLoadingState()
    }

    private fun setObservers() {
        val viewLifecycleOwner = albumRV.findViewTreeLifecycleOwner()!!
        mViewModel.apply {
            pagedAlbumList.observe(viewLifecycleOwner) {
                mBinding.apply {
                    albumRV.visible()
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        mAlbumListAdapter.submitData(it)
                    }
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
//                showSnackBar(mBinding,it)
                Toast.makeText(
                    this@AlbumChoicePopup as Context,
                    it,
                    1500
                ).show()
            }

            loading.observe(viewLifecycleOwner) {
//                mBinding.showLoadingView(it)
                Log.d(TAG, "LOADING... " + it.toString())
            }
        }
    }

    private fun observeLoadingState() {
        val viewLifecycleOwner = albumRV.findViewTreeLifecycleOwner()!!
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mAlbumListAdapter.loadStateFlow.collect {
                    mBinding.apply {
                        //                prependProgress.isVisible = it.source.prepend is LoadState.Loading
                        //                appendProgress.isVisible = it.source.append is LoadState.Loading
                        if (it.source.refresh is LoadState.Loading && mAlbumListAdapter.itemCount == 0) {
                            mViewModel.showLoader(true)
                        } else {
                            mViewModel.showLoader(false)
                            mBinding.albumRV.visibility = View.VISIBLE
                        }
                        if (it.source.refresh is LoadState.Error) {
                            //                    prependProgress.handleVisibility(false)
                            //                    appendProgress.handleVisibility(false)
                            //                    showSnackBar(this, Constants.ERROR_MESSAGE)
                            Toast.makeText(
                                this@AlbumChoicePopup as Context,
                                Constants.ERROR_MESSAGE,
                                1500
                            ).show()
                        }
                    }
                }
            }
        }
    }

}