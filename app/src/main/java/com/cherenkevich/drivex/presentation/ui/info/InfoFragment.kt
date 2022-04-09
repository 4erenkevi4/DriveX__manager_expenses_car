package com.cherenkevich.drivex.presentation.ui.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.presentation.ui.fragments.AbstractFragment


class InfoFragment : AbstractFragment() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var privacyPolicy: TextView
    private lateinit var terms: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar_back_Button)
        privacyPolicy = view.findViewById(R.id.privacy_policy)
        terms = view.findViewById(R.id.terms_andcond)
        setFloatingMenuVisibility(false)
        setToolbar(toolbar, R.string.menu_info, true)
        privacyPolicy.setOnClickListener {
            val adress: Uri = Uri.parse("https://pages.flycricket.io/drivex/privacy.html")
            val browser = Intent(Intent.ACTION_VIEW, adress)
            startActivity(browser)
        }
        terms.setOnClickListener {
            val adress: Uri = Uri.parse("https://pages.flycricket.io/drivex/terms.html")
            val browser = Intent(Intent.ACTION_VIEW, adress)
            startActivity(browser)
        }
    }
}