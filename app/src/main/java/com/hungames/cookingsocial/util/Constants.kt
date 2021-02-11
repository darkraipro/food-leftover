package com.hungames.cookingsocial.util

const val TAG_LOGIN = "LOGIN"
const val TAG_MAP = "MAP"
const val TAG_DISH = "DISH"

object IntentSignals{
    const val USER_DATA = "USER_DATA"
}

object Constants {
    const val SUCCESS_RESULT = 1
    const val FAILURE_RESULT = 0
    const val LOCATION_DATA = "LOCATION_DATA"
    const val RECEIVER = "RECEIVER"
    const val RESULT_DATA_KEY = "RESULT_DATA_KEY"
    const val ADDR_RECEIVER_JOB_ID = 1000
}

enum class NutritionType{
    ALL,
    MEAT,
    VEGETARIAN,
    VEGAN;
}