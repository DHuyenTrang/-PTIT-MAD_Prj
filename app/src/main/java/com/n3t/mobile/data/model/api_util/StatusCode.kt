package com.n3t.mobile.data.model.api_util

object StatusCode {
	/// [200] Success
	const val SUCCESS = 200

	/// [204] Success no content
	const val SUCCESS_NO_CONTENT = 204

	/// [400] Bad request
	const val BAD_REQUEST = 400

	/// [401] Unauthorized
	const val UNAUTHORIZED = 401

	/// [404] Not found
	const val NOT_FOUND = 404

	/// [500] Internal server error
	const val INTERNAL_SERVER_ERROR = 500

	/// [501] Not implemented
	const val NOT_IMPLEMENTED = 501

	/// [502] Bad gateway
	const val BAD_GATEWAY = 502

	/// [503] Service unavailable
	const val SERVICE_UNAVAILABLE = 503

	/// [504] Gateway timeout
	const val GATEWAY_TIMEOUT = 504

	/// [505] HTTP version not supported
	const val HTTP_VERSION_NOT_SUPPORTED = 505

	/// [999] Unknown
	const val UNKNOWN = 999
}

