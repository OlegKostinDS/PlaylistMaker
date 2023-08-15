package ru.dsvusial.playlistmaker.utils

object ConvertUtil {
     fun conventAmountToTrackString(amount: Int): String {
        var temp = amount % 100
        if (temp in 5..20)
            return "$amount треков"
        temp %= 10
        return when (temp) {
            1 -> "$amount трек"
            in (2..4) -> "$amount трека"
            else -> "$amount треков"
        }
    }
    fun conventAmountToMinutesString(amount: Int): String {
        var temp = amount % 100
        if (temp in 5..20)
            return "$amount минут"
        temp %= 10
        return when (temp) {
            1 -> "$amount минута"
            in (2..4) -> "$amount минуты"
            else -> "$amount минут"
        }
    }
}