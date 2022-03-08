package com.example.drivex.presentation.ui.info

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drivex.R
import com.example.drivex.presentation.ui.fragments.AbstractFragment

class InfoFragment : AbstractFragment() {

private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_info, container, false)
        setFloatingMenuVisibility(false)
        toolbar = root.findViewById(R.id.toolbar_back_Button)

        setToolbar(toolbar, R.string.menu_info,true)

        return root
    }
}