package ru.dsvusial.playlistmaker.utils.router

import android.app.Activity
import android.content.Intent
import android.net.Uri
import ru.dsvusial.playlistmaker.R

class SettingRouter(val activity: Activity) {
    fun openOfferActivity() {
        val url = Uri.parse(activity.getString(R.string.support_yandex_practicum_offer))
        val intent = Intent(Intent.ACTION_VIEW, url)
        activity.startActivity(intent)
    }

    fun openSupportActivity() {
        Intent().apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(activity.getString(R.string.contact_email)))
            putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.support_email_subject))
            putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.support_email_text_body))
            activity.startActivity(Intent.createChooser(this, "Sending email"))
        }
    }

    fun openSharingActivity() {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                activity.getString(R.string.yandex_practicum_android_dev_url)
            )
            type = "text/plain"
            activity.startActivity(this)
        }
    }

    fun onBackPressed() {
        activity.finish()
    }
}