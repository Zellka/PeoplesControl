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
            Category("", "Состояние дорог и прилегающих территорий", 5),
            Category("", "Состояние благоустройства города", 25),
            Category("", "Постройки в аварийном состоянии", 61),
            Category("", "Уборка территорий и вывоз отходов", 17),
            Category("", "Скопление животных", 23),
            Category("", "Некачественные товары", 4),
            Category("", "Проявления вандализма", 46),
            Category("", "Нарушение КЗОТ", 2),
            Category("", "Нарушение ПДД", 4),
            Category("", "Скопление криминальных элементов", 2),
            Category("", "Прорывы коммуникаций", 41),
            Category("", "Расписание транспорта", 2)
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