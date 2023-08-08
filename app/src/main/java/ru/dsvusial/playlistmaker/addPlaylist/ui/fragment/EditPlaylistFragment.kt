package ru.dsvusial.playlistmaker.addPlaylist.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.dsvusial.playlistmaker.R
import ru.dsvusial.playlistmaker.addPlaylist.domain.model.PlaylistData
import ru.dsvusial.playlistmaker.addPlaylist.ui.viewmodel.EditPlaylistViewModel
import ru.dsvusial.playlistmaker.detailedPlaylist.ui.fragment.EDIT_KEY
import ru.dsvusial.playlistmaker.music_library.ui.PLAYLIST_KEY
import ru.dsvusial.playlistmaker.utils.FilenameGenerator


class EditPlaylistFragment : AddPlaylistFragment() {
    private lateinit var playlistData: PlaylistData
    private lateinit var playlistNameEditText: TextInputEditText
    private lateinit var playlistNameEditTextLayout: TextInputLayout
    private lateinit var playlistDescEditText: TextInputEditText
    private lateinit var playlistDescEditTextLayout: TextInputLayout
    private lateinit var addImage: ImageView
    private lateinit var backBtn: MaterialToolbar
    private lateinit var sendBtn: AppCompatButton
    private var addUri: Uri? = null

    override val viewModel: EditPlaylistViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistData = requireArguments().getSerializable(EDIT_KEY)!! as PlaylistData
        viewModel.getData(playlistData)
        initUi(view)
        initPhotoPicker()
        setUi()
        initTextWatchers()
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlist ->
            playlistNameEditText.setText(playlist.playlistName)
            playlistDescEditText.setText(playlist.playlistDesc)
            val cornerRadius =
                requireActivity().resources.getDimensionPixelSize(R.dimen.edit_radius)
            Glide.with(requireActivity())
                .load(playlist.playlistUri)
                .placeholder(R.drawable.nodata)
                .transform(CenterCrop(), RoundedCorners(cornerRadius))
                .into(addImage)
        }
        sendBtn.setOnClickListener {
            lateinit var tempPlaylist: PlaylistData
            if (it.isEnabled) {

                if (addUri == null)
                    addUri = Uri.parse(playlistData.playlistUri)
                tempPlaylist = playlistData.copy(
                    id = playlistData.id,
                    playlistName = playlistNameEditText.text.toString(),
                    playlistDesc = playlistDescEditText.text.toString(),
                    playlistUri = addUri.toString(),
                    playlistTracks = playlistData.playlistTracks,
                    playlistAmount = playlistData.playlistAmount
                )
                viewModel.updatePlaylistForEdit(
                    tempPlaylist
                )
                findNavController().navigate(
                    R.id.action_editPlaylistFragment_to_detailedPlaylistFragment,
                    createArg(tempPlaylist)
                )

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    findNavController().popBackStack()

                }

            })
    }

    private fun initTextWatchers() {
        val textWatcherAddPlaylistNameBtn = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p3 > 0) {
                    playlistNameEditTextLayout.setInputStrokeColor(R.drawable.text_input_layout_selected)
                    viewModel.nameTextHasChanged(true)
                } else {
                    playlistNameEditTextLayout.setInputStrokeColor(R.drawable.text_input_layout_unselected)
                    viewModel.nameTextHasChanged(false)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
        playlistNameEditText.addTextChangedListener(textWatcherAddPlaylistNameBtn)
    }

    private fun initPhotoPicker() {
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    val imageFileName = FilenameGenerator.getImageName()
                    addUri = saveImageToPrivateStorage(uri, imageFileName)
                    viewModel.getData(playlistData.copy(playlistUri = addUri.toString()))
                    addImage.setImageURI(addUri)
                }
            }
        addImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initUi(view: View) {
        playlistNameEditText = view.findViewById(R.id.playlist_name_edit_text)
        playlistDescEditText = view.findViewById(R.id.playlist_desc_edit_text)
        playlistNameEditTextLayout = view.findViewById(R.id.playlist_name_layout)
        playlistDescEditTextLayout = view.findViewById(R.id.playlist_desc)
        backBtn = view.findViewById(R.id.button_back)
        sendBtn = view.findViewById(R.id.send_btn)
        addImage = view.findViewById(R.id.new_playlist_add_image)
    }

    private fun setUi() {
        backBtn.title = getString(R.string.edit)
        sendBtn.text = getString(R.string.save)
    }

    fun createArg(playlistData: PlaylistData): Bundle =
        bundleOf(PLAYLIST_KEY to playlistData)
}
