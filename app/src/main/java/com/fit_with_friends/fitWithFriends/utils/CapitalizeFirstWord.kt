package com.fit_with_friends.fitWithFriends.utils

class CapitalizeFirstWord {

    fun capitalizeWords(str: String?): String {
        if (str != null) {
            val phrase = StringBuilder()
            var capitalize = true
            for (c in str.toLowerCase().toCharArray()) {
                if (Character.isLetter(c) && capitalize) {
                    phrase.append(Character.toUpperCase(c))
                    capitalize = false
                    continue
                } else if (c == ' ') {
                    capitalize = true
                }
                phrase.append(c)
            }
            return phrase.toString()
        }
        return ""
    }
}