package com.example.wordle

object FourLetterWordList {
    private val words = listOf(
        "STAR","GAME","CODE","TASK","FAST","SLOW","NOTE","WAVE",
        "CATS","DOGS","MATH","TREE","FIRE","WIND","MOON","KING",
        "LION","BEAR","FISH","BIRD","JUMP","WALK","READ","PLAY"
    )

    fun getRandomFourLetterWord(): String = words.random()
}
