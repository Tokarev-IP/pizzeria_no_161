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
import com.iliatokarev.pizzeriano161.presentation.compose.AcceptAcceptingDialog
import com.iliatokarev.pizzeriano161.presentation.compose.AcceptDeletionDialog
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

    var orderIdToChangeCompletedState by rememberSaveable { mutableStateOf<String?>(null) }
    var showChangeCompletedStateDialog by rememberSaveable { mutableStateOf(false) }

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
            onOrderChangeCompletedState = { orderId ->
                orderIdToChangeCompletedState = orderId
                showChangeCompletedStateDialog = true
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

        if (showChangeCompletedStateDialog){
            orderIdToChangeCompletedState?.let { id ->
                AcceptAcceptingDialog(
                    onDismissRequest = {
                        showChangeCompletedStateDialog = false
                        orderIdToChangeCompletedState = null
                    },
                    onAcceptClicked = {
                        completedOrdersViewModel.setEvent(
                            CompletedOrdersUiEvent.MarkOrderAsNew(orderId = id)
                        )
                        showChangeCompletedStateDialog = false
                        orderIdToChangeCompletedState = null
                    },
                    infoText = stringResource(R.string.mark_order_as_new_dialog)
                )
            }
        }
    }
}