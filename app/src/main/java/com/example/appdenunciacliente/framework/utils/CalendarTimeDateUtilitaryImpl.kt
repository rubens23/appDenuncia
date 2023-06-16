package com.example.appdenunciacliente.framework.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarTimeDateUtilitaryImpl: CalendarTimeDateUtilitary {
    override fun pegarDataAtualFormatada(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        val currentDate = Date()

        return dateFormat.format(currentDate)
    }

}