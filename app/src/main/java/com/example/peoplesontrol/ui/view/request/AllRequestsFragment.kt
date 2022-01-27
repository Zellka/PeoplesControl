package com.example.peoplesontrol.ui.view.request

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.peoplesontrol.databinding.FragmentAllRequestsBinding
import com.example.peoplesontrol.ui.adapter.RequestPagerAdapter

class AllRequestsFragment : Fragment() {

    private var _binding: FragmentAllRequestsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = RequestPagerAdapter(this.requireContext(), childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}