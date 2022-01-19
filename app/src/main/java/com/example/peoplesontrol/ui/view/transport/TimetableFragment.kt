package com.example.peoplesontrol.ui.view.transport

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
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
import kotlinx.android.synthetic.main.layout_error.view.*
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            findNavController().navigate(R.id.action_transportFragment_to_busFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}