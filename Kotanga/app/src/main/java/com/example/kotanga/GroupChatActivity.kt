package com.example.kotanga

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.kotanga.databinding.ActivityGroupChatBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GroupChatActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var imageView: ImageView

    private lateinit var addUserInGroup: ImageButton
    private lateinit var groupLayout: LinearLayout
    private lateinit var currentUser: DatabaseReference
    private lateinit var currentGroup: DatabaseReference
    private lateinit var groupName: String
    private lateinit var messageAdapter: ArrayAdapter<String>
    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var dbManager: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_chat)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupNameTop = intent.getStringExtra("groupName")
        binding.groupName.text = groupNameTop

        this.setBackgroundColor()

        val messageEditText: EditText = findViewById(R.id.messageEditText)

        val database = Firebase.database
        val userId = Firebase.auth.currentUser?.uid
        currentUser = database.getReference("users/$userId")
        groupName =
            intent.getStringExtra("groupName")
                .toString() // Récupération du nom du groupe depuis l'intent
        val groupeRef = database.reference.child("groupes").child(groupName)



        //Récupération des users du groupes et mis dans les spinners

        val userList = mutableListOf<String>()
        val usersgroup = database.getReference("groupes/$groupName/usersIds")

        val spinnerUser = findViewById<Spinner>(R.id.depense_pay_by_spinner)
        val spinnerUser2 = findViewById<Spinner>(R.id.depense_pay_to_spinner)

        usersgroup.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Parcourez les enfants de la liste des utilisateurs et ajoutez les noms d'utilisateurs à la liste mutable
                for (childSnapshot in dataSnapshot.children) {
                    val userId = childSnapshot.getValue(String::class.java)
                    if (userId != null) {
                        val userRef = database.getReference("users/$userId")
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val username = snapshot.child("name").getValue(String::class.java)
                                if (username != null) {
                                    userList.add(username)
                                }
                                // Créez un adaptateur de spinner en utilisant la liste mutable
                                val adapter = ArrayAdapter(this@GroupChatActivity , android.R.layout.simple_spinner_item, userList)
                                // Définissez le style de la liste déroulante
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                // Attachez l'adaptateur au spinner
                                spinnerUser.adapter = adapter
                                spinnerUser2.adapter = adapter
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Gérez les erreurs ici
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérez les erreurs ici
            }
        })



        //Affichage des dépenses

        val depenseList = findViewById<ListView>(R.id.depense_list)
        val usersListe = findViewById<ListView>(R.id.personneListe)
        val depenseRef = database.getReference("groupes/$groupName/depenses")

        depenseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val depenses = mutableListOf<Depense>()
                for (childSnapshot in dataSnapshot.children) {
                    val depense = childSnapshot.getValue(Depense::class.java)
                    if (depense != null) {
                        depenses.add(depense)
                    }
                }
                val adapter = DepensesAdapter(this@GroupChatActivity, depenses, userId!!)
                depenseList.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérez les erreurs ici
            }
        })


        val spending_bouton = findViewById<Button>(R.id.group_spending_button)
        val group_balance_button = findViewById<Button>(R.id.group_balance_button)
        val group_transaction_button = findViewById<Button>(R.id.group_transaction_button)
        val addDepense = findViewById<Button>(R.id.add_depense_button)
        val addDepense2 = findViewById<Button>(R.id.add_depense_button2)


        val lightgrey = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.light_grey))
        val primarycolor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_color))

        val spinner = findViewById<Spinner>(R.id.depense_type_spinner)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0);

        val spinnerPrice = findViewById<Spinner>(R.id.depense_price_spinner)
        val adapterPrice: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_price,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrice.adapter = adapterPrice
        spinnerPrice.setSelection(0);

        setAllInvisible()
        binding.messageList.visibility = Button.VISIBLE
        binding.linearLayoutSendMessage.visibility = LinearLayout.VISIBLE

        spending_bouton.setOnClickListener {
            spending_bouton.backgroundTintList = lightgrey
            group_transaction_button.backgroundTintList = primarycolor
            group_balance_button.backgroundTintList = primarycolor

            //affichage du contenu
            setAllInvisible()
            binding.depenses.visibility = LinearLayout.VISIBLE
            binding.chatbutton.visibility = Button.VISIBLE
        }

        group_balance_button.setOnClickListener {
            //changement de la couleur des boutons
            group_balance_button.backgroundTintList = lightgrey
            group_transaction_button.backgroundTintList = primarycolor
            spending_bouton.backgroundTintList = primarycolor

            //affichage du contenu
            setAllInvisible()
            binding.balanceList.visibility = Button.VISIBLE
        }

        group_transaction_button.setOnClickListener {
            group_transaction_button.backgroundTintList = lightgrey
            group_balance_button.backgroundTintList = primarycolor
            spending_bouton.backgroundTintList = primarycolor

            //affichage du contenu
            setAllInvisible()
            binding.transactionList.visibility = Button.VISIBLE
        }

        binding.groupChatButton.setOnClickListener {
            setAllInvisible()
            binding.messageList.visibility = Button.VISIBLE
            binding.linearLayoutSendMessage.visibility = LinearLayout.VISIBLE
        }

        addDepense.setOnClickListener {
            binding.depenses.visibility = LinearLayout.INVISIBLE
            binding.addDepenses.visibility = LinearLayout.VISIBLE
            binding.addDepenseButton2.visibility = Button.VISIBLE
        }

        addDepense2.setOnClickListener {
            binding.depenses.visibility = LinearLayout.VISIBLE
            binding.addDepenses.visibility = LinearLayout.INVISIBLE
            val depense = Depense()
            depense.name = binding.depenseNameEditText.text.toString()
            depense.type = binding.depenseTypeSpinner.selectedItem.toString()
            depense.date = binding.depenseDateEdittexte.text.toString()
                depense.price = binding.depensePriceEdittexte.text.toString().toFloat()
            depense.priceType = binding.depensePriceSpinner.selectedItem.toString()
            //depense.payBy = binding.depensePayBySpinner.selectedItem
            //depense.payedFor = binding.depensePayToSpinner.text.toString()
            addDepense(groupName, depense)
        }


        //ajout photo
        imageView = findViewById<ImageButton>(R.id.imageView)
        imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_IMAGE_CAPTURE)
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

        addUserInGroup = findViewById(R.id.middle_button) // Ajout de l'ID du bouton
        //groupLayout = findViewById(R.id.group_layout) // Ajout de l'ID de la vue parente

        addUserInGroup.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = LayoutInflater.from(this).inflate(R.layout.popup_add_user_to_group, null)
            builder.setView(view)

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
                        val adapter = ArrayAdapter<String>(
                            this@GroupChatActivity,
                            android.R.layout.simple_list_item_1,
                            userList
                        )
                        userListView.adapter = adapter

                        userListView.setOnItemClickListener { parent, view, position, id ->
                            val selectedUserName = userList[position]

                            // Add the selected user to the current group
                            database.reference.child("groupes").child(groupName).child("users")
                                .child(selectedUserName.replace(".", "")).setValue(true)

                            // Add the current group to the selected user's groups
                            val selectedUserQuery =
                                usersRef.orderByChild("name").equalTo(selectedUserName)
                            selectedUserQuery.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (userSnapshot in dataSnapshot.children) {
                                        val userID = userSnapshot.key.toString()
                                        database.reference.child("users").child(userID)
                                            .child("groups").child(groupName).setValue(true)
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

            val dialog = builder.create()
            dialog.show()
        }

            binding.homebutton.setOnClickListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }

            binding.backbutton.setOnClickListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }

            binding.accountbutton.setOnClickListener {
                startActivity(Intent(this, Compte_Activity::class.java))
            }

            binding.persGroupe.setOnClickListener {
                setAllInvisible()
                binding.personneGroupe.visibility = LinearLayout.VISIBLE
                binding.addDepenseButton2.visibility = Button.VISIBLE
            }

            messageAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
            binding.messageList.adapter = messageAdapter

        messageAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        binding.messageList.adapter = messageAdapter

        val messageRef = database.getReference("groupes/$groupName/messages")
        messageRef.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Vider l'adaptateur avant d'ajouter de nouveaux messages
                messageAdapter.clear()

                for (childSnapshot in dataSnapshot.children) {
                    val message = childSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        val messageText = "${message.author}: ${message.content}"
                        messageAdapter.add(messageText)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gérer l'erreur ici
            }
        })

        /*val utilisateurRef = database.getReference("groupes/$groupName/users")

        val myListUser = mutableListOf<String>()
        utilisateurRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val value = childSnapshot.getValue()
                    if (value is String) {
                        val userName = value.substringAfterLast(" ")
                        myListUser.add(userName)
                    }
                }
                val adapter = ArrayAdapter(this@GroupChatActivity, android.R.layout.simple_list_item_1, myListUser)
                usersListe.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(HomeActivity.TAG, "Failed to read value.", error.toException())
            }
        })*/


        binding.sendMessageButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString()
            val userId = Firebase.auth.currentUser?.uid
            val userRef = database.getReference("users/$userId/name")
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val name = dataSnapshot.getValue(String::class.java)
                    val firstName = name?.substringBefore(" ")
                    val lastName = name?.substringAfter(" ")
                    val nomVal = lastName?.substringBefore(" ")
                    val authorName = firstName + " " + nomVal
                    writeMessage(groupName, messageContent, authorName)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                        // Gérer l'erreur ici
                }
            })
            binding.messageEditText.text.clear()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            try {
                val imageBitmap = data.getParcelableExtra<Bitmap>("data")
                val file = File.createTempFile("image_", ".jpg", cacheDir)
                val outputStream = FileOutputStream(file)
                imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // Afficher la photo dans l'ImageView
                val bitmap = BitmapFactory.decodeFile(file.path)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setAllInvisible() {
        binding.messageList.visibility = ScrollView.INVISIBLE
        binding.linearLayoutSendMessage.visibility = LinearLayout.INVISIBLE
        binding.depenses.visibility = LinearLayout.INVISIBLE
        binding.balanceList.visibility = Button.INVISIBLE
        binding.transactionList.visibility = Button.INVISIBLE
    }
}

data class Message(
     val author: String = "",
     val content: String = "",
     val timestamp: Long = 0
)

fun writeMessage(groupName: String, messageContent: String, authorName: String) {
    val database = Firebase.database
    val groupRef = database.reference.child("groupes").child(groupName)
    val messageRef = groupRef.child("messages").push()

    val message = Message(authorName, messageContent, System.currentTimeMillis())
    messageRef.setValue(message)
}

fun addDepense(groupName: String, depense: Depense){
    val database = Firebase.database
    val groupeRef = database.reference.child("groupes").child(groupName)

    // Récupérer une nouvelle clé pour la dépense
    val depenseKey = groupeRef.child("depenses").push().key ?: return

    // Enregistrer la dépense dans Firebase
    groupeRef.child("depenses").child(depenseKey).setValue(depense)
}
