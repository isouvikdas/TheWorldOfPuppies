package com.example.theworldofpuppies.profile.pet.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.address.presentation.component.TopAppBar
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.profile.pet.domain.PetUiState
import com.example.theworldofpuppies.profile.pet.domain.enums.Aggression
import com.example.theworldofpuppies.profile.pet.domain.enums.DogBreed
import com.example.theworldofpuppies.profile.pet.domain.enums.Gender
import com.example.theworldofpuppies.profile.pet.presentation.utils.toString
import com.example.theworldofpuppies.ui.theme.dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    petProfileViewModel: PetProfileViewModel,
    petUiState: PetUiState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    // collect edit state instead of many changedX
    val editState by petProfileViewModel.editState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val petPictureUri = editState.petPictureUri ?: petUiState.petPicture
    val name = editState.name ?: petUiState.name
    val breed = editState.breed ?: petUiState.breed
    val age = editState.age ?: petUiState.age
    val gender = editState.gender ?: petUiState.gender
    val aggression = editState.aggression ?: petUiState.aggression
    val isVaccinated = editState.isVaccinated ?: petUiState.isVaccinated
    val weight = editState.weight ?: petUiState.weight

    val breedError = petUiState.breedError
    val nameError = petUiState.nameError
    val ageError = petUiState.ageError
    val genderError = petUiState.genderError
    val aggressionError = petUiState.aggressionError
    val petPictureError = petUiState.petPictureError
    val weightError = petUiState.weightError

    val isRefreshing = petUiState.isLoading
    val pullToRefreshState = rememberPullToRefreshState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { petProfileViewModel.onPetPictureChange(it) }
    )

    val genders = Gender.entries
    val aggressions = Aggression.entries

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            PetProfileHeader(
                scrollBehavior = scrollBehavior,
                navController = navController
            )
        }
    ) {
        BreedSheet(petProfileViewModel = petProfileViewModel)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = { petProfileViewModel.loadPetProfile(forceRefresh = true) },
                        state = pullToRefreshState
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = MaterialTheme.dimens.small1),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Profile picture
                            item {
                                ProfileCircle(
                                    onClick = {
                                        imagePicker.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    selectedImageUri = petPictureUri,
                                    errorImage = R.drawable.pet_profile,
                                    errorMessage = petPictureError
                                )
                            }

                            // Name
                            item {
                                ProfileScreenField(
                                    heading = "Name",
                                    value = name,
                                    onValueChange = { petProfileViewModel.onNameChange(it) },
                                    errorMessage = nameError
                                )
                            }

                            // Gender
                            item {
                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        "Gender",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        genders.forEach { g ->
                                            val isSelected = g == gender
                                            ProfileFilterItem<Gender>(
                                                type = g.toString(context),
                                                isSelected = isSelected,
                                                onClick = { petProfileViewModel.onGenderChange(g) }
                                            )
                                        }
                                    }
                                    genderError?.let { error ->
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.W500
                                        )
                                    }
                                }
                            }

                            // Breed
                            item {
                                PetBreedField(
                                    heading = "Breed",
                                    value = breed?.breedName.orEmpty(),
                                    readOnly = true,
                                    onToggleClick = { petProfileViewModel.toggleModalBottomSheet() },
                                    errorMessage = breedError
                                )
                            }

                            // Age + Weight
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(0.5f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ProfileScreenField(
                                            modifier = Modifier.padding(end = 2.dp),
                                            heading = "Age",
                                            value = age,
                                            onValueChange = { petProfileViewModel.onAgeChange(it) },
                                            placeHolder = "Age in months",
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            errorMessage = ageError
                                        )
                                    }
                                    ProfileScreenField(
                                        modifier = Modifier.padding(start = 2.dp),
                                        heading = "Weight",
                                        value = weight,
                                        onValueChange = { petProfileViewModel.onWeightChange(it) },
                                        placeHolder = "Weight in kg",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        errorMessage = weightError
                                    )
                                }
                            }

                            // Aggression
                            item {
                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        "Aggression",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        aggressions.forEach { a ->
                                            val isSelected = a == aggression
                                            ProfileFilterItem<Aggression>(
                                                type = a.toString(context),
                                                isSelected = isSelected,
                                                onClick = { petProfileViewModel.onAggressionChange(a) }
                                            )
                                        }
                                    }
                                    aggressionError?.let { error ->
                                        Text(
                                            text = error,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.W500
                                        )
                                    }
                                }
                            }

                            // Vaccination
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Is Vaccinated",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    VaccinationSwitch(
                                        checked = isVaccinated,
                                        onClick = { petProfileViewModel.onVaccinatedChange(it) }
                                    )
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.dimens.large3))
                            }
                        }

                    }

                    PetProfileBottomSection(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        petProfileViewModel = petProfileViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun VaccinationSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onClick: (Boolean) -> Unit = { Boolean }
) {
    Switch(
        checked = checked,
        onCheckedChange = onClick,
        thumbContent = {
            if (checked) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.tertiary,
            checkedTrackColor = MaterialTheme.colorScheme.tertiary.copy(
                0.3f
            ),
            uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
            uncheckedTrackColor = MaterialTheme.colorScheme.onTertiary,
        )
    )

}

@Composable
fun ProfileCircle(
    modifier: Modifier = Modifier,
    radius: Dp = 60.dp,
    onClick: () -> Unit,
    selectedImageUri: Uri,
    errorImage: Int,
    errorMessage: String? = null
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(radius.times(2f))
                .background(Color.Transparent),
        ) {
            if (selectedImageUri != Uri.EMPTY) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Pet Profile Pic",
                    modifier = modifier.clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painterResource(errorImage),
                    contentDescription = "Pet profile pic",
                    modifier = Modifier.clip(CircleShape)
                )
            }
            Surface(
                modifier = Modifier
                    .size(radius.times(0.4f))
                    .offset(
                        x = radius.times(1.5f),
                        y = radius.times(1.5f)
                    ),
                shape = CircleShape,
                color = Color.White
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                        0.2f
                    ),
                    modifier = Modifier.clickable {
                        onClick()
                    }
                ) {
                    Icon(
                        Icons.Outlined.Camera,
                        contentDescription = null,
                        modifier = Modifier.padding(1.dp)
                    )
                }
            }
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W500
            )

        }

    }

}


@Composable
fun <T> ProfileFilterItem(
    modifier: Modifier = Modifier,
    type: String,
    isSelected: Boolean = false,
    onClick: () -> Unit

) {
    FilterChip(
        onClick = {
            onClick()
        },
        label = {
            Text(
                type,
                style = MaterialTheme.typography.titleMedium
            )
        },
        selected = isSelected,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(end = MaterialTheme.dimens.small1.div(2)),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(
                0.8f
            ),
            selectedLabelColor = Color.White
        ),
        border = BorderStroke(0.5.dp, color = Color.Gray)
    )
}

@Composable
fun PetBreedField(
    modifier: Modifier = Modifier,
    heading: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    isOptional: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit = { "" },
    errorMessage: String? = null,
    placeHolder: String = "",
    onToggleClick: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            if (isOptional) {
                Text(
                    text = "(Optional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            isError = errorMessage != null,
            supportingText = {
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.W500
                    )
                }
            },
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.LightGray.copy(0.3f),
                unfocusedContainerColor = Color.LightGray.copy(0.3f),
                errorContainerColor = Color.LightGray.copy(0.3f)
            ),
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.titleSmall,
            placeholder = {
                if (placeHolder.isNotEmpty())
                    Text(
                        text = placeHolder,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                else Text(
                    text = "Enter $heading",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onToggleClick()
                    }
                ) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )

                }
            }
        )

    }
}


@Composable
fun ProfileScreenField(
    modifier: Modifier = Modifier,
    heading: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    defaultValue: String = "",
    isOptional: Boolean = false,
    value: String = "",
    onValueChange: (String) -> Unit = { "" },
    errorMessage: String? = null,
    placeHolder: String = ""
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = heading,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            if (isOptional) {
                Text(
                    text = "(Optional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            isError = errorMessage != null,
            supportingText = {
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.W500
                    )
                }
            },
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.LightGray.copy(0.3f),
                unfocusedContainerColor = Color.LightGray.copy(0.3f),
                errorContainerColor = Color.LightGray.copy(0.3f)
            ),
            maxLines = maxLines,
            textStyle = MaterialTheme.typography.titleSmall,
            placeholder = {
                if (readOnly) Text(
                    text = defaultValue,
                    style = MaterialTheme.typography.titleSmall
                )
                else if (placeHolder.isNotEmpty())
                    Text(
                        text = placeHolder,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                else Text(
                    text = "Enter $heading",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            }
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetProfileHeader(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
//used the custom topappbar from address.presentation.component
    TopAppBar(
        navController = navController,
        scrollBehavior = scrollBehavior,
        title = "Pet Profile"
    ) {

        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "Profile",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
}

@Composable
fun PetProfileBottomSection(
    modifier: Modifier = Modifier,
    petProfileViewModel: PetProfileViewModel
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
                    petProfileViewModel.saveProfile()
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
                    text = "Save",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedSheet(modifier: Modifier = Modifier, petProfileViewModel: PetProfileViewModel) {

    val searchUiState by petProfileViewModel.searchUiState.collectAsStateWithLifecycle()
    val searchQuery = searchUiState.query
    val breedList by remember(searchUiState.results) {
        derivedStateOf {
            searchUiState.results.sortedBy { it.name.lowercase() }
        }
    }

    var showModalBottomSheet by petProfileViewModel.showModalSheet
    var skipPartially by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartially)

    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                petProfileViewModel.toggleModalBottomSheet()
            },
            sheetState = bottomSheetState,
            containerColor = Color.White
        ) {
            BreedSearchingScreen(
                searchQuery = searchQuery,
                onSearchTextChange = { petProfileViewModel.onSearchTextChange(it) },
                breedList = breedList,
                onBreedSelect = { petProfileViewModel.selectBreed(it) }
            )
        }
    }

}

@Composable
fun BreedSearchingScreen(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchTextChange: (String) -> Unit = { "" },
    breedList: List<DogBreed>?,
    onBreedSelect: (DogBreed) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimens.small1),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = onSearchTextChange,
                singleLine = true,
                placeholder = {
                    Text(
                        "Search Breed",
                        color = Color.Black.copy(0.9f)
                    )
                },
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.search_dens),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(21.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchTextChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(MaterialTheme.dimens.small2),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.LightGray.copy(0.4f),
                    unfocusedContainerColor = Color.LightGray.copy(0.4f)
                )
            )

            BreedSearchView(breedList = breedList, onBreedSelect = { onBreedSelect(it) })

        }

    }
}

@Composable
fun BreedSearchView(
    modifier: Modifier = Modifier,
    breedList: List<DogBreed>?,
    onBreedSelect: (DogBreed) -> Unit = {}
) {
    if (!breedList.isNullOrEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(breedList) { breed ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)

                ) {
                    Text(
                        breed.breedName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onBreedSelect(breed)
                            }
                    )
                    if (breed != breedList.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            thickness = 0.2.dp
                        )
                    }

                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "\uD83D\uDE3F",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                "Oops! No Matching result found",
                style = MaterialTheme.typography.bodyMedium,

                )
            Text(
                "Please try a different search query :)",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}



