package com.smartrecipe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smartrecipe.ui.screens.camera.CameraScreen
import com.smartrecipe.ui.screens.home.HomeScreen
import com.smartrecipe.ui.screens.recipe.RecipeDetailScreen
import com.smartrecipe.ui.screens.ingredients.IngredientsScreen
import com.smartrecipe.ui.screens.favorites.FavoritesScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Camera : Screen("camera")
    object RecipeDetail : Screen("recipe/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe/$recipeId"
    }
    object Ingredients : Screen("ingredients")
    object Favorites : Screen("favorites")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onCameraClick = { navController.navigate(Screen.Camera.route) },
                onIngredientSelect = { navController.navigate(Screen.Ingredients.route) },
                onFavoritesClick = { navController.navigate(Screen.Favorites.route) }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onIngredientsDetected = { ingredients ->
                    // Malzemeler tespit edildiğinde yapılacak işlemler
                    navController.navigate(Screen.Ingredients.route)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.RecipeDetail.route) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            RecipeDetailScreen(
                recipeId = recipeId ?: "",
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Ingredients.route) {
            IngredientsScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}