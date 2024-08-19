package com.example.vitaminapp.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vitaminapp.data.di.MedicinesModel
import com.example.vitaminapp.data.repository.Repository
import javax.inject.Inject

class ReminderViewModel @Inject constructor(private val repository: Repository): ViewModel(){
    fun getAll(): LiveData<List<MedicinesModel>> {
        return repository.getAll()
    }

    suspend fun delete(medicinesModel: MedicinesModel) {
        repository.delete(medicinesModel)
    }
}