package com.iliatokarev.pizzeriano161.presentation.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.ActionErrorView
import com.iliatokarev.pizzeriano161.basic.shimmerBrush

@Composable
fun ManagerScreenView(
    modifier: Modifier = Modifier,
    onAllPizzaClicked: () -> Unit,
    onNewOrdersClicked: () -> Unit,
    onCompletedOrdersClicked: () -> Unit,
    uiState: ManagerUiState,
    onTryAuthAgainClicked: () -> Unit,
) {
    Box(modifier = modifier) {
        if (uiState.isInitialLoading) {
            ShimmedView()
        } else if (uiState.isUserNull) {
            ActionErrorView { onTryAuthAgainClicked() }
        } else {
            ManagerView(
                onAllPizzaClicked = { onAllPizzaClicked() },
                onNewOrdersClicked = { onNewOrdersClicked() },
                onCompletedOrdersClicked = { onCompletedOrdersClicked() },
            )
        }
    }
}

@Composable
private fun ManagerView(
    modifier: Modifier = Modifier,
    onAllPizzaClicked: () -> Unit = {},
    onNewOrdersClicked: () -> Unit = {},
    onCompletedOrdersClicked: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { onAllPizzaClicked() },
            ) {
                Text(text = stringResource(R.string.menu))
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { onNewOrdersClicked() },
            ) {
                Text(text = stringResource(R.string.new_orders))
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = { onCompletedOrdersClicked() },
            ) {
                Text(text = stringResource(R.string.completed_orders))
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
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ManagerViewPreview() {
    ManagerView()
}