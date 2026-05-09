package co.com.enervibes.app

import android.app.Application
import co.com.enervibes.app.data.api.ServiceLocator

class EnervibesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
    }
}
