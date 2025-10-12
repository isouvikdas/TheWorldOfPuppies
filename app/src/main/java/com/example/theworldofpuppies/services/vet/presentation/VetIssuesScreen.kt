package com.example.theworldofpuppies.services.vet.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.theworldofpuppies.R
import com.example.theworldofpuppies.core.presentation.animation.bounceClick
import com.example.theworldofpuppies.navigation.Screen
import com.example.theworldofpuppies.services.core.presentation.component.ServiceTopAppBar
import com.example.theworldofpuppies.services.vet.domain.HealthIssue
import com.example.theworldofpuppies.services.vet.domain.VetUiState
import com.example.theworldofpuppies.ui.theme.dimens
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetIssuesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    vetViewModel: VetViewModel,
    vetUiState: VetUiState
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    LaunchedEffect(Unit) {
        vetViewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val selectedHealthIssues = vetUiState.selectedHealthIssues

    val healthIssueDescription = vetUiState.healthIssueDescription

    val healthIssues = vetUiState.healthIssues

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(0.2f),
        topBar = {
            VetIssuesHeader(
                scrollBehavior = scrollBehavior,
                navController = navController
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Column {
                        Text(
                            "What issue is your pet facing?",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = MaterialTheme.dimens.small1)
                                .padding(top = 16.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Select the symptoms or concerns so our vets can understand your pet’s needs better",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.dimens.small1),
                            textAlign = TextAlign.Center
                        )

                    }
                }

                items(healthIssues) { healthIssue ->
                    val isSelected = selectedHealthIssues.contains(healthIssue)
                    PetIssuesCard(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                        healthIssue = healthIssue,
                        isSelected = isSelected,
                        onSelect = { healthIssue ->
                            vetViewModel.onHealthIssueSelect(healthIssue)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    IssueDescribeField(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                        value = healthIssueDescription,
                        onValueChange = { value ->
                            vetViewModel.onHealthIssueDescriptionChange(value)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.extraLarge1))
                }
            }

            VetIssuesBottomSection(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                vetViewModel = vetViewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun IssueDescribeField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {}
) {
    val maxLength = 100

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Surface(
            color = Color.LightGray.copy(0.55f)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                supportingText = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${value.length}/$maxLength",
                            color = if (value.length == maxLength)
                                MaterialTheme.colorScheme.tertiary
                            else
                                Color.Black,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        "Describe the issue",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray
                    )
                },
                textStyle = MaterialTheme.typography.titleSmall,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun PetIssuesCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    healthIssue: HealthIssue,
    onSelect: (HealthIssue) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = if (isSelected) 7.dp else 0.dp,
        border =
            if (isSelected)
                BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary)
            else
                BorderStroke(0.dp, Color.Transparent)

    ) {
        Surface(
            color = Color.LightGray.copy(0.55f),
            modifier = Modifier.clickable {
                onSelect(healthIssue)
            }

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small1, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .fillMaxHeight()
                        .padding(vertical = if (healthIssue.description.isNullOrEmpty()) 8.dp else 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        healthIssue.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!healthIssue.description.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            "(${healthIssue.description})",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start
                        )

                    }
                }

                Icon(
                    if (isSelected) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiaryContainer,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetIssuesHeader(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    ServiceTopAppBar(
        scrollBehavior = scrollBehavior,
        navController = navController,
        title = "Pet Health Issues"
    ) {
        Icon(
            painterResource(R.drawable.bag_outline),
            contentDescription = "Cart",
            modifier = Modifier
                .size(21.dp)
                .bounceClick {
                    navController.navigate(Screen.CartScreen.route)
                }
        )
    }
}

@Composable
fun VetIssuesBottomSection(
    modifier: Modifier = Modifier,
    vetViewModel: VetViewModel,
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
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            Button(
                onClick = {
                    vetViewModel.onSaveIssueSelect(navController)
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
                )
            ) {
                Text(
                    text = "Save Issue",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
        }
    }

}

