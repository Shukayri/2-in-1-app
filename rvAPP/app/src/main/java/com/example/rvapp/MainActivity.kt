package com.example.rvapp
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rvapp.GuessingGameAct
import com.example.rvapp.NumbersGameAct
import com.example.rvapp.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var numbersGameButton: Button
    private lateinit var guessThePhraseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numbersGameButton = findViewById(R.id.btNumbersGame)
        numbersGameButton.setOnClickListener { startGame(NumbersGameAct()) }
        guessThePhraseButton = findViewById(R.id.btGuessThePhrase)
        guessThePhraseButton.setOnClickListener { startGame(GuessingGameAct()) }

        title = "2 in 1 App"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_numbers_game -> {
                startGame(NumbersGameAct())
                return true
            }
            R.id.mi_guess_the_phrase -> {
                startGame(GuessingGameAct())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGame(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }
}