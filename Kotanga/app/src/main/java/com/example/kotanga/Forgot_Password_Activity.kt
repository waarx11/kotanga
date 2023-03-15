package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.kotanga.databinding.ActivityForgotPasswordBinding
import com.example.kotanga.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Forgot_Password_Activity : AppCompatActivity() {

    private lateinit var etPassword:EditText

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etPassword = findViewById(R.id.et_password)

        auth = FirebaseAuth.getInstance()

        binding.btnLien.setOnClickListener{
            val sPassword = etPassword.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    Toast.makeText(this, "Vérifiez votre Email", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }

            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}