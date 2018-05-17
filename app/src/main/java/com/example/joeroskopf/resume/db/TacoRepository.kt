package com.example.joeroskopf.resume.db

import android.database.DatabaseErrorHandler
import android.util.Log
import com.example.joeroskopf.resume.model.network.TacoResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TacoRepository @Inject
constructor(private val tacoDao: TacoDao, private val appDatabase: AppDatabase) {

    /**
     * Saves the [TacoResponse] to local database
     */
    fun saveTacoToDatabase(tacoResponse: TacoResponse): Maybe<Boolean> {
        return Maybe.create<Boolean> {
            Log.d("D","tacoDebug - in repo going to save taco")
            try {
                val id = tacoDao.insertTaco(tacoResponse.toTacoEntity())
                Log.d("D","tacoDebug - insert went okay $id")
                if(id.isNotEmpty() && id[0] > 0) {
                    it.onSuccess(true)
                } else {
                    it.onError(Exception("Did not insert successfully"))
                }
            }catch (exception: Exception) {
                Log.d("D","tacoDebug exception in inserting!!! ${exception.localizedMessage}")
            }
        }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun checkIfTacoExists(id: String): Maybe<TacoEntity?> {
        return tacoDao.selectTaco(id).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteTaco(tacoEntity: TacoEntity) {
        tacoDao.deleteTaco(tacoEntity)
    }
}