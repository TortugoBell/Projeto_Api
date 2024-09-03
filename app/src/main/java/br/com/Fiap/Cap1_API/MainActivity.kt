package br.com.Fiap.Cap1_API

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.Fiap.Cap1_API.model.TrashMapViewModel
import br.com.Fiap.Cap1_API.ui.theme.MyApplicationTheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var state by remember { mutableStateOf(0) }
    val titles = listOf("Map", "Profile")
    val selectedIndex = remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex.value,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount -> change.consume() }
                }
        ) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedIndex.value == index,
                    onClick = {
                        selectedIndex.value = index
                        state = index
                    },
                    text = { Text(text = title) }
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            androidx.compose.animation.AnimatedVisibility(
                visible = state == 0,
                enter = slideInHorizontally(
                    initialOffsetX = { -1000 }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -1000 }
                )
            ) {
                TrashMapScreen()
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = state == 1,
                enter = slideInHorizontally(
                    initialOffsetX = { 1000 }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { 1000 }
                )
            ) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ProfileHeader()
        Spacer(modifier = Modifier.height(16.dp))
        ExperienceBar()
        Spacer(modifier = Modifier.height(16.dp))
        ChallengesList()
    }
}

@Composable
fun ProfileHeader() {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}
@Composable
fun ExperienceBar() {
    var sliderPosition by remember { mutableStateOf(0f) }
    val level = 0

    Column {
        Text(
            "Level: $level",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        )
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = Color(0xFFF9ADA0),
                activeTrackColor = Color(0xFF21B7D9),
                inactiveTrackColor = Color(0xFF2B59C3)
            ))
    }
}

@Composable
fun ChallengesList() {
    LazyColumn {
        items(4) { index ->
            ChallengeItem(
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                dateLimit = "2023-12-31",
                expPoints = 100
            )
        }
    }
}

@Composable
fun ChallengeItem(description: String, dateLimit: String, expPoints: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = description)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date Limit: $dateLimit")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "EXP: $expPoints")
        }
    }
}

@Composable
fun TrashMapScreen(viewModel: TrashMapViewModel = TrashMapViewModel()) {
    val searchQueries = listOf(
        "Oil" to Icons.Filled.LocalGasStation,
        "Electronic Waste" to Icons.Filled.Computer,
        "Compostables" to Icons.Filled.Eco
    )
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                properties = mapProperties,
                uiSettings = uiSettings,
                onMapClick = {
                }
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchQueries) { (query, icon) ->
                Button(
                    onClick = { viewModel.searchPlaces(query) },
                    modifier = Modifier.widthIn(min = 100.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(query)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrashMapScreenPreview() {
    MainScreen()
}