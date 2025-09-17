package com.iliatokarev.pizzeriano161.presentation.edit_order

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
import com.iliatokarev.pizzeriano161.presentation.compose.AddPizzaBottomSheet
import com.iliatokarev.pizzeriano161.presentation.main.MainUiEvent
import com.iliatokarev.pizzeriano161.presentation.main.MainViewModel

@Composable
fun EditOrderScreen(
    modifier: Modifier = Modifier,
    editOrderViewModel: EditOrderViewModel,
    mainViewModel: MainViewModel,
    myContext: Context = LocalContext.current,
    orderId: String,
) {
    val uiState by editOrderViewModel.getUiState().collectAsStateWithLifecycle()
    val orderData by editOrderViewModel.getOrderDataFlow().collectAsStateWithLifecycle()
    val pizzaDataList by editOrderViewModel.getAllPizzaFlow().collectAsStateWithLifecycle()

    var showPizzaBottomSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = uiState) {
        if (uiState.isError)
            Toast.makeText(
                myContext,
                R.string.there_is_an_error,
                Toast.LENGTH_SHORT
            ).show()

        if (uiState.isSaved)
            Toast.makeText(
                myContext,
                R.string.saved,
                Toast.LENGTH_SHORT
            ).show()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar(
                title = stringResource(R.string.edit_order)
            ) {
                mainViewModel.setEvent(MainUiEvent.GoBack)
            }
        }
    ) { innerPadding ->
        EditOrderScreenView(
            modifier = modifier.padding(innerPadding),
            orderData = orderData,
            uiState = uiState,
            onTryAgainClicked = {
                editOrderViewModel.setEvent(EditOrderUiEvent.DownloadAllData(orderId = orderId))
            },
            onSetTimeDay = { timeDay ->
                editOrderViewModel.setEvent(EditOrderUiEvent.SetDate(timeDay))
            },
            onSetTimeHour = { timeHour ->
                editOrderViewModel.setEvent(EditOrderUiEvent.SetHour(timeHour))
            },
            onDeletePizza = { pizzaName ->
                editOrderViewModel.setEvent(EditOrderUiEvent.DeletePizza(pizzaName))
            },
            onSaveChanges = { email, comment ->
                editOrderViewModel.setEvent(EditOrderUiEvent.UploadOrderData(email, comment))
            },
            onAddPizzaClicked = {
                showPizzaBottomSheet = true
            }
        )

        if (showPizzaBottomSheet){
            AddPizzaBottomSheet(
                pizzaDataList = pizzaDataList,
                onChoosePizza = { pizzaData ->
                    editOrderViewModel.setEvent(EditOrderUiEvent.AddPizza(pizzaData))
                    showPizzaBottomSheet = false
                },
                onDismissRequest = {
                    showPizzaBottomSheet = false
                }
            )
        }
    }
}