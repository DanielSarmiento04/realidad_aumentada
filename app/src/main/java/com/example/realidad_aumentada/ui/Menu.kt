package com.example.realidad_aumentada.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.realidad_aumentada.model.Food

@Composable
fun Menu(foods: List<Food>, onClick: (String) -> Unit) {
    var currentIndex by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        IconButton(onClick = {
            currentIndex = (currentIndex - 1 + foods.size) % foods.size
            onClick(foods[currentIndex].name)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "Anterior"
            )
        }

        CircularImage(imageId = foods[currentIndex].imageId)

        IconButton(onClick = {
            currentIndex = (currentIndex + 1) % foods.size
            onClick(foods[currentIndex].name)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "Siguiente"
            )
        }
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
            .clip(CircleShape)
            .border(width = 3.dp, color = MaterialTheme.colorScheme.secondary, CircleShape)
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            contentScale = ContentScale.FillBounds
        )
    }
}