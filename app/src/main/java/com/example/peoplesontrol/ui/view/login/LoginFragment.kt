package com.example.peoplesontrol.ui.view.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.FragmentLoginBinding
import com.example.peoplesontrol.ui.view.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNonAuth.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        binding.btnLogin.setOnClickListener {
            if (binding.editNumber.text!!.isNotEmpty() && binding.editPassword.text!!.isNotEmpty()) {
                startActivity(Intent(this.requireContext(), MainActivity::class.java))
            } else {
                binding.inputNumber.error = resources.getString(R.string.input_error)
                binding.inputPassword.error = resources.getString(R.string.input_error)
            }
        }
        setupEditTextListener()
    }

    private fun setupEditTextListener() {
        binding.editNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.inputNumber.error = null
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.inputPassword.error = null
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_loginFragment_to_regionFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}