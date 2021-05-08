package de.rayba.dmsinputservice.data

import java.util.*

interface IEPG {

    var id: Long
    var channel: String?
    var channelLogo: String?
    var epgID: Long
    var start: Date?
    var end: Date?
    var title: String?
    var subTitle: String?
    var description: String?

}