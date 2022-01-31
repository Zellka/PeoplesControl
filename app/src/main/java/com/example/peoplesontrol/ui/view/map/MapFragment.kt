package com.example.peoplesontrol.ui.view.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Request
import com.example.peoplesontrol.databinding.FragmentMapBinding
import com.example.peoplesontrol.ui.view.map.DialogRequestFragment.Companion.MAP
import com.example.peoplesontrol.ui.viewmodel.RequestViewModel
import com.example.peoplesontrol.ui.viewmodel.ViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.example.peoplesontrol.utils.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequestViewModel
    private var requests: ArrayList<Request> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        getRequests()
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

    private fun getRequests() {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.getRequests().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { requestList -> retrieveData(requestList) }
                            val mapFragment = childFragmentManager
                                .findFragmentById(R.id.map) as SupportMapFragment
                            mapFragment.getMapAsync(this)
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Error.showError(this.requireActivity())
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            })
        } else {
            viewModel.getRequestsFromDB().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.progressBar.visibility = View.GONE
                            resource.data?.let { requestList -> retrieveData(requestList) }
                            val mapFragment = childFragmentManager
                                .findFragmentById(R.id.map) as SupportMapFragment
                            mapFragment.getMapAsync(this)
                        }
                        Status.ERROR -> {
                            binding.progressBar.visibility = View.GONE
                            Error.showError(this.requireActivity())
                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                    }
                }
            })
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun retrieveData(list: List<Request>) {
        requests.addAll(list.filter { it.deleted_at == null })
    }

    override fun onMapReady(p0: GoogleMap) {
        val currentLatLng = LatLng(48.015884, 37.802850)
        for (i in requests) {
            when {
                i.problem_categories.isEmpty() -> {
                    p0.addMarker(
                        MarkerOptions()
                            .position(LatLng(i.latitude, i.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                            .title(i.location)
                    )
                }
                i.problem_categories[0].categoryId % 2 == 0 -> {
                    p0.addMarker(
                        MarkerOptions()
                            .position(LatLng(i.latitude, i.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .title(i.location)
                    )
                }
                i.problem_categories[0].categoryId % 3 == 0 -> {
                    p0.addMarker(
                        MarkerOptions()
                            .position(LatLng(i.latitude, i.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                            .title(i.location)
                    )
                }
                i.problem_categories[0].categoryId % 5 == 0 -> {
                    p0.addMarker(
                        MarkerOptions()
                            .position(LatLng(i.latitude, i.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            .title(i.location)
                    )
                }
                else -> {
                    p0.addMarker(
                        MarkerOptions()
                            .position(LatLng(i.latitude, i.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title(i.location)
                    )
                }
            }
        }
        p0.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        p0.setOnMarkerClickListener(this)
    }


    override fun onMarkerClick(p0: Marker): Boolean {
        val request = requests.find { request -> request.location == p0.title }
        val dialogAppealFragment = request?.let { DialogRequestFragment.newInstance(it) }
        dialogAppealFragment?.show(childFragmentManager, MAP)
        return true
    }
}