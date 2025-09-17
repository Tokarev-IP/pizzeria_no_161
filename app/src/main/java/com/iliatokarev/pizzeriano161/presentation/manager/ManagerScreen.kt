package com.iliatokarev.pizzeriano161.presentation.manager

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.presentation.main.MainUiEvent
import com.iliatokarev.pizzeriano161.presentation.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    managerViewModel: ManagerViewModel,
    myContext: Context = LocalContext.current,
) {
    val uiState by managerViewModel.getUiState().collectAsStateWithLifecycle()
    val isOpen by managerViewModel.getIsOpen().collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState.isError)
            Toast.makeText(
                myContext,
                R.string.there_is_an_error,
                Toast.LENGTH_SHORT
            ).show()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.main))
                }
            )
        }
    ) { paddingValues ->
        ManagerScreenView(
            modifier = modifier.padding(paddingValues),
            onAllPizzaClicked = {
                mainViewModel.setEvent(MainUiEvent.GoToAllPizzaScreen)
            },
            onNewOrdersClicked = {
                mainViewModel.setEvent(MainUiEvent.GoToNewOrdersScreen)
            },
            onCompletedOrdersClicked = {
                mainViewModel.setEvent(MainUiEvent.GoToCompletedOrdersScreen)
            },
            uiState = uiState,
            onTryAuthAgainClicked = {
                managerViewModel.setEvent(ManagerUiEvent.DoUserAuth)
            },
            isOpen = isOpen,
            onIsOpenStateChanged = {
                managerViewModel.setEvent(ManagerUiEvent.ChangeIsOpenState)
            },
        )
    }
}