package com.example.doctorlocationapp.ui.theme.screens

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doctorlocationapp.R
import com.example.doctorlocationapp.viewmodel.DoctorLocationViewModel
import com.google.android.gms.location.LocationServices

@Composable
fun DoctorLocationScreen(viewModel: DoctorLocationViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Top Bar with title
        TopAppBar(
            title = {
                Text(
                    text = "Find an eye doctor",
                    fontSize = 20.sp
                )
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White
        )

        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("By location", "By doctor", "Online & Lasik")

        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        icon = {
                            when (index) {
                                0 -> Icon(
                                    painter = painterResource(id = R.drawable.location_icon),
                                    contentDescription = "Location Icon"
                                )
                                1 -> Icon(
                                    painter = painterResource(id = R.drawable.doctor_icon),
                                    contentDescription = "Doctor Icon"
                                )
                                2 -> Icon(
                                    painter = painterResource(id = R.drawable.eye_icon),
                                    contentDescription = "Online Icon"
                                )
                            }
                        }
                    )
                }
            }
            when (tabIndex) {
                0 -> ByLocationTab(viewModel)
                1 -> TabContent("By doctor")
                2 -> TabContent("Online & Lasik")
            }
        }
    }
}

@Composable
fun ByLocationTab(viewModel: DoctorLocationViewModel) {
    val context = LocalContext.current
    var zipCode by remember { mutableStateOf("") }
    val response by viewModel.response.collectAsState(initial = "")
    val error by viewModel.error.collectAsState(initial = "")
    var showAlertDialog by remember { mutableStateOf(false) }

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            useLocation(context)
        } else {
            println("Permission denied")
        }
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        var network by remember { mutableStateOf("") }
        val networks = listOf("Access Network", "Insight Network", "Select Network", "I don’t know")

        // Dropdown for Network
        DropdownMenuDemo(networks) { network = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Zip Code Input and Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = zipCode,
                onValueChange = {
                    if (it.all { char -> char.isDigit() } && it.length <= 5) {
                        zipCode = it
                    }
                },
                label = { Text("Zip Code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.searchByZip(zipCode) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(4.dp),
            ) {
                Text("SEARCH BY ZIP")
            }
        }

        // "Search by County" Text with Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                .clickable { showAlertDialog = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painterResource(id = R.drawable.right_pointer_icon), contentDescription = "Location Icon", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search by County",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = { showAlertDialog = false },
                confirmButton = {
                    TextButton(onClick = { showAlertDialog = false }) {
                        Text("OK")
                    }
                },
                text = {
                    Text("Search by County Tapped")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // OR Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location Button with Icon
        Button(
            onClick = {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                Toast.makeText(context, "Using your location", Toast.LENGTH_SHORT).show() // Show toast
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(painterResource(id = R.drawable.focus_icon), contentDescription = "Location Icon", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "USE MY LOCATION",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Response or Error Text
        if (response.isNotEmpty()) {
            Text("Response: $response", modifier = Modifier.padding(top = 16.dp), color = Color.Green)
        }
        if (error.isNotEmpty()) {
            Text("Error: $error", modifier = Modifier.padding(top = 16.dp), color = Color.Red)
        }
    }
}

@SuppressLint("MissingPermission")
fun useLocation(context: Context) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            println("Location: ${it.latitude}, ${it.longitude}")
        } ?: run {
            println("Location is null")
        }
    }.addOnFailureListener {
        println("Failed to get location: ${it.message}")
    }
}

@Composable
fun DropdownMenuDemo(items: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(items.first()) }

    Column {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text("Network") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    onSelect(item)
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
    }
}

@Composable
fun TabContent(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message)
    }
}
