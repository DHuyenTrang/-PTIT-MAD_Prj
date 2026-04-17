package com.n3t.mobile.utils

interface SecureData {
    fun encode(data: String): String
    fun decode(encodedData: String): String
}

class GofaSecureUtils(
    private val shiftStep: Int,
) : SecureData {
    override fun encode(data: String): String {
        val encodedData = StringBuilder()
        data.forEach {
            encodedData.append(shiftChar(it, shiftStep))
        }
        val newData = encodedData.toString()
        return reverseData(newData)
    }

    override fun decode(encodedData: String): String {
        val newData = reverseData(encodedData)
        val decodedData = StringBuilder()
        newData.forEach {
            decodedData.append(shiftChar(it, -shiftStep))
        }
        return decodedData.toString()
    }

    private fun shiftChar(char: Char, step: Int): Char {
        val alphabet = reverseData(getAlphabet())
        val index = alphabet.indexOf(char)
        if (index == -1) {
            return char
        }
        var newIndex = (index + step) % alphabet.length
        if (newIndex < 0) {
            newIndex += alphabet.length
        }
        return alphabet[newIndex]
    }

    private fun reverseData(value: String): String {
        return value.reversed()
    }

    private fun getAlphabet(): String {
        val alphabet = StringBuilder()
        for (i in 97..122) {
            alphabet.append(i.toChar())
        }

        alphabet.append("/.-_:(),;+*!?=")

        for (i in 48..57) {
            alphabet.append(i.toChar())
        }

        for (i in 65..90) {
            alphabet.append(i.toChar())
        }

        return alphabet.toString()
    }

    override fun toString(): String {
        return ""
    }
}

