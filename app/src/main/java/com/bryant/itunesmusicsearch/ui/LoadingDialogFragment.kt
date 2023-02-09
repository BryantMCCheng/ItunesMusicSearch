package com.bryant.itunesmusicsearch.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bryant.itunesmusicsearch.R

class LoadingDialogFragment : DialogFragment() {
    companion object {
        private val dialog by lazy {
            LoadingDialogFragment()
        }

        fun newInstance(isCancelable: Boolean = false): LoadingDialogFragment {
            dialog.isCancelable = isCancelable
            return dialog
        }
    }

    override fun dismiss() {
        if (isAdded)
            super.dismiss()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded)
            super.show(manager, tag)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return requireActivity().layoutInflater.inflate(R.layout.progress, container)
    }
}