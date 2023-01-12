package ru.dsvusial.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    var tempEditTextString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar)
        val searchEditText = findViewById<EditText>(R.id.search_search)
        val searchClearBtn = findViewById<ImageView>(R.id.search_cancel_btn)
        searchClearBtn.visibility = View.GONE


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

    companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
