package com.example.kotanga

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class DepensesAdapter(context: Context, depenses: MutableList<Depense>, private val currentUser: String) :
    ArrayAdapter<Depense>(context, R.layout.activity_depenses, depenses) {

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_depenses, parent, false)

        val background = view.findViewById<LinearLayout>(R.id.background)
        val depenseName = view.findViewById<TextView>(R.id.depense_name)
        val price = view.findViewById<TextView>(R.id.price)
        val payby = view.findViewById<TextView>(R.id.payby)
        val payfor = view.findViewById<TextView>(R.id.payto)

        val lightRed = ContextCompat.getColorStateList(parent.context, R.color.light_red)
        val lightGreend = ContextCompat.getColorStateList(parent.context, R.color.light_red)

        val depense = getItem(position)

        if (depense?.payBy?.id != currentUser) {
            background.backgroundTintList = lightRed
            price.setTextColor((ContextCompat.getColor(context, R.color.red)))
        }
        else{
            background.backgroundTintList = lightGreend
            price.setTextColor((ContextCompat.getColor(context, R.color.green)))
        }
        depenseName.text = depense?.name
        price.text = "(${depense?.price})"
        payby.text = depense?.payBy?.name
        payfor.text = depense?.payFor?.name

        return view
    }
}
