package com.example.energycontrol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.energycontrol.databaseHandler.DatabaseHandler
import com.example.energycontrol.model.EnergySpentEntry
import com.example.energycontrol.model.Group
import java.util.regex.Pattern

class AddEnergyEntry : AppCompatActivity() {

    val dbHandler = DatabaseHandler(this)
    var groupList: List<Group> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_energy_entry)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //prepara o spinner de grupos
        prepareSpinner()

    }

    fun prepareSpinner() {
        groupList = dbHandler.listGroups()
        val nomes = groupList.map { it.name }

        val mySpinner: Spinner = findViewById(R.id.GroupInput)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner.adapter = adapter
    }


    fun saveEntry(view: View) {
        // Recupera os campos da interface
        val group = findViewById<Spinner>(R.id.GroupInput)
        val energyDate = findViewById<EditText>(R.id.DateTime)
        val value = findViewById<EditText>(R.id.valueField)

        // Log para verificar os valores
        Log.i("group", group.selectedItem.toString())
        Log.i("EnergyDate", energyDate.text.toString())
        Log.i("Value", value.text.toString())

        if(energyDate.text.isNotBlank() && value.text.isNotBlank() && energyDate.text.toString().isNotBlank() &&
            Pattern.matches("^([0-2][0-9]|(3)[0-1])/(0[1-9]|1[0-2])/([12][0-9]{3})$", energyDate.text.toString())){

            val dbHandler = DatabaseHandler(this)

            var selectedGroup = groupList.find { it.name == group.selectedItem.toString() }
            val idGroup = selectedGroup!!.id
                val e = EnergySpentEntry(idGroup!!, energyDate.text.toString(), value.text.toString().toDouble())

            dbHandler.addEnergyCostEntry(e)

            val intent = Intent(this, ListReleases::class.java)
            startActivity(intent)
        }else{
            val error = findViewById<TextView>(R.id.errormessage)
            error.visibility = View.VISIBLE
        }

    }


}