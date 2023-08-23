package com.epicdevler.ami.minote

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.epicdevler.ami.minote.ui.screens.home.HomeScreen
import com.epicdevler.ami.minote.ui.screens.home.notes.NotesVM
import com.epicdevler.ami.minote.ui.screens.home.tags.TagsVM
import com.epicdevler.ami.minote.ui.screens.note.NoteScreen
import com.epicdevler.ami.minote.ui.theme.MinoteTheme

class MainActivity : ComponentActivity() {

    val notesVM: NotesVM by viewModels()
    val tagsVM: TagsVM by viewModels()

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 200L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }

        setContent {
            navController = rememberNavController()
            MinoteTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorScheme.background),
                    color = colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable(route = "home") {
                            HomeScreen(
                                onNavigate = { navController.navigate(it) }
                            )
                        }
                        composable(
                            route = "note/?noteId={noteId}",
                            arguments = listOf(
                                navArgument("noteId") {
                                    nullable = true
                                    defaultValue = null
                                }
                            )
                        ) {
                            val noteId = it.arguments?.getString("noteId")
                            NoteScreen(
                                noteId = noteId?.ifEmpty { null },
                                onNavigate = {}
                            ) { navController.navigateUp() }
                        }
                    }
                }
            }
        }
    }

}