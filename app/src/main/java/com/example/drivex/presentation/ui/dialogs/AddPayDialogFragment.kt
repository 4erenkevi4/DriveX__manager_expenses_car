package com.example.drivex.presentation.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.drivex.R

class AddPayDialogFragment() : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview:View = inflater.inflate(R.layout.dialog_add_payments,container,false)
        return rootview
    }



}