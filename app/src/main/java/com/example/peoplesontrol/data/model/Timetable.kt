package com.example.peoplesontrol.data.model

import com.google.android.gms.maps.model.LatLng

object Timetable {
    val regions = arrayOf("Донецк")
    val busList = arrayOf("№ 26")
    val busStations =
        arrayOf(
            "ЖД вокзал",
            "ЖДИ",
            "Рынок",
            "Стоматология",
            "Завод Топаз",
            "пл.Бакинских Комиссаров",
            "ул.Розы Люксембург",
            "Университет",
            "Парк Щербакова",
            "АС Центр",
        )
    val stationsMarkers = listOf<LatLng>(
        LatLng(48.042737, 37.747531),
        LatLng(48.041687, 37.749248),
        LatLng(48.040334, 37.747295),
        LatLng(48.036775, 37.746489),
        LatLng(48.026676, 37.755555),
        LatLng(48.010320, 37.769510),
        LatLng(48.011236, 37.794265),
        LatLng(48.004722, 37.795785),
        LatLng(47.995216, 37.798662),
        LatLng(47.987455, 37.798827)
    )
}