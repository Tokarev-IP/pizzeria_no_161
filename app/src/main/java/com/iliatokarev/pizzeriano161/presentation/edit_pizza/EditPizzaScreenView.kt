package com.iliatokarev.pizzeriano161.presentation.edit_pizza

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.ActionErrorView
import com.iliatokarev.pizzeriano161.basic.shimmerBrush
import com.iliatokarev.pizzeriano161.domain.pizza.PizzaData
import com.iliatokarev.pizzeriano161.domain.pizza.pizzaDataPreviewEmptyPhoto

@Composable
fun EditPizzaScreenView(
    modifier: Modifier = Modifier,
    uiState: EditPizzaUiState,
    pizzaData: PizzaData?,
    onClearImageClicked: () -> Unit,
    onSelectedImageClicked: () -> Unit,
    onSavePizzaClicked: (name: String, price: Float, description: String) -> Unit,
    onTryAgainClicked: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.isInitialLoading)
            ShimmedView()
        else if (uiState.isDownloadError)
            ActionErrorView { onTryAgainClicked() }
        else {
            pizzaData?.let {
                AnimatedVisibility(uiState.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                EditPizzaView(
                    pizzaData = it,
                    onClearImageClicked = { onClearImageClicked() },
                    onSelectedImageClicked = { onSelectedImageClicked() },
                    onSavePizzaClicked = { name, price, description ->
                        onSavePizzaClicked(name, price, description)
                    },
                    isLoading = uiState.isLoading,
                    isSavedSuccessfully = uiState.isSaveSuccess,
                )
            } ?: run {
                ShimmedView()
            }
        }
    }
}

@Composable
private fun EditPizzaView(
    modifier: Modifier = Modifier,
    pizzaData: PizzaData,
    onClearImageClicked: () -> Unit = {},
    onSelectedImageClicked: () -> Unit = {},
    onSavePizzaClicked: (name: String, price: Float, description: String) -> Unit = { _, _, _ -> },
    isLoading: Boolean = false,
    isSavedSuccessfully: Boolean = false,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            ChooseImageView(
                pizzaData = pizzaData,
                onClearImageClicked = { onClearImageClicked() },
                onSelectImageClicked = { onSelectedImageClicked() },
                isLoading = isLoading,
            )
            Spacer(modifier = Modifier.height(20.dp))
            PizzaTextView(
                pizzaData = pizzaData,
                onSavePizzaClicked = { name, price, description ->
                    onSavePizzaClicked(name, price, description)
                },
                isLoading = isLoading,
                isSavedSuccessfully = isSavedSuccessfully,
            )
        }
    }
}

@Composable
private fun ChooseImageView(
    modifier: Modifier = Modifier,
    pizzaData: PizzaData,
    onClearImageClicked: () -> Unit,
    onSelectImageClicked: () -> Unit,
    isLoading: Boolean,
) {
    val pizzaImageUri = pizzaData.photoUriFirebase?.toUri() ?: pizzaData.photoUri

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        pizzaImageUri?.let { uri ->
            PizzaImageView(
                pizzaImageUri = uri,
                onClearImageClicked = { onClearImageClicked() },
                isLoading = isLoading,
            )
        } ?: run {
            OutlinedButton(
                onClick = { onSelectImageClicked() },
                modifier = modifier.align(Alignment.BottomCenter),
                enabled = !isLoading,
            ) {
                Text(text = stringResource(R.string.select_image))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PizzaImageView(
    modifier: Modifier = Modifier,
    pizzaImageUri: Uri,
    onClearImageClicked: () -> Unit,
    isLoading: Boolean,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AsyncImage(
            model = pizzaImageUri,
            contentDescription = null,
            modifier = modifier
                .height(180.dp)
                .width(240.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
        )
        IconButton(
            modifier = modifier.align(Alignment.TopEnd),
            onClick = { onClearImageClicked() },
            enabled = !isLoading,
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.clear_pizza_image_button),
            )
        }
    }
}

@Composable
private fun PizzaTextView(
    modifier: Modifier = Modifier,
    pizzaData: PizzaData,
    onSavePizzaClicked: (name: String, price: Float, description: String) -> Unit,
    isLoading: Boolean,
    isSavedSuccessfully: Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var isSaved by rememberSaveable { mutableStateOf(false) }

    var nameString by rememberSaveable { mutableStateOf(pizzaData.name) }
    var priceString by rememberSaveable { mutableStateOf(pizzaData.price.toString()) }
    var descriptionString by rememberSaveable { mutableStateOf(pizzaData.description) }

    var isNameStringError by rememberSaveable { mutableStateOf(false) }
    var isPriceStringError by rememberSaveable { mutableStateOf(false) }
    var isDescriptionStringError by rememberSaveable { mutableStateOf(false) }

    val pizzaImageUri = pizzaData.photoUriFirebase?.toUri() ?: pizzaData.photoUri
    var isPizzaImageError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pizzaData) {
        if (pizzaImageUri != null)
            isPizzaImageError = false
    }

    LaunchedEffect(isSavedSuccessfully) {
        isSaved = isSavedSuccessfully
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = isPizzaImageError,
        ) {
            Text(
                text = stringResource(R.string.no_image_error),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nameString,
            onValueChange = { textValue ->
                isNameStringError = false
                isSaved = false
                if (textValue.length < 100)
                    nameString = textValue
            },
            label = {
                Text(stringResource(R.string.name))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            isError = isNameStringError,
            enabled = !isLoading,
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = priceString,
            onValueChange = { textValue ->
                isPriceStringError = false
                isSaved = false
                if (textValue.length < 10)
                    priceString = textValue
            },
            label = {
                Text(stringResource(R.string.price))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            isError = isPriceStringError,
            enabled = !isLoading,
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = descriptionString,
            onValueChange = { textValue ->
                isDescriptionStringError = false
                isSaved = false
                if (textValue.length < 500)
                    descriptionString = textValue
            },
            label = {
                Text(stringResource(R.string.description))
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            isError = isDescriptionStringError,
            enabled = !isLoading,
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                if (nameString.isEmpty())
                    isNameStringError = true
                if (priceString.isEmpty() || priceString.toFloatOrNull() == null)
                    isPriceStringError = true
                if (descriptionString.isEmpty())
                    isDescriptionStringError = true
                if (pizzaImageUri == null)
                    isPizzaImageError = true

                if (!isNameStringError && !isPriceStringError && !isDescriptionStringError && !isPizzaImageError) {
                    onSavePizzaClicked(
                        nameString,
                        priceString.toFloatOrNull() ?: 0F,
                        descriptionString
                    )
                }
            },
            enabled = !isLoading && !isSaved,
        ) {
            AnimatedContent(
                targetState = isSaved,
            ) { isSavedState ->
                if (isSavedState)
                    Text(
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        text = stringResource(R.string.saved)
                    )
                else
                    Text(
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        text = stringResource(R.string.save)
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

@Composable
@Preview(showBackground = true)
private fun EditPizzaViewPreview() {
    EditPizzaView(
        pizzaData = pizzaDataPreviewEmptyPhoto
    )
}