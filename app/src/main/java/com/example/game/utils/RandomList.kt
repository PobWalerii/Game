package com.example.game.utils

import com.example.game.data.ItemImages
import java.util.*

object RandomList {

    fun makeRandomList(listImages: List<Int>, iterations: Int = 1): MutableList<ItemImages> {
        val list = mutableListOf<ItemImages>()
        val random = Random()
        var startImage = random.nextInt(listImages.size)
        repeat(listImages.size*iterations) {
            list.add(ItemImages(startImage+1.toLong(), listImages[startImage]))
            startImage++
            if (startImage == listImages.size) {
                startImage = 0
            }
        }
        return list
    }
}