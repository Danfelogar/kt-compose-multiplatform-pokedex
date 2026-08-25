package com.example.pokedex.data.local

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("pokedex.db")
    return Room.databaseBuilder(context = appContext, name = dbFile.absolutePath)
}