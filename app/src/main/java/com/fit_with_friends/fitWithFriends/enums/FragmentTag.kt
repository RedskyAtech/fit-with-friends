package com.fit_with_friends.fitWithFriends.enums

enum class FragmentTag(tag: String) {

    HOME("home"),
    COMPETITION("competition"),
    MOTIVATION("motivation"),
    CHALLENGE("challenge"),
    SETTINGS("settings");

    var tag: String
        internal set

    init {
        this.tag = tag
    }

    companion object {
        fun getFragmentTag(action: String): FragmentTag {
            for (receiverAction in values()) {
                if (receiverAction.tag == action)
                    return receiverAction
            }
            return HOME
        }
    }
}