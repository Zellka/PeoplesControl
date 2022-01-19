package com.example.peoplesontrol.ui.view.profile

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.FragmentArchiveBinding
import com.example.peoplesontrol.ui.adapter.AppealAdapter
import com.example.peoplesontrol.ui.view.appeal.DetailAppealFragment
import kotlinx.android.synthetic.main.layout_error.view.*

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
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
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = listOf(
            Appeal(
                11,
                "Петрова Ольга Николаевна",
                "Нарушение ПДД",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "Выполнено",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                10,
                14
            ),
            Appeal(
                16,
                "Петрова Ольга Николаевна",
                "Нарушение КЗОТ",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "Выполнено",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                15,
                1
            ),
            Appeal(
                19,
                "Петрова Ольга Николаевна",
                "Нарушение ПДД",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "Выполнено",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                5,
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
        findNavController().navigate(R.id.action_archiveFragment_to_detailAppealFragment, bundle)
    }

    private fun showErrorMessage(){
        val layout: View = activity?.layoutInflater!!.inflate(R.layout.layout_error, null)
        val text = layout.findViewById<View>(R.id.text_error) as TextView
        val img = layout.img_error
        text.text = "Ошибка сервера"
        text.width = 900
        img.setImageResource(R.drawable.ic_error)
        Toast(activity).apply {
            duration = Toast.LENGTH_LONG
            this.view = layout
            setGravity(Gravity.TOP, 0, 0)
        }.show()
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