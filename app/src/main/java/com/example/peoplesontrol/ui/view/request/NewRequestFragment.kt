package com.example.peoplesontrol.ui.view.request

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.peoplesontrol.databinding.FragmentNewRequestBinding
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.example.peoplesontrol.ui.adapter.PhotoAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.core.app.ActivityCompat
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.ui.viewmodel.RequestViewModel
import com.example.peoplesontrol.ui.viewmodel.ViewModelFactory
import com.example.peoplesontrol.utils.Status
import java.util.*
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.*
import com.example.peoplesontrol.ui.adapter.VideoAdapter
import com.example.peoplesontrol.ui.view.login.LoginActivity
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.google.android.gms.maps.model.LatLng
import java.io.File
import okhttp3.RequestBody
import okhttp3.MediaType
import kotlin.collections.HashMap
import com.example.peoplesontrol.utils.FileUtils

class NewRequestFragment : Fragment() {

    private var _binding: FragmentNewRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RequestViewModel

    private lateinit var adapterPhoto: PhotoAdapter
    private lateinit var adapterVideo: VideoAdapter
    private var photos = arrayListOf<Uri?>()
    private var videos = arrayListOf<String?>()
    private var uriList = arrayListOf<Uri>()

    private var categories = mutableListOf<Category>()
    private var titleCategories = mutableListOf<String>()
    private var fileUtils = FileUtils()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        getCategories()
        setupUI()
        binding.btnAddPhoto.setOnClickListener {
            addMedia(false)
        }
        binding.btnAddVideo.setOnClickListener {
            addMedia(true)
        }
        binding.btnAddMarker.setOnClickListener {
            val mapFragment = AddLatLngFragment()
            mapFragment.show(childFragmentManager, TAG_MAP)
        }
        var chooseCategory = ""
        binding.menuCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                chooseCategory = titleCategories[selectedItemPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        var currentAddress = ""
        var latLng: LatLng? = null
        binding.btnSend.setOnClickListener {
            if (Network.isConnected(this.requireActivity())) {
                if (isDataNotEmpty()) {
                    val category = categories.find { it.title == chooseCategory }
                    val categoriesId = mutableListOf<Int>()
                    category?.let { categoriesId.add(it.categoryId) }
                    val array: Array<Int> = categoriesId.toTypedArray()
                    if (Data.point != null) {
                        currentAddress = getAddress()
                        latLng = Data.point
                        Data.point = null
                    } else {
                        currentAddress = binding.editAddress.text.toString()
                        latLng = getLatLng(currentAddress)
                    }
                    createRequest(
                        RequestPost(
                            null,
                            null,
                            array,
                            binding.editDescription.text.toString(),
                            resources.getString(R.string.source),
                            0,
                            currentAddress,
                            latLng!!.latitude,
                            latLng!!.longitude
                        )
                    )
                } else {
                    binding.inputDescription.error = resources.getString(R.string.input_error)
                    binding.inputAddress.error = resources.getString(R.string.input_error)
                }
            } else {
                Error.showInternetError(this.requireActivity())
            }
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

    private fun getLatLng(currentAddress: String): LatLng {
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocationName(currentAddress, 1)
        val address: Address = addresses[0]
        return LatLng(address.latitude, address.longitude)
    }

    private fun getAddress(): String {
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(Data.point!!.latitude, Data.point!!.longitude, 1)
        return addresses[0].getAddressLine(0)
    }

    private fun setupUI() {
        binding.rvPhotos.layoutManager = GridLayoutManager(this.requireContext(), 3)
        adapterPhoto = PhotoAdapter { position: Int -> removePhoto(position) }
        binding.rvVideos.layoutManager = LinearLayoutManager(this.requireContext())
        adapterVideo = VideoAdapter { position: Int -> removeVideo(position) }
    }

    private fun isDataNotEmpty(): Boolean {
        return binding.editDescription.text!!.isNotEmpty() && (binding.editAddress.text!!.isNotEmpty() || Data.point != null)
    }

    private fun addMedia(isVideo: Boolean) {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) ==
            PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, PERMISSION_CODE)
        } else {
            if (isVideo) {
                pickVideoFromGallery()
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun removePhoto(position: Int) {
        photos.removeAt(position)
        adapterPhoto.removeItem(position)
    }


    private fun removeVideo(position: Int) {
        videos.removeAt(position)
        adapterVideo.removeItem(position)
    }

    private fun createRequest(request: RequestPost) {
        val list: MutableList<RequestBody> = ArrayList()
        for (uri in uriList) {
            list.add(prepareFiles(uri))
        }
        val map: HashMap<String, Array<RequestBody>> = hashMapOf()
        map["files"] = list.toTypedArray()
        viewModel.createRequest(request, map).observe(this.viewLifecycleOwner, Observer {
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
                        refreshToken()
                    }
                }
            }
        })
    }

    private fun getCategories() {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.getCategories().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { categories -> setCategories(categories) }
                            val adapter: ArrayAdapter<String> =
                                ArrayAdapter(
                                    this.requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    titleCategories
                                )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.menuCategory.adapter = adapter
                        }
                        Status.ERROR -> {
                            Error.showError(this.requireActivity())
                        }
                    }
                }
            })
        } else {
            Error.showInternetError(this.requireActivity())
        }
    }

    private fun setCategories(list: List<Category>) {
        categories.addAll(list)
        for (i in list) {
            titleCategories.add(i.title)
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
                        editor.putString(
                            "refreshToken",
                            Data.token.refreshToken
                        )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
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


    private fun pickVideoFromGallery() {
        val intent = Intent()
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, VIDEO_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            binding.rvPhotos.visibility = View.VISIBLE
            if (data.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData?.getItemAt(i)?.uri
                    val imagePath: String = imageUri?.let {
                        fileUtils.getPath(
                            this.requireContext(),
                            it
                        ).toString()
                    }.toString()
                    uriList.add(Uri.parse(imagePath))
                    photos.add(imageUri)
                    adapterPhoto.setData(photos)
                    binding.rvPhotos.adapter = adapterPhoto
                }
            } else {
                val imagePath: String = data.data?.let {
                    fileUtils.getPath(
                        this.requireContext(),
                        it
                    ).toString()
                }.toString()
                uriList.add(Uri.parse(imagePath))
                photos.add(data.data)
                adapterPhoto.setData(photos)
                binding.rvPhotos.adapter = adapterPhoto
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_PICK_CODE && data != null) {
            binding.rvVideos.visibility = View.VISIBLE
            if (data.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val videoUri = data.clipData?.getItemAt(i)?.uri
                    val videoPath: String = videoUri?.let {
                        fileUtils.getPath(
                            this.requireContext(),
                            it
                        ).toString()
                    }.toString()
                    uriList.add(Uri.parse(videoPath))
                    val file = File(Uri.parse(videoPath).path)
                    videos.add(file.name)
                    adapterVideo.setData(videos)
                    binding.rvVideos.adapter = adapterVideo
                }
            } else {
                val videoPath: String = data.data?.let {
                    fileUtils.getPath(
                        this.requireContext(),
                        it
                    ).toString()
                }.toString()
                uriList.add(Uri.parse(videoPath))
                val file = File(Uri.parse(videoPath).path)
                videos.add(file.name)
                adapterVideo.setData(videos)
                binding.rvVideos.adapter = adapterVideo
            }
        }
    }

    private fun clearEditText() {
        binding.editAddress.setText("")
        binding.editDescription.setText("")
        adapterPhoto.setData(listOf())
        adapterPhoto.notifyDataSetChanged()
        adapterVideo.setData(listOf())
        adapterVideo.notifyDataSetChanged()
    }


    private fun prepareFiles(fileUri: Uri): RequestBody {
        val file = File(fileUri.path)
        return RequestBody.create(
            MediaType.parse(resources.getString(R.string.header_multipart)),
            file
        )
    }

    companion object {
        private const val TAG_MAP = "MAP"
        private const val IMAGE_PICK_CODE = 1000
        private const val VIDEO_PICK_CODE = 1002
        private const val PERMISSION_CODE = 1001
    }
}