package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotanga.databinding.ActivityGroupChatBinding
import com.example.kotanga.databinding.ActivityHomeBinding

class GroupChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatBinding

    private lateinit var dbManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.groupName.text = "GroupName"

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.backbutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.accountbutton.setOnClickListener{
            startActivity(Intent(this, Compte_Activity::class.java))
        }
    }
}