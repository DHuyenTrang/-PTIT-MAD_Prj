package com.n3t.mobile.data.api

const val API_VERSION = "v5"

object ApiEndPoint {
    // ====== AUTH ======
    const val LOGIN = "/$API_VERSION/auth/login"
    const val VERIFY_OTP = "/$API_VERSION/auth/verify-otp-code"
    const val RESEND_OTP = "/$API_VERSION/auth/resend-otp-code"
    const val INFORMATION = "/$API_VERSION/auth/information"
    const val REFRESH_TOKEN = "/$API_VERSION/auth/refresh-token-new"
    const val REGISTER = "/$API_VERSION/auth/register"
    const val PASSWORD_FORGOT = "/$API_VERSION/auth/forgot-password"
    const val PASSWORD_UPDATE = "/$API_VERSION/auth/update-password"
    const val CHECK_PHONE_EXIST = "/$API_VERSION/auth/get-account-status"
    const val LOGIN_WITH_PASSWORD = "/$API_VERSION/auth/login-with-password"
    const val LOGOUT = "/$API_VERSION/auth/logout"

    // ====== PROFILE ======
    const val USER_PROFILE_APP = "/$API_VERSION/api/user/profile-app"

    // ====== LICENSE PLATE (Phạt nguội) ======
    const val LICENSE_PLATE_QUERY = "/$API_VERSION/api/license-plate/query"
    const val GET_LICENSE_PLATES = "/$API_VERSION/api/car/listLicensePlate"
    const val DELETE_LICENSE_PLATE = "/$API_VERSION/api/car/deleteLicensePlate"
    const val CAR_ADD = "/$API_VERSION/api/car/add"

    // ====== SEARCH MAP ======
    const val SEARCH_PLACE = "/$API_VERSION/Place/AutoComplete"
    const val DETAIL_PLACE = "/$API_VERSION/Place/Detail"

    // ====== ROUTING ======
    const val SEARCH_ROUTE_V7 = "/v7/api/routing/search-route"

    // ====== TRACKING / HISTORY ======
    const val TRACKING_MOVEMENT = "/tracking/movement"
    const val TRACKING_MOVEMENT_V2 = "/v2/tracking/movement"
    const val TRACKING_HISTORY = "/tracking/history"

    // ====== REVERSE GEOCODING ======
    const val REVERSE_LOCATION = "/gateway/nominatim-v3/reverse"

    // ====== AI (ChatGPT / LLM) ======
    const val GPT_PATH = "/v1/chat/completions"
    const val STT_PATH = "/ws/stt"
    const val TTS_PATH = "/ws/tts"
    const val LLM_PATH = "/ws/llm"

    // ====== MEDIA ======
    const val SEARCH_MEDIA = "/search"
}
