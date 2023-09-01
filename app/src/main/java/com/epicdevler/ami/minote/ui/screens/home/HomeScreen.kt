package com.epicdevler.ami.minote.ui.screens.home

import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.Indicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.epicdevler.ami.minote.ui.screens.home.BottomNavigation.navItems
import com.epicdevler.ami.minote.ui.screens.home.notes.NotesContent
import com.epicdevler.ami.minote.ui.screens.home.notes.NotesVM
import com.epicdevler.ami.minote.ui.screens.home.tags.TagsContent
import com.epicdevler.ami.minote.ui.theme.Shapes
import com.epicdevler.ami.minote.ui.theme.Shapes.top
import kotlinx.coroutines.launch

private object BottomNavigation {
    val navItems = listOf(
        NavItem(
            label = "Notes",
            icon = Icons.Rounded.AddCircle,
            contentType = NavItem.Content.Notes
        ),
        NavItem(
            label = "Tags",
            icon = Icons.Rounded.AddCircle,
            contentType = NavItem.Content.Tags
        )
    )

    data class NavItem(
        val label: String,
        val icon: ImageVector,
        val contentType: Content
    ) {
        enum class Content {
            Notes, Tags
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    notesUiState: NotesVM.UiState,
    onRequestLoadNote: (noteId: String?) -> Unit,
    onNavigate: (String) -> Unit
) {
    val pagerState = rememberPagerState {
        navItems.size
    }
    val navigationCoroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            AnimatedContent(
                targetState = index,
                label = "home_content",
                modifier = Modifier.fillMaxSize()
            ) { contentIndex ->
                when (contentIndex) {
                    0 -> NotesContent(
                        noteUiState = notesUiState,
                        onRequestLoadNote = onRequestLoadNote,
                        onNavigate = onNavigate
                    )

                    1 -> TagsContent(
                        onNavigate = onNavigate,
                        onNavigateUp = {
                            navigationCoroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    )
                }
            }

        }


        BottomNavigation(
            selectedIndex = pagerState.currentPage,
            onClick = { index, _ ->
                navigationCoroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun BottomNavigation(
    modifier: Modifier,
    selectedIndex: Int,
    onClick: (index: Int, contentType: BottomNavigation.NavItem.Content) -> Unit
) {

    Box(
        modifier = Modifier
            .shadow(
                offsetX = 0.dp,
                offsetY = 0.dp,
                blurRadius = 100.dp,
                color = Color.Black.copy(.3f)
            )
            .background(
                color = colorScheme.background,
                shape = Shapes.medium.top
            )
    ) {
        TabRow(
            modifier = modifier
                .padding(10.dp),
            containerColor = colorScheme.background,
            selectedTabIndex = selectedIndex,
            divider = {},
            indicator = { positions ->
                Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(positions[selectedIndex])
                        .fillMaxSize()
                        .zIndex(-1f)
                        .clip(shape = shapes.medium),
                    color = colorScheme.primary,
                )
            },
            tabs = {
                repeat(navItems.size) { itemIndex ->
                    val navItem = navItems[itemIndex]
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Transparent, shape = shapes.medium)
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null
                            ) { onClick(itemIndex, navItem.contentType) }
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = navItem.label,
                            textAlign = TextAlign.Center,
                            color = when (selectedIndex) {
                                itemIndex -> colorScheme.onPrimary
                                else -> colorScheme.secondary
                            },
                            style = typography.bodySmall,
                        )
                    }
                }
            }
        )
    }


}

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }
)