package com.iliatokarev.pizzeriano161.presentation.orders.uncompleted

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
import com.iliatokarev.pizzeriano161.presentation.compose.RejectionOrderDialog
import com.iliatokarev.pizzeriano161.presentation.main.MainUiEvent
import com.iliatokarev.pizzeriano161.presentation.main.MainViewModel
import com.iliatokarev.pizzeriano161.presentation.orders.common.UncompletedOrdersScreenView

@Composable
fun UncompletedOrdersScreen(
    modifier: Modifier = Modifier,
    uncompletedOrdersViewModel: UncompletedOrdersViewModel,
    mainViewModel: MainViewModel,
    localContext: Context = LocalContext.current,
) {
    val uiState by uncompletedOrdersViewModel.getUiState().collectAsStateWithLifecycle()
    val ordersDataList by uncompletedOrdersViewModel.getUncompletedOrdersList()
        .collectAsStateWithLifecycle()

    var showDeletionDialog by rememberSaveable { mutableStateOf(false) }
    var orderIdToDelete by rememberSaveable { mutableStateOf<String?>(null) }

    var orderIdToChangeCompletedState by rememberSaveable { mutableStateOf<String?>(null) }
    var showChangeCompletedStateDialog by rememberSaveable { mutableStateOf(false) }

    var orderIdToMarkAsConfirmed by rememberSaveable { mutableStateOf<String?>(null) }
    var orderIdToMarkAsRejected by rememberSaveable { mutableStateOf<String?>(null) }

    var showMarkAsConfirmedDialog by rememberSaveable { mutableStateOf(false) }
    var showMarkAsRejectedDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState.isDeleteOrderError || uiState.isError) {
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
                title = stringResource(R.string.new_orders)
            ) {
                mainViewModel.setEvent(MainUiEvent.GoBack)
            }
        }
    ) { paddingValues ->

        UncompletedOrdersScreenView(
            modifier = modifier.padding(paddingValues),
            orderDataList = ordersDataList,
            uiState = uiState,
            onReloadUncompletedOrdersClicked = {
                uncompletedOrdersViewModel.setEvent(UncompletedOrdersUiEvent.LoadUncompletedOrders)
            },
            onOrderChangeCompletedState = { orderId ->
                orderIdToChangeCompletedState = orderId
                showChangeCompletedStateDialog = true
            },
            onDeleteOrderClicked = { orderId ->
                orderIdToDelete = orderId
                showDeletionDialog = true
            },
            onMarkAsRejected = { orderId ->
                orderIdToMarkAsRejected = orderId
                showMarkAsRejectedDialog = true
            },
            onMarkAsConfirmed = { orderId ->
                orderIdToMarkAsConfirmed = orderId
                showMarkAsConfirmedDialog = true
            },
            onEditOrderClicked = { orderData ->
                mainViewModel.setEvent(MainUiEvent.GoToEditOrderScreen(orderData.id))
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
                        uncompletedOrdersViewModel.setEvent(
                            UncompletedOrdersUiEvent.DeleteOrder(id)
                        )
                        showDeletionDialog = false
                        orderIdToDelete = null
                    },
                    infoText = stringResource(R.string.delete_this_order),
                )
            }
        }

        if (showChangeCompletedStateDialog) {
            orderIdToChangeCompletedState?.let { id ->
                AcceptAcceptingDialog(
                    onDismissRequest = {
                        showChangeCompletedStateDialog = false
                        orderIdToChangeCompletedState = null
                    },
                    onAcceptClicked = {
                        uncompletedOrdersViewModel.setEvent(
                            UncompletedOrdersUiEvent.MarkOrdersAsCompleted(orderId = id)
                        )
                        showChangeCompletedStateDialog = false
                        orderIdToChangeCompletedState = null
                    },
                    infoText = stringResource(R.string.mark_order_as_completed_dialog)
                )
            }
        }

        if (showMarkAsConfirmedDialog) {
            orderIdToMarkAsConfirmed?.let { id ->
                AcceptAcceptingDialog(
                    onDismissRequest = {
                        showMarkAsConfirmedDialog = false
                        orderIdToMarkAsConfirmed = null
                    },
                    onAcceptClicked = {
                        uncompletedOrdersViewModel.setEvent(
                            UncompletedOrdersUiEvent.MarkOrderAsConfirmed(id)
                        )
                        showMarkAsConfirmedDialog = false
                        orderIdToMarkAsConfirmed = null
                    },
                    infoText = stringResource(R.string.mark_order_as_confirmed_dialog_text)
                )
            }
        }

        if (showMarkAsRejectedDialog){
            orderIdToMarkAsRejected?.let { id ->
                RejectionOrderDialog(
                    onDismissRequest = {
                        showMarkAsRejectedDialog = false
                        orderIdToMarkAsRejected = null
                    },
                    onAcceptClicked = { reason ->
                        uncompletedOrdersViewModel.setEvent(
                            UncompletedOrdersUiEvent.MarkOrderAsRejected(
                                orderId = id,
                                rejectedReason = reason
                            )
                        )
                        showMarkAsRejectedDialog = false
                        orderIdToMarkAsRejected = null
                    },
                    infoText = stringResource(R.string.mark_order_as_rejected_dialog_text)
                )
            }
        }
    }
}