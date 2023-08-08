package ru.dsvusial.playlistmaker.addPlaylist.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getPlaylistLiveData().observe(viewLifecycleOwner) { playlist ->
            playlistNameEditText.setText(playlist.playlistName)
            playlistDescEditText.setText(playlist.playlistDesc)
            Glide.with(requireActivity())
                .load(playlist.playlistUri)
                .placeholder(R.drawable.nodata)
                .centerCrop()
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
