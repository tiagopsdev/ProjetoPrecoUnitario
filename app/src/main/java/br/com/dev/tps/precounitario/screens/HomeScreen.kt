package br.com.dev.tps.precounitario.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.dev.tps.precounitario.R
import br.com.dev.tps.precounitario.components.CalculationItem
import br.com.dev.tps.precounitario.components.NormalTextComponent
import br.com.dev.tps.precounitario.data.HomeUiState
import br.com.dev.tps.precounitario.data.HomeViewModel
import br.com.dev.tps.precounitario.model.Calculation
import br.com.dev.tps.precounitario.navigation.PrecoUnitarioAppRouter
import br.com.dev.tps.precounitario.navigation.Screen
import br.com.dev.tps.precounitario.navigation.SystemBackButtonHandler
import br.com.dev.tps.precounitario.repository.Resources
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel?,
) {

    val homeUiState = homeViewModel?.homeUiState?.value ?: HomeUiState()

    val openDialog = remember { mutableStateOf(false) }
    val selectedCalculation = remember { mutableStateOf<Calculation?>(null) }

    //val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        homeViewModel?.loadCalculations()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                PrecoUnitarioAppRouter.navigatieTo(Screen.DetailScreen())
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {},
                actions = {
                    IconButton(onClick = {
                        homeViewModel?.signOut()
                        PrecoUnitarioAppRouter.navigatieTo(Screen.LoginScreen)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    NormalTextComponent(value = "Home")
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {
            when (homeUiState.calculationList) {
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resources.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        items(
                            homeUiState.calculationList.data ?: emptyList()
                        ) { calcItem ->
                            CalculationItem(
                                calculation = calcItem,
                                onLongClick = {
                                    openDialog.value = true
                                    selectedCalculation.value = calcItem
                                },
                            ) {
                                PrecoUnitarioAppRouter.navigatieTo(Screen.DetailScreen(calcItem.calcutationId))
                            }

                        }
                    }
                    AnimatedVisibility(visible = openDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialog.value = false
                            },
                            title = { Text(text = stringResource(R.string.delete_calculation)) },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        selectedCalculation.value?.calcutationId?.let {
                                            homeViewModel?.deleteNote(it)
                                        }
                                        openDialog.value = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    ),
                                ) {
                                    Text(text = "Delete")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { openDialog.value = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                        )
                    }
                }
                else -> {
                    Text(
                        text = homeUiState
                            .calculationList.throwable?.localizedMessage ?: "Unknown Error",
                        color = Color.Red
                    )
                }
            }
        }
    }
    SystemBackButtonHandler {
        homeViewModel?.signOut()
        PrecoUnitarioAppRouter.navigatieTo(Screen.LoginScreen)
    }
}

@Composable
@Preview
fun DefaultPreviewOfHomeScreen() {

    // Mock Calculation Data
    val mockCalculations = listOf(
        Calculation(
            userId = "user1",
            productName = "Product 1",
            quantity = 5.0,
            unitType = "kg",
            totalValue = 50.0,
            timestamp = Timestamp.now(),
            calcutationId = "calc1"
        ),
        Calculation(
            userId = "user2",
            productName = "Product 2",
            quantity = 3.0,
            unitType = "kg",
            totalValue = 30.0,
            timestamp = Timestamp.now(),
            calcutationId = "calc2"
        )
    )

    // Mock Resources Wrapper
    val mockResources = Resources.Success(mockCalculations)

    // Initialize homeUiState with mock values
    val homeUiState = HomeUiState(
        calculationList = mockResources,
        calculationDeletedStatus = false
    )

    val homeViewModel = HomeViewModel()
    homeViewModel.homeUiState.value = homeUiState

    HomeScreen(
        homeViewModel = homeViewModel
    )
}
