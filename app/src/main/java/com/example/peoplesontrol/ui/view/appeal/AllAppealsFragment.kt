package com.example.peoplesontrol.ui.view.appeal

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.peoplesontrol.databinding.FragmentAllAppealsBinding
import com.example.peoplesontrol.ui.adapter.AppealPagerAdapter

class AllAppealsFragment : Fragment() {

    private var _binding: FragmentAllAppealsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllAppealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = AppealPagerAdapter(this.requireContext(), childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}