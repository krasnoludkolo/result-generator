package io.krasnoludkolo.results.api

enum class FixtureResult {
    NOT_SET, HOST_WON, GUEST_WON, DRAW;

    fun fromResult(hostGoals: Int, guestGoals: Int): FixtureResult {
        return if (hostGoals > guestGoals) {
            HOST_WON
        } else if (hostGoals < guestGoals) {
            GUEST_WON
        } else {
            DRAW
        }
    }
}