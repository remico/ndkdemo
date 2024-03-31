package it.nekotak.ndkdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.nekotak.ndkdemo.ui.theme.NdkdemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jniWrapper = JNIWrapper()
        setContent {
            NdkdemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting(jniWrapper, jniWrapper.helloFromCpp())
                }
            }
        }
    }
}

@Composable
fun Greeting(jniWrapper: JNIWrapper, text: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Button(onClick = {
                jniWrapper.startLivePreview()
            }) {
                Text(text = "Start live preview")
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                jniWrapper.stopLivePreview()
            }) {
                Text(text = "Stop live preview")
            }
        }
    }
}
