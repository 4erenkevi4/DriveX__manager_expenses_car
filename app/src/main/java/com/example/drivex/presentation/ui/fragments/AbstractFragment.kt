package com.example.drivex.presentation.ui.fragments

import android.annotation.SuppressLint
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.MainActivity
import com.example.drivex.presentation.ui.setting.SettingFragment
import kotlinx.android.synthetic.main.app_bar_main.*

abstract class AbstractFragment : Fragment() {
    var imageCarUri: Uri? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setToolbar(toolbar: androidx.appcompat.widget.Toolbar?) {
        val activity = activity as MainActivity? ?: return
            activity.setSupportActionBar(toolbar)
    }

    fun setFloatingMenuVisibility(visibility: Boolean){
        val activity = activity as MainActivity? ?: return
        activity.boomMenu?.isVisible = visibility
    }

    fun createBitmapFile(uri: Uri): Bitmap? {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)

    }

    fun getCroppedBitmap(uri: Uri): Bitmap? {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}