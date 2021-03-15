package com.example.jetpackcomposelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposelayout.ui.theme.JetpackComposeLayoutTheme
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    //SimpleList()
                    //LazyList()
                    CustomBodyContent()

                }
            }
        }
    }
}

@Composable
fun CustomBodyContent(modifier: Modifier = Modifier){
    CustomColumn(modifier.padding(8.dp)) {
        Text(text = "Custom Column")
        Text(text = "Great Work")
        Text(text = "Wow good job")
    }
}

@Composable
fun CustomColumn(
    modifier: Modifier = Modifier,
    // custom layout attributes
    content: @Composable ()->Unit
){
    Layout(
        modifier = modifier,
        content = content) {masurables, constraints ->
        // measure and position children given constraints logic here
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeable = masurables.map { mesurable->
            mesurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 100

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeable.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0 , y =  yPosition)
                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }



    }

}

fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) = Modifier.layout {mesurable,constraints ->
    val placebale = mesurable.measure(constraints)

    // Check the composable has a first baseline
    check(placebale[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseLine = placebale[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placebaleY = firstBaselineToTop.roundToPx() - firstBaseLine
    val height = placebale.height + placebaleY

    layout(placebale.width,height) {
        // Where the composable gets placed
        placebale.placeRelative(0,placebaleY)
    }

}

@Composable
fun SimpleList() {
    /*
    We save the scrolling position with this state that can also be used to programmatically
    scroll the list
    */
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
    ) {
        repeat(100) {
            Text(text = "Item No $it")
        }
    }
}

@Composable
fun LazyList() {
    val listSize = 100
    /*
   We save the scrolling position with this state that can also be used to programmatically
   scroll the list
   We save the scrolling position with this state
   */
    val scrollState = rememberLazyListState()
    // We save the coroutine scope where our animated scroll will be executed
    val coroutineScope = rememberCoroutineScope()
    Column() {
        Row() {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {

                Text(text = "Scroll To Top")
            }
            
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text(text = "Scroll To Bottom")
            }
        }

        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(index = it)
            }
        }
    }


}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CoilImage(
            data = "https://developer.android.com/images/brand/Android_Robot.png",
            contentDescription = "Android Logo",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Lazy Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}


@Composable
fun LayoutCodeLab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Home", style = MaterialTheme.typography.h6)
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = "Welcome Richard")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.secondary)
                .clickable(onClick = {})
                .padding(16.dp)
                .fillMaxWidth()
        )
        {
            Surface(
                modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            ) {

            }
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Richard Dewan", fontWeight = FontWeight.W500)

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "5 minutes ago", style = MaterialTheme.typography.body2)
                }

            }
        }

        Row(
            modifier.padding(8.dp)
        ) {

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Click Me")
            }

        }
    }


}

@Preview
@Composable
fun TextWithPaddingToBaselinePreview(){
    JetpackComposeLayoutTheme {
        Text(text = "Hi there", Modifier.firstBaselineToTop(24.dp))
    }
}

@Preview
@Composable
fun TextWithNormalPaddingPreview(){
    JetpackComposeLayoutTheme {
        Text(text = "Hi there with normal padding", Modifier.padding(32.dp))
    }
}

@Preview
@Composable
fun SimpleListPreview() {
    SimpleList()
}

@Preview(name = "MainPreview", group = "MainActivity")
@Composable
fun PhotographerCardPreview() {
    PhotographerCard()
}

@Preview
@Composable
fun LayoutCodeLabPreview() {
    LayoutCodeLab()
}