package com.dauto.gamediscoveryapp.ui.notifications

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dauto.gamediscoveryapp.R
import com.dauto.gamediscoveryapp.databinding.FragmentNotificationsBinding
import com.dauto.gamediscoveryapp.domain.entity.Genres

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
        }
        var te = ""
        for(i in Genres.values()){
            te+="${i.name}, "
        }
        te=te.substring(0,te.length-2).lowercase()
        textView.text=te
        Log.e("superTAG",te)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (1..10).forEach { _ ->
            val dynamicTextview = TextView(context).apply {
                id = View.generateViewId()
            }
            dynamicTextview.text ="Dynamically added text"
            dynamicTextview.setBackgroundResource(R.drawable.text_view_drawable)
            binding.flowContainer.addView(dynamicTextview)
            binding.flowHelper.addView(dynamicTextview)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}