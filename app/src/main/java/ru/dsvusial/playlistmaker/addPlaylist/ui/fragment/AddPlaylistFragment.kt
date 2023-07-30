package ru.dsvusial.playlistmaker.addPlaylist.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.addPlaylist.ui.viewmodel.AddPlaylistViewModel


class AddPlaylistFragment : Fragment() {

    private lateinit var playlistNameEditText: TextInputEditText
    private lateinit var playlistNameEditTextLayout: TextInputLayout
    private lateinit var playlistDescEditText: TextInputEditText
    private lateinit var playlistDescEditTextLayout: TextInputLayout
    private lateinit var sendBtn: AppCompatButton
    private lateinit var backBtn: MaterialToolbar
    private lateinit var addImage: ImageView
    private lateinit var backDialog: MaterialAlertDialogBuilder
    var addUri: Uri? = null
    private val playlistIds: List<String> = mutableListOf()
    val viewModel: AddPlaylistViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistNameEditText = view.findViewById(R.id.playlist_name_edit_text)
        playlistDescEditText = view.findViewById(R.id.playlist_desc_edit_text)
        playlistNameEditTextLayout = view.findViewById(R.id.playlist_name_layout)
        playlistDescEditTextLayout = view.findViewById(R.id.playlist_desc)
        sendBtn = view.findViewById(R.id.send_btn)
        backBtn = view.findViewById(R.id.button_back)
        addImage = view.findViewById(R.id.new_playlist_add_image)
        initTextWatchers()
        initListeners()
        initDialogs()
        initPhotoPicker()
        viewModel.getButtonStatusLiveData().observe(viewLifecycleOwner) {
            sendBtn.isEnabled = it
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isNoData()) {
                        backDialog.show()
                    } else {
                        findNavController().popBackStack()
                    }
                }

            })

    }

    private fun initDialogs() {
        backDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
            }
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack()
            }
    }

    private fun initPhotoPicker() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    addImage.setImageURI(uri)
                    addUri = uri
                }
            }
        addImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }


    private fun initTextWatchers() {
        val textWatcherAddPlaylistNameBtn = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p3 > 0)
                    playlistNameEditTextLayout.setInputStrokeColor(R.drawable.text_input_layout_selected)
                else
                    playlistNameEditTextLayout.setInputStrokeColor(R.drawable.text_input_layout_unselected)
                viewModel.nameTextHasChanged(
                    (!playlistDescEditText.text.toString().isNullOrEmpty()) && p3 > 0
                )
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        playlistNameEditText.addTextChangedListener(textWatcherAddPlaylistNameBtn)

        val textWatcherAddPlaylistDescBtn = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p3 > 0)
                    playlistDescEditTextLayout.setInputStrokeColor(R.drawable.text_input_layout_selected)
                else
                    playlistDescEditTextLayout.setInputStrokeColor(R.drawable.text_input_layout_unselected)
                viewModel.nameTextHasChanged(
                    (!playlistNameEditText.text.toString().isNullOrEmpty()) && p3 > 0
                )

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        playlistDescEditText.addTextChangedListener(textWatcherAddPlaylistDescBtn)
    }

    fun TextInputLayout.setInputStrokeColor(colorId: Int) {
        this.defaultHintTextColor =
            resources.getColorStateList(colorId, null)
        this.setBoxStrokeColorStateList(resources.getColorStateList(colorId, null))
    }

    private fun initListeners() {
        backBtn.setOnClickListener {
            if (isNoData()) {
                backDialog.show()

            } else {
                findNavController().popBackStack()
            }
        }
        sendBtn.setOnClickListener {
            if (it.isEnabled) {
                findNavController().popBackStack()
                Toast.makeText(
                    requireContext(),
                    "Плейлист ${playlistNameEditText.text.toString()} создан", Toast.LENGTH_SHORT
                ).show()

                viewModel.addPlaylist(
                    PlaylistData(
                        id = 0,
                        playlistName = playlistNameEditText.text.toString(),
                        playlistDesc = playlistDescEditText.text.toString(),
                        playlistUri = addUri.toString(),
                        playlistTracks = playlistIds,
                        playlistAmount = playlistIds.size
                    )
                )

            }
        }
    }

    private fun isNoData(): Boolean {
        return (addUri != null
                || playlistNameEditText.text.toString().isNotEmpty()
                || playlistDescEditText.text.toString().isNotEmpty())
    }

}