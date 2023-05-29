package com.example.game.data

import com.example.game.R

// direction: true - up, false - down, null - random
// slide: true or false

object GameCollection {
    fun getGame(gameNumber: Int): GameSettings {
        return when (gameNumber) {
            1 -> GameSettings(
                direction = null,
                slide = true,
                listOf(
                    R.drawable.game1_slot1,
                    R.drawable.game1_slot2,
                    R.drawable.game1_slot3,
                    R.drawable.game1_slot4,
                    R.drawable.game1_slot5,
                    R.drawable.game1_slot6,
                    R.drawable.game1_slot7
                )
            )
            2 -> GameSettings(
                direction = false,
                slide = false,
                listOf(
                    R.drawable.game2_img1,
                    R.drawable.game2_img2,
                    R.drawable.game2_img3,
                )
            )
            else -> GameSettings(
                direction = null,
                slide = true,
                listOf(
                    R.drawable.game1_slot1,
                    R.drawable.game1_slot2,
                    R.drawable.game1_slot3,
                    R.drawable.game1_slot4,
                    R.drawable.game1_slot5,
                    R.drawable.game1_slot6,
                    R.drawable.game1_slot7
                )
            )
        }
    }
}