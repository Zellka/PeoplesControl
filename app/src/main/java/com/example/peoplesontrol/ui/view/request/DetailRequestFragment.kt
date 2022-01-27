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
        setupImages()
        binding.btnShowMap.setOnClickListener {
            val dialogMapFragment = DialogMapFragment.newInstance(
                request?.latitude!!.toDouble(),
                request?.longitude!!.toDouble()
            )
            dialogMapFragment.show(childFragmentManager, "MAP")
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
        binding.statusRequest.text = request?.status
        binding.idRequest.text = "№" + request?.requestId.toString()
        binding.descriptionRequest.text = request?.description
        binding.addressRequest.text = request?.location
        binding.dateRequest.text = request?.created_at
//        if (request?.media != "") {
//            binding.videoAppeal.visibility = View.VISIBLE
//            binding.titleVideo.visibility = View.VISIBLE
//            binding.videoAppeal.text = request?.media
//        }
    }

    private fun setupImages() {
        val images = intArrayOf(
            R.drawable.transport_1,
            R.drawable.road_2, R.drawable.city_3
        )
        var position = 0
        binding.imagesRequest.setFactory {
            val imgView = ImageView(this.requireContext())
            imgView.scaleType = ImageView.ScaleType.FIT_CENTER
            imgView.setPadding(8, 8, 8, 8)
            imgView
        }
        binding.imagesRequest.setImageResource(images[position])
        binding.imagesRequest.inAnimation =
            AnimationUtils.loadAnimation(this.requireContext(), android.R.anim.slide_in_left)
        binding.imagesRequest.outAnimation =
            AnimationUtils.loadAnimation(this.requireContext(), android.R.anim.slide_out_right)
        binding.btnPrev.setOnClickListener {
            position = if (position - 1 >= 0) position - 1 else 2
            binding.imagesRequest.setImageResource(images[position])
        }
        binding.btnNext.setOnClickListener {
            position = if (position + 1 < images.size) position + 1 else 0
            binding.imagesRequest.setImageResource(images[position])
        }
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
    }
}