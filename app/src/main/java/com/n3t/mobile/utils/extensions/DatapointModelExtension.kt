package com.n3t.mobile.utils.extensions

import android.location.Location
import com.n3t.mobile.data.model.nominatim.DatapointModel
import com.n3t.mobile.data.model.road.RoadInfo
import com.n3t.mobile.data.model.traffic.CameraInstruction
import com.n3t.mobile.data.model.traffic.PointLocationModel
import com.n3t.mobile.data.model.traffic.TrafficSignInstruction
import com.mapbox.geojson.Point

fun DatapointModel.toPointLocationModel(): PointLocationModel {
    var instruction: String = ""
    when(this.trafficSignId) {
        12 -> instruction = TrafficSignInstruction.ENTER_URBAN.instruction
        13 -> instruction = TrafficSignInstruction.EXIT_TOWN.instruction
        14 -> instruction = TrafficSignInstruction.START_NO_OVERTAKING.instruction
        15 -> instruction = TrafficSignInstruction.END_OVERTAKING.instruction
        18 -> instruction = TrafficSignInstruction.TOLL_BOOTH.instruction
        32 -> instruction = CameraInstruction.RED_LIGHT_CAMERA.instruction
        33 -> instruction = CameraInstruction.SPEED_CAMERA.instruction
        34 -> instruction = CameraInstruction.RED_LIGHT_CAMERA.instruction
        else -> {
            instruction = ""
        }
    }
    return PointLocationModel(this.id, this.lat, this.lon, this.compass, instruction)
}

fun DatapointModel.getPoint(): Point? {
    if (this.lat == null || this.lon == null) {
        return null
    }
    return Point.fromLngLat(this.lon, this.lat)
}
