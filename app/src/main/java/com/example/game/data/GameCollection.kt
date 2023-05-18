package com.example.game.data

import com.example.game.R

// direction: 1 - up, 2 - down, 3 - random
// slide: true or false

object GameCollection {
    fun getGame(gameNumber: Int): GameSettings {
        return when (gameNumber) {
            1 -> GameSettings(
                direction = 2,
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
            else -> GameSettings(
                direction = 1,
                slide = false,
                listOf(
                    R.drawable.game2_img1,
                    R.drawable.game2_img2,
                    R.drawable.game2_img3,
                )
            )
        }
    }
}