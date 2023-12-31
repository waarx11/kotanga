package com.example.kotanga

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.kotanga.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var name: TextView

    private lateinit var surname: TextView

    private lateinit var tvEmail: TextView

    private lateinit var tvPassword: TextView

    private lateinit var tvValPassword: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        name = findViewById(R.id.et_nom)

        surname = findViewById(R.id.et_prenom)

        tvEmail = findViewById(R.id.et_email)

        tvPassword = findViewById(R.id.et_password)

        tvValPassword = findViewById(R.id.et_valid_password)

        this.setBackgroundColor()

        binding.btnRegister.setOnClickListener {
            val sEmail = tvEmail.text.toString().trim()
            val sName = name.text.toString().trim()
            val sSurname = surname.text.toString().trim()
            val sPassword = tvPassword.text.toString().trim()
            val sValPassword = tvValPassword.text.toString().trim()

            if (sPassword.equals(sValPassword)) {
                auth.createUserWithEmailAndPassword(sEmail, sPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            updateUI(user)
                            val database = Firebase.database
                            val userId = Firebase.auth.currentUser?.uid
                            val userRef = database.getReference("users/$userId/name")
                            userRef.setValue("$sSurname $sName $sEmail")
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext, "Authentification impossible.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                        }
                    }
            }
            else{
                Toast.makeText(
                    baseContext, "La confirmation de mot de passe ne correspond pas au mot de passe, réessayer.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this,LoginActivity::class.java))
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