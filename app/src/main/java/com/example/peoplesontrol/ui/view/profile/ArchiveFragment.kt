package com.example.peoplesontrol.ui.view.profile

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Profile
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.databinding.FragmentArchiveBinding
import com.example.peoplesontrol.ui.adapter.RequestAdapter
import com.example.peoplesontrol.ui.view.profile.ProfileFragment.Companion.PROFILE
import com.example.peoplesontrol.ui.view.request.DetailRequestFragment.Companion.REQUEST
import com.example.peoplesontrol.ui.view.request.RequestFragment.Companion.IS_ADD_TO_REQUEST

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RequestAdapter
    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            profile = it.getParcelable(PROFILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvRequests.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = RequestAdapter { appeal: Request, isAdd: Boolean -> showRequest(appeal, isAdd) }
        if (profile?.requests?.isNotEmpty() == true) {
            binding.listEmpty.visibility = View.INVISIBLE
            adapter.apply {
                profile?.requests?.filter { it.deleted_at != null }?.let { setData(it) }
                notifyDataSetChanged()
            }
        } else {
            binding.listEmpty.visibility = View.VISIBLE
        }
        binding.rvRequests.adapter = adapter
    }

    private fun showRequest(request: Request, isAddRequest: Boolean) {
        if (isAddRequest) {
            val bundle =
                bundleOf(REQUEST to request, IS_ADD_TO_REQUEST to isAddRequest)
            findNavController().navigate(R.id.action_archiveFragment_to_editRequestFragment, bundle)
        } else {
            val bundle = bundleOf(REQUEST to request)
            findNavController().navigate(
                R.id.action_archiveFragment_to_detailAppealFragment,
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
            findNavController().navigate(R.id.action_archiveFragment_to_profileFragment)
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