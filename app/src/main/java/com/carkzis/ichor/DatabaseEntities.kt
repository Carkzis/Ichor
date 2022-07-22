package com.carkzis.ichor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalHeartRate(
    @PrimaryKey
    val pk: String,
    val date: String,
    val value: String
)