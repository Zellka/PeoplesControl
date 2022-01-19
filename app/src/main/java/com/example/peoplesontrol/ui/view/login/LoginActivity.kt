package com.example.peoplesontrol.ui.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.peoplesontrol.R
import com.example.peoplesontrol.databinding.ActivityLoginBinding
import com.example.peoplesontrol.ui.view.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startActivity(Intent(this, MainActivity::class.java))

        val navController = findNavController(R.id.nav_host_fragment_activity_login)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
    }
}