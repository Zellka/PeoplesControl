package com.example.peoplesontrol.ui.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.FragmentRegionsBinding
import com.example.peoplesontrol.ui.adapter.RegionAdapter

class RegionsFragment : Fragment() {

    private var _binding: FragmentRegionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RegionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf("Донецк", "Макеевка", "Ясиноватая", "Горловка", "Шахтёрск")

        binding.rvRegions.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = RegionAdapter { region: String -> nextLogin(region) }
        adapter.setData(list)
        binding.rvRegions.adapter = adapter
    }

    private fun nextLogin(region: String) {
        val bundle = bundleOf(REGION_KEY to region)
        findNavController().navigate(R.id.action_regionsFragment_to_loginFragment, bundle)
    }

    companion object {
        const val REGION_KEY = "REGION"
    }
}