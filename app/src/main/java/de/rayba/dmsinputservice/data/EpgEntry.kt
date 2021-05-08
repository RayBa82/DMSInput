package de.rayba.dmsinputservice.data

import java.util.*

class EpgEntry : IEPG {

    override var id: Long = 0
    override var epgID: Long = 0
    override var start: Date? = null
    override var end: Date? = null
    override var channel: String? = null
    override var title: String? = null
    override var subTitle: String? = null
    override var description: String? = null
    override var channelLogo: String? = null
    var eventId: String? = null
    var pDC: String? = null

}