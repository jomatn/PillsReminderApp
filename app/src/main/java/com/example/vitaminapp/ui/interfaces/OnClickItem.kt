package com.example.vitaminapp.ui.interfaces

import com.example.vitaminapp.data.di.MedicinesModel

interface OnClickItem {
    fun onLongClick(medicinesModel: MedicinesModel)

    fun onClick(medicinesModel: MedicinesModel)
}