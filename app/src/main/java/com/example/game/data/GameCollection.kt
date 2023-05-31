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
                    R.drawable.game1_img1,
                    R.drawable.game1_img2,
                    R.drawable.game1_img3,
                    R.drawable.game1_img4,
                    R.drawable.game1_img5,
                    R.drawable.game1_img6,
                    R.drawable.game1_img7
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
                    R.drawable.game1_img1,
                    R.drawable.game1_img2,
                    R.drawable.game1_img3,
                    R.drawable.game1_img4,
                    R.drawable.game1_img5,
                    R.drawable.game1_img6,
                    R.drawable.game1_img7,
                )
            )
        }
    }
}