package com.alorb.smarttoll.common.composable

import androidx.compose.foundation.layout.RowScope
import com.alorb.smarttoll.ui.theme.SmartTollTheme


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    TopAppBar(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = actions,
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MyTopAppBarPreview(){
    SmartTollTheme {
        MyTopAppBar(
            title = "Title"
        )
        Spacer(modifier = Modifier.fillMaxHeight())
    }
}