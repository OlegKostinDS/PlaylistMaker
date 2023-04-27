package ru.dsvusial.playlistmaker.mediaPlayer.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import ru.dsvusial.playlistmaker.App
import ru.dsvusial.playlistmaker.PRACTICUM_EXAMPLE_PREFERENCES
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.THEME_KEY

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        val sharingBtn = findViewById<FrameLayout>(R.id.settings_sharing_btn)
        val supportBtn = findViewById<FrameLayout>(R.id.settings_support_btn)
        val userCaseBtn = findViewById<FrameLayout>(R.id.settings_user_case_btn)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.isChecked =
            getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE).getBoolean(
                THEME_KEY,
                false
            )


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            getSharedPreferences(PRACTICUM_EXAMPLE_PREFERENCES, MODE_PRIVATE).edit()
                .putBoolean(THEME_KEY, checked).apply()
        }
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
                startActivity(Intent.createChooser(this, "Sending email"))
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