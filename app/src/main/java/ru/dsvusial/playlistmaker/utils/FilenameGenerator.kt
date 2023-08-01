package ru.dsvusial.playlistmaker.utils

object FilenameGenerator {
    fun getImageName(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        val str = (10..14)
            .map { allowedChars.random() }
            .joinToString("")
            .plus(".jpg")
        return str
    }
}