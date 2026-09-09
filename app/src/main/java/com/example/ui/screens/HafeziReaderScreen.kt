package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.data.QuranData
import com.example.player.PageAudioPlayerManager
import com.example.player.ReciterOption
import com.example.ui.theme.QuranCardBorder
import com.example.ui.theme.QuranDarkGreen
import com.example.ui.theme.QuranGoldLight
import com.example.ui.theme.QuranGoldPrimary
import com.example.ui.theme.QuranGoldSecondary
import com.example.ui.theme.QuranSurface
import com.example.ui.theme.QuranSurfaceVariant
import com.example.ui.theme.QuranTeal
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.CoilNetworkConfig
import com.example.util.HafeziPageDownloader
import com.example.util.rememberCoilImageLoader
import com.example.viewmodel.HafeziViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HafeziReaderScreen(
    initialPage: Int,
    viewModel: HafeziViewModel,
    onNavigateBack: () -> Unit
) {
    val clampedInitial = (initialPage - 1).coerceIn(0, 603)
    val pagerState = rememberPagerState(initialPage = clampedInitial, pageCount = { 604 })
    val coroutineScope = rememberCoroutineScope()

    val currentPage = pagerState.currentPage + 1
    val currentSurah = remember(currentPage) { QuranData.getSurahForPage(currentPage) }
    val currentPara = remember(currentPage) { QuranData.getParaForPage(currentPage) }

    val bookmarks by viewModel.bookmarks.collectAsState()
    val isBookmarked = bookmarks.contains(currentPage)

    val isControlsVisible by viewModel.isControlsVisible.collectAsState()
    val audioState by viewModel.audioPlayerState.collectAsState()

    var showReciterSheet by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }

    // Sync pager changes to ViewModel
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged(pagerState.currentPage + 1)
    }

    // Auto-advance listener: When audio auto-advances, scroll pager
    LaunchedEffect(audioState.activePagePlaying) {
        if (audioState.isPlaying && audioState.activePagePlaying != currentPage) {
            pagerState.animateScrollToPage(audioState.activePagePlaying - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuranDarkGreen)
            .testTag("hafezi_reader_screen")
    ) {
        // Full Page HorizontalPager for 604 Hafezi Quran Pages
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { it }
        ) { pageIndex ->
            val pageNum = pageIndex + 1
            QuranPageViewer(
                pageNumber = pageNum,
                onSingleTap = { viewModel.toggleControlsVisibility() }
            )
        }

        // Top Bar (Animated Overlay)
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = QuranSurface.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = QuranGoldPrimary
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentSurah.nameEnglish,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentSurah.nameArabic,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = QuranGoldPrimary
                                )
                            )
                        }
                        Text(
                            text = "Juz ${currentPara.number} (${currentPara.nameRoman}) • Page $currentPage / 604",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = QuranGoldLight,
                                fontSize = 12.sp
                            )
                        )
                    }

                    // Reciter selector button
                    IconButton(
                        onClick = { showReciterSheet = true },
                        modifier = Modifier.testTag("reciter_select_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Select Reciter",
                            tint = QuranGoldPrimary
                        )
                    }

                    // Bookmark toggle button
                    IconButton(
                        onClick = { viewModel.toggleBookmark(currentPage) },
                        modifier = Modifier.testTag("bookmark_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) QuranGoldPrimary else TextSecondary
                        )
                    }
                }
            }
        }

        // Sticky Bottom Audio Player Bar
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                color = QuranSurface.copy(alpha = 0.96f),
                shadowElevation = 12.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, QuranCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    // Audio Progress Bar
                    val currentPos = audioState.currentPositionMs.toFloat()
                    val duration = audioState.durationMs.coerceAtLeast(1L).toFloat()
                    val sliderValue = (currentPos / duration).coerceIn(0f, 1f)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = if (audioState.isPlaying) QuranTeal else TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Page $currentPage • ${audioState.selectedReciter.displayName}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = QuranGoldLight,
                                    fontWeight = FontWeight.Medium
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Speed button
                        TextButton(
                            onClick = { showSpeedDialog = true },
                            modifier = Modifier.height(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = QuranGoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${audioState.playbackSpeed}x",
                                style = MaterialTheme.typography.labelSmall.copy(color = QuranGoldPrimary)
                            )
                        }
                    }

                    // Progress Slider
                    Slider(
                        value = sliderValue,
                        onValueChange = { frac ->
                            val targetMs = (frac * duration).toLong()
                            viewModel.seekAudio(targetMs)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(22.dp)
                            .testTag("audio_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = QuranGoldPrimary,
                            activeTrackColor = QuranGoldPrimary,
                            inactiveTrackColor = QuranSurfaceVariant
                        )
                    )

                    // Timestamps & Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTime(audioState.currentPositionMs),
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )

                        // Playback Control Buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Previous Page Button
                            IconButton(
                                onClick = {
                                    if (currentPage > 1) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(currentPage - 2)
                                        }
                                    }
                                },
                                enabled = currentPage > 1,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous Page",
                                    tint = if (currentPage > 1) QuranGoldPrimary else TextMuted
                                )
                            }

                            // Replay 10s
                            IconButton(
                                onClick = {
                                    val newPos = (audioState.currentPositionMs - 10_000L).coerceAtLeast(0L)
                                    viewModel.seekAudio(newPos)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay10,
                                    contentDescription = "Replay 10s",
                                    tint = TextSecondary
                                )
                            }

                            // Big Play / Pause Button
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(QuranGoldPrimary)
                                    .clickable {
                                        viewModel.toggleAudioPlayPause(currentPage)
                                    }
                                    .testTag("play_pause_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (audioState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = QuranDarkGreen,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (audioState.isPlaying) "Pause" else "Play",
                                        tint = QuranDarkGreen,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            // Forward 10s
                            IconButton(
                                onClick = {
                                    val newPos = (audioState.currentPositionMs + 10_000L).coerceAtMost(audioState.durationMs)
                                    viewModel.seekAudio(newPos)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Forward10,
                                    contentDescription = "Forward 10s",
                                    tint = TextSecondary
                                )
                            }

                            // Next Page Button
                            IconButton(
                                onClick = {
                                    if (currentPage < 604) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(currentPage)
                                        }
                                    }
                                },
                                enabled = currentPage < 604,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next Page",
                                    tint = if (currentPage < 604) QuranGoldPrimary else TextMuted
                                )
                            }
                        }

                        Text(
                            text = formatTime(audioState.durationMs),
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }
            }
        }

        // Reciter Selection Sheet
        if (showReciterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showReciterSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = QuranSurface,
                dragHandle = null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Select Reciter (Qari)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Text(
                        text = "Page-by-page recitation from EveryAyah CDN",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    PageAudioPlayerManager.availableReciters.forEach { reciter ->
                        val isSelected = audioState.selectedReciter.id == reciter.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) QuranSurfaceVariant else Color.Transparent)
                                .clickable {
                                    viewModel.setReciter(reciter)
                                    showReciterSheet = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setReciter(reciter)
                                    showReciterSheet = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = QuranGoldPrimary,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = reciter.displayName,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = reciter.subName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Speed Dialog
        if (showSpeedDialog) {
            ModalBottomSheet(
                onDismissRequest = { showSpeedDialog = false },
                containerColor = QuranSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Audio Playback Speed",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 1.75f)
                    speeds.forEach { speed ->
                        val isSelected = audioState.playbackSpeed == speed
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) QuranSurfaceVariant else Color.Transparent)
                                .clickable {
                                    viewModel.setPlaybackSpeed(speed)
                                    showSpeedDialog = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    viewModel.setPlaybackSpeed(speed)
                                    showSpeedDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = QuranGoldPrimary,
                                    unselectedColor = TextMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${speed}x Normal Speed",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isSelected) QuranGoldPrimary else TextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun QuranPageViewer(
    pageNumber: Int,
    onSingleTap: () -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val context = LocalContext.current
    val imageLoader = rememberCoilImageLoader()
    var localFile by remember(pageNumber) { mutableStateOf<File?>(null) }
    var isLoading by remember(pageNumber) { mutableStateOf(true) }
    var isError by remember(pageNumber) { mutableStateOf(false) }
    var retryTrigger by remember(pageNumber) { mutableIntStateOf(0) }

    // Step 1: Check existing local file or download via OkHttpClient
    LaunchedEffect(pageNumber, retryTrigger) {
        val existingFile = HafeziPageDownloader.getPageFile(context, pageNumber)
        if (existingFile.exists() && existingFile.length() > 1000) {
            localFile = existingFile
            isLoading = false
            isError = false
        } else {
            isLoading = true
            isError = false
            val result = HafeziPageDownloader.downloadPage(context, pageNumber)
            if (result.isSuccess) {
                localFile = result.getOrNull()
                isLoading = false
                isError = false
            } else {
                localFile = null
                isLoading = false
                isError = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFBF0)) // Authentic Mushaf warm parchment tone
            .pointerInput(pageNumber) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        if (scale > 1.2f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2.2f
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            offset = Offset(
                                (centerX - tapOffset.x) * 1.2f,
                                (centerY - tapOffset.y) * 1.2f
                            )
                        }
                    },
                    onTap = {
                        onSingleTap()
                    }
                )
            }
            .pointerInput(pageNumber) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(1f, 3.5f)
                    scale = newScale
                    if (newScale > 1f) {
                        val maxOffsetX = (size.width * (newScale - 1f)) / 2f
                        val maxOffsetY = (size.height * (newScale - 1f)) / 2f
                        offset = Offset(
                            x = (offset.x + pan.x).coerceIn(-maxOffsetX, maxOffsetX),
                            y = (offset.y + pan.y).coerceIn(-maxOffsetY, maxOffsetY)
                        )
                    } else {
                        offset = Offset.Zero
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                // Loading State: CircularProgressIndicator while downloading
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = QuranGoldSecondary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Loading Page $pageNumber...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF6B5E43),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            isError || localFile == null -> {
                // Error State: Clean Retry button that purges broken file and retries
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = Color(0xFF8B2525),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Failed to load Page $pageNumber",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color(0xFF7A2020),
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Please check your internet connection. Tap Retry to reload the page.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF5A4A35),
                                textAlign = TextAlign.Center
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                HafeziPageDownloader.deletePageFile(context, pageNumber)
                                retryTrigger++
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = QuranGoldPrimary,
                                contentColor = QuranDarkGreen
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("retry_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Retry",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            else -> {
                // Step 2: Render local file or direct CDN with TrustAllCerts OkHttpClient ImageLoader
                val imageSource: Any = localFile ?: "https://everyayah.com/data/quranpng/${pageNumber}.png"
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageSource)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Quran Page $pageNumber",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offset.x,
                            translationY = offset.y
                        )
                )
            }
        }
    }
}

@Composable
fun ZoomableQuranPage(
    pageNumber: Int,
    onSingleTap: () -> Unit
) {
    QuranPageViewer(pageNumber = pageNumber, onSingleTap = onSingleTap)
}

private fun formatTime(millis: Long): String {
    val totalSeconds = (millis / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%02d:%02d", minutes, seconds)
}
