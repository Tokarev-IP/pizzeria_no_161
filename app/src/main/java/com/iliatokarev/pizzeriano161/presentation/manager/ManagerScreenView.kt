package com.iliatokarev.pizzeriano161.presentation.manager

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    isOpen: Boolean?,
    onIsOpenStateChanged: () -> Unit,
    isOveHot: Boolean?,
    onIsHotStateChanged: () -> Unit,
) {
    Box(modifier = modifier) {
        if (uiState.isInitialError) {
            ActionErrorView { onTryAuthAgainClicked() }
        } else {
            if (isOpen == null || isOveHot == null) {
                ShimmedView()
            } else {
                AnimatedVisibility(uiState.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                ManagerView(
                    onAllPizzaClicked = { onAllPizzaClicked() },
                    onNewOrdersClicked = { onNewOrdersClicked() },
                    onCompletedOrdersClicked = { onCompletedOrdersClicked() },
                    isOpen = isOpen,
                    onIsOpenStateChanged = { onIsOpenStateChanged() },
                    isOvenHot = isOveHot,
                    onIsHotStateChanged = { onIsHotStateChanged() },
                )
            }
        }
    }
}

@Composable
private fun ManagerView(
    modifier: Modifier = Modifier,
    onAllPizzaClicked: () -> Unit = {},
    onNewOrdersClicked: () -> Unit = {},
    onCompletedOrdersClicked: () -> Unit = {},
    isOpen: Boolean = true,
    onIsOpenStateChanged: () -> Unit = {},
    isOvenHot: Boolean = false,
    onIsHotStateChanged: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = stringResource(R.string.pizzeria_161),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(20.dp))
            WebLinkText()
            Spacer(modifier = Modifier.height(36.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            IsOpenSwitchView(
                isOpen = isOpen,
                onIsOpenStateChanged = { onIsOpenStateChanged() },
            )
            Spacer(modifier = Modifier.height(20.dp))
            IsHotSwitchView(
                isHot = isOvenHot,
                onIsHotStateChanged = { onIsHotStateChanged() },
            )
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

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
private fun WebLinkText() {
    Text(
        buildAnnotatedString {
            withLink(
                LinkAnnotation.Url(
                    "https://pizzeria-161.web.app/",
                    TextLinkStyles(style = SpanStyle(color = Color.Blue))
                )
            ) {
                append("pizzeria-161.web.app")
            }
        }
    )
}

@Composable
private fun IsOpenSwitchView(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onIsOpenStateChanged: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(
                targetState = isOpen,
            ) { open ->
                if (open)
                    Text(
                        text = stringResource(R.string.open),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                else
                    Text(
                        text = stringResource(R.string.closed),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
            }

            Switch(
                checked = isOpen,
                onCheckedChange = { onIsOpenStateChanged() },
            )
        }
    }
}

@Composable
private fun IsHotSwitchView(
    modifier: Modifier = Modifier,
    isHot: Boolean,
    onIsHotStateChanged: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(
                targetState = isHot,
            ) { hot ->
                if (hot)
                    Text(
                        text = stringResource(R.string.oven_is_on),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                else
                    Text(
                        text = stringResource(R.string.oven_is_off),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
            }

            Switch(
                checked = isHot,
                onCheckedChange = { onIsHotStateChanged() },
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