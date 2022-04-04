package com.jdars.shared_online_business.Utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class BaseUtils {
    companion object{
        fun showMessage(view: View, message: String) {
            Snackbar.make(
                view,
                message, Snackbar.LENGTH_LONG
            ).show()
        }
    }
}