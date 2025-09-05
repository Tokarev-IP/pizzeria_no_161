package com.iliatokarev.pizzeriano161.presentation.orders.common

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.ActionErrorView
import com.iliatokarev.pizzeriano161.basic.shimmerBrush
import com.iliatokarev.pizzeriano161.domain.order.OrderData
import com.iliatokarev.pizzeriano161.domain.order.orderDataListPreview
import com.iliatokarev.pizzeriano161.presentation.orders.completed.CompletedOrdersUiState
import com.iliatokarev.pizzeriano161.presentation.orders.uncompleted.UncompletedOrdersUiState

@Composable
fun UncompletedOrdersScreenView(
    modifier: Modifier = Modifier,
    orderDataList: List<OrderData>,
    uiState: UncompletedOrdersUiState,
    onReloadUncompletedOrdersClicked: () -> Unit,
    onOrderChangeCompletedState: (orderData: OrderData) -> Unit,
    onDeleteOrderClicked: (orderId: String) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (uiState.isInitialLoading) {
            ShimmedView()
        } else if (uiState.isLoadingOrdersError) {
            ActionErrorView { onReloadUncompletedOrdersClicked() }
        } else {
            if (uiState.isLoading)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            OrdersView(
                modifier = modifier,
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

@Composable
fun CompletedOrdersScreenView(
    modifier: Modifier = Modifier,
    orderDataList: List<OrderData>,
    uiState: CompletedOrdersUiState,
    onReloadCompletedOrdersClicked: () -> Unit,
    onOrderChangeCompletedState: (orderData: OrderData) -> Unit,
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
            if (uiState.isLoading)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            OrdersView(
                modifier = modifier,
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

@Composable
private fun OrdersView(
    modifier: Modifier = Modifier,
    orderDataList: List<OrderData> = emptyList(),
    isLoading: Boolean = false,
    onChangeCompletedState: (OrderData) -> Unit = {},
    onDeleteOrderClicked: (orderId: String) -> Unit = {}
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
                    }
                )
            }
        }
}

@Composable
private fun OrderItemView(
    modifier: Modifier = Modifier,
    orderData: OrderData,
    onChangeCompletedState: (orderData: OrderData) -> Unit,
    onDeleteOrderClicked: (orderId: String) -> Unit,
    isLoading: Boolean,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { onChangeCompletedState(orderData) },
        enabled = !isLoading,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OrderSubmenuBox(
                modifier = modifier.align(Alignment.End),
                onChangeCompletedState = { onChangeCompletedState(orderData) },
                onDeleteOrderClicked = { onDeleteOrderClicked(orderData.id) },
                isLoadingState = isLoading,
                isCompleted = orderData.isCompleted,
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                OrderItemMainInfo(
                    modifier = modifier.weight(1F),
                    orderData = orderData,
                )
                OrderItemPizzaInfo(
                    modifier = modifier.weight(1F),
                    orderData = orderData,
                )
            }
        }
    }
}

@Composable
private fun OrderItemMainInfo(
    modifier: Modifier = Modifier,
    orderData: OrderData,
) {
    Column(modifier = modifier) {
        if (orderData.isCompleted)
            Text(
                text = stringResource(R.string.status_completed),
                color = Color.Green,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
        else
            Text(
                text = stringResource(R.string.status_new_order),
                color = Color.Red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = orderData.time,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = orderData.consumerName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = orderData.consumerPhone,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = orderData.additionalInfo,
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
    Column(modifier = modifier) {
        for (pizza in orderData.pizzaList) {
            ElevatedCard(
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    text = pizza,
                    fontSize = 16.sp,
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
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    AnimatedContent(
                        targetState = isCompleted,
                    ) { isOrderCompleted ->
                        if (isOrderCompleted)
                            Text(text = stringResource(R.string.mark_as_new_order))
                        else
                            Text(text = stringResource(R.string.mark_as_completed_order))
                    }
                },
                onClick = {
                    expanded = false
                    onChangeCompletedState
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null
                    )
                }
            )
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