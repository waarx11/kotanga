package com.example.kotanga

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotanga.databinding.ActivityGroupChatBinding

class GroupChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatBinding

    private lateinit var dbManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)

        binding.groupName.text = "GroupName"
    }
}