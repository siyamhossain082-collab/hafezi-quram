package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ParaInfo
import com.example.data.QuranData
import com.example.data.SurahInfo
import com.example.player.AudioPlayerState
import com.example.player.PageAudioPlayerManager
import com.example.player.ReciterOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookmarkItem(
    val pageNumber: Int,
    val surahName: String,
    val paraNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class ReaderUiState(
    val currentPage: Int = 1,
    val currentSurah: SurahInfo = QuranData.surahs.first(),
    val currentPara: ParaInfo = QuranData.paras.first(),
    val isBookmarked: Boolean = false,
    val isControlsVisible: Boolean = true
)

class HafeziViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("hafezi_quran_prefs", Context.MODE_PRIVATE)

    private val _lastReadPage = MutableStateFlow(1)
    val lastReadPage: StateFlow<Int> = _lastReadPage.asStateFlow()

    private val _lastReadSurah = MutableStateFlow("Al-Fatihah")
    val lastReadSurah: StateFlow<String> = _lastReadSurah.asStateFlow()

    private val _lastReadPara = MutableStateFlow(1)
    val lastReadPara: StateFlow<Int> = _lastReadPara.asStateFlow()

    private val _bookmarks = MutableStateFlow<Set<Int>>(emptySet())
    val bookmarks: StateFlow<Set<Int>> = _bookmarks.asStateFlow()

    private val _surahSearchQuery = MutableStateFlow("")
    val surahSearchQuery: StateFlow<String> = _surahSearchQuery.asStateFlow()

    private val _paraSearchQuery = MutableStateFlow("")
    val paraSearchQuery: StateFlow<String> = _paraSearchQuery.asStateFlow()

    private val _readerPage = MutableStateFlow(1)
    val readerPage: StateFlow<Int> = _readerPage.asStateFlow()

    private val _isControlsVisible = MutableStateFlow(true)
    val isControlsVisible: StateFlow<Boolean> = _isControlsVisible.asStateFlow()

    // Page Audio Player Manager
    val audioPlayerManager: PageAudioPlayerManager = PageAudioPlayerManager(
        context = application.applicationContext,
        onAutoAdvanceNextPage = { nextPage ->
            onPageChanged(nextPage)
        }
    )

    val audioPlayerState: StateFlow<AudioPlayerState> = audioPlayerManager.playerState

    val filteredSurahs: StateFlow<List<SurahInfo>> = combine(
        MutableStateFlow(QuranData.surahs),
        _surahSearchQuery
    ) { surahs, query ->
        if (query.isBlank()) {
            surahs
        } else {
            val q = query.trim().lowercase()
            surahs.filter {
                it.nameEnglish.lowercase().contains(q) ||
                it.nameTranslation.lowercase().contains(q) ||
                it.nameArabic.contains(q) ||
                it.number.toString() == q
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuranData.surahs)

    val filteredParas: StateFlow<List<ParaInfo>> = combine(
        MutableStateFlow(QuranData.paras),
        _paraSearchQuery
    ) { paras, query ->
        if (query.isBlank()) {
            paras
        } else {
            val q = query.trim().lowercase()
            paras.filter {
                it.nameRoman.lowercase().contains(q) ||
                it.nameArabic.contains(q) ||
                it.number.toString() == q
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuranData.paras)

    init {
        loadPersistedData()
    }

    private fun loadPersistedData() {
        val savedPage = prefs.getInt("key_last_read_page", 1)
        val surah = QuranData.getSurahForPage(savedPage)
        val para = QuranData.getParaForPage(savedPage)

        _lastReadPage.value = savedPage
        _lastReadSurah.value = surah.nameEnglish
        _lastReadPara.value = para.number
        _readerPage.value = savedPage

        val savedBookmarks = prefs.getStringSet("key_bookmarks", emptySet()) ?: emptySet()
        _bookmarks.value = savedBookmarks.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun onPageChanged(page: Int) {
        val clamped = page.coerceIn(1, 604)
        _readerPage.value = clamped
        val surah = QuranData.getSurahForPage(clamped)
        val para = QuranData.getParaForPage(clamped)

        _lastReadPage.value = clamped
        _lastReadSurah.value = surah.nameEnglish
        _lastReadPara.value = para.number

        prefs.edit()
            .putInt("key_last_read_page", clamped)
            .apply()
    }

    fun toggleControlsVisibility() {
        _isControlsVisible.value = !_isControlsVisible.value
    }

    fun toggleBookmark(page: Int) {
        val current = _bookmarks.value.toMutableSet()
        if (current.contains(page)) {
            current.remove(page)
        } else {
            current.add(page)
        }
        _bookmarks.value = current
        prefs.edit()
            .putStringSet("key_bookmarks", current.map { it.toString() }.toSet())
            .apply()
    }

    fun isBookmarked(page: Int): Boolean {
        return _bookmarks.value.contains(page)
    }

    fun setSurahSearchQuery(query: String) {
        _surahSearchQuery.value = query
    }

    fun setParaSearchQuery(query: String) {
        _paraSearchQuery.value = query
    }

    fun playPageAudio(page: Int) {
        audioPlayerManager.playPage(page)
    }

    fun toggleAudioPlayPause(page: Int) {
        audioPlayerManager.togglePlayPause(page)
    }

    fun setReciter(reciter: ReciterOption) {
        audioPlayerManager.setReciter(reciter)
    }

    fun setPlaybackSpeed(speed: Float) {
        audioPlayerManager.setPlaybackSpeed(speed)
    }

    fun seekAudio(positionMs: Long) {
        audioPlayerManager.seekTo(positionMs)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }
}
