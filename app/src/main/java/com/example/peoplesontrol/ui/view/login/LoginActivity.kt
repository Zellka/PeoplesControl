package com.example.peoplesontrol.ui.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.peoplesontrol.R
import com.example.peoplesontrol.data.api.ApiHelper
import com.example.peoplesontrol.data.api.RetrofitBuilder
import com.example.peoplesontrol.data.db.DatabaseBuilder
import com.example.peoplesontrol.data.db.DatabaseHelperImpl
import com.example.peoplesontrol.data.model.Data
import com.example.peoplesontrol.data.model.Message
import com.example.peoplesontrol.data.model.TokenData
import com.example.peoplesontrol.databinding.ActivityLoginBinding
import com.example.peoplesontrol.ui.view.MainActivity
import com.example.peoplesontrol.ui.viewmodel.LoginViewModel
import com.example.peoplesontrol.ui.viewmodel.LoginViewModelFactory
import com.example.peoplesontrol.utils.Status

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        val navController = findNavController(R.id.nav_host_fragment_activity_login)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
        val sharedPreference = getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
        val refreshToken = sharedPreference.getString("refreshToken", null)
        refreshToken(Message(refreshToken.toString()), "Bearer " + refreshToken.toString())
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(this))
            )
        )[LoginViewModel::class.java]
    }

    private fun refreshToken(message: Message, token: String) {
        viewModel.refreshToken(message, token).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { tokens -> setTokens(tokens) }
                        val sharedPreference =
                            getSharedPreferences("REFRESH_TOKEN", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.clear()
                        editor.putString("refreshToken", Data.token.refreshToken)
                        editor.apply()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        })
    }

    private fun setTokens(tokens: TokenData) {
        Data.token = tokens
    }
}