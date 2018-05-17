package com.example.joeroskopf.resume.ui.main

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.joeroskopf.resume.db.TacoRepository
import com.example.joeroskopf.resume.network.TacoService
import java.util.logging.Logger
import javax.inject.Inject

/**
 * Factory for creating a PlacesViewModel with injectable dependencies
 */
class MainViewModelFactory @Inject constructor(
        private val tacoService: TacoService,
        private val tacoRepository: TacoRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(tacoService, tacoRepository) as T
}