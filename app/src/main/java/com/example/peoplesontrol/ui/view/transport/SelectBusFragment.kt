package com.example.peoplesontrol.ui.view.transport

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.FragmentSelectBusBinding
import com.example.peoplesontrol.ui.adapter.RegionAdapter

class SelectBusFragment : Fragment() {

    private var _binding: FragmentSelectBusBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RegionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectBusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list =
            listOf("Маршрут № 8", "Маршрут № 19", "Маршрут № 26", "Маршрут № 32", "Маршрут № 41")

        binding.rvBus.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = RegionAdapter { bus: String -> nextLogin(bus) }
        adapter.setData(list)
        binding.rvBus.adapter = adapter
    }

    private fun nextLogin(bus: String) {
        findNavController().navigate(R.id.action_busFragment_to_transportFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            findNavController().navigate(R.id.action_busFragment_to_cityFragment)
        }
        if (id == R.id.action_search) {
            val searchView = item.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    adapter.filter.filter(query)
                    return true
                }
            })
        }
        return super.onOptionsItemSelected(item)
    }
}