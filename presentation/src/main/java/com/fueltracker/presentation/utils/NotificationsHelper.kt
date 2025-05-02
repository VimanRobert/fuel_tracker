package com.fueltracker.presentation.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

object NotificationsHelper {

    fun showConnectDialogIfNeeded(context: Context) {
        val firebaseAuth = FirebaseAuth.getInstance().currentUser
//        if (!shouldShowConnectPrompt(context)) return
        if (firebaseAuth != null) return

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 40, 20, 20)
        }

        val message = TextView(context).apply {
            text = "You're not connected. Log in using the mobile app to sync your account."
            textSize = 20f
            setTextColor(Color.BLACK)
        }

        val checkBox = CheckBox(context).apply {
            text = "Don't show again"
            textSize = 20f
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = 24
            layoutParams = params
        }

        layout.addView(message)
        layout.addView(checkBox)

        val dialog = AlertDialog.Builder(context)
            .setTitle("No account connected")
            .setView(layout)
            .setPositiveButton("OK") { dialogInterface, _ ->
                if (checkBox.isChecked) {
                    disableDialog(context)
                }
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()

        dialog.window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.90).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private const val PREF_NAME = "car_app_prefs"
    private const val KEY_SHOW_CONNECT_PROMPT = "show_connect_prompt"

    private fun shouldShowConnectPrompt(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SHOW_CONNECT_PROMPT, true)
    }

    private fun disableDialog(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_SHOW_CONNECT_PROMPT, false).apply()
    }

}