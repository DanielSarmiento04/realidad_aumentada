package com.example.realidad_aumentada.repository

import com.example.realidad_aumentada.model.Food
import com.example.realidad_aumentada.R

class FoodRepository {
    private val foodList = listOf(
        Food("burger", R.drawable.burger),
        Food("instant", R.drawable.instant),
        Food("momos", R.drawable.momos),
        Food("pizza", R.drawable.pizza),
        Food("ramen", R.drawable.ramen)
    )

    fun getFoods(): List<Food> = foodList
}
