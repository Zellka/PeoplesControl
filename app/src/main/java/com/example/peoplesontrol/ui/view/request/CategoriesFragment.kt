package com.example.peoplesontrol.ui.view.request

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.databinding.FragmentCategoriesBinding
import com.example.peoplesontrol.ui.adapter.CategoryAdapter
import com.example.peoplesontrol.ui.viewmodel.RequestViewModel
import com.example.peoplesontrol.ui.viewmodel.ViewModelFactory
import com.example.peoplesontrol.utils.Status
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequestViewModel

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
        setupViewModel()
        setupUI()
        getCategories()
        binding.swipeRefresh.setOnRefreshListener {
            binding.progressBar.visibility = View.INVISIBLE
            getCategories()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this.requireContext()))
            )
        )[RequestViewModel::class.java]
    }

    private fun setupUI() {
        binding.rvCategories.layoutManager = GridLayoutManager(this.requireContext(), 2)
        adapter = CategoryAdapter { category: Category -> showRequests(category) }
        adapter.setData(arrayListOf())
        binding.rvCategories.adapter = adapter
    }

    private fun getCategories() {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.getCategories().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.rvCategories.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { categories -> setCategories(categories) }
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this.requireContext(),
                                resource.message,
                                Toast.LENGTH_LONG
                            )
                                .show()
                            Error.showError(this.requireActivity())
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvCategories.visibility = View.GONE
                        }
                    }
                }
            })
        } else {
            viewModel.getCategoriesFromDB().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.rvCategories.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { categories -> setCategories(categories) }
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this.requireContext(),
                                resource.message,
                                Toast.LENGTH_LONG
                            ).show()
                            Error.showError(this.requireActivity())
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvCategories.visibility = View.GONE
                        }
                    }
                }
            })
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun setCategories(list: List<Category>) {
        adapter.apply {
            setData(list)
            notifyDataSetChanged()
        }
    }

    private fun showRequests(category: Category) {
        if (Network.isConnected(this.requireActivity())) {
            Data.categoryId = category.categoryId
            findNavController().navigate(R.id.action_categoriesFragment_to_requestsFragment)
        } else {
            Error.showInternetError(this.requireActivity())
        }
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