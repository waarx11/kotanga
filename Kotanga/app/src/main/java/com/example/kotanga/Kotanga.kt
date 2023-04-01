package com.example.kotanga

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Kotanga : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        /*val database = FirebaseDatabase.getInstance().reference
        database.child("message").setValue("Bonjour, Firebase !")*/

        /*database.child("message").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val message = dataSnapshot.getValue(String::class.java)
                Log.d("Firebase", "Message : $message")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "Erreur : " + databaseError.message)
            }
        })*/

    }
}
