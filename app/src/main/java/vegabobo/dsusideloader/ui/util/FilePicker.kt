package vegabobo.dsusideloader.ui.util

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

private const val TAG = "FilePicker"

private val SUPPORTED_MIME_TYPES = arrayOf(
    "application/gzip",
    "application/x-gzip",
    "application/x-xz",
    "application/zip",
    "application/octet-stream",
)

/**
 * Returns a function that opens the system file picker (Storage Access Framework).
 *
 * The picker is started directly with ACTION_OPEN_DOCUMENT (no Intent.createChooser wrapper),
 * and, if the system has no handler for it, falls back to ACTION_GET_CONTENT.
 * Failures are logged with the "FilePicker" tag, so they can be diagnosed with logcat.
 */
@Composable
fun rememberFilePicker(onFilePicked: (Uri) -> Unit): () -> Unit {
    val context = LocalContext.current
    val currentOnFilePicked by rememberUpdatedState(onFilePicked)

    val getContentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri ->
        Log.d(TAG, "GetContent result: $uri")
        if (uri != null) {
            currentOnFilePicked(uri)
        }
    }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        Log.d(TAG, "OpenDocument result: $uri")
        if (uri != null) {
            currentOnFilePicked(uri)
        }
    }

    return remember(context, openDocumentLauncher, getContentLauncher) {
        val openPicker: () -> Unit = {
            try {
                Log.d(TAG, "Launching ACTION_OPEN_DOCUMENT")
                openDocumentLauncher.launch(SUPPORTED_MIME_TYPES)
            } catch (e: Exception) {
                Log.e(TAG, "ACTION_OPEN_DOCUMENT failed, trying ACTION_GET_CONTENT", e)
                try {
                    getContentLauncher.launch("*/*")
                } catch (e2: Exception) {
                    Log.e(TAG, "ACTION_GET_CONTENT failed too", e2)
                    Toast.makeText(context, "Unable to open file picker", Toast.LENGTH_LONG).show()
                }
            }
        }
        openPicker
    }
}
