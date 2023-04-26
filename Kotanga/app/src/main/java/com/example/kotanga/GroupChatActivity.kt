package com.example.kotanga

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.kotanga.databinding.ActivityGroupChatBinding
import com.example.kotanga.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupChatActivity : AppCompatActivity() {
    private lateinit var addUserInGroup: Button
    private lateinit var groupLayout: LinearLayout
    private lateinit var currentUser: DatabaseReference
    private lateinit var groupName: String

    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var dbManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val groupNameTop = intent.getStringExtra("groupName")

        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        currentUser = database.getReference("users/$userId")
        groupName =
            intent.getStringExtra("groupName").toString() // Récupération du nom du groupe depuis l'intent

        addUserInGroup = findViewById(R.id.middle_button) // Ajout de l'ID du bouton
        groupLayout = findViewById(R.id.group_layout) // Ajout de l'ID de la vue parente

        addUserInGroup.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.popup_add_user_to_group, null)
            builder.setView(view)

            val userList = mutableListOf<String>()

            // Get a reference to the "users" node in Firebase
            val usersRef = database.reference.child("users")

            // Query the database for all users except the current user
            val currentUserID = Firebase.auth.currentUser?.uid
            usersRef.orderByChild("name").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userID = userSnapshot.key.toString()
                        if (userID != currentUserID) {
                            val userName = userSnapshot.child("name").value.toString()
                            userList.add(userName)
                        }
                    }

                    // Set up the dialog
                    val userListView = view.findViewById<ListView>(R.id.user_list_view)
                    val adapter = ArrayAdapter<String>(this@GroupChatActivity, android.R.layout.simple_list_item_1, userList)
                    userListView.adapter = adapter

                    userListView.setOnItemClickListener { parent, view, position, id ->
                        val selectedUserName = userList[position]

                        // Add the selected user to the current group
                        database.reference.child("groupes").child(groupName).child("users").child(selectedUserName).setValue(true)

                        // Add the current group to the selected user's groups
                        val selectedUserQuery = usersRef.orderByChild("name").equalTo(selectedUserName)
                        selectedUserQuery.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (userSnapshot in dataSnapshot.children) {
                                    val userID = userSnapshot.key.toString()
                                    database.reference.child("users").child(userID).child("groups").child(groupName).setValue(true)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }


        binding.groupName.text = "$groupNameTop"

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.backbutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.accountbutton.setOnClickListener{
            startActivity(Intent(this, Compte_Activity::class.java))
        }

        binding.parambutton.setOnClickListener{
            startActivity(Intent(this, ParametersActivity::class.java))
        }
    }
}