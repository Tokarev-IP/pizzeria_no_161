package com.iliatokarev.pizzeriano161.presentation.edit_pizza

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.BasicTopAppBar
import com.iliatokarev.pizzeriano161.presentation.main.MainUiEvent
import com.iliatokarev.pizzeriano161.presentation.main.MainViewModel

@Composable
fun EditPizzaScreen(
    modifier: Modifier = Modifier,
    editPizzaViewModel: EditPizzaViewModel,
    mainViewModel: MainViewModel,
    pizzaId: String?,
) {
    val uiState by editPizzaViewModel.getUiState().collectAsStateWithLifecycle()
    val pizzaData by editPizzaViewModel.getPizzaDataFlow().collectAsStateWithLifecycle()

    val choosePhotoLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { resultUri ->
        editPizzaViewModel.setEvent(EditPizzaUiEvent.SetImageUri(resultUri))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BasicTopAppBar(
                title = stringResource(R.string.edit_pizza)
            ) {
                mainViewModel.setEvent(MainUiEvent.GoBack)
            }
        }
    ) { paddingValues ->

        EditPizzaScreenView(
            modifier = modifier.padding(paddingValues),
            uiState = uiState,
            pizzaData = pizzaData,
            onClearImageClicked = {
                editPizzaViewModel.setEvent(EditPizzaUiEvent.ClearImageUri)
            },
            onSelectedImageClicked = {
                choosePhotoLaunch.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                    )
                )
            },
            onSavePizzaClicked = { name, price, description ->
                editPizzaViewModel.setEvent(
                    EditPizzaUiEvent.SavePizzaData(
                        ShortPizzaData(
                            name = name,
                            price = price,
                            description = description
                        )
                    )
                )
            },
            onTryAgainClicked = {
                editPizzaViewModel.setEvent(EditPizzaUiEvent.DownloadEditPizza(pizzaId))
            }
        )
    }
}