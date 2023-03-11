package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotanga.databinding.ActivityForgotPasswordBinding
import com.example.kotanga.databinding.ActivityRegisterBinding

class Forgot_Password_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLien.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}