package vegabobo.dsusideloader.ui.cards.installation

import android.net.Uri
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import vegabobo.dsusideloader.R
import vegabobo.dsusideloader.ui.components.CardBox
import vegabobo.dsusideloader.ui.screen.home.InstallationCardState
import vegabobo.dsusideloader.ui.util.rememberFilePicker

@Composable
fun InstallationCard(
    uiState: InstallationCardState,
    modifier: Modifier = Modifier,
    minPercentageOfFreeStorage: String,
    onClickClear: () -> Unit,
    onClickInstall: () -> Unit,
    onClickRetryInstallation: () -> Unit,
    onClickUnmountSdCardAndRetry: () -> Unit,
    onClickSetSeLinuxPermissive: () -> Unit,
    onClickCancelInstallation: () -> Unit,
    onClickDiscardInstalledGsiAndInstall: () -> Unit,
    onClickDiscardDsu: () -> Unit,
    onClickRebootToDynOS: () -> Unit,
    onSelectFileSuccess: (Uri) -> Unit,
    onClickViewLogs: () -> Unit,
    onClickViewCommands: () -> Unit,
) {
    val openFilePicker = rememberFilePicker { onSelectFileSuccess(it) }
    val currentOpenFilePicker by rememberUpdatedState(openFilePicker)

    val textFieldInteraction = remember { MutableInteractionSource() }

    // Open the picker once per press, from a side-effect (never from composition itself).
    LaunchedEffect(textFieldInteraction) {
        textFieldInteraction.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                currentOpenFilePicker()
            }
        }
    }

    CardBox(
        cardTitle = stringResource(R.string.installation),
        addToggle = false,
        modifier = modifier,
    ) {
        InstallationCardStep(
            uiState = uiState,
            textFieldInteraction = textFieldInteraction,
            onClickClear = onClickClear,
            onClickInstall = onClickInstall,
            onClickRetryInstallation = onClickRetryInstallation,
            onClickUnmountSdCardAndRetry = onClickUnmountSdCardAndRetry,
            onClickSetSeLinuxPermissive = onClickSetSeLinuxPermissive,
            onClickCancelInstallation = onClickCancelInstallation,
            onClickDiscardInstalledGsiAndInstall = onClickDiscardInstalledGsiAndInstall,
            onClickDiscardDsu = onClickDiscardDsu,
            onClickRebootToDynOS = onClickRebootToDynOS,
            onClickViewLogs = onClickViewLogs,
            onClickViewCommands = onClickViewCommands,
            minPercentageOfFreeStorage = minPercentageOfFreeStorage,
        )
    }
}
