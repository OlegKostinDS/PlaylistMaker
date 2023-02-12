package ru.dsvusial.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dsvusial.playlistmaker.network.*

class SearchActivity : AppCompatActivity() {
    private val baseUrl = "https://itunes.apple.com"
    private lateinit var searchEditText: EditText
    private lateinit var searchTracksRecyclerView: RecyclerView
    private var tracks = ArrayList<TrackData>()
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val songService = retrofit.create(TrackApi::class.java)

    var tempEditTextString = ""

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar)
        searchEditText = findViewById<EditText>(R.id.search_search)
        val searchClearBtn = findViewById<ImageView>(R.id.search_cancel_btn)
        searchTracksRecyclerView = findViewById<RecyclerView>(R.id.search_tracks_recyclerview)
        searchClearBtn.visibility = View.GONE
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            search()
                true
            }
            false
        }

        val textWatcherSearchBtn = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchClearBtn.visibility = clearButtonVisibility(p0)
                tempEditTextString = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        searchEditText.addTextChangedListener(textWatcherSearchBtn)
        searchClearBtn.setOnClickListener {
            searchEditText.text.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }


        searchTracksRecyclerView.adapter =
            TrackAdapter(tracks)
        searchTracksRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun search() {
        songService.search(searchEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                when (response.code()) {
                    200 ->
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            searchTracksRecyclerView.adapter?.notifyDataSetChanged()

                        } else {
                            Toast.makeText(
                                this@SearchActivity,
                                "ПОИСК НЕ ДАЛ РЕЗУЛЬТАТОВ",
                                Toast.LENGTH_LONG
                            ).show()

                        }


                    else -> {Toast.makeText(
                        this@SearchActivity,
                        "ПОИСК НЕ ДАЛ РЕЗУЛЬТАТОВ",
                        Toast.LENGTH_LONG
                    ).show()}
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PRODUCT_AMOUNT, tempEditTextString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchEditText = findViewById<EditText>(R.id.search_search)
        tempEditTextString = savedInstanceState.getString(PRODUCT_AMOUNT).toString()
        searchEditText.setText(tempEditTextString)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
