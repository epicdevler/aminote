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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.epicdevler.ami.minote.ui.screens.home.HomeScreen
import com.epicdevler.ami.minote.ui.screens.home.notes.NotesVM
import com.epicdevler.ami.minote.ui.screens.home.tags.CreateTagDialog
import com.epicdevler.ami.minote.ui.screens.home.tags.CreateTagVM
import com.epicdevler.ami.minote.ui.screens.home.tags.TagsVM
import com.epicdevler.ami.minote.ui.screens.note.NoteChangeRequest
import com.epicdevler.ami.minote.ui.screens.note.NoteScreen
import com.epicdevler.ami.minote.ui.screens.note.NoteVM
import com.epicdevler.ami.minote.ui.screens.note.SelectTagDialog
import com.epicdevler.ami.minote.ui.screens.search.SearchReason
import com.epicdevler.ami.minote.ui.screens.search.SearchScreen
import com.epicdevler.ami.minote.ui.screens.tag.TagNotesScreen
import com.epicdevler.ami.minote.ui.theme.MinoteTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val notesVM: NotesVM by viewModels()
    private val noteVM: NoteVM by viewModels()
    private val tagsVM: TagsVM by viewModels()

    private lateinit var navController: NavHostController

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
            val navigator = DialogNavigator()
            navController = rememberNavController(navigator)
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
                                notesUiState = notesVM.uiState,
                                onRequestLoadNote = { noteId ->
                                    noteVM.onEvent(
                                        event = NoteVM.Event.LoadNote(noteId)
                                    )
                                },
                                onNavigate = {
                                    navController.navigate(it)
                                }
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
                        ) {// backStack ->
//                            val noteId = backStack.arguments?.getString("noteId")
                            NoteScreen(
                                uiState = noteVM.uiState,
                                onRequestChange = {
                                    when (it) {
                                        is NoteChangeRequest.Save -> {
                                            noteVM.onEvent(
                                                event = NoteVM.Event.Save(
                                                    title = it.title,
                                                    content = it.content
                                                )
                                            )
                                        }

                                        is NoteChangeRequest.AddTag -> Unit
                                    }
                                },
                                onNavigate = { route ->
                                    navController.navigate(route)
                                }
                            ) { navController.navigateUp() }
                        }
                        composable(
                            route = "tag/?tagId={tagId}/notes",
                            arguments = listOf(
                                navArgument("tagId") {
                                    nullable = false
                                }
                            )
                        ) { backStack ->
                            val tagId = backStack.arguments?.getString("tagId")!!
                            TagNotesScreen(
                                tagId = tagId,
                                onNavigate = { navController.navigate(it) }
                            ) { navController.navigateUp() }
                        }

                        composable(
                            route = "search/?tagId={tagId}/{searchReason}",
                            arguments = listOf(
                                navArgument("tagId") {
                                    nullable = true
                                    defaultValue = null
                                }
                            ),
                        ) { backStack ->
                            val searchReason =
                                SearchReason.valueOf(backStack.arguments?.getString("searchReason")!!)
                            val tagId = backStack.arguments?.getString("tagId")
                            SearchScreen(
                                reason = searchReason,
                                forTag = tagId,
                                onRequestLoadNote = { noteId ->
                                    noteVM.onEvent(
                                        event = NoteVM.Event.LoadNote(noteId)
                                    )
                                },
                                onNavigate = {
                                    navController.navigate(it) {
                                        popUpTo(navController.currentDestination?.route!!) {
                                            inclusive = true
                                        }
                                    }
                                }
                            ) { navController.navigateUp() }
                        }

                        dialog(route = "selectTag/{noteId}") { backStack ->
                            val noteId = backStack.arguments?.getString("noteId")!!
                            SelectTagDialog(
                                noteId = noteId,
                                onNavigate = { navController.navigate(it) }
                            )
                        }
                        dialog(route = "createTag") {
                            val vm: CreateTagVM = viewModel()
                            CreateTagDialog(
                                uiState = vm.uiState,
                                onCreate = { vm.create(it) },
                                onNavigateUp = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }

}