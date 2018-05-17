package com.example.joeroskopf.resume.db

import com.example.joeroskopf.resume.model.network.TacoResponse
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TacoRepository @Inject
constructor(private val tacoDao: TacoDao, private val appDatabase: AppDatabase) {

    fun saveTacoToDatabase(tacoResponse: TacoResponse): List<Long> {
        return tacoDao.insertTaco(tacoResponse.toTacoEntity())
    }
}