package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.update
import com.example.unscramble.data.SCORE_INCREASE
class GameViewModel : ViewModel() {
    // --- Trạng thái Giao diện người dùng (UI State) ---
    // Biến riêng tư, có thể thay đổi, chỉ dùng bên trong ViewModel
    private val _uiState = MutableStateFlow(GameUiState())
    // Biến công khai, chỉ đọc, để giao diện có thể theo dõi
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // --- Logic của Trò chơi ---
    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        val initialScrambledWord = pickRandomWordAndShuffle()
        _uiState.value = GameUiState(currentScrambledWord = initialScrambledWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        return if (usedWords.contains(currentWord)) {
            pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    // Thêm biến để lưu nội dung người dùng gõ
    var userGuess by mutableStateOf("")
        private set // Chỉ cho phép ViewModel thay đổi giá trị này

    // Thêm hàm để cập nhật nội dung người dùng gõ
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            // Đoán đúng, cập nhật điểm và chuyển sang từ mới
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            // Người dùng đoán sai, cập nhật isGuessedWordWrong thành true
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false, // Reset trạng thái lỗi
                currentScrambledWord = pickRandomWordAndShuffle(), // Lấy từ mới
                score = updatedScore, // Cập nhật điểm
                currentWordCount = currentState.currentWordCount.inc() // Tăng số từ
            )
        }
    }

    fun skipWord() {
        // Qua vòng mới nhưng không cộng điểm
        updateGameState(_uiState.value.score)
        // Reset ô nhập liệu
        updateUserGuess("")
    }



}