package com.yoon.lactosefree.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [(Favorite::class)], exportSchema = false, version = 1)
@TypeConverters(UriConverters::class)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private lateinit var INSTANCE: FavoriteRoomDatabase
        internal fun getDatabase(context: Context): FavoriteRoomDatabase {
            if (!this::INSTANCE.isInitialized) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRoomDatabase::class.java,
                        "favorite_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}