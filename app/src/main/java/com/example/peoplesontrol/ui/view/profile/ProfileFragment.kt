package com.example.peoplesontrol.ui.view.profile

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.FragmentProfileBinding
import com.example.peoplesontrol.ui.adapter.AppealAdapter
import com.example.peoplesontrol.ui.view.appeal.DetailAppealFragment

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AppealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        val bundle = bundleOf(DetailAppealFragment.APPEAL to appeal)
        findNavController().navigate(R.id.action_profileFragment_to_detailAppealFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_archive) {
            findNavController().navigate((R.id.action_profileFragment_to_archiveFragment))
        }
        if (id == R.id.action_setting) {
            findNavController().navigate((R.id.action_profileFragment_to_editProfileFragment))
        }
        return super.onOptionsItemSelected(item)
    }
}