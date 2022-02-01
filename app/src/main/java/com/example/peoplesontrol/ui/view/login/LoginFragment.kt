package com.example.peoplesontrol.ui.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.data.model.Login
import com.example.peoplesontrol.data.model.TokenData
import com.example.peoplesontrol.databinding.FragmentLoginBinding
import com.example.peoplesontrol.ui.view.MainActivity
import com.example.peoplesontrol.ui.viewmodel.LoginViewModel
import com.example.peoplesontrol.ui.viewmodel.LoginViewModelFactory
import com.example.peoplesontrol.utils.Error
import com.example.peoplesontrol.utils.Status
import com.example.peoplesontrol.utils.Network

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.btnNonAuth.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.btnLogin.setOnClickListener {
            if (Network.isConnected(this.requireActivity())) {
                val phone = binding.editLogin.text.toString().replace("-", "").replace("(", "")
                    .replace(")", "").replace(" ", "")
                if (binding.editLogin.text!!.isNotEmpty() && binding.editPassword.text!!.isNotEmpty()) {
                    login(
                        Login(
                            phone,
                            binding.editPassword.text.toString()
                        )
                    )
                } else {
                    binding.inputLogin.error = resources.getString(R.string.input_error)
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

    private fun login(login: Login) {
        viewModel.login(login).observe(this.viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { tokens -> setTokens(tokens) }
                        startActivity(Intent(this.requireContext(), MainActivity::class.java))
                    }
                    Status.ERROR -> {
                        Error.showError(this.requireActivity())
                    }
                }
            }
        })
    }

    private fun setTokens(tokens: TokenData) {
        Data.token = tokens
        val sharedPreference =
            this.requireContext().getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.clear()
        editor.putString("refreshToken", Data.token.refreshToken)
        editor.apply()
    }

    private fun setupEditTextListener() {
        binding.editLogin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.inputLogin.error = null
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
}