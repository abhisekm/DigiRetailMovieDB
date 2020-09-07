package com.abhisekm.digiretailmoviedb.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*


@Dao
interface MovieDao {
    @Query("select * from movies_table order by vote_average DESC")
    fun getMovies(): LiveData<List<DatabaseMovies>>

    @Query("select * from movies_table order by vote_average DESC, id DESC")
    fun getMoviesPaged(): DataSource.Factory<Int, DatabaseMovies>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: DatabaseMovies)
}


@Database(entities = [DatabaseMovies::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}

private lateinit var INSTANCE: MovieDatabase

fun getDatabase(context: Context): MovieDatabase {
    synchronized(MovieDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MovieDatabase::class.java,
                "movieDb").build()
        }
    }
    return INSTANCE
}