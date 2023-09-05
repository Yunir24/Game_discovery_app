package com.dauto.gamediscoveryapp.ui.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dauto.gamediscoveryapp.databinding.FragmentFavoriteBinding
import com.dauto.gamediscoveryapp.domain.GameResult
import com.dauto.gamediscoveryapp.ui.ViewModelFactory
import com.dauto.gamediscoveryapp.ui.utils.getAppComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val component by lazy {
        getAppComponent()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val binding get() = _binding!!
    private val favoriteAdapter = FavoriteAdpater()


    private val favoriteViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FavoriteViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.e("hometest","favorite fragment "+favoriteViewModel.toString())
        Log.e("hometest","favorite fragment "+viewModelFactory.toString())
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter=favoriteAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        favoriteAdapter.gameItemClickListener ={
            findNavController().navigate(FavoriteFragmentDirections.actionNavigationDashboardToGameDetailFragment(it.id))
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                favoriteViewModel.stateFlow.collect{ gameResult ->
                    when(gameResult){
                        is GameResult.ApiError -> TODO()
                        is GameResult.Exception -> TODO()
                        is GameResult.Loading ->
                        {
                            binding.progressBar.isVisible=true
                            binding.recyclerView.isVisible = false
                        }
                        is GameResult.Success -> {
                            binding.progressBar.isVisible=false
                            binding.recyclerView.isVisible = true
                            favoriteAdapter.submitList(gameResult.data)
                        }
                    }

                }
        }
        }
        binding.editTextInput.doAfterTextChanged {text->

              if (text != null && text.length>2)  Toast.makeText(requireContext(), text.toString(), Toast.LENGTH_SHORT).show()

        }
//        binding.inputLayout.addOnEditTextAttachedListener { object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                if (s.isNullOrEmpty()) {
//                    // hide keyboard
//                    val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
//                    inputMethodManager?.hideSoftInputFromWindow(binding.editTextInput.windowToken, 0)
//                    // remove focus
//                    binding.editTextInput.clearFocus()
//                }
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if((s?.length ?: 0) > 2){
//                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        } }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}