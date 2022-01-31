package com.example.drivex.presentation.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.fragment.app.Fragment

abstract class AbstractFragment:Fragment() {
    var imageCarUri: Uri? = null

     fun createBitmapFile(uri: Uri): Bitmap? {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)

    }
}