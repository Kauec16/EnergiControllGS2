package com.example.energycontrol

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.energycontrol.databaseHandler.DatabaseHandler
import com.example.energycontrol.model.Group

class CadastrosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastros)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }




    fun backScreen(view: View) {
        val intent = Intent(this, GroupsList::class.java)
        startActivity(intent)
    }

    fun save(view: View) {
        val nameEditText = findViewById<EditText>(R.id.GroupName)
        val frequencyEditText = findViewById<Spinner>(R.id.FrequencyInput)
        if(nameEditText.text.isNotBlank()){
            val dbHandler = DatabaseHandler(this)
            val g = Group(nameEditText.text.toString(), frequencyEditText.selectedItem.toString())
            dbHandler.addGroup(g)


            val intent = Intent(this, GroupsList::class.java)
            startActivity(intent)
        }else{
            val error = findViewById<TextView>(R.id.errormessage)
            error.visibility = View.VISIBLE
        }

    }
}