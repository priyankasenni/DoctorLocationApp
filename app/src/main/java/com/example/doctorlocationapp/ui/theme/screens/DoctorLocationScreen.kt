package com.example.doctorlocationapp.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.doctorlocationapp.R
import com.example.doctorlocationapp.viewmodel.DoctorLocationViewModel

@Composable
fun DoctorLocationScreen(viewModel: DoctorLocationViewModel = viewModel()) {
    val selectedTab = remember { mutableStateOf(0) }
    val tabs = listOf("By location", "By doctor", "Online & Lasik")

    Column(modifier = Modifier.fillMaxSize()) {

        // Tabs Section
        TabRow(selectedTabIndex = selectedTab.value, backgroundColor = Color.White) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = { selectedTab.value = index },
                    text = {
                        Text(
                            title,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        when (selectedTab.value) {
            0 -> ByLocationTab(viewModel)
            1 -> TabContent("By doctor")
            2 -> TabContent("Online & Lasik")
        }
    }
}

@Composable
fun ByLocationTab(viewModel: DoctorLocationViewModel) {
    var zipCode by remember { mutableStateOf("") }
    val response by viewModel.response.collectAsState(initial = "")
    val error by viewModel.error.collectAsState(initial = "")

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
                .height(IntrinsicSize.Min), // Ensures both elements have the same height
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = zipCode,
                onValueChange = { zipCode = it },
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
                    .fillMaxHeight().padding(4.dp)
            ) {
                Text("SEARCH BY ZIP", color = Color.White)
            }
        }

        // "Search by County" Text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                text = "Search by County",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // OR Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.Gray
            )
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp, color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location Button
        Button(
            onClick = { viewModel.useLocation() },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "USE MY LOCATION",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Response or Error Text
        if (response.isNotEmpty()) {
            Text("Response: $response", color = Color.Green, modifier = Modifier.padding(top = 16.dp))
        }
        if (error.isNotEmpty()) {
            Text("Error: $error", color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        }
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
