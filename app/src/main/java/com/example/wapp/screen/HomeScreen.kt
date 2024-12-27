package com.example.wapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wapp.MainViewModel
import com.example.wapp.RouteApp
import com.example.wapp.components.BottomMenu
import com.example.wapp.screen.auth.AuthViewModel
import com.example.wapp.screen.components.BottomMenuScreen
import com.example.wapp.screen.components.HistoryComponents
import com.example.wapp.screen.components.HomeComponents
import com.example.wapp.screen.components.PhotoComponents
import com.example.wapp.screen.components.TentangComponents
import com.example.wapp.screen.maps.MapsViewModel
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(mapsViewModel: MapsViewModel, navController: NavController, authViewModel: AuthViewModel){

    authViewModel.getUser()

    val pagerIcon = listOf("Home", "Notification", "History", "Tentang")
    val pagerState = rememberPagerState {
        pagerIcon.size
    }
    val isClick = remember {
        mutableStateOf(1)
    }
    val courutineScope = rememberCoroutineScope()


    LaunchedEffect(pagerState) {
        snapshotFlow {
            pagerState.currentPage
        }.collect{
            isClick.value = it
        }
    }
    LaunchedEffect(Unit){
        authViewModel.getUser()
    }

    val menuItems = listOf(
        BottomMenuScreen.HomePage,
        BottomMenuScreen.GaleryScreen,
        BottomMenuScreen.HistoryScreen,
        BottomMenuScreen.AboutPage,
    )

    Scaffold { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)){
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page: Int ->
                when(page) {
                    0 -> {
                        HomeComponents(
                            paddingValues = paddingValues,
                            navController = navController,
                            authViewModel = authViewModel
                        )

                    }
                    1 -> PhotoComponents(
                        navController = navController,
                        mapsViewModel= mapsViewModel
                    )
                    2 -> HistoryComponents(
                        authViewModel = authViewModel,
                        mapsViewModel = mapsViewModel,
                        navController = navController
                    )
                    3 -> TentangComponents(paddingValues=paddingValues, authViewModel = authViewModel, onLogout = {
                        navController.navigate(RouteApp.AuthScreen.routeid){
                            popUpTo(BottomMenuScreen.HomePage.route){inclusive = true}
                        }
                    })
                }
                
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding() + 20.dp), contentAlignment = Alignment.BottomCenter){
                BottomMenu(isClick.value, onClick = {
                    isClick.value = it
                    courutineScope.launch {
                        if(!pagerState.isScrollInProgress){
                            pagerState.animateScrollToPage(isClick.value)
                        }
                    }
                }, menuItems = menuItems)
            }
        }
    }

}