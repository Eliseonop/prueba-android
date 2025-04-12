//package com.tcontur.login_tcontur.ui.data.modules
//import android.content.Context
//import com.tcontur.login_tcontur.ui.data.viewmodel.session.SessionManager
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
//        return SessionManager(context)
//    }
//}