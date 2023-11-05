package ru.jengle88.deliveryapp.ui.screen.main_screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import ru.jengle88.deliveryapp.R
import ru.jengle88.deliveryapp.app.DeliveryApp
import ru.jengle88.deliveryapp.ui.screen.BaseFragmentWithComposeView
import ru.jengle88.deliveryapp.ui.theme.DeliveryAppTheme
import ru.jengle88.deliveryapp.ui.theme.colorCommonBackground
import javax.inject.Inject

class MainFragment : BaseFragmentWithComposeView(
    layoutRes = R.layout.fragment_main
) {

    @Inject
    lateinit var factory: MainFragmentViewModel.Factory
    private val viewModel: MainFragmentViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as DeliveryApp).appComponent
            .inject(this)
    }

    override fun composeContent(composeView: ComposeView) {
        composeView.setContent {
            DeliveryAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorCommonBackground),
                ) {
                    MainFragmentUi(viewModel)
                }
            }
        }
    }
}