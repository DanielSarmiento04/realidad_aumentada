package com.example.realidad_aumentada.ui
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.realidad_aumentada.viewmodel.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val currentModel by viewModel.currentModel
    val foods = viewModel.foods

    ARScreen(model = currentModel)
    Menu(foods = foods, onClick = { viewModel.updateModel(it) })
}
