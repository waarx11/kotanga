package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotanga.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.tvHavenAccount.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPw.setOnClickListener{
            startActivity(Intent(this, Forgot_Password_Activity::class.java))
        }
    }
}