package com.iliatokarev.pizzeriano161.presentation.all_pizza

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.BasicTopAppBar
import com.iliatokarev.pizzeriano161.presentation.compose.AcceptAcceptingDialog
import com.iliatokarev.pizzeriano161.presentation.compose.AcceptDeletionDialog
import com.iliatokarev.pizzeriano161.presentation.main.MainUiEvent
import com.iliatokarev.pizzeriano161.presentation.main.MainViewModel

@Composable
fun AllPizzaScreen(
    modifier: Modifier = Modifier,
    allPizzaViewModel: AllPizzaViewModel,
    mainViewModel: MainViewModel,
    localContext: Context = LocalContext.current,
) {
    val uiState by allPizzaViewModel.getUiState().collectAsStateWithLifecycle()
    val allPizzaDataList by allPizzaViewModel.getAllPizzaDataList().collectAsStateWithLifecycle()

    var pizzaIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }
    var showDialogToDeletePizza by rememberSaveable { mutableStateOf(false) }

    var pizzaIdToChangeAvailableState by rememberSaveable { mutableStateOf<String?>(null) }
    var showDialogToMarkPizzaUnavailable by rememberSaveable { mutableStateOf(false) }
    var showDialogToMarkPizzaAvailable by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = uiState.isError) {
        if (uiState.isError)
            Toast.makeText(
                localContext,
                R.string.error,
                Toast.LENGTH_SHORT
            ).show()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar(
                title = stringResource(R.string.menu)
            ) { mainViewModel.setEvent(MainUiEvent.GoBack) }
        }
    ) { paddingValues ->
        AllPizzaScreenView(
            modifier = modifier.padding(paddingValues),
            uiState = uiState,
            pizzaDataList = allPizzaDataList,
            onPizzaItemClicked = { pizzaId ->
                mainViewModel.setEvent(MainUiEvent.GoToEditPizzaScreen(pizzaId = pizzaId))
            },
            onAddPizzaItemClick = {
                mainViewModel.setEvent(MainUiEvent.GoToEditPizzaScreen(pizzaId = null))
            },
            onDeletePizzaClicked = { pizzaId ->
                pizzaIdToDelete = pizzaId
                showDialogToDeletePizza = true
            },
            onTryAgainClicked = {
                allPizzaViewModel.setEvent(AllPizzaUiEvent.DownloadAllPizza)
            },
            onMarkPizzaAsAvailable = { pizzaId ->
                pizzaIdToChangeAvailableState = pizzaId
                showDialogToMarkPizzaAvailable = true
            },
            onMarkPizzaAsUnavailable = { pizzaId ->
                pizzaIdToChangeAvailableState = pizzaId
                showDialogToMarkPizzaUnavailable = true
            },
        )
    }

    if (showDialogToDeletePizza) {
        pizzaIdToDelete?.let { id ->
            AcceptDeletionDialog(
                onDismissRequest = {
                    pizzaIdToDelete = null
                    showDialogToDeletePizza = false
                },
                onDeleteClicked = {
                    allPizzaViewModel.setEvent(AllPizzaUiEvent.DeletePizza(pizzaId = id))
                    pizzaIdToDelete = null
                    showDialogToDeletePizza = false
                },
                infoText = stringResource(R.string.delete_this_pizza)
            )
        }
    }

    if (showDialogToMarkPizzaUnavailable) {
        pizzaIdToChangeAvailableState?.let { id ->
            AcceptAcceptingDialog(
                onDismissRequest = {
                    pizzaIdToChangeAvailableState = null
                    showDialogToMarkPizzaUnavailable = false
                },
                onAcceptClicked = {
                    allPizzaViewModel.setEvent(
                        AllPizzaUiEvent.ChangeAvailableState(pizzaId = id)
                    )
                    pizzaIdToChangeAvailableState = null
                    showDialogToMarkPizzaUnavailable = false
                },
                infoText = stringResource(R.string.mark_pizza_as_unavailable_dialog_text),
            )
        }
    }

    if (showDialogToMarkPizzaAvailable) {
        pizzaIdToChangeAvailableState?.let { id ->
            AcceptAcceptingDialog(
                onDismissRequest = {
                    pizzaIdToChangeAvailableState = null
                    showDialogToMarkPizzaAvailable = false
                },
                onAcceptClicked = {
                    allPizzaViewModel.setEvent(
                        AllPizzaUiEvent.ChangeAvailableState(pizzaId = id)
                    )
                    pizzaIdToChangeAvailableState = null
                    showDialogToMarkPizzaAvailable = false
                },
                infoText = stringResource(R.string.mark_pizza_as_available_dialog_text),
            )
        }
    }
}