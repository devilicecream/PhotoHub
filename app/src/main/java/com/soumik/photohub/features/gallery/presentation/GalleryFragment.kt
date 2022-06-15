package com.soumik.photohub.features.gallery.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.soumik.photohub.core.utils.*
import com.soumik.photohub.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private lateinit var mBinding : FragmentGalleryBinding
    @Inject lateinit var mViewModel: GalleryViewModel
    private val mPhotoListAdapter : PhotoListAdapter by lazy {
        PhotoListAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentGalleryBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setViews()
        setObservers()
        observeLoadingState()
    }

    private fun init() {
        mViewModel.getPhotos()
    }

    private fun setViews() {
        mBinding.apply {
            galleryRV.apply {
                layoutManager = StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
                adapter = mPhotoListAdapter
            }

        }
    }

    private fun setObservers() {
        mViewModel.apply {
            pagedPhotoList.observe(viewLifecycleOwner) {
                mBinding.apply {
                    progressCircular.gone()
                    galleryRV.visible()
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        mPhotoListAdapter.submitData(it)
                    }
                }
            }

            errorMessage.observe(viewLifecycleOwner) {
                mBinding.progressCircular.gone()
                showSnackBar(mBinding,it)
            }

            loading.observe(viewLifecycleOwner) {
                mBinding.progressCircular.handleVisibility(it)
            }
        }
    }

    /*
     * observing loading state of the adapter
     */
    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mPhotoListAdapter.loadStateFlow.collect {
                    mBinding.apply {
                        prependProgress.isVisible = it.source.prepend is LoadState.Loading
                        appendProgress.isVisible = it.source.append is LoadState.Loading
                        if (it.source.refresh is LoadState.Loading && mPhotoListAdapter.itemCount == 0) {
                            mViewModel.showLoader(true)
                        } else {
                            mViewModel.showLoader(false)
                            mBinding.galleryRV.visibility = View.VISIBLE
                        }
                        if (it.source.refresh is LoadState.Error) {
                            prependProgress.handleVisibility(false)
                            appendProgress.handleVisibility(false)
                            showSnackBar(this,Constants.ERROR_MESSAGE)
                        }
                    }
                }
            }
        }
    }

}