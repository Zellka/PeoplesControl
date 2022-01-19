package com.example.peoplesontrol.ui.view.appeal

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Appeal
import com.example.peoplesontrol.databinding.FragmentAppealBinding
import com.example.peoplesontrol.ui.adapter.AppealAdapter
import android.view.SubMenu
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_error.view.*


class AppealFragment : Fragment() {

    private var _binding: FragmentAppealBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AppealAdapter

    private var cities = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = listOf(
            Appeal(
                123,
                "Иванов Иван Иванович",
                "Нарушение ПДД",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "https://ahjdhajshd.com",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                0,
                14
            ),
            Appeal(
                124,
                "Петров Иван Иванович",
                "Нарушение КЗОТ",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                0,
                1
            ),
            Appeal(
                125,
                "Иванов Василия Иванович",
                "Нарушение ПДД",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                0,
                15
            ),
            Appeal(
                126,
                "Иванов Юрий Иванович",
                "Нарушение КЗОТ",
                "г.Донецк, ул.Артёма, 66",
                "53",
                "26",
                "В обработке",
                "https://ahjdhajshd.com",
                "14.01.22",
                "Большая авария, 2 машины, 1 пострадавший",
                0,
                24
            )
        )

        binding.rvAppeals.layoutManager = LinearLayoutManager(this.requireContext())
        adapter = AppealAdapter { appeal: Appeal -> showAppeal(appeal) }
        adapter.setData(list)
        binding.rvAppeals.adapter = adapter

        cities = arrayListOf(
            "Донецк",
            "Макеевка",
            "Харцызск",
            "Горловка",
            "Ясиноватая",
            "Торез"
        )
    }

    private fun showAppeal(appeal: Appeal) {
        val bundle = bundleOf(DetailAppealFragment.APPEAL to appeal)
        findNavController().navigate(R.id.action_appealsFragment_to_detailAppealFragment, bundle)
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
        inflater.inflate(R.menu.appeal_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            findNavController().navigate(R.id.action_appealsFragment_to_categoriesFragment)
        }
        if (id == R.id.action_city) {
            item.subMenu.clear()
            val sub = item.subMenu
            for ((id, city) in cities.withIndex()) {
                sub.add(0, id, 0, city)
            }
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