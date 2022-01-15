package com.example.peoplesontrol.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.FragmentProfileBinding
import com.example.peoplesontrol.ui.adapter.AppealAdapter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AppealAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = listOf(
            Appeal(
                131,
                "Петрова Ольга Николаевна",
                "Авария",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                0,
                14
            ),
            Appeal(
                146,
                "Петрова Ольга Николаевна",
                "Авария",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                4,
                1
            )
        )

        binding.rvAppeals.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = AppealAdapter { appeal: Appeal -> showAppeal(appeal) }
        adapter.setData(list)
        binding.rvAppeals.adapter = adapter
    }

    private fun showAppeal(appeal: Appeal) {
        findNavController().navigate(R.id.action_profileFragment_to_appealFragment)
    }
}