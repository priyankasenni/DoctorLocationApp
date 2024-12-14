package com.example.doctorlocationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.doctorlocationapp.ui.theme.DoctorLocationAppTheme
import com.example.doctorlocationapp.ui.theme.screens.DoctorLocationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoctorLocationAppTheme {
                // The Surface is required for applying Material3 theme components.
                Surface {
                    DoctorLocationScreen()                }
            }
        }
    }
}
