package com.example.fitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlaceholderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val textView = TextView(requireContext()).apply {
            text = "이 화면은 추후 구현 예정입니다."
            textSize = 18f
            setPadding(48, 48, 48, 48)
        }
        return textView
    }
}
