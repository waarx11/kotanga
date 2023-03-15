package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kotanga.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var tvEmail: EditText

    private lateinit var tvPassword: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        tvEmail = findViewById(R.id.et_email)

        tvPassword = findViewById(R.id.et_password)

        binding.btnLogin.setOnClickListener{

            val sEmail = tvEmail.text.toString().trim()
            val sPassword = tvPassword.text.toString().trim()

            if(!sEmail.isNullOrEmpty() and !sPassword.isNullOrEmpty()) {
                auth.signInWithEmailAndPassword(sEmail, sPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext, "Authentification impossible.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                        }
                    }
            }else{
                Toast.makeText(
                    baseContext, "Merci de remplir les champs.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvHavenAccount.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPw.setOnClickListener{
            startActivity(Intent(this, Forgot_Password_Activity::class.java))
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}