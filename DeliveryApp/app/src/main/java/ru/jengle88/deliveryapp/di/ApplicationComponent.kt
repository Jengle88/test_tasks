package ru.jengle88.deliveryapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.jengle88.deliveryapp.ui.screen.main_screen.MainFragment
import javax.inject.Singleton

@Singleton
@Component(modules = []/*[AppModule::class, UseCaseModule::class]*/)
interface ApplicationComponent {

    fun inject(mainFragment: MainFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance appContext: Context
        ): ApplicationComponent
    }
}