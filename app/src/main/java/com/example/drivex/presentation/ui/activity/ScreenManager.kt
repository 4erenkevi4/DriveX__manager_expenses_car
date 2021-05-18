package com.example.drivex.presentation.ui.activity

import android.view.View
import android.widget.TextView

interface ScreenManager {

   fun  initCalendar(textViewDate: TextView)
   fun initPhotoButton(view: View)
   fun initSaveButton(view: View)
   fun putData()
   fun showToast(text: String,view: View )

}