package de.rayba.dmsinputservice.handler

import android.sax.RootElement
import android.util.Xml
import de.rayba.dmsinputservice.data.Channel
import de.rayba.dmsinputservice.data.ChannelGroup
import de.rayba.dmsinputservice.data.ChannelRoot
import org.apache.commons.lang3.math.NumberUtils
import org.xml.sax.ContentHandler
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * The Class ChannelHandler.
 *
 * @author RayBa
 */
class ChannelHandler : DefaultHandler() {

    private lateinit var rootElements: LinkedList<ChannelRoot>
    private lateinit var currentRoot: ChannelRoot
    private lateinit var currentGroup: ChannelGroup
    private lateinit var currentChannel: Channel
    private var favPosition: Int = 1

    @Throws(SAXException::class, IOException::class)
    fun parse(inputStream: InputStream): MutableList<ChannelRoot> {
        Xml.parse(inputStream, Xml.Encoding.UTF_8, getContentHandler())
        return rootElements
    }

    private fun getContentHandler(): ContentHandler {
        val channels = RootElement("channels")
        val rootElement = channels.getChild("root")
        val groupElement = rootElement.getChild("group")
        val channelElement = groupElement.getChild("channel")
        val logoElement = channelElement.getChild("logo")

        channels.setStartElementListener { rootElements = LinkedList() }

        rootElement.setStartElementListener { attributes ->
            currentRoot = ChannelRoot()
            currentRoot.name = attributes.getValue("name")
            rootElements.add(currentRoot)
        }

        groupElement.setStartElementListener { attributes ->
            currentGroup = ChannelGroup()
            currentGroup.name = attributes.getValue("name")
            currentRoot.groups.add(currentGroup)
        }

        channelElement.setStartElementListener { attributes ->
            currentChannel = Channel()
            currentChannel.channelID = NumberUtils.toLong(attributes.getValue("ID"))
            currentChannel.position = favPosition
            currentChannel.name = attributes.getValue("name")
            currentChannel.epgID = NumberUtils.toLong(attributes.getValue("EPGID"))
            currentGroup.channels.add(currentChannel)
            favPosition++
        }

        logoElement.setEndTextElementListener { body -> currentChannel.logoUrl = body }

        return channels.contentHandler
    }

}
