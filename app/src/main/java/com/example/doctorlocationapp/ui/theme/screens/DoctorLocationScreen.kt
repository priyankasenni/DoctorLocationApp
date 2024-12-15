package com.example.doctorlocationapp.ui.theme.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doctorlocationapp.R
import com.example.doctorlocationapp.viewmodel.DoctorLocationViewModel
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorLocationScreen(viewModel: DoctorLocationViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar with title
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.find_eye_doctor),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.green)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.light_gray)
            )
        )

        // Tabs
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf(
            stringResource(R.string.tab_by_location),
            stringResource(R.string.tab_by_doctor),
            stringResource(R.string.tab_online_lasik)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                color = when (index) {
                                    0 -> colorResource(id = R.color.location_color)
                                    1 -> colorResource(id = R.color.doctor_color)
                                    2 -> colorResource(id = R.color.eye_color)
                                    else -> Color.Unspecified
                                }
                            )
                        },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = when (index) {
                                        0 -> R.drawable.location_icon
                                        1 -> R.drawable.doctor_icon
                                        2 -> R.drawable.eye_icon
                                        else -> 0
                                    }
                                ),
                                contentDescription = when (index) {
                                    0 -> stringResource(R.string.location_icon)
                                    1 -> stringResource(R.string.doctor_icon)
                                    2 -> stringResource(R.string.online_icon)
                                    else -> null
                                },
                                tint = when (index) {
                                    0 -> colorResource(id = R.color.location_color)
                                    1 -> colorResource(id = R.color.doctor_color)
                                    2 -> colorResource(id = R.color.eye_color)
                                    else -> Color.Unspecified
                                }
                            )
                        }
                    )
                }
            }
            when (tabIndex) {
                0 -> ByLocationTab(viewModel)
                1 -> TabContent(stringResource(R.string.tab_by_doctor))
                2 -> TabContent(stringResource(R.string.tab_online_lasik))
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
    var locationText by remember { mutableStateOf("") }

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            useLocation(context) { location ->
                locationText = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
                Toast.makeText(context, locationText, Toast.LENGTH_SHORT).show()
            }
        } else {
            println("Permission denied")
        }
    }

    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        var network by remember { mutableStateOf("") }
        val networks = listOf(
            stringResource(R.string.access_network),
            stringResource(R.string.insight_network),
            stringResource(R.string.select_network),
            stringResource(R.string.dont_know)
        )

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
                label = { Text(stringResource(R.string.zip_code)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.searchByZip(zipCode) },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(4.dp),
            ) {
                Text(stringResource(R.string.search_by_zip))
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
            Icon(
                painterResource(id = R.drawable.right_pointer_icon),
                contentDescription = stringResource(R.string.location_icon),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search_by_county),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = { showAlertDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = { showAlertDialog = false },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
                    ) {
                        Text(stringResource(R.string.ok))
                    }
                },
                text = {
                    Text(stringResource(R.string.alert_dialog_message))
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
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location Button with Icon
        Button(
            onClick = {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            },
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.green)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.focus_icon),
                    contentDescription = stringResource(R.string.location_icon),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.use_my_location),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Response or Error Text
        if (response.isNotEmpty()) {
            Text(
                "${stringResource(R.string.response)} $response",
                modifier = Modifier.padding(top = 16.dp),
                color = Color.Green
            )
        }
        if (error.isNotEmpty()) {
            Text(
                "${stringResource(R.string.error)} $error",
                modifier = Modifier.padding(top = 16.dp),
                color = Color.Red
            )
        }
    }
}

@SuppressLint("MissingPermission")
fun useLocation(context: Context, onLocationFound: (Location) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let {
            onLocationFound(it)
        } ?: run {
            Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun TabContent(content: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = content)
    }
}

@Composable
fun DropdownMenuDemo(items: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            readOnly = true,
            label = { Text(stringResource(R.string.network)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        )

        // DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .background(colorResource(id = R.color.background_gray))
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = item
                        expanded = false
                        onSelect(item)
                    },
                    text = { Text(item) }
                )
            }
        }
    }
}
