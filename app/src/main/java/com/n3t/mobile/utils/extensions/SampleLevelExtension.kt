package com.n3t.mobile.utils.extensions

import com.n3t.mobile.data.model.traffic.CameraInstruction
import com.n3t.mobile.data.model.traffic.PointLocationModel
import com.n3t.mobile.data.model.traffic.SampleLevel
import com.n3t.mobile.data.model.traffic.TrafficSignInstruction

//    motorway: 1
//    trunk: 2
//    primary: 3
//    secondary: 4
//    tertiary: 5
//    unclassified: 6
//    residential: 7
//    service: 8
fun SampleLevel.toImportanceInt(): Int {
    var importance: Int = 9
    when(this.classData) {
        "motorway" -> importance = 1
        "trunk" -> importance = 2
        "primary" -> importance = 3
        "secondary" -> importance = 4
        "tertiary" -> importance = 5
        "unclassified" -> importance = 6
        "residential" -> importance = 7
        "service" -> importance = 8
        else -> {
            importance = 9
        }
    }
    return importance
}

