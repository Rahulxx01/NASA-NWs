package com.udacity.asteroidradar.model

import com.google.gson.JsonObject
import org.json.JSONObject
import kotlin.jvm.internal.Ref
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class CurrentAsteroidResponse (

    val links : Links,
    val element_count : Int,
    val near_earth_objects : Object
)
