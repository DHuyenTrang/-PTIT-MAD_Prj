package com.n3t.mobile.utils.validation;

object GofaValidation {
    fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    fun isValidatePhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^(0|84|\\+84)\\d{9,12}\$"))
    }

    fun isValidatePassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidateLoginPassword(password: String): Boolean {
        return password.length >= 8 && password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+\$"))
    }

    fun isValidateOTP(otp: String): Boolean {
        return otp.length == 6
    }

    fun isValidateLicensePlateFirst(licensePlate: String): Boolean {
        return licensePlate.length == 3
    }

    fun isValidateLicensePlateSecond(licensePlate: String): Boolean {
        // is number and length 4-5
        return licensePlate.length in 4..5 && licensePlate.toIntOrNull() != null
    }

    fun isValidateLicensePlate(licensePlate: String): Boolean {
        return licensePlate.trim().length in 7..9
    }
}

