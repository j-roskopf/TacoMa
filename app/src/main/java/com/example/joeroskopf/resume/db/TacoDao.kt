package com.example.joeroskopf.resume.db

import android.arch.persistence.room.*
import com.example.joeroskopf.resume.model.network.TacoResponse
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface TacoDao {
    @Query("SELECT * FROM taco_favorites")
    fun selectAll(): Maybe<List<TacoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaco(vararg tacoEntity: TacoEntity) : List<Long>

    @Delete
    fun deleteTaco(tacoEntity: TacoEntity)
}