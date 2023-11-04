package ru.jengle88.deliveryapp.app

import android.app.Application
import ru.jengle88.deliveryapp.di.ApplicationComponent
import ru.jengle88.deliveryapp.di.DaggerApplicationComponent

class DeliveryApp: Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent
            .factory()
            .create(applicationContext)
    }
}