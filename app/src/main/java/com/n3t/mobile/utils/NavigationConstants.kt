package com.n3t.mobile.utils

/**
 * Navigation-related constants ported from gofa-android.
 * Controls camera zoom, pitch, speed thresholds and distance triggers.
 */
object NavigationConstants {

    // ── Zoom Levels ──────────────────────────────────────────────────────
    /** Zoom when approaching a turn (< 300 m). */
    const val TINY_MAP_ZOOM_LEVEL = 16.0

    /** Default navigation zoom level. */
    const val DEFAULT_MAP_ZOOM_LEVEL = 17.0

    /** Medium zoom level for intermediate distances. */
    const val MEDIUM_MAP_ZOOM_LEVEL = 17.0

    /** Maximum zoom for closest view. */
    const val MAX_MAP_ZOOM_LEVEL = 18.5

    // ── Pitch ────────────────────────────────────────────────────────────
    /** Default camera pitch during navigation (degrees). */
    const val DEFAULT_MAP_PITCH = 45.0

    /** Minimum pitch – used when near a maneuver (< 300 m). */
    const val MIN_MAP_PITCH = 0.0

    // ── Speed Thresholds (km/h) ──────────────────────────────────────────
    /** Below this speed the recenter button is shown when user drags map. */
    const val SPEED_SCALE = 10

    /** Below this speed the camera bearing is NOT updated (user might be stopped). */
    const val MIN_USER_SPEED_TO_ACTION = 10

    /** Threshold that divides two zoom-level branching strategies. */
    const val MIN_USER_SPEED_TO_CHECK = 60

    // ── Navigation Distance Thresholds (meters) ─────────────────────────
    /** Distance remaining for arrival sound. */
    const val DISTANCE_TO_PLAY_SOUND_ARRIVE_DESTINATION = 50.0

    /** Lower bound distance to play instruction sound (low speed). */
    const val LOWER_DISTANCE_TO_PLAY_SOUND_CHANGE_NAVIGATION = 100.0

    /** Upper bound distance to play instruction sound (high speed). */
    const val UPPER_DISTANCE_TO_PLAY_SOUND_CHANGE_NAVIGATION = 300.0

    /** Minimum distance traveled before trusting maneuver bearing. */
    const val MIN_DISTANCE_TRAVELED_FOR_BEARING = 20.0

    /** Snap-to-route distance threshold – if snap point is further than this, use raw GPS. */
    const val SNAP_DISTANCE_THRESHOLD = 30.0

    /** Distance remaining threshold for arrival detection. */
    const val ARRIVAL_DISTANCE_THRESHOLD = 5.0
}
