package ru.dsvusial.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.settings.ui.view_model.SettingViewModel
import ru.dsvusial.playlistmaker.utils.router.SettingRouter

class SettingsFragment : Fragment() {
    val viewModel by viewModel<SettingViewModel>()
    val settingRouter by lazy {
        SettingRouter(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.settings_toolbar)
        val sharingBtn = view.findViewById<FrameLayout>(R.id.settings_sharing_btn)
        val supportBtn = view.findViewById<FrameLayout>(R.id.settings_support_btn)
        val userCaseBtn = view.findViewById<FrameLayout>(R.id.settings_user_case_btn)
        val themeSwitcher = view.findViewById<SwitchMaterial>(R.id.themeSwitcher)

        viewModel.getSwitcherStatusLiveData().observe(viewLifecycleOwner) {
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
