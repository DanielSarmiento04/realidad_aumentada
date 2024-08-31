package com.example.realidad_aumentada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ar.core.Config
import com.example.realidad_aumentada.theme.Realidad_aumentadaTheme
import com.example.realidad_aumentada.theme.Translucent
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Realidad_aumentadaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        MainActivityScreen()

                    }
                }
            }
        }
    }
}

@Composable
fun MainActivityScreen() {
    var currentScreen by remember { mutableStateOf("home") }

    when (currentScreen) {
        "home" -> HomeScreen(onNavigate = { currentScreen = it })
        "settings" -> SettingsScreen()
        // Add more screens here as needed
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { onNavigate("settings") }) {
            Text(text = "Go to Settings")
        }
    }
}



@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Settings", style = MaterialTheme.typography.h4)
        // Add other settings UI components here
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Menu(
    modifier: Modifier,
    onClick: (String) -> Unit
)
{
    var currentIndex by remember {
        mutableStateOf(0)
    }

    val itemsList = listOf(
        Food("burger", R.drawable.burger),
        Food("instant", R.drawable.instant),
        Food("momos", R.drawable.momos),
        Food("pizza", R.drawable.pizza),
        Food("ramen", R.drawable.ramen),
    )

    fun updateIndex(offset : Int) {
        currentIndex = ( currentIndex + offset + itemsList.size ) % itemsList.size
        onClick(itemsList[currentIndex].name)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            updateIndex(-1)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription = "Anterior"
            )
        }

        CircularImage(imageId = itemsList[currentIndex].imageId)

        IconButton(onClick = {
            updateIndex(+1)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription = "Siguiente"
            )
        }
    }
}



@Composable
fun ARScreen(
    model : String
) {
    val nodes = remember {
        mutableListOf<ArNode>()
    }

    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }

    val placeModelButton = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()){
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = {arSceneView ->
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(arSceneView.engine,PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.8f
                    ){

                    }
                    onAnchorChanged = {
                        placeModelButton.value = !isAnchored
                    }
                    onHitResult = {node, hitResult ->
                        placeModelButton.value = node.isTracking
                    }

                }
                nodes.add(modelNode.value!!)
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )
        if(placeModelButton.value){
            Button(onClick = {
                modelNode.value?.anchor()
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Place It")
            }
        }
    }

    LaunchedEffect(key1 = model){
        modelNode.value?.loadModelGlbAsync(
            glbFileLocation = "models/${model}.glb",
            scaleToUnits = 0.8f
        )
    }

}

@Composable
fun CircularImage(
    modifier: Modifier = Modifier,
    imageId: Int
) {
    Box(
        modifier = modifier
            .size(140.dp)
            .clip(CircleShape)  // Corrected CircleShape
            .border(width = 3.dp, color = Translucent, CircleShape)
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}


data class Food(var name:String, var imageId:Int) {

}
