package com.iliatokarev.pizzeriano161.presentation.orders.completed

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
import com.iliatokarev.pizzeriano161.presentation.dialogs.AcceptDeletionDialog
import com.iliatokarev.pizzeriano161.presentation.main.MainUiEvent
import com.iliatokarev.pizzeriano161.presentation.main.MainViewModel
import com.iliatokarev.pizzeriano161.presentation.orders.common.CompletedOrdersScreenView

@Composable
fun CompletedOrdersScreen(
    modifier: Modifier = Modifier,
    completedOrdersViewModel: CompletedOrdersViewModel,
    mainViewModel: MainViewModel,
    localContext: Context = LocalContext.current,
) {
    val uiState by completedOrdersViewModel.getUiState().collectAsStateWithLifecycle()
    val completedOrdersList by completedOrdersViewModel.getCompletedOrdersList()
        .collectAsStateWithLifecycle()

    var showDeletionDialog by rememberSaveable { mutableStateOf(false) }
    var orderIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        if (uiState.isDeleteOrderError || uiState.isMarkOrderAsNewError) {
            Toast.makeText(
                localContext,
                R.string.error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar(
                title = stringResource(R.string.completed_orders)
            ) {
                mainViewModel.setEvent(MainUiEvent.GoBack)
            }
        }
    ) { innerPadding ->

        CompletedOrdersScreenView(
            modifier = modifier.padding(innerPadding),
            orderDataList = completedOrdersList,
            uiState = uiState,
            onReloadCompletedOrdersClicked = {
                completedOrdersViewModel.setEvent(CompletedOrdersUiEvent.LoadCompletedOrders)
            },
            onOrderChangeCompletedState = { orderData ->
                completedOrdersViewModel.setEvent(
                    CompletedOrdersUiEvent.MarkOrderAsNew(orderData)
                )
            },
            onDeleteOrderClicked = { orderId ->
                orderIdToDelete = orderId
                showDeletionDialog = true
            },
        )

        if (showDeletionDialog) {
            orderIdToDelete?.let { id ->
                AcceptDeletionDialog(
                    onDismissRequest = {
                        showDeletionDialog = false
                        orderIdToDelete = null
                    },
                    onDeleteClicked = {
                        completedOrdersViewModel.setEvent(
                            CompletedOrdersUiEvent.DeleteOrder(id)
                        )
                        showDeletionDialog = false
                        orderIdToDelete = null
                    },
                    infoText = stringResource(R.string.delete_this_order),
                )
            }
        }
    }
}