package com.example.windowsoverlay.Model

typealias Matchdetails = ArrayList<Matchdetail>

data class Matchdetail (
    val fixture: Fixture? = null,
    val league: League? = null,
    val teams: Teams? = null,
    val goals: ExtratimeClass? = null,
    val score: Score? = null,
    val events: List<Event>? = null,
    val lineups: List<Lineup>? = null,
    val statistics: List<MatchdetailStatistic>? = null,
    val players: List<MatchdetailPlayer>? = null
)

data class Event (
    val time: Time? = null,
    val team: Away? = null,
    val player: Assist? = null,
    val assist: Assist? = null,
    val type: Type? = null,
    val detail: String? = null,
    val comments: String? = null
)

data class Assist (
    val id: Long? = null,
    val name: String? = null
)

data class Away (
    val id: Long? = null,
    val name: Name? = null,
    val logo: String? = null,
    val colors: Colors? = null,
    val update: String? = null,
    val winner: Boolean? = null
)

data class Colors (
    val player: Goalkeeper? = null,
    val goalkeeper: Goalkeeper? = null
)

data class Goalkeeper (
    val primary: String? = null,
    val number: String? = null,
    val border: String? = null
)

enum class Name {
    Leicester,
    Tottenham
}

data class Time (
    val elapsed: Long? = null,
    val extra: Long? = null
)

enum class Type {
    Card,
    Goal,
    Subst
}

data class Fixture (
    val id: Long? = null,
    val referee: String? = null,
    val timezone: String? = null,
    val date: String? = null,
    val timestamp: Long? = null,
    val periods: Periods? = null,
    val venue: Venue? = null,
    val status: Status? = null
)

data class Periods (
    val first: Long? = null,
    val second: Long? = null
)

data class Status (
    val long: String? = null,
    val short: String? = null,
    val elapsed: Long? = null
)

data class Venue (
    val id: Long? = null,
    val name: String? = null,
    val city: String? = null
)

data class ExtratimeClass (
    val home: Long? = null,
    val away: Long? = null
)

data class League (
    val id: Long? = null,
    val name: String? = null,
    val country: String? = null,
    val logo: String? = null,
    val flag: String? = null,
    val season: Long? = null,
    val round: String? = null
)

data class Lineup (
    val team: Away? = null,
    val coach: Coach? = null,
    val formation: String? = null,
    val startXI: List<StartXi>? = null,
    val substitutes: List<StartXi>? = null
)

data class Coach (
    val id: Long? = null,
    val name: String? = null,
    val photo: String? = null
)

data class StartXi (
    val player: StartXIPlayer? = null
)

data class StartXIPlayer (
    val id: Long? = null,
    val name: String? = null,
    val number: Long? = null,
    val pos: Pos? = null,
    val grid: String? = null
)

enum class Pos {
    D,
    F,
    G,
    M
}

data class MatchdetailPlayer (
    val team: Away? = null,
    val players: List<PlayerPlayer>? = null
)

data class PlayerPlayer (
    val player: Coach? = null,
    val statistics: List<PlayerStatistic>? = null
)

data class PlayerStatistic (
    val games: Games? = null,
    val offsides: Long? = null,
    val shots: Shots? = null,
    val goals: StatisticGoals? = null,
    val passes: Passes? = null,
    val tackles: Tackles? = null,
    val duels: Duels? = null,
    val dribbles: Dribbles? = null,
    val fouls: Fouls? = null,
    val cards: Cards? = null,
    val penalty: Penalty? = null
)

data class Cards (
    val yellow: Long? = null,
    val red: Long? = null
)

data class Dribbles (
    val attempts: Long? = null,
    val success: Long? = null,
    val past: Long? = null
)

data class Duels (
    val total: Long? = null,
    val won: Long? = null
)

data class Fouls (
    val drawn: Long? = null,
    val committed: Long? = null
)

data class Games (
    val minutes: Long? = null,
    val number: Long? = null,
    val position: Pos? = null,
    val rating: String? = null,
    val captain: Boolean? = null,
    val substitute: Boolean? = null
)

data class StatisticGoals (
    val total: Long? = null,
    val conceded: Long? = null,
    val assists: Long? = null,
    val saves: Long? = null
)

data class Passes (
    val total: Long? = null,
    val key: Long? = null,
    val accuracy: String? = null
)

data class Penalty (
    val won: Any? = null,
    val commited: Any? = null,
    val scored: Long? = null,
    val missed: Long? = null,
    val saved: Long? = null
)

data class Shots (
    val total: Long? = null,
    val on: Long? = null
)

data class Tackles (
    val total: Long? = null,
    val blocks: Long? = null,
    val interceptions: Long? = null
)

data class Score (
    val halftime: ExtratimeClass? = null,
    val fulltime: ExtratimeClass? = null,
    val extratime: ExtratimeClass? = null,
    val penalty: ExtratimeClass? = null
)

data class MatchdetailStatistic (
    val team: Away? = null,
    val statistics: List<StatisticStatistic>? = null
)

data class StatisticStatistic (
    val type: String? = null,
    val value: Value? = null
)

sealed class Value {
    class IntegerValue(val value: Long)  : Value()
    class StringValue(val value: String) : Value()
    class NullValue()                    : Value()
}

data class Teams (
    val away: Away? = null,
    val home: Away? = null
)

