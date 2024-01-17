package com.bryant.itunesmusicsearch.data.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class History(@PrimaryKey var keyword: String = "")