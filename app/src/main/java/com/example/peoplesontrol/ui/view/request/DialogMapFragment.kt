package com.example.peoplesontrol.ui.view.request

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.FragmentDialogMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DialogMapFragment : DialogFragment(), OnMapReadyCallback {

    private var _binding: FragmentDialogMapBinding? = null
    private val binding get() = _binding!!

    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(LATITUDE)
            longitude = it.getDouble(LONGITUDE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.btnClose.setOnClickListener {
            dialog?.cancel()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }?.let {
            MarkerOptions()
                .position(it)
        }?.let {
            p0.addMarker(
                it
            )
        }
        latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }?.let {
            CameraUpdateFactory.newLatLng(
                it
            )
        }?.let { p0.moveCamera(it) }
    }

    companion object {
        private const val LATITUDE = "LATITUDE"
        private const val LONGITUDE = "LONGITUDE"

        @JvmStatic
        fun newInstance(latitude: Double, longitude: Double) =
            DialogMapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(LATITUDE, latitude)
                    putDouble(LONGITUDE, longitude)
                }
            }
    }
}