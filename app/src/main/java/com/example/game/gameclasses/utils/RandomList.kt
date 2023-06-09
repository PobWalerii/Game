package com.example.game.gameclasses.utils

import com.example.game.data.ItemImages
import java.util.*

object RandomList {

    fun makeRandomList(listImages: List<Int>, iterations: Int = 3): MutableList<ItemImages> {
        val list = mutableListOf<ItemImages>()
        val random = Random()
        var startImage = random.nextInt(listImages.size)
        var ii = 1L
        repeat(listImages.size*iterations) {
            list.add(ItemImages(startImage+1.toLong(), ii, listImages[startImage]))
            ii++
            startImage++
            if (startImage == listImages.size) {
                startImage = 0
            }
        }
        return list
    }
}