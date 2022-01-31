package com.example.peoplesontrol.ui.view.request

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.databinding.FragmentDetailRequestBinding
import com.example.peoplesontrol.ui.viewmodel.RequestViewModel
import com.example.peoplesontrol.ui.viewmodel.ViewModelFactory
import com.example.peoplesontrol.utils.Status
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailRequestFragment : Fragment() {

    private var _binding: FragmentDetailRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequestViewModel
    private var request: Request? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        arguments?.let {
            request = it.getParcelable(REQUEST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        request?.requestId?.let { watchRequest(it) }
        setRequest()
        binding.btnShowMap.setOnClickListener {
            val dialogMapFragment = DialogMapFragment.newInstance(
                request?.latitude!!.toDouble(),
                request?.longitude!!.toDouble()
            )
            dialogMapFragment.show(childFragmentManager, TAG_MAP)
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

    private fun setRequest() {
        if (request?.problem_categories?.isEmpty() == true) {
            binding.category.text = resources.getString(R.string.input_category)
        } else {
            binding.category.text = request?.problem_categories?.get(0)?.title
        }
        binding.statusRequest.text = request?.status
        binding.idRequest.text = "№" + request?.requestId.toString()
        binding.descriptionRequest.text = request?.description
        binding.addressRequest.text = request?.location
        binding.dateRequest.text = request?.created_at
        binding.btnAddRequest.setOnClickListener {
            val bundle =
                bundleOf(REQUEST to request, RequestFragment.IS_ADD_TO_REQUEST to true)
            findNavController().navigate(R.id.action_detailFragment_to_editRequestFragment, bundle)
        }
    }

    private fun watchRequest(id: Long) {
        viewModel.watchRequest(id).observe(this.viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(this.requireContext(), resource.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }

    companion object {
        const val REQUEST = "REQUEST"
        const val TAG_MAP = "MAP"
    }
}