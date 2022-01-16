package com.example.peoplesontrol.ui.view.transport

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.peoplesontrol.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        val directions =
            arrayOf(
                "Прямое",
                "Обратно"
            )
        val adapterDirections: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, directions)
        adapterDirections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.directions.adapter = adapterDirections

        val busStations =
            arrayOf(
                "ЖД",
                "Бакины",
                "Роза",
                "Университетская",
                "Крытый"
            )
        val adapterStations: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, busStations)
        adapterStations.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.busStations.adapter = adapterStations

        val cal = Calendar.getInstance()
        binding.time.text = SimpleDateFormat("HH:mm").format(cal.time)
        binding.time.setOnClickListener {
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
    }

    override fun onMapReady(p0: GoogleMap) {
        p0.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016, 143.321),
                    LatLng(-34.747, 145.592),
                    LatLng(-34.364, 147.891),
                    LatLng(-33.501, 150.217),
                    LatLng(-32.306, 149.248),
                    LatLng(-32.491, 147.309)
                )
        )
        p0.addMarker(
            MarkerOptions()
                .position(LatLng(-35.016, 143.321))
                .title("Start")
        )
        p0.addMarker(
            MarkerOptions()
                .position(LatLng(-32.491, 147.309))
                .title("End")
        )
        p0.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-35.016, 143.321), 5f))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            findNavController().navigate(R.id.action_transportFragment_to_busFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}