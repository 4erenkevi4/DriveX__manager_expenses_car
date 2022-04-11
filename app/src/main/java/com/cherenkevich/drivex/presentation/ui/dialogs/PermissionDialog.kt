package com.cherenkevich.drivex.presentation.ui.dialogs

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.presentation.ui.notification.NotificationViewModel
import com.cherenkevich.drivex.utils.Constans
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class PermissionDialog(
) : DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.location_permishion_title))
        builder.setMessage(getString(R.string.location_permishion_text))
        builder.setPositiveButton(
            getString(R.string.accept_general)
        ) { dialog, id ->
            requestPermissions()
        }
        builder.setNegativeButton(
            getString(R.string.cancel_general)
        ) { dialog, id ->
            Toast.makeText(activity, getString(R.string.location_permishion_warning), Toast.LENGTH_LONG)
                .show()
        }
        builder.setCancelable(true)
        return builder.create()
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.FOREGROUND_SERVICE
                    ),
                    Constans.REQUEST_CODE_GET_PERMISSION
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    Constans.REQUEST_CODE_GET_PERMISSION
                )
            }
        }
    }
}


