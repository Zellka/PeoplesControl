package com.example.peoplesontrol.ui.view.appeal

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.databinding.FragmentCategoriesBinding
import com.example.peoplesontrol.ui.adapter.CategoryAdapter

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = listOf(
            Category(R.drawable.transport_1, "Общественный транспорт", 5),
            Category(R.drawable.road_2, "Состояние дорог и прилегающий территорий", 25),
            Category(R.drawable.city_3, "Состояние благоустройства города", 61),
            Category(R.drawable.breaking_4, "Аварийные участки города", 17),
            Category(R.drawable.building_5, "Постройки в аварийном состоянии", 23),
            Category(R.drawable.trash_6, "Уборка территории и вывоз отходов", 4),
            Category(R.drawable.food_7, "Некачественные товары", 46),
            Category(R.drawable.animal_8, "Скопление животных", 2),
            Category(R.drawable.wind_9, "Последствия стихийных бедствий", 4),
            Category(R.drawable.tank_10, "Последствие военных действий", 2),
            Category(R.drawable.vandalism_11, "Проявления вандализма", 41),
            Category(R.drawable.fort_12, "Состояние убежищ", 2),
            Category(R.drawable.work_13, "Состояние рабочего места", 12),
            Category(R.drawable.crime_14, "Скопление криминальных элементов", 75),
            Category(R.drawable.car_15, "Нарушение ПДД", 2),
        )

        binding.rvCategories.layoutManager = GridLayoutManager(this.requireContext(), 2)
        adapter = CategoryAdapter { category: Category -> showAppeals(category) }
        adapter.setData(list)
        binding.rvCategories.adapter = adapter
    }

    private fun showAppeals(category: Category) {
        findNavController().navigate(R.id.action_categoriesFragment_to_appealsFragment)
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