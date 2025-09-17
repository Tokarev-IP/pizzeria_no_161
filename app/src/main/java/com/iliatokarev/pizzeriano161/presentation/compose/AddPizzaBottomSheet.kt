package com.iliatokarev.pizzeriano161.presentation.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import com.iliatokarev.pizzeriano161.domain.pizza.pizzaDataListPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPizzaBottomSheet(
    modifier: Modifier = Modifier,
    pizzaDataList: List<PizzaData>,
    onChoosePizza: (pizzaData: PizzaData) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
    ) {
        AddPizzaBottomView(
            pizzaDataList = pizzaDataList,
            onChoosePizza = { pizzaData ->
                onChoosePizza(pizzaData)
            },
        )
    }
}

@Composable
private fun AddPizzaBottomView(
    modifier: Modifier = Modifier,
    pizzaDataList: List<PizzaData>,
    onChoosePizza: (pizzaData: PizzaData) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        items(pizzaDataList.size) { index ->
            PizzaItemView(
                pizzaData = pizzaDataList[index],
                onChoosePizza = { pizzaData ->
                    onChoosePizza(pizzaData)
                },
            )
        }
    }
}

@Composable
private fun PizzaItemView(
    modifier: Modifier = Modifier,
    pizzaData: PizzaData,
    onChoosePizza: (pizzaData: PizzaData) -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { onChoosePizza(pizzaData) }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = pizzaData.name,
                modifier = modifier.weight(3f),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
            Text(
                textAlign = TextAlign.End,
                text = pizzaData.price.toString() + " ₽",
                modifier = modifier.weight(1f),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddPizzaBottomViewPreview() {
    AddPizzaBottomView(
        pizzaDataList = pizzaDataListPreview,
    )
}