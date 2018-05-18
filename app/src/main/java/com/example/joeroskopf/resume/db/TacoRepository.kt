package com.example.joeroskopf.resume.db

import android.arch.lifecycle.LiveData
import com.example.joeroskopf.resume.model.network.TacoResponse
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TacoRepository @Inject
constructor(private val tacoDao: TacoDao) {

    /**
     * Saves the [TacoResponse] to local database
     */
    fun saveTacoToDatabase(tacoResponse: TacoResponse): Maybe<Boolean> {
        return Maybe.create<Boolean> {
            val id = tacoDao.insertTaco(tacoResponse.toTacoEntity())
            if(id.isNotEmpty() && id[0] > 0) {
                it.onSuccess(true)
            } else {
                it.onError(Exception("Did not insert successfully"))
            }
        }.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Check if a [TacoEntity] exists in the database.
     * This is used to know if we should try and insert a taco or remove it from the DB
     *
     * @param id - the ID of th taco
     */
    fun checkIfTacoExists(id: String): Maybe<TacoEntity?> {
        return tacoDao.selectTaco(id).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Database operation to delete a [TacoEntity]
     *
     * @param tacoEntity - the Taco to delete!
     */
    fun deleteTaco(tacoEntity: TacoEntity) {
        tacoDao.deleteTaco(tacoEntity)
    }

    fun selectAllTacos(): LiveData<List<TacoEntity>> {
        return tacoDao.selectAll()
    }
}