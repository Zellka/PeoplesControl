package com.example.peoplesontrol.ui.view.transport

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.FragmentSelectCityBinding
import com.example.peoplesontrol.ui.adapter.RegionAdapter

class SelectCityFragment : Fragment() {

    private var _binding: FragmentSelectCityBinding? = null
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
        _binding = FragmentSelectCityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf("Донецк", "Макеевка", "Ясиноватая", "Горловка", "Шахтёрск")

        binding.rvCities.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = RegionAdapter { city: String -> nextLogin(city) }
        adapter.setData(list)
        binding.rvCities.adapter = adapter
    }

    private fun nextLogin(city: String) {
        findNavController().navigate(R.id.action_cityFragment_to_busFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
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