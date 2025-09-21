package com.iliatokarev.pizzeriano161.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iliatokarev.pizzeriano161.R
import com.iliatokarev.pizzeriano161.basic.CancelAcceptButtonView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RejectionOrderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onAcceptClicked: (reason: String) -> Unit,
    infoText: String,
) {
    var textState by rememberSaveable { mutableStateOf("") }

    BasicAlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = { onDismissRequest() },
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = infoText,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                )
                Spacer(modifier = modifier.height(20.dp))
                OutlinedTextField(
                    value = textState,
                    onValueChange = { value ->
                        textState = value
                    },
                    label = { Text(text = stringResource(R.string.reason_of_rejection)) },
                    supportingText = {
                        Text(text = stringResource(R.string.reason_of_rejection_supporting_text))
                    }
                )
                Spacer(modifier = modifier.height(20.dp))
                CancelAcceptButtonView(
                    onCancelClicked = { onDismissRequest() },
                    onAcceptClicked = { onAcceptClicked(textState) },
                )
            }
        }
    }
}
