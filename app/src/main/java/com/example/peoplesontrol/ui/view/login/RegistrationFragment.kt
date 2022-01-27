package com.example.peoplesontrol.ui.view.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Signup
import com.example.peoplesontrol.databinding.FragmentRegistrationBinding
import com.example.peoplesontrol.ui.viewmodel.LoginViewModel
import com.example.peoplesontrol.ui.viewmodel.LoginViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Network
import com.example.peoplesontrol.utils.Status

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.btnLogin.setOnClickListener {
            if (Network.isConnected(this.requireActivity())) {
                if (binding.editName.text!!.isNotEmpty() && binding.editNumber.text!!.isNotEmpty() && binding.editPassword.text!!.isNotEmpty()) {
                    signup(
                        Signup(
                            binding.editName.text.toString(),
                            binding.editNumber.text.toString(),
                            binding.editPassword.text.toString()
                        )
                    )
                } else {
                    binding.inputName.error = resources.getString(R.string.input_error)
                    binding.inputNumber.error = resources.getString(R.string.input_error)
                    binding.inputPassword.error = resources.getString(R.string.input_error)
                }
            } else {
                Error.showInternetError(this.requireActivity())
            }
        }
        setupEditTextListener()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this.requireContext()))
            )
        )[LoginViewModel::class.java]
    }

    private fun signup(signup: Signup) {
        viewModel.signup(signup).observe(this.viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            this.requireContext(),
                            resources.getString(R.string.success_signup),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    }
                    Status.ERROR -> {
                        Toast.makeText(this.requireContext(), resource.message, Toast.LENGTH_LONG)
                            .show()
                        Error.showError(this.requireActivity())
                    }
                }
            }
        })
    }

    private fun setupEditTextListener() {
        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.inputName.error = null
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })
        binding.editNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.inputNumber.error = null
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.inputPassword.error = null
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}