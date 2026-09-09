package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.screens.HafeziReaderScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ParaIndexScreen
import com.example.ui.screens.SurahIndexScreen
import com.example.viewmodel.HafeziViewModel

object NavRoutes {
    const val HOME_SCREEN = "home_screen"
    const val SURAH_INDEX_SCREEN = "surah_index_screen"
    const val PARA_INDEX_SCREEN = "para_index_screen"
    const val HAFEZI_READER_SCREEN = "hafezi_reader_screen"
    const val HAFEZI_READER_ROUTE = "hafezi_reader_screen/{pageNumber}"
}

@Composable
fun QuranNavGraph(
    navController: NavHostController,
    viewModel: HafeziViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME_SCREEN,
        modifier = modifier
    ) {
        composable(NavRoutes.HOME_SCREEN) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToSurahIndex = {
                    navController.navigate(NavRoutes.SURAH_INDEX_SCREEN)
                },
                onNavigateToParaIndex = {
                    navController.navigate(NavRoutes.PARA_INDEX_SCREEN)
                },
                onNavigateToReader = { pageNumber ->
                    navController.navigate("${NavRoutes.HAFEZI_READER_SCREEN}/$pageNumber")
                }
            )
        }

        composable(NavRoutes.SURAH_INDEX_SCREEN) {
            SurahIndexScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToReader = { pageNumber ->
                    navController.navigate("${NavRoutes.HAFEZI_READER_SCREEN}/$pageNumber")
                }
            )
        }

        composable(NavRoutes.PARA_INDEX_SCREEN) {
            ParaIndexScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToReader = { pageNumber ->
                    navController.navigate("${NavRoutes.HAFEZI_READER_SCREEN}/$pageNumber")
                }
            )
        }

        composable(
            route = NavRoutes.HAFEZI_READER_ROUTE,
            arguments = listOf(
                navArgument("pageNumber") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val pageNumber = backStackEntry.arguments?.getInt("pageNumber") ?: 1
            HafeziReaderScreen(
                initialPage = pageNumber,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
