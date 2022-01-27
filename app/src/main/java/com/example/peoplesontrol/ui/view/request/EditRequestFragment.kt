package com.example.peoplesontrol.ui.view.request

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.*
import com.example.peoplesontrol.databinding.FragmentEditRequestBinding
import com.example.peoplesontrol.ui.adapter.PhotoAdapter
import com.example.peoplesontrol.ui.view.login.LoginActivity
import com.example.peoplesontrol.ui.view.request.DetailRequestFragment.Companion.REQUEST
import com.example.peoplesontrol.ui.view.request.RequestFragment.Companion.IS_ADD_TO_REQUEST
import com.example.peoplesontrol.ui.viewmodel.ProfileViewModel
import com.example.peoplesontrol.ui.viewmodel.ProfileViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.example.peoplesontrol.utils.Status
import com.google.android.gms.maps.model.LatLng
import java.util.*

class EditRequestFragment : Fragment() {

    private var _binding: FragmentEditRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private lateinit var adapterPhoto: PhotoAdapter

    private var photos = arrayListOf<Uri?>()
    private var request: Request? = null
    private var isAddToRequest = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            request = it.getParcelable(REQUEST)
            isAddToRequest = it.getBoolean(IS_ADD_TO_REQUEST)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        if (request?.problem_categories?.isNotEmpty() == true) {
            binding.category.text = request?.problem_categories!![0].title
        } else {
            binding.category.text = resources.getString(R.string.no_category)
        }
        binding.editDescription.setText(request?.description)
        binding.editAddress.setText(request?.location)
        binding.rvPhotos.layoutManager = GridLayoutManager(this.requireContext(), 3)
        adapterPhoto = PhotoAdapter()
        binding.btnAddPhoto.setOnClickListener {
            addPhoto()
        }
        var array: Array<Int> = arrayOf()
        binding.btnSend.setOnClickListener {
            val categoriesId = mutableListOf<Int>()
            if (request?.problem_categories?.isNotEmpty() == true) {
                request?.problem_categories?.get(0)?.let { it1 -> categoriesId.add(it1.categoryId) }
                array = categoriesId.toTypedArray()
            }
            val currentAddress = binding.editAddress.text.toString()
            val latLng = getLatLng(currentAddress)
            if (isAddToRequest) {
                createRequest(
                    RequestPost(
                        null,
                        request?.requestId,
                        array,
                        binding.editDescription.text.toString(),
                        resources.getString(R.string.source),
                        0,
                        currentAddress,
                        latLng.latitude,
                        latLng.longitude,
                    )
                )
            } else {
                request?.requestId?.let { it1 ->
                    request?.rating?.let { it2 ->
                        RequestPost(
                            request?.requestId,
                            null,
                            array,
                            binding.editDescription.text.toString(),
                            resources.getString(R.string.source),
                            it2,
                            currentAddress,
                            latLng.latitude,
                            latLng.longitude,
                        )
                    }?.let { it3 ->
                        updateRequest(
                            it1,
                            it3
                        )
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this.requireContext()))
            )
        )[ProfileViewModel::class.java]
    }

    private fun getLatLng(currentAddress: String): LatLng {
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocationName(currentAddress, 1)
        val address: Address = addresses[0]
        return LatLng(address.latitude, address.longitude)
    }

    private fun updateRequest(id: Long, request: RequestPost) {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.updateRequest(id, request).observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            clearEditText()
                            Toast.makeText(
                                this.requireContext(),
                                resources.getString(R.string.success_update),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
        } else {
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun createRequest(request: RequestPost) {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.createRequest(request).observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            clearEditText()
                            Toast.makeText(
                                this.requireContext(),
                                resources.getString(R.string.success_add),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        Status.ERROR -> {
                            if (resource.message?.contains("401") == true) {
                                refreshToken()
                            } else {
                                Error.showError(this.requireActivity())
                            }
                        }
                    }
                }
            })
        } else {
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun addPhoto() {
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
                    Toast.makeText(
                        this.requireContext(),
                        resources.getString(R.string.error_permission),
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun refreshToken() {
        viewModel.refreshToken().observe(this.viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { tokens -> setTokens(tokens) }
                        val sharedPreference = this.requireContext()
                            .getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.clear()
                        editor.putString("refreshToken", Data.token.refreshToken)
                        editor.apply()
                        Toast.makeText(
                            this.requireContext(),
                            resources.getString(R.string.try_again),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.ERROR -> {
                        startActivity(Intent(this.requireContext(), LoginActivity::class.java))
                    }
                }
            }
        })
    }

    private fun setTokens(tokens: TokenData) {
        Data.token = tokens
    }

    private fun clearEditText() {
        binding.editAddress.setText("")
        binding.editDescription.setText("")
        binding.editVideo.setText("")
        adapterPhoto.setData(listOf())
        adapterPhoto.notifyDataSetChanged()
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }
}