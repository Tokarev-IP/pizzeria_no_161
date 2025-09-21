package com.iliatokarev.pizzeriano161.presentation.orders.common

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.ActionErrorView
import com.iliatokarev.pizzeriano161.basic.shimmerBrush
import com.iliatokarev.pizzeriano161.basic.toEmailLink
import com.iliatokarev.pizzeriano161.basic.toTelLink
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.order.orderDataListPreview
import com.iliatokarev.pizzeriano161.presentation.compose.FlowGridLayout
import com.iliatokarev.pizzeriano161.presentation.orders.completed.CompletedOrdersUiState
import com.iliatokarev.pizzeriano161.presentation.orders.uncompleted.UncompletedOrdersUiState

@Composable
fun UncompletedOrdersScreenView(
    modifier: Modifier = Modifier,
    orderDataList: List<OrderData>,
    uiState: UncompletedOrdersUiState,
    onReloadUncompletedOrdersClicked: () -> Unit,
    onOrderChangeCompletedState: (orderId: String) -> Unit,
    onDeleteOrderClicked: (orderId: String) -> Unit,
    onMarkAsConfirmed: (orderId: String) -> Unit,
    onMarkAsRejected: (orderId: String) -> Unit,
    onEditOrderClicked: (orderData: OrderData) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (uiState.isInitialLoading) {
            ShimmedView()
        } else if (uiState.isLoadingOrdersError) {
            ActionErrorView { onReloadUncompletedOrdersClicked() }
        } else {
            Column {
                AnimatedVisibility(uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().shadow(elevation = 20.dp),
                    )
                }
                OrdersView(
                    orderDataList = orderDataList,
                    isLoading = uiState.isLoading,
                    onChangeCompletedState = { orderId ->
                        onOrderChangeCompletedState(orderId)
                    },
                    onDeleteOrderClicked = { orderId ->
                        onDeleteOrderClicked(orderId)
                    },
                    onMarkAsConfirmed = { orderId ->
                        onMarkAsConfirmed(orderId)
                    },
                    onMarkAsRejected = { orderId ->
                        onMarkAsRejected(orderId)
                    },
                    onEditOrderClicked = { orderData ->
                        onEditOrderClicked(orderData)
                    },
                )
            }
        }
    }
}

@Composable
fun CompletedOrdersScreenView(
    modifier: Modifier = Modifier,
    orderDataList: List<OrderData>,
    uiState: CompletedOrdersUiState,
    onReloadCompletedOrdersClicked: () -> Unit,
    onOrderChangeCompletedState: (orderId: String) -> Unit,
    onDeleteOrderClicked: (orderId: String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (uiState.isInitialLoading) {
            ShimmedView()
        } else if (uiState.isLoadingOrdersError) {
            ActionErrorView { onReloadCompletedOrdersClicked() }
        } else {
            Column {
                AnimatedVisibility(uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().shadow(elevation = 20.dp),
                    )
                }
                OrdersView(
                    orderDataList = orderDataList,
                    isLoading = uiState.isLoading,
                    onChangeCompletedState = { orderData ->
                        onOrderChangeCompletedState(orderData)
                    },
                    onDeleteOrderClicked = { orderId ->
                        onDeleteOrderClicked(orderId)
                    },
                )
            }
        }
    }
}

@Composable
private fun OrdersView(
    modifier: Modifier = Modifier,
    orderDataList: List<OrderData> = emptyList(),
    isLoading: Boolean = false,
    onChangeCompletedState: (orderId: String) -> Unit = {},
    onDeleteOrderClicked: (orderId: String) -> Unit = {},
    onMarkAsConfirmed: (orderId: String) -> Unit = {},
    onMarkAsRejected: (orderId: String) -> Unit = {},
    onEditOrderClicked: (orderData: OrderData) -> Unit = {},
) {
    if (orderDataList.isEmpty())
        Text(
            modifier = modifier.fillMaxWidth(),
            text = stringResource(R.string.no_orders),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
        )
    else
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(orderDataList.size) {
                OrderItemView(
                    orderData = orderDataList[it],
                    isLoading = isLoading,
                    onChangeCompletedState = { orderData ->
                        onChangeCompletedState(orderData)
                    },
                    onDeleteOrderClicked = { orderId ->
                        onDeleteOrderClicked(orderId)
                    },
                    onMarkAsConfirmed = { orderId ->
                        onMarkAsConfirmed(orderId)
                    },
                    onMarkAsRejected = { orderId ->
                        onMarkAsRejected(orderId)
                    },
                    onEditOrderClicked = { orderData ->
                        onEditOrderClicked(orderData)
                    },
                )
            }
        }
}

@Composable
private fun OrderItemView(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    onChangeCompletedState: (orderId: String) -> Unit,
    onDeleteOrderClicked: (orderId: String) -> Unit,
    onMarkAsConfirmed: (orderId: String) -> Unit,
    onMarkAsRejected: (orderId: String) -> Unit,
    onEditOrderClicked: (orderData: OrderData) -> Unit,
    isLoading: Boolean,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = !orderData.isConfirmed && !orderData.isCompleted,
                ) {
                    Text(
                        text = stringResource(R.string.status_new),
                        color = Color.Green,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                AnimatedVisibility(
                    visible = !orderData.isConfirmed && orderData.isCompleted,
                ) {
                    Text(
                        text = stringResource(R.string.status_rejected),
                        color = Color.Red,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                AnimatedVisibility(
                    visible = orderData.isConfirmed,
                ) {
                    Text(
                        text = stringResource(R.string.status_confirmed),
                        color = Color.Green,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                OrderSubmenuBox(
                    onChangeCompletedState = { onChangeCompletedState(orderData.id) },
                    onDeleteOrderClicked = { onDeleteOrderClicked(orderData.id) },
                    isLoadingState = isLoading,
                    isCompleted = orderData.isCompleted,
                    onMarkAsConfirmed = { onMarkAsConfirmed(orderData.id) },
                    onMarkAsRejected = { onMarkAsRejected(orderData.id) },
                    onEditOrderClicked = { onEditOrderClicked(orderData) },
                )
            }
            OrderItemMainInfo(
                orderData = orderData,
                isLoading = isLoading,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OrderItemPizzaInfo(orderData = orderData)
        }
    }
}

@Composable
private fun OrderItemMainInfo(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    isLoading: Boolean,
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxWidth()) {
        if (orderData.isCompleted)
            Text(
                text = stringResource(R.string.completed_order),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Cyan,
            )
        else
            Text(
                text = stringResource(R.string.new_order),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Cyan,
            )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.date_data, orderData.time),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.name_data, orderData.consumerName),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.phone_data, orderData.consumerPhone),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Blue,
            modifier = modifier.clickable(
                enabled = !isLoading,
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = orderData.consumerName.toTelLink().toUri()
                    }
                    context.startActivity(intent)
                },
            )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.email_data, orderData.consumerEmail),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Blue,
            modifier = modifier.clickable(
                enabled = !isLoading,
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = orderData.consumerName.toEmailLink().toUri()
                    }
                    context.startActivity(intent)
                },
            )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.sum_data, orderData.sum.toString()),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.comment_data, orderData.additionalInfo),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
private fun OrderItemPizzaInfo(
    modifier: Modifier = Modifier,
    orderData: OrderData,
) {
    FlowGridLayout {
        for (pizza in orderData.pizzaList) {
            ElevatedCard {
                Text(
                    modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    text = pizza,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun OrderSubmenuBox(
    modifier: Modifier = Modifier,
    onDeleteOrderClicked: () -> Unit,
    onChangeCompletedState: () -> Unit,
    onMarkAsConfirmed: () -> Unit,
    onMarkAsRejected: () -> Unit,
    onEditOrderClicked: () -> Unit,
    isLoadingState: Boolean,
    isCompleted: Boolean,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = !expanded },
            enabled = !isLoadingState,
        ) {
            Icon(
                Icons.Outlined.MoreVert,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.delete))
                },
                onClick = {
                    expanded = false
                    onDeleteOrderClicked()
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = null
                    )
                }
            )
            if (!isCompleted)
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.edit))
                    },
                    onClick = {
                        expanded = false
                        onEditOrderClicked()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    }
                )
            DropdownMenuItem(
                text = {
                    if (isCompleted)
                        Text(text = stringResource(R.string.mark_as_new_order))
                    else
                        Text(text = stringResource(R.string.mark_as_completed_order))
                },
                onClick = {
                    expanded = false
                    onChangeCompletedState()
                },
            )
            if (!isCompleted) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.mark_order_as_confirmed))
                    },
                    onClick = {
                        expanded = false
                        onMarkAsConfirmed()
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.mark_order_as_rejected))
                    },
                    onClick = {
                        expanded = false
                        onMarkAsRejected()
                    },
                )
            }
        }
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
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun OrdersViewPreview() {
    OrdersView(
        orderDataList = orderDataListPreview
    )
}