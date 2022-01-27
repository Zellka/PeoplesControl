package com.example.peoplesontrol.ui.view.request

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.databinding.FragmentRequestBinding
import com.example.peoplesontrol.ui.adapter.RequestAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.ui.view.request.DetailRequestFragment.Companion.REQUEST
import com.example.peoplesontrol.ui.viewmodel.RequestViewModel
import com.example.peoplesontrol.ui.viewmodel.ViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.example.peoplesontrol.utils.Status

class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequestViewModel

    private lateinit var adapter: RequestAdapter

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            position = it.getInt(POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModel()
        setupUI()
        getRequests()
        binding.swipeRefresh.setOnRefreshListener {
            binding.progressBar.visibility = View.INVISIBLE
            getRequests()
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
        ).get(RequestViewModel::class.java)
    }

    private fun setupUI() {
        binding.rvRequests.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = RequestAdapter { request: Request, isAdd: Boolean -> showRequest(request, isAdd) }
        adapter.setData(arrayListOf())
        binding.rvRequests.adapter = adapter
    }

    private fun getRequests() {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.getRequestsByCategoryId(Data.categoryId)
                .observe(this.viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                binding.rvRequests.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                resource.data?.let { requests -> setRequests(requests) }
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
                                binding.rvRequests.visibility = View.GONE
                            }
                        }
                    }
                })
        } else {
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun setRequests(list: List<Request>) {
        val newList = list.filter { it.deleted_at == null }
        adapter.apply {
            when (position) {
                0 -> {
                    val sortList = newList.sortedBy { it.requestId }
                    setData(sortList)
                }
                1 -> {
                    val sortList = newList.sortedBy { it.description }
                    setData(sortList)
                }
                2 -> {
                    val sortList = newList.sortedBy { it.rating }
                    setData(sortList)
                }
                else -> {
                    val sortList = newList.sortedBy { it.requestId }
                    setData(sortList)
                }
            }
            //setData(newList)
            notifyDataSetChanged()
        }
    }

    private fun showRequest(request: Request, isAddRequest: Boolean) {
        if (isAddRequest) {
            val bundle = bundleOf(REQUEST to request, IS_ADD_TO_REQUEST to isAddRequest)
            findNavController().navigate(
                R.id.action_requestsFragment_to_editRequestFragment,
                bundle
            )
        } else {
            val bundle = bundleOf(REQUEST to request)
            findNavController().navigate(
                R.id.action_requestsFragment_to_detailRequestFragment,
                bundle
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            findNavController().navigate(R.id.action_requestsFragment_to_categoriesFragment)
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

    companion object {
        private const val POSITION = "POSITION"
        const val IS_ADD_TO_REQUEST = "IS_ADD_TO_REQUEST"

        @JvmStatic
        fun newInstance(position: Int) =
            RequestFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION, position)
                }
            }
    }
}