package com.n3t.mobile.utils.MapUltis

import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets

data class CameraProvider(
    val center: Point? = null,                  // Ánh xạ đến target with Map4D
    val zoom: Double? = null,
    val pitch: Double? = null,                  // = tilt with Map4D
    val bearing: Double? = null,
    val padding: EdgeInsets? = null             // Ánh xạ đến padding with Map4D
) {

    companion object {
        const val OFFSET = 1
    }
    /**
     * Chuyển đổi đối tượng CameraProvider thành MFCameraPosition của Map4D.
     * @return Một đối tượng MFCameraPosition đã được cấu hình.
     */
    
    fun toMapbox(): CameraOptions {
        return CameraOptions.Builder()
            .center(center)
            .zoom(zoom)
            .pitch(pitch)
            .bearing(bearing)
            .padding(padding)
            .build()
    }


    class Builder {
        private var center: Point? = null               // Ánh xạ đến target with Map4D
        private var zoom: Double? = null
        private var pitch: Double? = null               // = tilt with Map4D
        private var bearing: Double? = null
        private var padding: EdgeInsets? = null         // Ánh xạ đến padding with Map4D

        /**
         * Thiết lập tọa độ trung tâm của camera.
         */
        fun center(center: Point) = apply {
            this.center = center
        }

        /**
         * Thiết lập mức độ zoom.
         */
        fun zoom(zoom: Double) = apply { this.zoom = zoom }

        /**
         * Thiết lập góc nghiêng của camera (pitch).
         */
        fun pitch(tilt: Double) = apply { this.pitch = tilt }

        /**
         * Thiết lập hướng của camera (tính bằng độ từ hướng Bắc).
         */
        fun bearing(bearing: Double) = apply { this.bearing = bearing }

        /**
         * Thiết lập vùng đệm (padding) xung quanh view.
         */
        fun padding(padding : EdgeInsets) = apply {
            this.padding = padding
        }

        /**
         * Xây dựng và trả về một đối tượng CameraProvider hoàn chỉnh.
         */
        fun build() = CameraProvider(
            center = center,
            zoom = zoom,
            pitch = pitch,
            bearing = bearing,
            padding = padding
        )
    }

//    // For test
//    companion object {
//        @JvmStatic
//        fun test(args: Array<String>) {
//            // 1. Tạo một đối tượng camera chung bằng Builder
//            val universalCamera = Builder()
//                .center(Point.fromLngLat(21.028511, 105.804817)) // Tọa độ Hà Nội
//                .zoom(15.0)
//                .pitch(30.0)
//                .bearing(90.0)
//                .padding(EdgeInsets(10.0, 20.0, 30.0, 40.0))
//                .build()
//
//            // 2. Chuyển đổi sang đối tượng camera của Map4D
//            val map4dCamera = universalCamera.toMap4D()
//            println("Map4D Camera: $map4dCamera")
//
//            // 3. Chuyển đổi sang đối tượng camera của Mapbox
//            val mapboxCamera = universalCamera.toMapbox()
//            println("Mapbox Camera: $mapboxCamera")
//        }
//    }



}

