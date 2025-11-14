package com.iliatokarev.pizzeriano161.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.CancelAcceptButtonView
import com.iliatokarev.pizzeriano161.domain.rejection.RejectionData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RejectionOrderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAcceptClicked: (reason: String) -> Unit,
    infoText: String,
    rejectionReasonsList: List<RejectionData>,
    onAddReason: (reason: String) -> Unit,
    onDeleteReasonById: (id: Int) -> Unit,
) {
    BasicAlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            RejectionDialogView(
                modifier = modifier,
                onDismissRequest = { onDismissRequest() },
                onAcceptClicked = { reason -> onAcceptClicked(reason) },
                infoText = infoText,
                rejectionReasonsList = rejectionReasonsList,
                onAddReason = { reason -> onAddReason(reason) },
                onDeleteReasonById = { id -> onDeleteReasonById(id) },
            )
        }
    }
}

@Composable
private fun RejectionDialogView(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAcceptClicked: (reason: String) -> Unit,
    infoText: String,
    rejectionReasonsList: List<RejectionData>,
    onAddReason: (reason: String) -> Unit,
    onDeleteReasonById: (id: Int) -> Unit,
) {
    var reasonText by rememberSaveable { mutableStateOf("") }
    var standardReasonText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Text(
                text = infoText,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
            Spacer(modifier = modifier.height(20.dp))
            OutlinedTextField(
                value = reasonText,
                onValueChange = { value ->
                    reasonText = value
                },
                label = { Text(text = stringResource(R.string.reason_of_rejection)) },
                supportingText = {
                    Text(text = stringResource(R.string.reason_of_rejection_supporting_text))
                },
                trailingIcon = {
                    IconButton(
                        onClick = { reasonText = "" },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear)
                        )
                    }
                }
            )
            Spacer(modifier = modifier.height(20.dp))
            CancelAcceptButtonView(
                onCancelClicked = { onDismissRequest() },
                onAcceptClicked = { onAcceptClicked(reasonText.trim()) },
            )
        }
        item {
            Spacer(modifier = modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = modifier.height(20.dp))
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.you_can_add_standard_rejection_reason),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
            Spacer(modifier = modifier.height(20.dp))
        }
        items(rejectionReasonsList.size) { index ->
            val reasonItem = rejectionReasonsList[index]
            ElevatedCard(
                onClick = { reasonText = reasonItem.reason },
                modifier = modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = modifier.fillMaxWidth().padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = modifier.weight(8F),
                        text = reasonItem.reason,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                    IconButton(
                        modifier = modifier.weight(2F),
                        onClick = { onDeleteReasonById(reasonItem.id) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.delete)
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(4.dp))
        }
        item {
            OutlinedTextField(
                value = standardReasonText,
                onValueChange = { value ->
                    standardReasonText = value
                },
                label = { Text(text = stringResource(R.string.standard_rejection_reason)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddReason(standardReasonText.trim())
                        standardReasonText = ""
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAddReason(standardReasonText.trim())
                            standardReasonText = ""
                            keyboardController?.hide()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = stringResource(R.string.add)
                        )
                    }
                },
            )
        }
    }
}
