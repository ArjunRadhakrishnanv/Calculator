package org.fossify.math.helpers

import android.content.Context
import org.fossify.commons.helpers.BaseConfig

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }
}