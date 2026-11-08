package com.imtusharahmed.composeanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.imtusharahmed.composeanimation.ui.main.ComposeMotionLabApp
import com.imtusharahmed.composeanimation.ui.theme.ComposeAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAnimationTheme {
                ComposeMotionLabApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ComposeAnimationTheme {
        ComposeMotionLabApp()
    }
}
