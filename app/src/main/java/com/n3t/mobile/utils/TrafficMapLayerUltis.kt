package com.n3t.mobile.utils

//class TrafficMapLayerUltis {
//    fun drawableToBitmap(drawable: Drawable): Bitmap? {
//        var bitmap: Bitmap? = null
//        if (drawable is BitmapDrawable) {
//            val bitmapDrawable = drawable
//            if (bitmapDrawable.bitmap != null) {
//                return bitmapDrawable.bitmap
//            }
//        }
//        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
//            Bitmap.createBitmap(
//                1,
//                1,
//                Bitmap.Config.ARGB_8888
//            ) // Single color bitmap will be created of 1x1 pixel
//        } else {
//            Bitmap.createBitmap(
//                drawable.intrinsicWidth,
//                drawable.intrinsicHeight,
//                Bitmap.Config.ARGB_8888
//            )
//        }
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//        return bitmap
//    }
//
//    private fun addImageBulletinsToStyle(style: Style, resources: Resources) {
//        val genders = enumValues<TrafficType>()
//        genders.forEach {
//            if (style.getStyleImage(it.name) != null) {
//                style.removeStyleImage(it.name)
//            }
//        }
//        genders.forEach {
//            val trafficIcon = it.trafficIcon
//            if (trafficIcon!= null) {
//                drawableToBitmap(resources.getDrawable(trafficIcon))?.let { it1 ->
//                    style.addImage(
//                        it.name,
//                        it1
//                    )
//                }
//            }
//        }
//    }
//
//    fun addLayer(style: Style, resources: Resources, iconSize: Double) {
//        addImageBulletinsToStyle(style, resources)
//        val imageExpression = Expression.match(
//            Expression.get("type"),
//            Expression.literal(TrafficType.trafficJam.name),
//            Expression.literal(TrafficType.trafficJam.name),
//            Expression.literal(TrafficType.restrictedRoad.name),
//            Expression.literal(TrafficType.restrictedRoad.name),
//            Expression.literal(TrafficType.accident.name),
//            Expression.literal(TrafficType.accident.name),
//            Expression.literal(TrafficType.flood.name),
//            Expression.literal(TrafficType.flood.name),
//            Expression.literal("default-icon"),
//        )
//        val placesLayer = SymbolLayer(NavigationMapRoute.layer_bulletins, NavigationMapRoute.source_bulletins)
//            .iconAllowOverlap(true)
//            .iconIgnorePlacement(true)
//            .iconRotationAlignment(IconRotationAlignment.VIEWPORT)
//            .iconImage(imageExpression)
//            .iconSize(iconSize)
//        if (style.styleLayerExists(NavigationMapRoute.layer_bulletins)) {
//            style.removeStyleLayer(NavigationMapRoute.layer_bulletins)
//        }
//        style.addLayer(placesLayer)
//    }
//
//    fun updateTrafficBulletinOnMapLayer(style: Style, featureCollection: FeatureCollection, resources: Resources, iconSize: Double) {
//        if (style.styleSourceExists(NavigationMapRoute.source_bulletins)) {
//            style.removeStyleSource(NavigationMapRoute.source_bulletins)
//        }
//
//        if (!style.styleSourceExists(NavigationMapRoute.source_bulletins)) {
//            geoJsonSource(NavigationMapRoute.source_bulletins) {
//            }.featureCollection(featureCollection).bindTo(style)
//        }
//        style.getSource(NavigationMapRoute.source_bulletins)?.apply {
//            (this as GeoJsonSource).featureCollection(featureCollection)
//        }
//        addLayer(style, resources, iconSize)
//    }
//
//    companion object {
//        private const val NEXT_SPEED_LAYER_ID = "NEXT_SPEED_LAYER_ID"
//        private const val NEXT_SPEED_SOURCE_ID = "NEXT_SPEED_SOURCE_ID"
//
//        private const val NEXT_BOARD_LAYER_ID = "MapboxCarTrafficBulletinLayerId"
//        private const val NEXT_BOARD_SOURCE_ID = "MapboxCarTrafficBulletinLayerIdSource"
//
//        private const val OTHER_BOARD_LAYER_ID = "OTHER_BOARD_LAYER_ID"
//        private const val OTHER_BOARD_SOURCE_ID = "OTHER_BOARD_SOURCE_ID"
//    }
//}

