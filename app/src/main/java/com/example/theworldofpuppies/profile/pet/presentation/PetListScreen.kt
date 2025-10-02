package com.example.theworldofpuppies.profile.pet.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.domain.PetListUiState
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petListUiState: PetListUiState,
    petProfileViewModel: PetProfileViewModel,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    val isPetSelectionView by petProfileViewModel.isPetSelectionView.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        petProfileViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val pets = petListUiState.pets

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
            topBar = {
                PetListHeader(scrollBehavior = scrollBehavior, navController = navController)
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
                ) {
                    item {
                        Button(
                            onClick = {
                                petProfileViewModel.changeEditingState(false)
                                navController.navigate(Screen.PetProfileScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.buttonHeight)
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = MaterialTheme.colorScheme.secondary,
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                        ) {
                            Text(
                                text = "Add New Pet",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    items(pets) { pet ->
                        val selectedPet by petProfileViewModel.selectedPetForService.collectAsStateWithLifecycle()
                        val isSelected =
                            pet.id == selectedPet?.id
                        PetProfileCard(
                            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                            elevation = if (isPetSelectionView && isSelected) 8.dp else 0.dp,
                            isPetListView = true,
                            isPetSelectionView = isPetSelectionView,
                            isHomeScreenView = false,
                            pet = pet,
                            selectPet = { pet ->
                                    petProfileViewModel.fillExistingPetData(pet)
                                    navController.navigate(Screen.PetProfileScreen.route)
                            },
                            onDeleteClick = { pet ->
                                petProfileViewModel.deletePet(pet.id, context)
                            },
                            isSelected = isSelected,
                            selectPetForService = { pet ->
                                petProfileViewModel.onPetSelection(pet)
                            }

                        )
                    }
                }

                if (isPetSelectionView) {
                    PetListBottomSection(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        petProfileViewModel = petProfileViewModel,
                        context = context,
                        navController = navController
                    )
                }

            }
        }
    }
}

@Composable
fun PetListBottomSection(
    modifier: Modifier = Modifier,
    petProfileViewModel: PetProfileViewModel,
    context: Context,
    navController: NavController
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zIndex(1f),
        color = Color.White,
        shape = RoundedCornerShape(
            topStart = MaterialTheme.dimens.small3,
            topEnd = MaterialTheme.dimens.small3
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.LightGray.copy(0.55f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
            Button(
                onClick = {
                    petProfileViewModel.onBookNowClick(navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.dimens.buttonHeight)
                    .padding(horizontal = MaterialTheme.dimens.small1),
                shape = RoundedCornerShape(MaterialTheme.dimens.small1),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
            ) {
                Text(
                    text = "Book Now",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetListHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController,
) {
    //used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Your Pets"
    ) {
    }
}