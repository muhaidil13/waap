package com.example.wapp

sealed class RouteApp(
    val routeid: String,
    val title: String
){
    object SplashScreen:RouteApp(routeid = "SplashScreen", title="Splash")
    object IntroScreen: RouteApp(routeid = "IntroScreen", title= "Intro")
    object AuthScreen: RouteApp(routeid = "AuthScreen", title="Auth")
    object SigninScreen: RouteApp(routeid = "SignInScreen", title="SignIn")
    object SignUpScreen: RouteApp(routeid = "SignUpScreen", title = "SignUp")
    object FullMapScreen: RouteApp(routeid = "FullMapScreen", title = "Full Map")
    object WisataScreen: RouteApp(routeid = "WisataScreen", title = "Wisata")
    object KulinerScreen: RouteApp(routeid = "KulinerScreen", title = "Kuliner")
    object ProfileScreen: RouteApp(routeid = "ProfileScreen", title = "Profile")
    object AdminHome: RouteApp(routeid = "AdminScreen", title = "Home")
    object AdminWisata: RouteApp(routeid = "AdminWisata", title = "Wisata")
    object AdminKuliner: RouteApp(routeid = "AdminKuliner", title = "Kuliner")
    object NavigasiSuccess: RouteApp(routeid = "NavigasiSuccess", title = "Success")
    object UsersScreeen: RouteApp(routeid = "UsersScreeen", title = "Users")
}
