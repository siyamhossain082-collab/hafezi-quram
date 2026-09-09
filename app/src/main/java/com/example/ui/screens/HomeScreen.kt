package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.QuranData
import com.example.ui.theme.PremiumBadgeEnd
import com.example.ui.theme.PremiumBadgeStart
import com.example.ui.theme.QuranAccentCyan
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
import com.example.viewmodel.HafeziViewModel

data class DashboardActionCard(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val testTag: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HafeziViewModel,
    onNavigateToSurahIndex: () -> Unit,
    onNavigateToParaIndex: () -> Unit,
    onNavigateToReader: (pageNumber: Int) -> Unit
) {
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }
    var showOverflowMenu by remember { mutableStateOf(false) }
    var showPremiumDialog by remember { mutableStateOf(false) }
    var show99NamesSheet by remember { mutableStateOf(false) }
    var showTajweedSheet by remember { mutableStateOf(false) }
    var showQuranInfoSheet by remember { mutableStateOf(false) }
    var showQuickReadSheet by remember { mutableStateOf(false) }
    var showSavedBookmarksSheet by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val lastReadPage by viewModel.lastReadPage.collectAsState()
    val lastReadSurah by viewModel.lastReadSurah.collectAsState()
    val lastReadPara by viewModel.lastReadPara.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val audioState by viewModel.audioPlayerState.collectAsState()

    val actionCards = listOf(
        DashboardActionCard(
            title = "Para Index",
            subtitle = "30 Sections",
            icon = Icons.Default.AutoStories,
            iconTint = QuranGoldPrimary,
            testTag = "card_para_index",
            onClick = onNavigateToParaIndex
        ),
        DashboardActionCard(
            title = "Surah Index",
            subtitle = "114 Chapters",
            icon = Icons.Default.MenuBook,
            iconTint = QuranTeal,
            testTag = "card_surah_index",
            onClick = onNavigateToSurahIndex
        ),
        DashboardActionCard(
            title = "99 Names",
            subtitle = "Asma-ul-Husna",
            icon = Icons.Default.Star,
            iconTint = QuranGoldLight,
            testTag = "card_99_names",
            onClick = { show99NamesSheet = true }
        ),
        DashboardActionCard(
            title = "Tajweed Guide",
            subtitle = "Pronunciation Rules",
            icon = Icons.Default.RecordVoiceOver,
            iconTint = QuranAccentCyan,
            testTag = "card_tajweed_guide",
            onClick = { showTajweedSheet = true }
        ),
        DashboardActionCard(
            title = "Quran Info",
            subtitle = "Facts & Statistics",
            icon = Icons.Default.Info,
            iconTint = Color(0xFF60A5FA),
            testTag = "card_quran_info",
            onClick = { showQuranInfoSheet = true }
        ),
        DashboardActionCard(
            title = "Quick Read",
            subtitle = "Yasin, Kahf, Mulk",
            icon = Icons.Default.Diamond,
            iconTint = Color(0xFFF472B6),
            testTag = "card_quick_read",
            onClick = { showQuickReadSheet = true }
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        containerColor = QuranDarkGreen,
        bottomBar = {
            NavigationBar(
                containerColor = QuranSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 0,
                    onClick = { selectedBottomNavIndex = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = QuranDarkGreen,
                        selectedTextColor = QuranGoldPrimary,
                        indicatorColor = QuranGoldPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_home")
                )
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 1,
                    onClick = {
                        selectedBottomNavIndex = 1
                        onNavigateToSurahIndex()
                    },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "Quran") },
                    label = { Text("Quran") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = QuranDarkGreen,
                        selectedTextColor = QuranGoldPrimary,
                        indicatorColor = QuranGoldPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_quran")
                )
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 2,
                    onClick = {
                        selectedBottomNavIndex = 2
                        onNavigateToReader(lastReadPage)
                        viewModel.playPageAudio(lastReadPage)
                    },
                    icon = {
                        Icon(
                            if (audioState.isPlaying) Icons.Default.GraphicEq else Icons.Default.Headphones,
                            contentDescription = "Audio"
                        )
                    },
                    label = { Text("Audio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = QuranDarkGreen,
                        selectedTextColor = QuranGoldPrimary,
                        indicatorColor = QuranGoldPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_audio")
                )
                NavigationBarItem(
                    selected = selectedBottomNavIndex == 3,
                    onClick = {
                        selectedBottomNavIndex = 3
                        showSavedBookmarksSheet = true
                    },
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                    label = { Text("Saved") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = QuranDarkGreen,
                        selectedTextColor = QuranGoldPrimary,
                        indicatorColor = QuranGoldPrimary,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_saved")
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Top Header: "Holy Quran Pro", "GO PREMIUM" badge, Overflow menu
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Holy Quran",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = QuranGoldPrimary
                            ) {
                                Text(
                                    text = "PRO",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = QuranDarkGreen,
                                        letterSpacing = 1.sp
                                    )
                                )
                            }
                        }
                        Text(
                            text = "15-Line Hafezi Mushaf & Recitation",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = QuranGoldLight
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // GO PREMIUM Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(PremiumBadgeStart, PremiumBadgeEnd)
                                    )
                                )
                                .clickable { showPremiumDialog = true }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                                .testTag("go_premium_badge"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = QuranDarkGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "GO PREMIUM",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = QuranDarkGreen,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Overflow Menu Button
                        Box {
                            IconButton(
                                onClick = { showOverflowMenu = true },
                                modifier = Modifier.testTag("overflow_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More Options",
                                    tint = TextSecondary
                                )
                            }

                            DropdownMenu(
                                expanded = showOverflowMenu,
                                onDismissRequest = { showOverflowMenu = false },
                                modifier = Modifier.background(QuranSurface)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Quick Bookmarks (${bookmarks.size})", color = TextPrimary) },
                                    onClick = {
                                        showOverflowMenu = false
                                        showSavedBookmarksSheet = true
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = QuranGoldPrimary)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Reciters & Audio Settings", color = TextPrimary) },
                                    onClick = {
                                        showOverflowMenu = false
                                        onNavigateToReader(lastReadPage)
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Headphones, contentDescription = null, tint = QuranTeal)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("About Holy Quran Pro", color = TextPrimary) },
                                    onClick = {
                                        showOverflowMenu = false
                                        showAboutDialog = true
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = QuranGoldLight)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Hero Visual Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(QuranSurface)
                        .border(1.dp, QuranCardBorder, RoundedCornerShape(18.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_quran_hero),
                        contentDescription = "Holy Quran Art",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        QuranDarkGreen.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = QuranGoldLight,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "The Noble Quran • 15 Lines Per Page",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextPrimary.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }

            // "LAST READ" Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .testTag("last_read_banner"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = QuranSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, QuranGoldPrimary.copy(alpha = 0.4f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        QuranSurfaceVariant.copy(alpha = 0.9f),
                                        QuranSurface
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        tint = QuranGoldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LAST READ",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = QuranGoldPrimary,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = lastReadSurah,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Page $lastReadPage • Juz $lastReadPara",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = QuranGoldLight
                                    )
                                )
                            }

                            // Play/Resume Button
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(QuranGoldPrimary, QuranGoldSecondary)
                                        )
                                    )
                                    .clickable {
                                        onNavigateToReader(lastReadPage)
                                    }
                                    .testTag("resume_reading_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Resume Reading",
                                    tint = QuranDarkGreen,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Category Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Explore Mushaf",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "604 Pages Total",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = QuranGoldPrimary
                        )
                    )
                }
            }

            // 2-Column Grid Action Cards
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    for (i in actionCards.indices step 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                ActionCardItem(card = actionCards[i])
                            }
                            if (i + 1 < actionCards.size) {
                                Box(modifier = Modifier.weight(1f)) {
                                    ActionCardItem(card = actionCards[i + 1])
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // GO PREMIUM Dialog
        if (showPremiumDialog) {
            AlertDialog(
                onDismissRequest = { showPremiumDialog = false },
                containerColor = QuranSurface,
                icon = {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = QuranGoldPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                },
                title = {
                    Text(
                        text = "Holy Quran Pro",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Enjoy the ultimate spiritual reading experience:",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                        PremiumFeatureRow("100% Ad-Free uninterrupted reading")
                        PremiumFeatureRow("High-Res 15-Line Hafezi Mushaf offline caching")
                        PremiumFeatureRow("EveryAyah Lossless Audio sync & repeat")
                        PremiumFeatureRow("Unlimited bookmarks & quick notes")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showPremiumDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = QuranGoldPrimary,
                            contentColor = QuranDarkGreen
                        )
                    ) {
                        Text("PRO ACTIVATED", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPremiumDialog = false }) {
                        Text("Close", color = TextSecondary)
                    }
                }
            )
        }

        // 99 Names of Allah Sheet
        if (show99NamesSheet) {
            ModalBottomSheet(
                onDismissRequest = { show99NamesSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = QuranSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "99 Beautiful Names (أسماء الله الحسنى)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "The divine attributes of Allah Almighty",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(QuranData.namesOfAllah) { name ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = QuranSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, QuranCardBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${name.number}.",
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                color = QuranGoldPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = name.transliteration,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextPrimary
                                                )
                                            )
                                            Text(
                                                text = name.meaning,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = TextSecondary
                                                )
                                            )
                                        }
                                    }
                                    Text(
                                        text = name.arabic,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = QuranGoldPrimary,
                                            fontSize = 20.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Tajweed Guide Sheet
        if (showTajweedSheet) {
            ModalBottomSheet(
                onDismissRequest = { showTajweedSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = QuranSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Tajweed Rules Guide (أحكام التجويد)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Essential guidelines for correct Quranic recitation",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(QuranData.tajweedRules) { rule ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = QuranSurfaceVariant),
                                border = androidx.compose.foundation.BorderStroke(1.dp, QuranCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = rule.title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = QuranGoldLight
                                            )
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = QuranDarkGreen
                                        ) {
                                            Text(
                                                text = rule.category,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = QuranTeal
                                                )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = rule.description,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextSecondary
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Example: ",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = TextMuted
                                            )
                                        )
                                        Text(
                                            text = rule.example,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = QuranGoldPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Quran Info Sheet
        if (showQuranInfoSheet) {
            ModalBottomSheet(
                onDismissRequest = { showQuranInfoSheet = false },
                containerColor = QuranSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Quran Facts & Statistics",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val facts = listOf(
                        "Total Surahs" to "114 Chapters",
                        "Total Verses" to "6,236 Ayahs",
                        "Total Paras (Juz)" to "30 Sections",
                        "Mushaf Pages" to "604 Standard 15-Line Pages",
                        "Meccan Surahs" to "86 Revealed in Makkah",
                        "Medinan Surahs" to "28 Revealed in Madinah",
                        "Longest Surah" to "Surah Al-Baqarah (286 Ayahs)",
                        "Shortest Surah" to "Surah Al-Kawthar (3 Ayahs)",
                        "Central Surah" to "Surah Al-Kahf (Page 293)"
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(facts) { (label, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(QuranSurfaceVariant)
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary
                                    )
                                )
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = QuranGoldLight
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Quick Read Sheet
        if (showQuickReadSheet) {
            ModalBottomSheet(
                onDismissRequest = { showQuickReadSheet = false },
                containerColor = QuranSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Quick Read Daily Surahs",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Jump instantly to commonly recited Surahs",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    val quickList = listOf(
                        Triple("Surah Al-Kahf (الكهف)", "Friday Sunnah recitation", 293),
                        Triple("Surah Ya-Sin (يس)", "Heart of the Quran", 440),
                        Triple("Surah Al-Mulk (الملك)", "Protection of the grave", 562),
                        Triple("Surah Ar-Rahman (الرحمن)", "The beauty of creation", 531),
                        Triple("Surah Al-Waqi'ah (الواقعة)", "Barakah & protection from poverty", 534),
                        Triple("Ayat Al-Kursi (آية الكرسي)", "Surah Al-Baqarah Ayah 255", 42)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickList) { (title, subtitle, startPage) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showQuickReadSheet = false
                                        onNavigateToReader(startPage)
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = QuranSurfaceVariant),
                                border = androidx.compose.foundation.BorderStroke(1.dp, QuranCardBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = subtitle,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary
                                            )
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = QuranGoldPrimary
                                    ) {
                                        Text(
                                            text = "Page $startPage",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = QuranDarkGreen
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Saved Bookmarks Sheet
        if (showSavedBookmarksSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSavedBookmarksSheet = false },
                containerColor = QuranSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "Saved Bookmarks (${bookmarks.size})",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (bookmarks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No saved bookmarks yet",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                                )
                                Text(
                                    text = "Tap the bookmark icon on any page in the reader to save it here.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(340.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(bookmarks.sorted()) { pageNum ->
                                val surah = QuranData.getSurahForPage(pageNum)
                                val para = QuranData.getParaForPage(pageNum)
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showSavedBookmarksSheet = false
                                            onNavigateToReader(pageNum)
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = QuranSurfaceVariant),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, QuranCardBorder)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Page $pageNum • ${surah.nameEnglish}",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = QuranGoldLight
                                                )
                                            )
                                            Text(
                                                text = "Juz ${para.number} (${para.nameRoman}) • ${surah.nameArabic}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = TextSecondary
                                                )
                                            )
                                        }
                                        IconButton(
                                            onClick = { viewModel.toggleBookmark(pageNum) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove bookmark",
                                                tint = TextMuted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // About Dialog
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                containerColor = QuranSurface,
                title = {
                    Text(
                        text = "Holy Quran Pro",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldPrimary
                        )
                    )
                },
                text = {
                    Text(
                        text = "A native high-performance Android Quran application featuring standard 15-line printed Hafezi Mushaf pages (604 pages), offline disk caching, pinch zoom, and synchronized page-by-page audio recitation powered by EveryAyah CDN and Media3 ExoPlayer.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("OK", color = QuranGoldPrimary)
                    }
                }
            )
        }
    }
}

@Composable
fun ActionCardItem(card: DashboardActionCard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = card.onClick)
            .testTag(card.testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = QuranSurface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, QuranCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(QuranSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = card.icon,
                    contentDescription = card.title,
                    tint = card.iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = card.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = card.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PremiumFeatureRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = QuranTeal,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
        )
    }
}
