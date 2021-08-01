package com.example.bookhub.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase: RoomDatabase() {
    //RoomDatabase is the return type of the class and gonna use all the libraries from RoomDatabase
    abstract fun bookDao(): BookDao



}