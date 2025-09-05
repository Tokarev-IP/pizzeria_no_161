package com.iliatokarev.pizzeriano161.presentation.all_pizza

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.ActionErrorView
import com.iliatokarev.pizzeriano161.basic.shimmerBrush
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaDataListPreview

@Composable
fun AllPizzaScreenView(
    modifier: Modifier = Modifier,
    uiState: AllPizzaUiState,
    pizzaDataList: List<PizzaData>,
    onPizzaItemClicked: (pizzaId: String) -> Unit,
    onAddPizzaItemClick: () -> Unit,
    onDeletePizzaClicked: (pizzaId: String) -> Unit,
    onTryAgainClicked: () -> Unit,
) {
    Box(modifier = modifier) {
        if (uiState.isInitialLoading) {
            ShimmedView()
        }
        else if (uiState.isDownloadError) {
            ActionErrorView { onTryAgainClicked() }
        } else {
            if (uiState.isLoading)
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            AllPizzaView(
                pizzaDataList = pizzaDataList,
                onPizzaItemClicked = { pizzaId ->
                    onPizzaItemClicked(pizzaId)
                },
                onAddPizzaItemClick = { onAddPizzaItemClick() },
                onDeletePizzaClicked = { pizzaId ->
                    onDeletePizzaClicked(pizzaId)
                }
            )
        }
    }
}

@Composable
private fun AllPizzaView(
    modifier: Modifier = Modifier,
    pizzaDataList: List<PizzaData>,
    onPizzaItemClicked: (pizzaId: String) -> Unit = {},
    onAddPizzaItemClick: () -> Unit = {},
    onDeletePizzaClicked: (pizzaId: String) -> Unit = {},
    isLoadingState: Boolean = false,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        items(pizzaDataList.size) {
            PizzaItemView(
                pizzaData = pizzaDataList[it],
                onClick = { pizzaId ->
                    onPizzaItemClicked(pizzaId)
                },
                onDeletePizzaClicked = { pizzaId ->
                    onDeletePizzaClicked(pizzaId)
                },
                isLoadingState = isLoadingState,
            )
        }
        item {
            Spacer(modifier = modifier.height(16.dp))
            AddPizzaItemView(
                onAddPizzaItemClick = { onAddPizzaItemClick() },
                isLoadingState = isLoadingState,
            )
        }
    }
}

@Composable
private fun PizzaItemView(
    modifier: Modifier = Modifier,
    pizzaData: PizzaData,
    onClick: (pizzaId: String) -> Unit,
    onDeletePizzaClicked: (pizzaId: String) -> Unit,
    isLoadingState: Boolean,
) {
    val uri =
        if (pizzaData.photoUriFirebase == null) pizzaData.photoUri else pizzaData.photoUriFirebase.toUri()

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { onClick(pizzaData.id) },
        enabled = !isLoadingState,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            PizzaSubmenuBox(
                modifier = modifier.align(Alignment.End),
                onDeletePizzaClicked = {
                    onDeletePizzaClicked(pizzaData.id)
                },
                isLoadingState = isLoadingState,
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                AsyncImage(
                    modifier = modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = pizzaData.name,
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Normal,
                    )
                    Spacer(modifier = modifier.height(12.dp))
                    Text(
                        text = pizzaData.price.toString(),
                        textAlign = TextAlign.Start,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Normal,
                    )
                }
            }
            Spacer(modifier = modifier.height(12.dp))
            Text(
                text = pizzaData.description,
                textAlign = TextAlign.End,
                fontSize = 20.sp,
                fontStyle = FontStyle.Normal,
            )
        }
    }
}

@Composable
private fun AddPizzaItemView(
    modifier: Modifier = Modifier,
    onAddPizzaItemClick: () -> Unit,
    isLoadingState: Boolean,
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = { onAddPizzaItemClick() },
        enabled = !isLoadingState,
    ) {
        Text(
            text = stringResource(R.string.add_pizza),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
        )
    }
}

@Composable
private fun PizzaSubmenuBox(
    modifier: Modifier = Modifier,
    onDeletePizzaClicked: () -> Unit,
    isLoadingState: Boolean,
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
                    onDeletePizzaClicked()
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Delete,
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
        repeat(3) {
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
private fun AllPizzaViewPreview() {
    AllPizzaView(
        pizzaDataList = PizzaDataListPreview
    )
}