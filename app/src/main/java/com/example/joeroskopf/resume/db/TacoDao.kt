package com.example.joeroskopf.resume.db

import android.arch.persistence.room.*
import io.reactivex.Maybe

@Dao
interface TacoDao {
    @Query("SELECT * FROM taco_favorites")
    fun selectAll(): Maybe<List<TacoEntity>>

    @Query("SELECT * FROM taco_favorites WHERE id like :id")
    fun selectTaco(id: String): Maybe<TacoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaco(vararg tacoEntity: TacoEntity) : List<Long>

    @Delete
    fun deleteTaco(tacoEntity: TacoEntity)
}