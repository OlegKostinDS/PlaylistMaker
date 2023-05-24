package ru.dsvusial.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.settings.ui.view_model.SettingViewModel
import ru.dsvusial.playlistmaker.utils.router.SettingRouter

class SettingsActivity : AppCompatActivity() {

    val viewModel by viewModel<SettingViewModel>()
    val settingRouter by lazy {
        SettingRouter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        val sharingBtn = findViewById<FrameLayout>(R.id.settings_sharing_btn)
        val supportBtn = findViewById<FrameLayout>(R.id.settings_support_btn)
        val userCaseBtn = findViewById<FrameLayout>(R.id.settings_user_case_btn)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.getSwitcherStatusLiveData().observe(this) {
            themeSwitcher.isChecked = it
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onSwitcherClicked(checked)

        }
        sharingBtn.setOnClickListener {
            settingRouter.openSharingActivity()
        }

        supportBtn.setOnClickListener {
            settingRouter.openSupportActivity()
        }

        userCaseBtn.setOnClickListener {
            settingRouter.openOfferActivity()
        }

        toolbar.setNavigationOnClickListener { settingRouter.onBackPressed() }
    }
}