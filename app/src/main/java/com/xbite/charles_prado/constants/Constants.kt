package com.xbite.charles_prado.constants

import android.Manifest

class Constants {
    companion object{
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0
    }
}