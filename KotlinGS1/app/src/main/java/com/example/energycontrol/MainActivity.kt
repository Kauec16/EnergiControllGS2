package com.example.energycontrol

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainText = findViewById(R.id.mainText)

        val buttonCadastros: Button = findViewById<Button>(R.id.Cadastros)
        buttonCadastros.setOnClickListener {
            val intent = Intent(this, GroupsList::class.java)
            startActivity(intent)
        }

        val buttonReleases: Button = findViewById<Button>(R.id.releases)
        buttonReleases.setOnClickListener {
            val intent = Intent(this, ListReleases::class.java)
            startActivity(intent)
        }


        val buttonGraficos: Button = findViewById<Button>(R.id.Graficos)
        buttonGraficos.setOnClickListener {
            val intent = Intent(this, Chart::class.java)
            startActivity(intent)
        }


        val buttonRanking: Button = findViewById<Button>(R.id.Ranking)
        buttonRanking.setOnClickListener {
            val intent = Intent(this, Ranking::class.java)
            startActivity(intent)
        }



    }





}