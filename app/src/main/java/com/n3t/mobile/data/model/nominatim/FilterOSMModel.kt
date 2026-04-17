package com.n3t.mobile.data.model.nominatim

import android.location.Location

class FilterOSMModel(var osmId: Int, var distanceToPoint: Double?,
                     var osmIdBearing: Double?, var farestLocation: Location?,
                     var listLocation :ArrayList<Location>, var extratag: ExtratagModel?)
