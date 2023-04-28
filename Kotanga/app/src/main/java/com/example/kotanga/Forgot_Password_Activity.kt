package com.example.kotanga

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.kotanga.databinding.ActivityForgotPasswordBinding
import com.example.kotanga.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Forgot_Password_Activity : AppCompatActivity() {

    private lateinit var etEMail:EditText

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.setBackgroundColor()

        etEMail = findViewById(R.id.et_email)

        auth = FirebaseAuth.getInstance()

        binding.btnLien.setOnClickListener{
            val sEmail = etEMail.text.toString()
            auth.sendPasswordResetEmail(sEmail)
                .addOnSuccessListener {
                    Toast.makeText(this, "Mail envoyer avec succes!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Mail inconnue!", Toast.LENGTH_SHORT).show()
                }

            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun setBackgroundColor() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        var isNightModeOn = sharedPreferences.getBoolean("isNightModeOn", false)
        if (isNightModeOn) {
            binding.root.setBackgroundColor(resources.getColor(R.color.primary_color_darkMode))
        } else {
            binding.root.setBackgroundColor(resources.getColor(R.color.primary_color))
        }
    }
}