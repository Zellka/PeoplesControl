package com.example.peoplesontrol.ui.view.request

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.databinding.FragmentDialogMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddLatLngFragment : DialogFragment(), OnMapReadyCallback {

    private var _binding: FragmentDialogMapBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
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
        val currentLatLng = LatLng(48.015884, 37.802850)
        p0.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        var count = 0
        p0.setOnMapClickListener {
            if (count == 0) {
                Data.point = it
                p0.addMarker(
                    MarkerOptions().position(it).icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED
                        )
                    )
                )
                count++
                Toast.makeText(
                    this.requireContext(),
                    resources.getString(R.string.save_address),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}