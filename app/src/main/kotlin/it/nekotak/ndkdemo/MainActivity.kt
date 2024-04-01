package it.nekotak.ndkdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.nekotak.ndkdemo.ui.theme.NdkdemoTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NdkdemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CameraControlScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun CameraControlScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    var greetingText by remember { mutableStateOf("*****") }

    // single shot launched effect
    LaunchedEffect(key1 = Unit) {
        greetingText = viewModel.helloFromCpp()
    }

    CameraControlScreenContent(
        greetingText,
        authenticate = { viewModel.authenticate("192.168.1.106") },
        onUiEvent = viewModel::onUiEvent,
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun CameraControlScreenContent(
    greetingText: String,
    authenticate: () -> Unit,
    onUiEvent: (MainViewModel.UiEvent) -> Unit,
    uiState: MainViewModel.UiState,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = greetingText,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(20.dp))
        AnimatedVisibility(
            visible = !uiState.isAuthenticated,
            enter = expandVertically(animationSpec = tween(durationMillis = 1000)),
            exit = shrinkVertically(animationSpec = tween(durationMillis = 1000))
        ) {
            Button(onClick = {
                authenticate()
            }) {
                Text(text = "Authenticate")
            }
        }
        AnimatedVisibility(
            visible = uiState.isAuthenticated,
            enter = expandVertically(animationSpec = tween(durationMillis = 1000)),
            exit = shrinkVertically(animationSpec = tween(durationMillis = 1000))
        ) {
            Row {
                Button(onClick = {
                    onUiEvent(MainViewModel.UiEvent.StartLivePreview)
                }) {
                    Text(text = "Start live preview")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    onUiEvent(MainViewModel.UiEvent.StopLivePreview)
                }) {
                    Text(text = "Stop live preview")
                }
            }
        }
    }
}
