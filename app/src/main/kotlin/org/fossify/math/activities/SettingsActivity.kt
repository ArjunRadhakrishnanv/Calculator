package org.fossify.math.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

import org.fossify.commons.compose.extensions.enableEdgeToEdgeSimple
import org.fossify.commons.compose.extensions.onEventValue
import org.fossify.commons.compose.theme.AppThemeSurface

import org.fossify.commons.extensions.isOrWasThankYouInstalled
import org.fossify.commons.extensions.launchPurchaseThankYouIntent

import org.fossify.commons.helpers.isTiramisuPlus
import org.fossify.math.compose.SettingsScreen
import org.fossify.math.extensions.config
import org.fossify.math.extensions.launchChangeAppLanguageIntent
import java.util.Locale
import kotlin.system.exitProcess

import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            AppThemeSurface {
                Surface(color = Color.Black) {
                    val context = LocalContext.current
                    var preventPhoneFromSleeping by remember { mutableStateOf(config.preventPhoneFromSleeping) }
                    var vibrateOnButtonPress by remember { mutableStateOf(config.vibrateOnButtonPress) }
                    val useEnglish = config.useEnglish
                    val wasUseEnglishToggled = config.wasUseEnglishToggled
                    val showCheckmarksOnSwitches = config.showCheckmarksOnSwitches

                    val isUseEnglishEnabled by remember {
                        derivedStateOf {
                            (wasUseEnglishToggled || Locale.getDefault().language != "en") && !isTiramisuPlus()
                        }
                    }
                    val isOrWasThankYouInstalled = onEventValue {
                        context.isOrWasThankYouInstalled(allowPretend = false)
                    }
                    val displayLanguage = remember { Locale.getDefault().displayLanguage }
                    SettingsScreen(
                        displayLanguage = displayLanguage,
                        goBack = ::finish,
                        preventPhoneFromSleeping = preventPhoneFromSleeping,
                        onPreventPhoneFromSleeping = {
                            config.preventPhoneFromSleeping = it
                            preventPhoneFromSleeping = it
                        },
                        vibrateOnButtonPressFlow = vibrateOnButtonPress,
                        onVibrateOnButtonPressFlow = {
                            config.vibrateOnButtonPress = it
                            vibrateOnButtonPress = it
                        },
                        isOrWasThankYouInstalled = isOrWasThankYouInstalled,
                        onThankYou = ::launchPurchaseThankYouIntent,
                        isUseEnglishEnabled = isUseEnglishEnabled,
                        isUseEnglishChecked = useEnglish,
                        onUseEnglishPress = { isChecked ->
                            config.useEnglish = isChecked
                            exitProcess(0)
                        },
                        onSetupLanguagePress = ::launchChangeAppLanguageIntent,
                        showCheckmarksOnSwitches = showCheckmarksOnSwitches,
                    )
                }
            }
        }
    }
}