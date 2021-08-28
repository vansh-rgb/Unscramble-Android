package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private lateinit var currentWord : String
    private var wordsList : MutableList<String> = mutableListOf()

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private var _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord : LiveData<String>
        get() = _currentScrambledWord

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while(tempWord.toString().equals(currentWord,false)) {
            tempWord.shuffle()
        }
        if(wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            wordsList.add(currentWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
        }
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        if(playerWord.equals(currentWord,false)) {
            increaseScore()
            return true
        }
        return false
    }

    fun reinitializeData() {
        _currentWordCount.value = 0
        _score.value = 0
        wordsList.clear()
        getNextWord()
    }
}