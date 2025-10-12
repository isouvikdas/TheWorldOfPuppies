package com.example.theworldofpuppies.navigation

import android.util.Log
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.theworldofpuppies.address.presentation.AddressDetailScreen
import com.example.theworldofpuppies.address.presentation.AddressScreen
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.auth.presentation.WelcomeScreen
import com.example.theworldofpuppies.auth.presentation.login.LoginScreen
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.register.RegisterScreen
import com.example.theworldofpuppies.auth.presentation.register.RegistrationViewModel
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.booking.dog_training.presentation.DogTrainingBookingScreen
import com.example.theworldofpuppies.booking.dog_training.presentation.DogTrainingBookingViewModel
import com.example.theworldofpuppies.booking.grooming.presentation.BookingGroomingScreen
import com.example.theworldofpuppies.booking.grooming.presentation.GroomingBookingViewModel
import com.example.theworldofpuppies.booking.pet_walk.presentation.BookingPetWalkScreen
import com.example.theworldofpuppies.booking.pet_walk.presentation.BookingPetWalkViewModel
import com.example.theworldofpuppies.booking.vet.presentation.VetBookingScreen
import com.example.theworldofpuppies.booking.vet.presentation.VetBookingViewModel
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.core.presentation.nav_items.bottomNav.BottomNavigationItems
import com.example.theworldofpuppies.home.presentation.HomeScreen
import com.example.theworldofpuppies.messages.presentation.MessageScreen
import com.example.theworldofpuppies.pet_insurance.presentation.PetInsuranceScreen
import com.example.theworldofpuppies.pet_insurance.presentation.PetInsuranceViewModel
import com.example.theworldofpuppies.profile.pet.presentation.PetListScreen
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileScreen
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileViewModel
import com.example.theworldofpuppies.profile.presentation.ProfileScreen
import com.example.theworldofpuppies.profile.presentation.ProfileViewModel
import com.example.theworldofpuppies.review.presentation.ReviewScreen
import com.example.theworldofpuppies.review.presentation.ReviewViewModel
import com.example.theworldofpuppies.services.dog_training.presentation.DogTrainingScreen
import com.example.theworldofpuppies.services.dog_training.presentation.DogTrainingViewModel
import com.example.theworldofpuppies.services.grooming.presentation.GroomingScreen
import com.example.theworldofpuppies.services.grooming.presentation.GroomingViewModel
import com.example.theworldofpuppies.booking.history.presentation.BookingHistoryScreen
import com.example.theworldofpuppies.booking.history.presentation.BookingHistoryViewModel
import com.example.theworldofpuppies.profile.user.presentation.UpdateUserScreen
import com.example.theworldofpuppies.profile.user.presentation.UpdateUserViewModel
import com.example.theworldofpuppies.review.presentation.utils.ReviewEventManager
import com.example.theworldofpuppies.services.pet_walking.presentation.PetWalkingScreen
import com.example.theworldofpuppies.services.pet_walking.presentation.PetWalkingViewModel
import com.example.theworldofpuppies.services.vet.presentation.VetIssuesScreen
import com.example.theworldofpuppies.services.vet.presentation.VetScreen
import com.example.theworldofpuppies.services.vet.presentation.VetViewModel
import com.example.theworldofpuppies.shop.cart.presentation.CartScreen
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.order.presentation.CheckoutScreen
import com.example.theworldofpuppies.shop.order.presentation.OrderViewModel
import com.example.theworldofpuppies.shop.order.presentation.order_history.OrderHistoryScreen
import com.example.theworldofpuppies.shop.product.presentation.SearchScreen
import com.example.theworldofpuppies.shop.product.presentation.ShopHomeScreen
import com.example.theworldofpuppies.shop.product.presentation.product_detail.ProductDetailScreen
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductListScreen
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    data object RegistrationScreen : Screen("RegistrationScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object WelcomeScreen : Screen("WelcomeScreen")
    data object ProductDetailScreen : Screen("ProductDetailScreen")
    data object ProductListScreen : Screen("ProductListScreen")
    data object CartScreen : Screen("CartScreen")
    data object SearchScreen : Screen("SearchScreen")
    data object CheckoutScreen : Screen("CheckoutScreen")
    data object AddressScreen : Screen("AddressScreen")
    data object AddressDetailScreen : Screen("AddressDetailScreen")
    data object OrderHistoryScreen : Screen("OrderHistoryScreen")
    data object PetProfileScreen : Screen("PetProfileScreen")
    data object PetListScreen : Screen("PetListScreen")
    data object BookingGroomingScreen : Screen("BookingGroomingScreen")
    data object BookingPetWalkScreen : Screen("BookingPetWalkScreen")
    data object GroomingScreen : Screen("GroomingScreen")
    data object PetWalkingScreen : Screen("PetWalkingScreen")
    data object VetScreen : Screen("VetScreen")
    data object VetIssuesScreen : Screen("VetIssuesScreen")
    data object VetBookingScreen : Screen("VetBookingScreen")
    data object DogTrainingScreen : Screen("DogTrainingScreen")
    data object DogTrainingBookingScreen : Screen("DogTrainingBookingScreen")
    data object PetInsuranceScreen : Screen("PetInsuranceScreen")
    data object ReviewScreen : Screen("ReviewScreen")
    data object UpdateUserScreen: Screen("UpdateUserScreen")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit,
    onProfileButtonVisibilityChanged: (Boolean) -> Unit,
    onSignOutDialogVisibilityChanged: (Boolean) -> Unit,
    onTopBarVisibilityChanged: (Boolean) -> Unit,
    authViewModel: AuthViewModel,
    isLoggedIn: Boolean,
    onGesturesChanged: (Boolean) -> Unit,
    searchIconVisibilityChanged: (Boolean) -> Unit,
    orderViewModel: OrderViewModel
) {

    val registrationViewModel = koinViewModel<RegistrationViewModel>()
    val registrationUiState by registrationViewModel.registrationUiState.collectAsStateWithLifecycle()

    val loginViewModel = koinViewModel<LoginViewModel>()
    val loginUiState by loginViewModel.loginUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        registrationViewModel.resetState()
        loginViewModel.resetState()
    }

    val cartViewModel = koinViewModel<CartViewModel>()
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()

    val startingRoute = when {
        isLoggedIn -> BottomNavigationItems.Home.route
        else -> Screen.WelcomeScreen.route
    }

    val productViewModel = koinViewModel<ProductViewModel>()
    val productListState by productViewModel.productListState.collectAsStateWithLifecycle()
    val categoryListState by productViewModel.categoryListState.collectAsStateWithLifecycle()
    val featuredProductListState by productViewModel.featuredProductListState.collectAsStateWithLifecycle()
    val productDetailState by productViewModel.productDetailState.collectAsStateWithLifecycle()

    val profileViewModel = koinViewModel<ProfileViewModel>()
    val addressViewModel = koinViewModel<AddressViewModel>()
    val addressUiState by addressViewModel.addressUiState.collectAsStateWithLifecycle()
    val addressDetailUiState by addressViewModel.addressDetailUiState.collectAsStateWithLifecycle()

    val orderUiState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
    val orderHistoryUiState by orderViewModel.orderHistoryUiState.collectAsStateWithLifecycle()

    val groomingViewModel = koinViewModel<GroomingViewModel>()
    val groomingUiState by groomingViewModel.groomingUiState.collectAsStateWithLifecycle()

    val petWalkingViewModel = koinViewModel<PetWalkingViewModel>()
    val petWalkingUiState by petWalkingViewModel.petWalkingUiState.collectAsStateWithLifecycle()

    val petProfileViewModel = koinViewModel<PetProfileViewModel>()
    val petProfileUiState by petProfileViewModel.editState.collectAsStateWithLifecycle()
    val petListUiState by petProfileViewModel.petListUiState.collectAsStateWithLifecycle()
    val selectedPetForService by petProfileViewModel.selectedPetForService.collectAsStateWithLifecycle()

    val groomingBookingViewModel = koinViewModel<GroomingBookingViewModel>()

    val bookingPetWalkViewModel = koinViewModel<BookingPetWalkViewModel>()

    val vetViewModel = koinViewModel<VetViewModel>()
    val vetUiState by vetViewModel.vetUiState.collectAsStateWithLifecycle()

    val vetBookingViewModel = koinViewModel<VetBookingViewModel>()

    val dogTrainingViewModel = koinViewModel<DogTrainingViewModel>()
    val dogTrainingUiState by dogTrainingViewModel.dogTrainingUiState.collectAsStateWithLifecycle()

    val dogTrainingBookingViewModel = koinViewModel<DogTrainingBookingViewModel>()
    val dogTrainingBookingUiState by dogTrainingBookingViewModel.dogTrainingBookingUiState.collectAsStateWithLifecycle()

    val petInsuranceViewModel = koinViewModel<PetInsuranceViewModel>()
    val petInsuranceUiState by petInsuranceViewModel.petInsuranceUiState.collectAsStateWithLifecycle()
    val petInsuranceBookingUiState by petInsuranceViewModel.petInsuranceBookingUiState.collectAsStateWithLifecycle()

    val reviewViewModel = koinViewModel<ReviewViewModel>()
    val reviewUiState by reviewViewModel.reviewUiState.collectAsStateWithLifecycle()
    val reviewListState by reviewViewModel.reviewListState.collectAsStateWithLifecycle()

    val bookingHistoryViewModel = koinViewModel<BookingHistoryViewModel>()
    val bookingHistoryUiState by bookingHistoryViewModel.bookingHistoryUiState.collectAsStateWithLifecycle()

    val updateUserViewModel = koinViewModel<UpdateUserViewModel>()
    val updateUserUiState by updateUserViewModel.updateUserUiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = startingRoute,
        enterTransition = { slideInHorizontally { it } }
    ) {

        composable(route = Screen.WelcomeScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.LoginScreen.route) },
                onRegisterClick = { navController.navigate(Screen.RegistrationScreen.route) }
            )
        }

        composable(route = Screen.RegistrationScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            RegisterScreen(
                registrationViewModel = registrationViewModel,
                registrationUiState = registrationUiState,
                onVerify = {
                    navController.navigate(BottomNavigationItems.Home.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        popUpTo(Screen.RegistrationScreen.route) { inclusive = true }
                        popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.LoginScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            LoginScreen(
                loginUiState = loginUiState,
                loginViewModel = loginViewModel,
                onRegisterClick = {
                    navController.navigate(Screen.RegistrationScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                },
                onVerify = {
                    navController.navigate(BottomNavigationItems.Home.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        popUpTo(Screen.RegistrationScreen.route) { inclusive = true }
                        popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = BottomNavigationItems.Home.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            HomeScreen(
                navController = navController,
                petListUiState = petListUiState,
                petProfileViewModel = petProfileViewModel
            )
        }

        composable(route = BottomNavigationItems.Booking.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            BookingHistoryScreen(
                bookingHistoryViewModel = bookingHistoryViewModel,
                bookingHistoryUiState = bookingHistoryUiState,
                reviewViewModel = reviewViewModel,
                navController = navController,
                reviewUiState = reviewUiState
            )
        }

        composable(route = BottomNavigationItems.Shop.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(true)
            onGesturesChanged(true)
            ShopHomeScreen(
                productListState = productListState,
                categoryListState = categoryListState,
                featuredProductListState = featuredProductListState,
                productViewModel = productViewModel,
                onProductSelect = {
                    navController.navigate(Screen.ProductDetailScreen.route)
                },
                getCategories = { productViewModel.fetchCategories() },
                getProducts = { productViewModel.fetchNextPage() },
                getFeaturedProducts = { productViewModel.fetchFeaturedProducts() },
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        composable(route = BottomNavigationItems.Profile.route) {
            onBottomBarVisibilityChanged(true)
            onProfileButtonVisibilityChanged(false)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(false)
            onGesturesChanged(true)
            ProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                petProfileViewModel = petProfileViewModel
            )
        }

        composable(route = Screen.SearchScreen.route) {
            onBottomBarVisibilityChanged(false)
            onTopBarVisibilityChanged(false)
            onProfileButtonVisibilityChanged(false)
            onGesturesChanged(false)
            searchIconVisibilityChanged(false)
            SearchScreen(
                productViewModel = productViewModel,
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        composable(route = Screen.ProductListScreen.route) {
            onBottomBarVisibilityChanged(false)
            onProfileButtonVisibilityChanged(true)
            onTopBarVisibilityChanged(true)
            searchIconVisibilityChanged(true)
            onGesturesChanged(true)
            val productType = productListState.listType

            ProductListScreen(
                onProductSelect = {
                    productViewModel.setProduct(it)
                    navController.navigate(Screen.ProductDetailScreen.route)
                },
                isLoading = productListState.isLoading,
                onLoadMore = { productViewModel.fetchNextPage() },
                productTypeLabel = productType,
                categoryListState = categoryListState,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel
            )
        }
        composable(route = Screen.ProductDetailScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            ProductDetailScreen(
                productDetailState = productDetailState,
                cartViewModel = cartViewModel,
                navController = navController,
                reviewListState = reviewListState,
                reviewViewModel = reviewViewModel
            )
        }
        composable(route = Screen.CartScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            CartScreen(
                cartUiState = cartUiState,
                cartViewModel = cartViewModel,
                navController = navController
            )
        }
        composable(route = Screen.CheckoutScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            CheckoutScreen(
                navController = navController,
                orderViewModel = orderViewModel,
                cartViewModel = cartViewModel,
                addressViewModel = addressViewModel,
                addressUiState = addressUiState,
                orderUiState = orderUiState
            )
        }
        composable(route = Screen.AddressScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            AddressScreen(
                navController = navController,
                addressViewModel = addressViewModel,
                addressUiState = addressUiState
            )
        }
        composable(route = Screen.AddressDetailScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            AddressDetailScreen(
                navController = navController,
                addressDetailUiState = addressDetailUiState,
                addressViewModel = addressViewModel
            )
        }
        composable(route = Screen.OrderHistoryScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            OrderHistoryScreen(
                navController = navController,
                orderHistoryUiState = orderHistoryUiState,
                reviewViewModel = reviewViewModel,
                reviewUiState = reviewUiState
            )
        }

        composable(route = Screen.PetProfileScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            PetProfileScreen(
                navController = navController,
                editState = petProfileUiState,
                petProfileViewModel = petProfileViewModel

            )
        }
        composable(route = Screen.GroomingScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            GroomingScreen(
                navController = navController,
                groomingUiState = groomingUiState,
                groomingViewModel = groomingViewModel,
                changePetSelectionView = { value ->
                    petProfileViewModel.changePetSelectionView(value, Category.GROOMING)
                },
                reviewViewModel = reviewViewModel,
                reviewListState = reviewListState
            )
        }
        composable(route = Screen.BookingGroomingScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            BookingGroomingScreen(
                navController = navController,
                groomingBookingViewModel = groomingBookingViewModel,
                addressUiState = addressUiState,
                addressViewModel = addressViewModel,
                groomingUiState = groomingUiState,
                selectedPetForBooking = selectedPetForService
            )
        }

        composable(route = Screen.PetWalkingScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            PetWalkingScreen(
                navController = navController,
                petWalkingViewModel = petWalkingViewModel,
                petWalkingUiState = petWalkingUiState,
                changePetSelectionView = { value ->
                    petProfileViewModel.changePetSelectionView(value, Category.WALKING)
                },
                reviewListState = reviewListState,
                reviewViewModel = reviewViewModel
            )
        }
        composable(route = Screen.BookingPetWalkScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            BookingPetWalkScreen(
                navController = navController,
                petWalkingUiState = petWalkingUiState,
                addressUiState = addressUiState,
                addressViewModel = addressViewModel,
                bookingPetWalkViewModel = bookingPetWalkViewModel,
                selectedPetForBooking = selectedPetForService
            )
        }
        composable(route = Screen.VetScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            VetScreen(
                navController = navController,
                vetViewModel = vetViewModel,
                vetUiState = vetUiState,
                changePetSelectionView = { value ->
                    petProfileViewModel.changePetSelectionView(value, Category.VETERINARY)
                },
                reviewListState = reviewListState,
                reviewViewModel = reviewViewModel
            )
        }
        composable(route = Screen.VetIssuesScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            VetIssuesScreen(
                navController = navController,
                vetViewModel = vetViewModel,
                vetUiState = vetUiState
            )
        }
        composable(route = Screen.VetBookingScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            VetBookingScreen(
                navController = navController,
                vetBookingViewModel = vetBookingViewModel,
                vetUiState = vetUiState,
                addressUiState = addressUiState,
                addressViewModel = addressViewModel,
                selectedPetForBooking = selectedPetForService
            )
        }

        composable(route = Screen.DogTrainingScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            DogTrainingScreen(
                navController = navController,
                dogTrainingViewModel = dogTrainingViewModel,
                dogTrainingUiState = dogTrainingUiState,
                changePetSelectionView = { value ->
                    petProfileViewModel.changePetSelectionView(
                        value,
                        selectedService = Category.DOG_TRAINING
                    )
                },
                reviewListState = reviewListState,
                reviewViewModel = reviewViewModel
            )
        }

        composable(route = Screen.DogTrainingBookingScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            DogTrainingBookingScreen(
                navController = navController,
                dogTrainingBookingViewModel = dogTrainingBookingViewModel,
                dogTrainingUiState = dogTrainingUiState,
                dogTrainingBookingUiState = dogTrainingBookingUiState,
                addressUiState = addressUiState,
                addressViewModel = addressViewModel,
                selectedPetForBooking = selectedPetForService
            )
        }
        composable(route = Screen.PetInsuranceScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            PetInsuranceScreen(
                navController = navController,
                petInsuranceViewModel = petInsuranceViewModel,
                petInsuranceUiState = petInsuranceUiState,
                petInsuranceBookingUiState = petInsuranceBookingUiState
            )
        }
        composable(route = Screen.PetListScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            PetListScreen(
                navController = navController,
                petListUiState = petListUiState,
                petProfileViewModel = petProfileViewModel,
            )
        }

        composable(route = Screen.ReviewScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            ReviewScreen(
                navController = navController,
                reviewViewModel = reviewViewModel,
                reviewUiState = reviewUiState
            )
        }

        composable(route = Screen.UpdateUserScreen.route) {
            hideAllChrome(
                onBottomBarVisibilityChanged,
                onTopBarVisibilityChanged,
                onProfileButtonVisibilityChanged,
                onGesturesChanged,
                searchIconVisibilityChanged
            )
            UpdateUserScreen(
                navController = navController,
                updateUserViewModel = updateUserViewModel,
                updateUserUiState = updateUserUiState
            )
        }

    }
}

private fun hideAllChrome(
    onBottomBar: (Boolean) -> Unit,
    onTopBar: (Boolean) -> Unit,
    onProfile: (Boolean) -> Unit,
    onGestures: (Boolean) -> Unit,
    onSearchIcon: (Boolean) -> Unit
) {
    onBottomBar(false)
    onTopBar(false)
    onProfile(false)
    onGestures(false)
    onSearchIcon(false)
}
