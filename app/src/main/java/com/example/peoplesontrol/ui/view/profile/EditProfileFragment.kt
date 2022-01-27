package com.example.peoplesontrol.ui.view.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.data.model.Profile
import com.example.peoplesontrol.data.model.ProfilePost
import com.example.peoplesontrol.data.model.TokenData
import com.example.peoplesontrol.databinding.FragmentEditProfileBinding
import com.example.peoplesontrol.ui.view.login.LoginActivity
import com.example.peoplesontrol.ui.view.profile.ProfileFragment.Companion.PROFILE
import com.example.peoplesontrol.ui.viewmodel.ProfileViewModel
import com.example.peoplesontrol.ui.viewmodel.ProfileViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.example.peoplesontrol.utils.Status

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            profile = it.getParcelable(PROFILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.editName.setText(profile?.full_name)
        binding.editNumber.setText(profile?.phone)
        binding.editEmail.setText(profile?.email)
        if (Network.isConnected(this.requireActivity())) {
            binding.btnOk.setOnClickListener {
                updateProfile(
                    ProfilePost(
                        binding.editName.text.toString(),
                        "ул.Артёма, 15",
                        binding.editNumber.text.toString(),
                        binding.editEmail.text.toString(),
                        false,
                        false,
                        false
                    )
                )
            }
            binding.btnDeleteProfile.setOnClickListener {
                showMessage()
            }
        } else {
            Error.showInternetError(this.requireActivity())
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

    private fun updateProfile(updateProfile: ProfilePost) {
        profile?.profileId?.let {
            viewModel.updateProfile(it, updateProfile)
                .observe(this.viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this.requireContext(),
                                    resources.getString(R.string.success_update),
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate((R.id.action_editProfileFragment_to_profileFragment))
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
        }
    }

    private fun deleteProfile() {
        profile?.let {
            viewModel.deleteProfile(it?.profileId)
                .observe(this.viewLifecycleOwner, Observer {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                Toast.makeText(
                                    this.requireContext(),
                                    resources.getString(R.string.success_remove),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(
                                    Intent(
                                        this.requireContext(),
                                        LoginActivity::class.java
                                    )
                                )
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
        }
    }

    private fun logout() {
        if (Network.isConnected(this.requireActivity())) {
            viewModel.logout().observe(this.viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Toast.makeText(
                                this.requireContext(),
                                resources.getString(R.string.success_logout),
                                Toast.LENGTH_SHORT
                            ).show()
                            val sharedPreference = this.requireContext()
                                .getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
                            sharedPreference.edit().clear().apply()
                            startActivity(Intent(this.requireContext(), LoginActivity::class.java))
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

    private fun showMessage() {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle(resources.getString(R.string.title_delete_profile))
            .setMessage(resources.getString(R.string.question_delete_profile))
            .setCancelable(true)
            .setPositiveButton(resources.getString(R.string.btn_ok)) { _, _ ->
                deleteProfile()
            }
            .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, id ->
                dialog.cancel()
            }
        builder.create()
        builder.show()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.exit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            findNavController().navigate((R.id.action_editProfileFragment_to_profileFragment))
        }
        if (id == R.id.action_exit) {
            logout()
        }
        return super.onOptionsItemSelected(item)
    }
}