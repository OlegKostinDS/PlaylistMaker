package ru.dsvusial.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        val sharingBtn = findViewById<FrameLayout>(R.id.settings_sharing_btn)
        val supportBtn = findViewById<FrameLayout>(R.id.settings_support_btn)
        val userCaseBtn = findViewById<FrameLayout>(R.id.settings_user_case_btn)

        sharingBtn.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.yandex_practicum_android_dev_url))
                type = "text/plain"
                startActivity(this)
            }
        }

        supportBtn.setOnClickListener {
            Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_email_text_body))
                startActivity(this)
            }
        }

        userCaseBtn.setOnClickListener {
            val url = Uri.parse(getString(R.string.support_yandex_practicum_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}