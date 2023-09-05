package com.dauto.gamediscoveryapp.ui.gamedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.dauto.gamediscoveryapp.databinding.FragmentFullPhotoViewerBinding
import com.dauto.gamediscoveryapp.ui.adapters.FullPhotoViewerAdapter


class FullPhotoViewerFragment : Fragment() {


    private val args by navArgs<FullPhotoViewerFragmentArgs>()
    private lateinit var viewPagerAdapter: FullPhotoViewerAdapter

    private var _binding: FragmentFullPhotoViewerBinding? = null
    private val binding: FragmentFullPhotoViewerBinding
        get() = _binding ?: throw RuntimeException("FragmentFullPhotoViewerBinding is not exist")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentFullPhotoViewerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listImage = args.photo.toList()
        viewPagerAdapter = FullPhotoViewerAdapter(requireContext(), listImage)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.currentItem = args.position
        binding.indicator.setViewPager(binding.viewPager)

    }

}