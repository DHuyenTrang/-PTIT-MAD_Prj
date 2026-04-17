package com.n3t.mobile.data.api

object ApiEndPoint {
    // ====== AUTH ======
    const val LOGIN = "/auth/login"
    const val VERIFY_OTP = "/auth/verify-otp-code"
    const val RESEND_OTP = "/auth/resend-otp-code"
    const val INFORMATION = "/auth/information"
    const val REFRESH_TOKEN = "/auth/refresh-token-new"
    const val REGISTER = "/auth/register"
    const val PASSWORD_FORGOT = "/auth/forgot-password"
    const val PASSWORD_UPDATE = "/auth/update-password"
    const val CHECK_EMAIL_EXIST = "/auth/get-account-status"
    const val LOGIN_WITH_PASSWORD = "/auth/login-with-password"
    const val LOGOUT = "/auth/logout"

    // ====== PROFILE ======
    const val USER_PROFILE_APP = "/api/user/profile-app"

    // ====== LICENSE PLATE (Phạt nguội) ======
    const val LICENSE_PLATE_QUERY = "/api/license-plate/query"
    const val GET_LICENSE_PLATES = "/api/car/listLicensePlate"
    const val DELETE_LICENSE_PLATE = "/api/car/deleteLicensePlate"
    const val CAR_ADD = "/api/car/add"

    // ====== TRACKING / HISTORY ======
    const val TRACKING_MOVEMENT = "/tracking/movement"
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

    // ====== STATION ======
    const val GET_POIS_STATION = "api/stations"

    // ====== GOONG ======
    const val GOONG_AUTOCOMPLETE = "v2/place/autocomplete"
    const val GOONG_DETAIL = "v2/place/detail"
    const val GOONG_DIRECTION = "v2/direction"
}
