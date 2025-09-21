package com.iliatokarev.pizzeriano161.presentation.edit_order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.ActionErrorView
import com.iliatokarev.pizzeriano161.basic.TimeUtilsForOrder
import com.iliatokarev.pizzeriano161.basic.shimmerBrush
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.order.orderDataPreview
import com.iliatokarev.pizzeriano161.presentation.compose.FlowGridLayout
import java.util.Calendar

@Composable
fun EditOrderScreenView(
    modifier: Modifier = Modifier,
    orderData: OrderData?,
    uiState: EditOrderUiState,
    onTryAgainClicked: () -> Unit,
    onSetTimeHour: (timeHour: String) -> Unit,
    onSetTimeDay: (timeDay: String) -> Unit,
    onSaveChanges: (email: String, comment: String, sum: Float) -> Unit,
    onAddPizzaClicked: () -> Unit,
    onDeletePizza: (pizzaName: String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (uiState.isInitialLoading)
            ShimmedView()
        else if (uiState.isInitialLoadingError)
            ActionErrorView { onTryAgainClicked() }
        else {
            orderData?.let { order ->
                if (uiState.isLoading)
                    CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
                else
                    EditOrderView(
                        orderData = order,
                        onSetTimeHour = { timeHour ->
                            onSetTimeHour(timeHour)
                        },
                        onSetTimeDay = { timeDay ->
                            onSetTimeDay(timeDay)
                        },
                        onSaveChanges = { email, comment, sum ->
                            onSaveChanges(email, comment, sum)
                        },
                        onAddPizzaClicked = { onAddPizzaClicked() },
                        onDeletePizza = { pizzaName ->
                            onDeletePizza(pizzaName)
                        },
                    )
            } ?: run {
                ShimmedView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditOrderView(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    onSetTimeHour: (timeHour: String) -> Unit = {},
    onSetTimeDay: (timeDay: String) -> Unit = {},
    onDeletePizza: (pizzaName: String) -> Unit = {},
    onSaveChanges: (email: String, comment: String, sum: Float) -> Unit = { _, _, _ -> },
    onAddPizzaClicked: () -> Unit = {},
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            OrderDataListView(
                orderData = orderData,
                onChooseTimeHourClicked = { showTimePicker = true },
                onChooseTimeDayClicked = { showDatePicker = true },
                onDeletePizza = { pizzaName: String ->
                    onDeletePizza(pizzaName)
                },
                onSaveChanges = { email, comment, sum ->
                    onSaveChanges(email, comment, sum)
                },
                onAddPizzaClicked = { onAddPizzaClicked() },
            )
        }
    }

    if (showTimePicker) {
        TimePickerDialogView(
            onConfirm = { time ->
                onSetTimeHour(time)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (showDatePicker) {
        DatePickerDialogView(
            onDateSelected = { dateMillis ->
                dateMillis?.let { millis ->
                    val dateString = TimeUtilsForOrder.longToDateString(millis)
                    onSetTimeDay(dateString)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun OrderDataListView(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    onChooseTimeHourClicked: () -> Unit,
    onChooseTimeDayClicked: () -> Unit,
    onDeletePizza: (pizzaName: String) -> Unit,
    onSaveChanges: (emailText: String, commentText: String, sum: Float) -> Unit,
    onAddPizzaClicked: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var emailText by rememberSaveable { mutableStateOf(orderData.consumerEmail) }
    var commentText by rememberSaveable { mutableStateOf(orderData.additionalInfo) }
    var sumText by rememberSaveable { mutableStateOf(orderData.sum.toString()) }

    var sumTextError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = orderData.sum) {
        sumText = orderData.sum.toString()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.name_data, orderData.consumerName),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.phone_data, orderData.consumerPhone),
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedCard {
                Row(
                    modifier = modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = orderData.timeHour,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(
                        onClick = { onChooseTimeHourClicked() },
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                }
            }
            OutlinedCard {
                Row(
                    modifier = modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = orderData.timeDay,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(
                        onClick = { onChooseTimeDayClicked() },
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = sumText,
            onValueChange = { value ->
                if (sumTextError)
                    sumTextError = false
                sumText = value
            },
            label = { Text(stringResource(R.string.sum)) },
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
            ),
            isError = sumTextError,
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = emailText,
            onValueChange = { value ->
                emailText = value
            },
            label = { Text(stringResource(R.string.email)) },
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = commentText,
            onValueChange = { value ->
                commentText = value
            },
            label = { Text(stringResource(R.string.comment)) },
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
        )
        Spacer(modifier = Modifier.height(20.dp))

        FlowGridLayout {
            for (pizza in orderData.pizzaList) {
                EditPizzaListItemView(
                    onDeleteClicked = { onDeletePizza(pizza) },
                    pizzaName = pizza,
                )
            }
            AddPizzaIconView { onAddPizzaClicked() }
        }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = {
                val sum = sumText.toFloatOrNull()
                sum?.let { sumFloat ->
                    onSaveChanges(emailText, commentText, sumFloat)
                } ?: run { sumTextError = true }
            },
        ) {
            Text(
                text = stringResource(R.string.save),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EditPizzaListItemView(
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit,
    pizzaName: String,
) {
    OutlinedCard {
        Row(
            modifier = modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = pizzaName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            IconButton(
                onClick = { onDeleteClicked() }
            ) {
                Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.delete))
            }
        }
    }
}

@Composable
private fun AddPizzaIconView(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit
) {
    OutlinedCard(
        onClick = { onAddClicked() },
    ) {
        Box(
            modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Icon(Icons.Default.Add, stringResource(R.string.add))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialogView(
    modifier: Modifier = Modifier,
    onConfirm: (time: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimeInput(
                    state = timePickerState,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = {
                            val hour =
                                if (timePickerState.hour == 0) "00" else timePickerState.hour.toString()
                            val minute =
                                if (timePickerState.minute == 0) "00" else timePickerState.minute.toString()
                            val time = "$hour:$minute"
                            onConfirm(time)
                        },
                    ) {
                        Text(stringResource(R.string.accept))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialogView(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun ShimmedView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        repeat(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditOrderViewPreview() {
    EditOrderView(
        orderData = orderDataPreview,
    )
}