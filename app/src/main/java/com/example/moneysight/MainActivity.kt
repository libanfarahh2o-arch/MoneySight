package com.moneysight.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moneysight.app.ui.BillChecklistScreen
import com.moneysight.app.ui.MoneySightScreen
import com.moneysight.app.ui.theme.MoneySightTheme
import com.moneysight.app.viewmodel.AppScreen
import com.moneysight.app.viewmodel.MoneySightViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MoneySightViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            MoneySightTheme(themeMode = themeMode) {
                val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
                when (currentScreen) {
                    AppScreen.MAIN -> MoneySightScreen(viewModel = viewModel)
                    AppScreen.BILL_CHECKLIST -> BillChecklistScreen(viewModel = viewModel)
                }
            }
        }
    }
}
