package com.mapbox.services.android.navigation.v5;

public class NavigationMapRoute {
    public static final String CONGESTION_KEY = "congestion";
    public static final String SOURCE_KEY = "source";
    public static final String INDEX_KEY = "index";

    public static final String GENERIC_ROUTE_SOURCE_ID = "mapbox-navigation-route-source";
    public static final String GENERIC_ROUTE_LAYER_ID = "mapbox-navigation-route-layer";
    public static final String WAYPOINT_SOURCE_ID = "mapbox-navigation-waypoint-source";
    public static final String WAYPOINT_LAYER_ID = "mapbox-navigation-waypoint-layer";
    public static final String ID_FORMAT = "%s-%d";
    public static final String GENERIC_ROUTE_SHIELD_LAYER_ID = "mapbox-navigation-route-shield-layer";
    public static final int TWO_POINTS = 2;
    public static final int THIRTY = 30;

    public static final String layer_congestion = "layer_congestion";
    public static final String bridge_layer = "bridge-oneway-arrow-white";
    public static final String source_congestion = "source_congestion";

    public static final String source_station = "source_station";
    public static final String layer_station = "layer_station";

    public static final String layer_bulletins = "layer_bulletins";
    public static final String source_bulletins = "source_bulletins";
    public static final String source_route = "source_route";
    public static final String layer_route = "layer_route";
    public static final String boder_source_route = "boder_source_route";
    public static final String border_layer_route = "border_layer_route";


    public static final String layer_next_speed = "layer_next_speed";
    public static final String source_next_speed = "source_next_speed";


    public static final String layer_next_board = "layer_next_board";
    public static final String source_next_board = "source_next_board";


    public static final String layer_other_board = "layer_other_board";
    public static final String source_other_board = "source_other_board";

    public static final String layer_traffic = "overlay-tiles-layer";
    public static final String source_traffic = "overlay-tiles-source";

    public static final String ARROW_BEARING = "mapbox-navigation-arrow-bearing";
    public static final String ARROW_SHAFT_SOURCE_ID = "mapbox-navigation-arrow-shaft-source";
    public static final String ARROW_HEAD_SOURCE_ID = "mapbox-navigation-arrow-head-source";
    public static final String ARROW_SHAFT_CASING_LINE_LAYER_ID = "mapbox-navigation-arrow-shaft-casing-layer";
    public static final String ARROW_SHAFT_LINE_LAYER_ID = "mapbox-navigation-arrow-shaft-layer";
    public static final String ARROW_HEAD_ICON = "mapbox-navigation-arrow-head-icon";
    public static final String ARROW_HEAD_ICON_CASING = "mapbox-navigation-arrow-head-icon-casing";
    public static final int MAX_DEGREES = 360;
    public static final String ARROW_HEAD_CASING_LAYER_ID = "mapbox-navigation-arrow-head-casing-layer";
    public static final Float[] ARROW_HEAD_CASING_OFFSET = {0f, -7f};
    public static final String ARROW_HEAD_LAYER_ID = "mapbox-navigation-arrow-head-layer";
    public static final Float[] ARROW_HEAD_OFFSET = {0f, -7f};
    public static final int MIN_ARROW_ZOOM = 10;
    public static final int MAX_ARROW_ZOOM = 22;
    public static final float MIN_ZOOM_ARROW_SHAFT_SCALE = 2.6f;
    public static final float MAX_ZOOM_ARROW_SHAFT_SCALE = 13.0f;
    public static final float MIN_ZOOM_ARROW_SHAFT_CASING_SCALE = 3.4f;
    public static final float MAX_ZOOM_ARROW_SHAFT_CASING_SCALE = 17.0f;
    public static final float MIN_ZOOM_ARROW_HEAD_SCALE = 0.2f;
    public static final float MAX_ZOOM_ARROW_HEAD_SCALE = 0.8f;
    public static final float MIN_ZOOM_ARROW_HEAD_CASING_SCALE = 0.2f;
    public static final float MAX_ZOOM_ARROW_HEAD_CASING_SCALE = 0.8f;
    public static final float OPAQUE = 0.0f;
    public static final int ARROW_HIDDEN_ZOOM_LEVEL = 14;
    public static final float TRANSPARENT = 1.0f;
    public static final String LAYER_ABOVE_UPCOMING_MANEUVER_ARROW = "com.mapbox.annotations.points";
}
