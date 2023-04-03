package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotanga.databinding.ActivityCompteBinding
import com.example.kotanga.databinding.ActivityHomeBinding

class Compte_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityCompteBinding

    companion object {
        const val TAG = "CompteActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}