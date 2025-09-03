package com.example.wordle

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var guessCount = 0
    private lateinit var targetWord: String

    private lateinit var etGuess: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnReset: Button

    private lateinit var tvAnswer: TextView
    private lateinit var tvGuess1: TextView
    private lateinit var tvGuess2: TextView
    private lateinit var tvGuess3: TextView
    private lateinit var tvCheck1: TextView
    private lateinit var tvCheck2: TextView
    private lateinit var tvCheck3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        startNewGame()

        btnSubmit.setOnClickListener {
            val raw = etGuess.text.toString().trim()
            val guess = raw.uppercase(Locale.ROOT)

            if (guess.length != 4) {
                Toast.makeText(this, "Enter exactly 4 letters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            applyGuess(guess)
            etGuess.setText("")
            hideKeyboard()
        }

        btnReset.setOnClickListener { startNewGame() }
    }

    private fun bindViews() {
        etGuess = findViewById(R.id.etGuess)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnReset = findViewById(R.id.btnReset)

        tvAnswer = findViewById(R.id.tvAnswer)
        tvGuess1 = findViewById(R.id.tvGuess1)
        tvGuess2 = findViewById(R.id.tvGuess2)
        tvGuess3 = findViewById(R.id.tvGuess3)
        tvCheck1 = findViewById(R.id.tvCheck1)
        tvCheck2 = findViewById(R.id.tvCheck2)
        tvCheck3 = findViewById(R.id.tvCheck3)
    }

    private fun startNewGame() {
        guessCount = 0
        targetWord = FourLetterWordList.getRandomFourLetterWord()

        // Clear UI
        tvGuess1.text = ""; tvGuess2.text = ""; tvGuess3.text = ""
        tvCheck1.text = ""; tvCheck2.text = ""; tvCheck3.text = ""

        tvAnswer.text = "ANSWER: $targetWord"
        tvAnswer.visibility = View.GONE

        btnSubmit.isEnabled = true
        btnSubmit.text = "Submit"
        btnReset.visibility = View.GONE
        etGuess.isEnabled = true
    }

    private fun applyGuess(guess: String) {
        if (guessCount >= 3) return

        guessCount += 1
        val check = checkGuess(guess, targetWord)

        when (guessCount) {
            1 -> { tvGuess1.text = guess; tvCheck1.text = check }
            2 -> { tvGuess2.text = guess; tvCheck2.text = check }
            3 -> { tvGuess3.text = guess; tvCheck3.text = check }
        }

        // Win or out of guesses → end game
        val isWin = guess == targetWord
        if (isWin || guessCount == 3) {
            endGame(isWin)
        }
    }

    private fun endGame(won: Boolean) {
        btnSubmit.isEnabled = false
        etGuess.isEnabled = false
        btnReset.visibility = View.VISIBLE
        tvAnswer.visibility = View.VISIBLE

        if (won) {
            Toast.makeText(this, "You got it!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Out of guesses!", Toast.LENGTH_SHORT).show()
        }
    }

    /** XO+X scoring: O = right letter & place, + = in word wrong place, X = not in word */
    private fun checkGuess(guess: String, target: String): String {
        val g = guess.uppercase(Locale.ROOT)
        val t = target.uppercase(Locale.ROOT)
        val sb = StringBuilder()

        for (i in 0..3) {
            val c = g[i]
            sb.append(
                when {
                    c == t[i] -> 'O'
                    t.contains(c) -> '+'
                    else -> 'X'
                }
            )
        }
        return sb.toString()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { v -> imm.hideSoftInputFromWindow(v.windowToken, 0) }
    }
}
