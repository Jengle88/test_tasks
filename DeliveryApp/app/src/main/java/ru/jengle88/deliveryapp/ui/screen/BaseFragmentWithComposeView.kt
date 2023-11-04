package ru.jengle88.deliveryapp.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ru.jengle88.deliveryapp.R

abstract class BaseFragmentWithComposeView(
    @LayoutRes private val layoutRes: Int
): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(layoutRes, container, false)
        val composeView = rootView.findViewById<ComposeView>(R.id.compose_view)

        composeContent(composeView)

        return rootView
    }

    abstract fun composeContent(composeView: ComposeView)
}