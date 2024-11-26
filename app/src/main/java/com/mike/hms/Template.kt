package com.mike.hms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mike.hms.ui.theme.CommonComponents as CC

import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTemplate(
    navController: NavController,
    title: String,
    topContent: @Composable () -> Unit,
    bodyContent: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val toolbarHeight = screenHeight * 0.1f
    val expandedToolbarHeight = screenHeight * 0.3f

    // Convert dp to pixels
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }
    val expandedToolbarHeightPx = with(LocalDensity.current) { expandedToolbarHeight.toPx() }

    // Track toolbar offset
    var toolbarOffsetHeightPx by remember { mutableFloatStateOf(0f) }
    val maxOffset = expandedToolbarHeightPx - toolbarHeightPx

    // System UI Controller
    val systemUiController = rememberSystemUiController()
    val isTopBarFullyShown = remember(toolbarOffsetHeightPx) { toolbarOffsetHeightPx >= 0 }

    // Update the status bar visibility
    LaunchedEffect(isTopBarFullyShown) {
        systemUiController.isStatusBarVisible = !isTopBarFullyShown
    }

    // Create nested scroll connection
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx + delta
                toolbarOffsetHeightPx = newOffset.coerceIn(-maxOffset, 0f)
                return Offset.Zero
            }
        }
    }

    // Calculate alpha for expanded title based on scroll
    val expandedTitleAlpha = remember(toolbarOffsetHeightPx) {
        ((toolbarOffsetHeightPx + maxOffset) / maxOffset).coerceIn(0f, 1f)
    }

    Box(
        modifier = Modifier
            .background(
                CC.primaryColor()
            )
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // Main content
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(
                    CC.primaryColor(), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Spacer to ensure the content starts below the expanded toolbar
            Spacer(modifier = Modifier.height(expandedToolbarHeight))
            bodyContent()
        }

        // Toolbar with animations
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    with(LocalDensity.current) {
                        (expandedToolbarHeightPx + toolbarOffsetHeightPx).toDp()
                    }
                ),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = if (toolbarOffsetHeightPx < 0) 4.dp else 0.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Expanded title
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(expandedToolbarHeight),
                    contentAlignment = Alignment.Center
                ) {
                    topContent()
                }

                // Collapsed toolbar
                if (toolbarOffsetHeightPx < 0) {
                    TopAppBar(
                        title = {
                            Text(
                                text = title,
                                style = CC.titleTextStyle()
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = CC.primaryColor()
                        ),
                        modifier = Modifier.alpha(1f - expandedTitleAlpha)
                    )
                }
            }
        }
    }
}
