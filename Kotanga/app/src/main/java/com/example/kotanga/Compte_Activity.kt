package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotanga.databinding.ActivityCompteBinding

class Compte_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCompteBinding

    companion object {
        const val TAG = "CompteActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compte)
        binding = ActivityCompteBinding.inflate(layoutInflater)

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}