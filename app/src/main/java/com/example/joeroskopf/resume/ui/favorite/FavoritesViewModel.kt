package com.example.joeroskopf.resume.ui.favorite

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.example.joeroskopf.resume.db.TacoEntity
import com.example.joeroskopf.resume.db.TacoRepository

class FavoritesViewModel(private val tacoRepository: TacoRepository) : ViewModel() {

    fun fetchAllTacos(): LiveData<List<TacoEntity>> {
        return tacoRepository.selectAllTacos()
    }
}
