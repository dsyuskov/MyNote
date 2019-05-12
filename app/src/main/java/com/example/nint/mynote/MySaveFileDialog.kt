package com.example.nint.mynote

import android.os.Bundle
import android.view.View
import com.rustamg.filedialogs.SaveFileDialog


class MySaveFileDialog: SaveFileDialog() {
    companion object {
        const val DEFAULT_FILE = "default_file"
    }
    var mDefaultFileName = ""

    override fun extractArguments(savedInstanceState: Bundle?) {

        val arguments = arguments

        if (arguments != null && arguments.containsKey(DEFAULT_FILE)) {
            mDefaultFileName = arguments.getString(DEFAULT_FILE)
        }
        super.extractArguments(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFileNameText.setText(mDefaultFileName)
    }
}