package io.krasnoludkolo.results.domain

data class TwoTeamsSet(
    val _1: String,
    val _2: String
) {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is TwoTeamsSet) return false
        return (_1 == other._1 && _2 == other._2) || (_1 == other._2 && _2 == other._1)
    }

    override fun hashCode(): Int {
        var result = _1.hashCode()
        result = 31 * result + _2.hashCode()
        return result
    }

    fun containsOneOf(other: TwoTeamsSet): Boolean {
        return _1 == other._1 || _2 == other._2 || _1 == other._2 || _2 == other._1
    }
}