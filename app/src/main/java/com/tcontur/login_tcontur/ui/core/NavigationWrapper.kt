package com.tcontur.login_tcontur.ui.core
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tcontur.login_tcontur.ui.core.login.LoginScreen
import com.tcontur.login_tcontur.ui.core.login.SessionManager
import com.tcontur.login_tcontur.ui.core.venta.BoletosScreen
import com.tcontur.login_tcontur.ui.core.home.HomeScreen
import com.tcontur.login_tcontur.ui.core.protobin.ProtoScreen
import com.tcontur.login_tcontur.ui.core.report.ReportScreen

@Composable
fun NavigationWrapper(context: Context) {
    val navController = rememberNavController()
    val sessionManager = remember { SessionManager(context) }
    val startDestination =
        if (sessionManager.isLogged() != null) AppRoute.HomeRoute else AppRoute.LoginRoute
    NavHost(navController = navController, startDestination = startDestination) {
        composable<AppRoute.LoginRoute> {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(AppRoute.HomeRoute) {
                        popUpTo(AppRoute.LoginRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                context = context
            )
        }
        composable<AppRoute.HomeRoute> {
            HomeScreen(navController = navController)
        }
//        composable<Detail> { backStackEntry ->
//            val detail = backStackEntry.toRoute<Detail>()
//            DetailScreen(detail.name)
//        }

        composable<AppRoute.BoletoRoute> {
            BoletosScreen(navController = navController)
        }

        composable<AppRoute.ReportRoute> {
            ReportScreen(navController = navController)
        }

        composable<AppRoute.ProtoRoute> {
            ProtoScreen(navController = navController)
        }
    }
}
