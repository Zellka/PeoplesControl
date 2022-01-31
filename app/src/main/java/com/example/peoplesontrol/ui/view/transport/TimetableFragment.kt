package com.example.peoplesontrol.ui.view.transport

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.model.Timetable
import com.example.peoplesontrol.databinding.FragmentTimetableBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.*

class TimetableFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentTimetableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimetableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupUI()
        val cal = Calendar.getInstance()
        binding.time.text = SimpleDateFormat("HH:mm").format(cal.time)
        binding.timeContainer.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(
                this.requireContext(),
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
        binding.btnCheckTimetable.setOnClickListener {
            binding.titleMyTime.visibility = View.VISIBLE
            binding.timeStation.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
    }

    private fun setupUI() {
        val adapterStations: ArrayAdapter<String> =
            ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                Timetable.busStations
            )
        adapterStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.busStations.adapter = adapterStations

        val adapterRegions: ArrayAdapter<String> =
            ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                Timetable.regions
            )
        adapterRegions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.regions.adapter = adapterRegions

        val adapterBus: ArrayAdapter<String> =
            ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                Timetable.busList
            )
        adapterBus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.bus.adapter = adapterBus
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(Timetable.stationsMarkers)
        )
        p0.addMarker(
            MarkerOptions()
                .position(LatLng(48.042806, 37.747487))
                .title("ЖД вокзал")
        )
        p0.addMarker(
            MarkerOptions()
                .position(LatLng(47.987455, 37.798827))
                .title("АС Центр")
        )
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(48.010320, 37.769510), 11f))
    }
}