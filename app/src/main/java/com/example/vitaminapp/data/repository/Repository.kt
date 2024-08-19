package com.example.vitaminapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vitaminapp.data.di.MedicinesModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Repository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    fun getAll(): LiveData<List<MedicinesModel>> {
        val liveData = MutableLiveData<List<MedicinesModel>>()
        db.collection("medicines")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                val medicines = value?.toObjects(MedicinesModel::class.java)
                liveData.value = medicines ?: emptyList()
            }
        return liveData
    }

    fun getMedicineById(id: String): LiveData<MedicinesModel> {
        val liveData = MutableLiveData<MedicinesModel>()
        db.collection("medicines").document(id).get()
            .addOnSuccessListener { document ->
                val medicine = document.toObject(MedicinesModel::class.java)
                liveData.value = medicine
            }
        return liveData
    }

    suspend fun insert(medicinesModel: MedicinesModel) {
        db.collection("medicines").add(medicinesModel).await()
    }

    suspend fun delete(medicinesModel: MedicinesModel) {
        db.collection("medicines").document(medicinesModel.id!!).delete().await()
    }

    suspend fun update(medicinesModel: MedicinesModel) {
        db.collection("medicines").document(medicinesModel.id!!).set(medicinesModel).await()
    }
}
