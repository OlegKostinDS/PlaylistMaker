package ru.dsvusial.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtil {
    fun formatTimeMillisToString(timeMillis: Int) =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeMillis)
            .toString()
    fun formatTimeMillisToMinutesString(timeMillis: Int):Int =
       timeMillis/(1000*60)

}
