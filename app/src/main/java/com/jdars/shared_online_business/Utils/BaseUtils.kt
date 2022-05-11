package com.jdars.shared_online_business.Utils

import android.R
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class BaseUtils {
    companion object{
        fun showMessage(view: View, message: String) {
            Snackbar.make(
                view,
                message, Snackbar.LENGTH_LONG
            ).show()
        }

        fun changeMiliSecondToTime(milliSeconds: Long, dateFormat: String?): String? {
            val formatter = SimpleDateFormat(dateFormat)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }

        fun animationOpenScreen(): NavOptions {
            return navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = R.animator.fade_in
                    exit = R.animator.fade_out
                }
            }
        }
    }
}