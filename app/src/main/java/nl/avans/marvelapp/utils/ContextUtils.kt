package nl.avans.marvelapp.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import java.util.*

/*
 * https://lokalise.com/blog/android-app-localization/
 */
class ContextUtils(base: Context) : ContextWrapper(base) {
    companion object {
        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextUtils {
            var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            val localeList = LocaleList(localeToSwitchTo)

            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            context = context.createConfigurationContext(configuration)

            return ContextUtils(context)
        }
    }
}