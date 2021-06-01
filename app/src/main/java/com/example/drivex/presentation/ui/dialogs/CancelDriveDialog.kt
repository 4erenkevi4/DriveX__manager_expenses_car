package com.example.drivex.presentation.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.drivex.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class CancelDriveDialog : DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: (() -> Unit)) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.Base_V7_Theme_AppCompat_Light)
            .setTitle("Cancel the Drive?")
            .setMessage("Are you sure that you want to cancel the current run and delete its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                yesListener?.let { yes ->
                    yes()
                }

            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}