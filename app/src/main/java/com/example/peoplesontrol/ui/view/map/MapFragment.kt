package com.example.peoplesontrol.ui.view.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.layout_error.view.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_CODE
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng)
        p0.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        p0.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        p0.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        val dialogAppealFragment = DialogAppealFragment()
        dialogAppealFragment.show(childFragmentManager, "MAP")
        return true
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

    companion object {
        private const val PERMISSION_CODE = 101
    }
}