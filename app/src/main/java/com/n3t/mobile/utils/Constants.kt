package com.n3t.mobile.utils

import com.mapbox.geojson.Point

object Constants {
    const val APP_NAME = "GOFA_APP_ANDROID"
    const val BUILD_DATE = "11/03/2024"
    const val PRIVACY_POLICY_URL = "https://gofa.vn/chinh-sach-bao-mat/"
    const val TERM_OF_SERVICE_URL = "https://gofa.vn/dieu-khoan-dich-vu-2/"
    const val VOV_SOURCE_URL = "https://vovgiaothong.vn/"
    const val ONESIGNAL_APP_ID = "4cd6ec51-6431-45c6-8670-16f20fa7cb97"

    const val ENABLE_TAKE_SNAPSHOT = true
    const val ENABLE_RUN_ON_EMULATOR = false
    const val ENABLE_FAKE_LOCATION = false

    // Unit km/h
    // speed less than when user zoom in map then app not dismiss camera tracking
    const val SPEED_SCALE = 10

    const val DATA_POINT_FREE_STYLE = "FreeStyle"

    val MAP_STYLE = listOf<String>("3D", "V? Tinh")

    val WARNING_SIGNS = listOf<Int>(200, 300, 500, 1000)
    // Distance to display next traffic sign. Can be changed by user
    // Default: 300m
    const val DEFAULT_WARNING_SIGNS = 300
    // Min distance to display next traffic sign. Can not be changed by user
    // Default: 50m
    const val FIXED_MIN_DISTANCE_UPDATE_TRAFFIC_SIGN = 50

    // Speed to display speed board (Km/h)
    const val MIN_SPEED_TO_SHOW_SPEED_BOARD = 5
    const val VOLUME_SOUND_WARNING = 50

    // Distance to call api tracking (m)
    const val DISTANCE_TO_TRACKING_MOVEMENT = 100

    // Speed to update camera pitch (Km/h)
    const val MIN_SPEED_TO_UPDATE_CAMERA_PITCH = 10

    const val DISTANCE_TO_DISPLAY_NEXT_DOWN_SPEED = 300
    const val DISTANCE_TO_DISPLAY_NEXT_UP_SPEED = 50
    const val DISTANCE_TO_UPDATE_BEARING = 50

    const val MAX_BEARING_CURRENT_SPEED = 90.0
    const val MAX_BEARING_NEXT_SPEED = 70.0
    const val MAX_BEARING_CURRENT_OSM = 60.0

    const val DISTANCE_TO_PLAY_SOUND_ARRIVE_DESTINATION: Double = 50.0
    // Toc do cho viec chia zoomlevel, pitch, va am thanh cho dan duong
    const val MIN_USER_SPEED_TO_CHECK = 60
    const val MIN_USER_SPEED_TO_ACTION = 10
    const val MIN_NAVIGATION_DISTANCE_UPDATE_SPEED = 10
    const val LOWER_DISTANCE_TO_PLAY_SOUND_CHANGE_NAVIGATION: Double = 100.0
    const val UPPER_DISTANCE_TO_PLAY_SOUND_CHANGE_NAVIGATION: Double = 300.0

    const val DEFAULT_SPEED_LIMIT: Int = 50
//    const val UNDEFINED_SPEED_LIMIT: Int = 800
    const val DEFAULT_GPS_SPEED: Int = 0
    const val GPS_SPEED_BOUNDS: Int = 30
    const val MAX_GPS_SPEED: Int = 10
    const val MIN_GPS_SPEED: Int = -10
    const val MIN_DISTANCE_SNAP: Int = 10

    const val TINY_MAP_ZOOM_LEVEL = 16.0
    const val DEFAULT_MAP_ZOOM_LEVEL = 17.0
    const val MAX_MAP_ZOOM_LEVEL = 18.5
    const val MEDIUM_MAP_ZOOM_LEVEL = 17.0
    const val DEFAULT_MAP_PITCH = 45.0
    const val MIN_MAP_PITCH = 0.0
    const val DEFAULT_MAP_PITCH_NAVIGATION = 45.0
    val PLAYBACK_CHANGE_SPEED = listOf<Int>(1, 5, 10, 20, 50)

    val truongSaLocation: Point = Point.fromLngLat(115.843447, 10.699310)
    val hoangSaLocation: Point = Point.fromLngLat(112.749323, 16.635074)
    val eastSeaLocation = listOf(
        //	Point.fromLngLat(106.420325, 20.203947),
        //	Point.fromLngLat(105.796236, 19.375268),
        Point.fromLngLat(109.097377, 14.574211),
        // Point.fromLngLat(107.209859, 10.313873)
    )

    // gofa logger
    const val LOG = "wjrxikYxeiqhxZnxllWxckhww/oos"

    val vovLinks = listOf(
        /**** MINHTV CH?NH S?A THEO TRANG TR? VOV GIAO THÔNG ****/
        "https://play.vovgiaothong.vn/live/gthn/playlist.m3u8",// Hà N?i
        "https://play.vovgiaothong.vn/live/gthcm/playlist.m3u8", //HCM
        "https://play.vovgiaothong.vn/live/mekong/playlist.m3u8", //mekong"
        "https://play.vovgiaothong.vn/live/duyenhai/playlist.m3u8" // Duyên h?i

        /**** URL cu b? l?i VOV HÀ N?I ****/
//        "https://str.vov.gov.vn/vovlive/vovGTHN.sdp_aac/media_w1736114884_329744.m3u8",
//        "https://str.vov.gov.vn/vovlive/vovGTHCM.sdp_aac/chunklist_w801097335.m3u8",
//        "https://str.vov.gov.vn/vovlive/vovgtMeKong.sdp_aac/chunklist_w532359496.m3u8",
//        "https://str.vov.gov.vn/vovlive/vovgtMeKong.sdp_aac/chunklist_w532359496.m3u8",
    )

    val LICENSE_STATUS_INACTIVE = "inactive"
    val LICENSE_STATUS_ACTIVE = "active"
    val MAX_VIA_POINT = 5


    override fun toString(): String {
        return ""
    }
}

