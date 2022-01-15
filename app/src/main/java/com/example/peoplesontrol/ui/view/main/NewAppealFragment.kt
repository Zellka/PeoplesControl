package com.example.peoplesontrol.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.peoplesontrol.databinding.FragmentNewAppealBinding
import android.widget.ArrayAdapter

class NewAppealFragment : Fragment() {

    private var _binding: FragmentNewAppealBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAppealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories =
            arrayOf(
                "Нарушение ПДД",
                "Скопление животных",
                "Нарушение КЗОТ",
                "Городское пространство"
            )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter = adapter
    }
}