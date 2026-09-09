package com.example.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ReciterOption(
    val id: String,
    val displayName: String,
    val subName: String,
    val urlPattern: String
)

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val activePagePlaying: Int = 1,
    val selectedReciter: ReciterOption = ReciterOption(
        id = "alafasy",
        displayName = "Mishary Rashid Alafasy",
        subName = "EveryAyah 128kbps",
        urlPattern = "https://everyayah.com/data/Alafasy_128kbps/PageMp3s/Page%03d.mp3"
    ),
    val playbackSpeed: Float = 1.0f,
    val errorMessage: String? = null
)

class PageAudioPlayerManager(
    private val context: Context,
    private val onAutoAdvanceNextPage: (nextPage: Int) -> Unit
) {
    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressTrackerJob: Job? = null

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    companion object {
        val availableReciters = listOf(
            ReciterOption(
                id = "alafasy",
                displayName = "Mishary Rashid Alafasy",
                subName = "Murattal 128kbps",
                urlPattern = "https://everyayah.com/data/Alafasy_128kbps/PageMp3s/Page%03d.mp3"
            ),
            ReciterOption(
                id = "abdulbasit",
                displayName = "Abdul Basit",
                subName = "Murattal 192kbps",
                urlPattern = "https://everyayah.com/data/Abdul_Basit_Murattal_192kbps/PageMp3s/Page%03d.mp3"
            ),
            ReciterOption(
                id = "husary",
                displayName = "Mahmoud Khalil Al-Husary",
                subName = "Murattal 128kbps",
                urlPattern = "https://everyayah.com/data/Husary_128kbps/PageMp3s/Page%03d.mp3"
            )
        )
    }

    init {
        initPlayer()
    }

    private fun initPlayer() {
        if (exoPlayer != null) return
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playerState.value = _playerState.value.copy(isPlaying = isPlaying)
                    if (isPlaying) {
                        startProgressTracker()
                    } else {
                        stopProgressTracker()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            _playerState.value = _playerState.value.copy(isLoading = true)
                        }
                        Player.STATE_READY -> {
                            val dur = exoPlayer?.duration?.coerceAtLeast(0L) ?: 0L
                            _playerState.value = _playerState.value.copy(
                                isLoading = false,
                                durationMs = dur,
                                errorMessage = null
                            )
                        }
                        Player.STATE_ENDED -> {
                            _playerState.value = _playerState.value.copy(
                                isPlaying = false,
                                isLoading = false,
                                currentPositionMs = 0L
                            )
                            stopProgressTracker()
                            val currentPage = _playerState.value.activePagePlaying
                            if (currentPage < 604) {
                                val nextPage = currentPage + 1
                                onAutoAdvanceNextPage(nextPage)
                                playPage(nextPage)
                            }
                        }
                        Player.STATE_IDLE -> {
                            _playerState.value = _playerState.value.copy(isLoading = false)
                        }
                    }
                }
            })
        }
    }

    fun playPage(pageNumber: Int) {
        val clampedPage = pageNumber.coerceIn(1, 604)
        initPlayer()
        val reciter = _playerState.value.selectedReciter
        val audioUrl = String.format(reciter.urlPattern, clampedPage)

        _playerState.value = _playerState.value.copy(
            activePagePlaying = clampedPage,
            isLoading = true,
            currentPositionMs = 0L,
            errorMessage = null
        )

        exoPlayer?.let { player ->
            player.stop()
            val mediaItem = MediaItem.fromUri(audioUrl)
            player.setMediaItem(mediaItem)
            player.playbackParameters = PlaybackParameters(_playerState.value.playbackSpeed)
            player.prepare()
            player.play()
        }
    }

    fun togglePlayPause(currentPage: Int) {
        val player = exoPlayer ?: return
        if (player.isPlaying) {
            player.pause()
        } else {
            // If already loaded for this page, resume; otherwise play page
            if (_playerState.value.activePagePlaying == currentPage && player.playbackState != Player.STATE_IDLE) {
                player.play()
            } else {
                playPage(currentPage)
            }
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playerState.value = _playerState.value.copy(currentPositionMs = positionMs)
    }

    fun setReciter(reciter: ReciterOption) {
        _playerState.value = _playerState.value.copy(selectedReciter = reciter)
        if (_playerState.value.isPlaying) {
            playPage(_playerState.value.activePagePlaying)
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
        exoPlayer?.playbackParameters = PlaybackParameters(speed)
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressTrackerJob = scope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    _playerState.value = _playerState.value.copy(
                        currentPositionMs = pos,
                        durationMs = dur
                    )
                }
                delay(400)
            }
        }
    }

    private fun stopProgressTracker() {
        progressTrackerJob?.cancel()
        progressTrackerJob = null
    }

    fun release() {
        stopProgressTracker()
        exoPlayer?.release()
        exoPlayer = null
    }
}
