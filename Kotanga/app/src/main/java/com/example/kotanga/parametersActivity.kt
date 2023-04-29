package com.example.kotanga

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.kotanga.databinding.ActivityCompteBinding
import com.example.kotanga.databinding.ActivityParametersBinding

@SuppressLint("UseSwitchCompatOrMaterialCode")
class ParametersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParametersBinding
    private var isNightModeOn = false
    private lateinit var switchDarkMode: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParametersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.setBackgroundColor()

        switchDarkMode = binding.switchDarkMode

        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        isNightModeOn = sharedPreferences.getBoolean("isNightModeOn", false)

        switchDarkMode.isChecked = isNightModeOn

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isNightModeOn = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isNightModeOn = false
            }
            delegate.applyDayNight()
        }

        binding.homebutton.setOnClickListener{
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.accountbutton.setOnClickListener{
            startActivity(Intent(this, Compte_Activity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isNightModeOn", isNightModeOn)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        isNightModeOn = getSharedPreferences("myPrefs", Context.MODE_PRIVATE).getBoolean("isNightModeOn", false)
        switchDarkMode.isChecked = isNightModeOn
    }

    private fun setBackgroundColor() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        var isNightModeOn2 = sharedPreferences.getBoolean("isNightModeOn", false)
        if (isNightModeOn2) {
            binding.root.setBackgroundColor(resources.getColor(R.color.primary_color_darkMode))
        } else {
            binding.root.setBackgroundColor(resources.getColor(R.color.primary_color))
        }
    }
}
