package com.example.rvapp
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.guessphrase.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class GuessingGameAct : AppCompatActivity() {
    private lateinit var rvMain: ConstraintLayout
    private lateinit var EnterField: EditText
    private lateinit var EnterButton: Button
    private lateinit var messages: ArrayList<String>
    private lateinit var phrase: TextView
    private lateinit var letters: TextView

    private var secret = "this is the secret phrase"
    private var myAnswerDictionary = mutableMapOf<Int, Char>()
    private var myAnswer = ""
    private var guessedLetters = ""
    private var count = 0
    private var guessPhrase = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guessphrase)

        for(i in secret.indices){
            if(secret[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '*'
                myAnswer += '*'
            }
        }

        rvMain = findViewById(R.id.rvMainPhrase)
        messages = ArrayList()

        rvMessages.adapter = MessageAdapter(this, messages)
        rvMessages.layoutManager = LinearLayoutManager(this)

        EnterField = findViewById(R.id.etGuessField)
        EnterButton = findViewById(R.id.btGuessButton)
        EnterButton.setOnClickListener { addMessage() }

        phrase = findViewById(R.id.tvPhrase)
        letters = findViewById(R.id.tvLetters)

        updateText()

        title = "Guess the Phrase"
    }

    override fun recreate() {
        super.recreate()
        secret = "this is the secret phrase"
        myAnswerDictionary.clear()
        myAnswer = ""

        for(i in secret.indices){
            if(secret[i] == ' '){
                myAnswerDictionary[i] = ' '
                myAnswer += ' '
            }else{
                myAnswerDictionary[i] = '*'
                myAnswer += '*'
            }
        }

        guessedLetters = ""
        count = 0
        guessPhrase = true
        messages.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("answer", secret)

        val keys = myAnswerDictionary.keys.toIntArray()
        val values = myAnswerDictionary.values.toCharArray()
        outState.putIntArray("keys", keys)
        outState.putCharArray("values", values)

        outState.putString("myAnswer", myAnswer)
        outState.putString("guessedLetters", guessedLetters)
        outState.putInt("count", count)
        outState.putBoolean("phrase", guessPhrase)
        outState.putStringArrayList("messages", messages)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        secret = savedInstanceState.getString("answer", "nothing here")

        val keys = savedInstanceState.getIntArray("keys")
        val values = savedInstanceState.getCharArray("values")
        if(keys != null && values != null){
            if(keys.size == values.size){
                myAnswerDictionary = mutableMapOf<Int, Char>().apply {
                    for (i in keys.indices) this [keys[i]] = values[i]
                }
            }
        }

        myAnswer = savedInstanceState.getString("myAnswer", "")
        guessedLetters = savedInstanceState.getString("guessedLetters", "")
        count = savedInstanceState.getInt("count", 0)
        guessPhrase = savedInstanceState.getBoolean("guessPhrase", true)
        messages.addAll(savedInstanceState.getStringArrayList("messages")!!)
        updateText()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.getItem(1)
        if(item.title == "Other Game"){ item.title = "Numbers Game" }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_new_game -> {
                com.example.rvapp.AlertDialog(this,"Are you sure you want to abandon the current game?")
                return true
            }
            R.id.mi_other_game -> {
                changeScreen(NumbersGameAct())
                return true
            }
            R.id.mi_back -> {
                changeScreen(MainActivity())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeScreen(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    private fun addMessage(){
        val msg = EnterField.text.toString()

        if(guessPhrase){
            if(msg == secret){
                disableEntry()
                com.example.rvapp.AlertDialog(this,"You win!\n\nPlay again?")
            }else{
                messages.add("Wrong guess: $msg")
                guessPhrase = false
                updateText()
            }
        }else{
            if(msg.isNotEmpty() && msg.length==1){
                myAnswer = ""
                guessPhrase = true
                checkLetters(msg[0])
            }else{
                Snackbar.make(rvMain, "Please enter one letter only", Snackbar.LENGTH_SHORT).show()
            }
        }

        EnterField.text.clear()
        EnterField.clearFocus()
        rvMessages.adapter?.notifyDataSetChanged()
    }

    private fun disableEntry(){
        EnterButton.isEnabled = false
        EnterButton.isClickable = false
        EnterField.isEnabled = false
        EnterField.isClickable = false
    }

    @SuppressLint("SetTextI18n")
    private fun updateText(){
        phrase.text = "Phrase:  " + myAnswer.uppercase(Locale.getDefault())
        letters.text = "Guessed Letters:  $guessedLetters"
        if(guessPhrase){
            EnterField.hint = "Guess the full phrase"
        }else{
            EnterField.hint = "Guess a letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var found = 0
        for(i in secret.indices){
            if(secret[i] == guessedLetter){
                myAnswerDictionary[i] = guessedLetter
                found++
            }
        }
        for(i in myAnswerDictionary){myAnswer += myAnswerDictionary[i.key]}
        if(myAnswer==secret){
            disableEntry()
            com.example.rvapp.AlertDialog(this,"You win!\n\nPlay again?")
        }
        if(guessedLetters.isEmpty()){guessedLetters+=guessedLetter}else{guessedLetters+= ", $guessedLetter"
        }
        if(found>0){
            messages.add("Found $found ${guessedLetter.toUpperCase()}(s)")
        }else{
            messages.add("No ${guessedLetter.toUpperCase()}s found")
        }
        count++
        val guessesLeft = 10 - count
        if(count<10){messages.add("$guessesLeft guesses remaining")}
        updateText()
        rvMessages.scrollToPosition(messages.size - 1)
    }
}