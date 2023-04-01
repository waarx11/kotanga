package com.example.kotanga
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class GroupMenu_Activity : AppCompatActivity() {
    private lateinit var createGroupButton: Button
    private lateinit var groupLayout: LinearLayout
    val intents = mutableListOf<Intent>()

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_menu)

        createGroupButton = findViewById(R.id.create_group_button)
        groupLayout = findViewById(R.id.group_layout)

        createGroupButton.setOnClickListener {

            val builder = AlertDialog.Builder(this)

            val view = LayoutInflater.from(this).inflate(R.layout.popup_create_group, null)
            builder.setView(view)

            builder.setPositiveButton("Create") { dialog, which ->
                val groupNameEditText = view.findViewById<EditText>(R.id.group_name_textfield)
                val groupName = groupNameEditText.text.toString()

                val groupButton = Button(this)
                groupButton.text = groupName
                groupButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                groupButton.gravity = Gravity.CENTER_HORIZONTAL
                groupLayout.addView(groupButton)

                groupButton.setOnClickListener{
                    val intent = Intent(this, Group_Activity::class.java)
                    intent.putExtra("groupName", groupName)
                    intents.add(intent)
                    startActivity(intent)
                }

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