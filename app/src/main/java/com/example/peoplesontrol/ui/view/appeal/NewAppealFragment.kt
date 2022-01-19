package com.example.peoplesontrol.ui.view.appeal

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.peoplesontrol.databinding.FragmentNewAppealBinding
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.peoplesontrol.ui.adapter.PhotoAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.location.Geocoder
import java.util.*

class NewAppealFragment : Fragment() {

    private var _binding: FragmentNewAppealBinding? = null
    private val binding get() = _binding!!

    private var photos = arrayListOf<Uri?>()

    private lateinit var adapterPhoto: PhotoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAppealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories = listOf(
            "Общественный транспорт",
            "Состояние дорог и прилегающий территорий",
            "Состояние благоустройства города",
            "Аварийные участки города",
            "Постройки в аварийном состоянии",
            "Уборка территории и вывоз отходов",
            "Некачественные товары",
            "Скопление животных",
            "Последствия стихийных бедствий",
            "Последствие военных действий",
            "Проявления вандализма",
            "Состояние убежищ",
            "Состояние рабочего места",
            "Скопление криминальных элементов",
            "Нарушение ПДД"
        )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.editCategory.adapter = adapter

        binding.rvPhotos.layoutManager = GridLayoutManager(this.requireContext(), 3)
        adapterPhoto = PhotoAdapter()
        binding.btnAddPhoto.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                pickImageFromGallery()
            }
        }
        binding.btnSend.setOnClickListener {
            val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
            val addresses: List<Address> =
                geocoder.getFromLocationName(binding.editAddress.text.toString(), 1)
            val address: Address = addresses[0]
            val longitude: Double = address.longitude
            val latitude: Double = address.latitude
            Toast.makeText(this.requireContext(), "$longitude and $latitude", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this.requireContext(), "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data?.clipData != null) {
                val count = data.clipData?.itemCount
                binding.rvPhotos.visibility = View.VISIBLE
                for (i in 0 until count!!) {
                    val imageUri = data.clipData?.getItemAt(i)?.uri
                    photos.add(imageUri)
                }
                adapterPhoto.setData(photos)
                binding.rvPhotos.adapter = adapterPhoto
            }
        }
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }
}