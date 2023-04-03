package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.example.kotanga.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var createGroupButton: Button

    companion object {
        const val TAG = "HomeActivity"
    }
    private lateinit var groupLayout: LinearLayout
    val intents = mutableListOf<Intent>()
    private lateinit var currentUser: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createGroupButton = findViewById(R.id.create_group_button)

        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        val userRef = database.getReference("users/$userId/name")
        currentUser = database.getReference("users/$userId")

        groupLayout = findViewById(R.id.group_layout)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.getValue(String::class.java)
                val firstName = name?.substringBefore(" ")
                Log.d(TAG, "User name is: $name")

                binding.tvName.text = "Bienvenue : $firstName"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        binding.accountbutton.setOnClickListener{
            startActivity(Intent(this, Compte_Activity::class.java))
        }

        /*binding.chatbutton.setOnClickListener{
            startActivity(Intent(this, GroupMenu_Activity::class.java))
        }*/

        currentUser.child("groups").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupLayout.removeAllViews()
                for (groupSnapshot in dataSnapshot.children) {
                    val groupName = groupSnapshot.key.toString()
                    val groupButton = Button(applicationContext)
                    groupButton.text = groupName
                    groupButton.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    groupButton.gravity = Gravity.CENTER_HORIZONTAL
                    groupLayout.addView(groupButton)
                    groupButton.setOnClickListener{
                        val intent = Intent(applicationContext, GroupChatActivity::class.java)
                        intent.putExtra("groupName", groupName)
                        intents.add(intent)
                        startActivity(intent)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        createGroupButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.popup_create_group, null)
            builder.setView(view)

            builder.setPositiveButton("Create") { dialog, which ->
                val groupNameEditText = view.findViewById<EditText>(R.id.group_name_textfield)
                val groupName = groupNameEditText.text.toString()

                // Generate a key for the new group
                val groupKey = database.reference.child("groups").push().key

                // Add the new group to the database
                if (groupKey != null) {
                    val groupData = hashMapOf<String, Any>(
                        "name" to groupName,
                        "users" to hashMapOf(userId to true)
                    )
                    database.reference.child("groups").child(groupKey).setValue(groupData)
                        .addOnSuccessListener {
                            // Create a button for the new group
                            val groupButton = Button(this)
                            groupButton.text = groupName
                            groupButton.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            groupButton.gravity = Gravity.CENTER_HORIZONTAL
                            //groupLayout.addView(groupButton)

                            groupButton.setOnClickListener{
                                val intent = Intent(this, GroupChatActivity::class.java)
                                intent.putExtra("groupName", groupName)
                                intents.add(intent)
                                startActivity(intent)
                            }
                        }
                }

                // Add the new group to the currentUser's groups
                currentUser.child("groups").child(groupName).setValue(true)

                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}
