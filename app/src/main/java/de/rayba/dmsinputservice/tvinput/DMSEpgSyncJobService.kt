package de.rayba.dmsinputservice.tvinput

import android.media.tv.TvContract
import android.net.Uri
import com.google.android.media.tv.companionlibrary.model.Channel
import com.google.android.media.tv.companionlibrary.model.InternalProviderData
import com.google.android.media.tv.companionlibrary.model.Program
import com.google.android.media.tv.companionlibrary.sync.EpgSyncJobService
import com.google.android.media.tv.companionlibrary.utils.TvContractUtils
import de.rayba.dmsinputservice.date.getFloatDate
import de.rayba.dmsinputservice.handler.ChannelHandler
import de.rayba.dmsinputservice.handler.EpgEntryHandler
import de.rayba.dmsinputservice.okttp3.HTTPUtil
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Request
import java.io.IOException
import java.util.*

class DMSEpgSyncJobService : EpgSyncJobService() {


    private val channelHandler = ChannelHandler()
    private val epgHandler = EpgEntryHandler()

    override fun getChannels(): List<Channel> {
        val url = "getchannelsxml.html?logo=1&favonly=1&tuner=1"
        return getChannels(url)
    }

    private fun getChannels(url: String): MutableList<Channel> {
        val channels = mutableListOf<Channel>()
        val request = Request.Builder()
                .url("https://tv.baun.lan/api/$url")
                .build()

        val client = HTTPUtil.getHttpClient()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            response.body?.byteStream()?.use {
                val channelRoot = channelHandler.parse(it)
                channelRoot.forEach { root ->
                    root.groups.forEach { group ->
                        group.channels.forEach { channel ->
                            val channelId = channel.channelID
                            val internalProviderData = InternalProviderData()
                            internalProviderData.isRepeatable = true
                            internalProviderData.videoType = TvContractUtils.SOURCE_TYPE_HTTP_PROGRESSIVE
                            internalProviderData.videoUrl = "http://tv.baun.lan/upnp/channelstream/$channelId.ts"
                            internalProviderData.put("epgId", channel.epgID)
                            val builder = Channel.Builder()
                                    .setOriginalNetworkId(channelId)
                                    .setDisplayName(channel.name)
                                    .setDisplayNumber(channel.position.toString())
                                    .setInternalProviderData(internalProviderData)
                                    .setChannelLogo("http://tv.baun.lan/" + channel.logoUrl)
                                    .setPackageName(applicationContext.packageName)
                                    .setTransportStreamId(0)
                                    .setServiceId(0)
                                    .setServiceType(TvContract.Channels.SERVICE_TYPE_AUDIO_VIDEO)
                            channels.add(builder.build())
                        }
                    }
                }
            }

        }
        return channels
    }

    override fun getProgramsForChannel(
            channelUri: Uri?,
            channel: Channel?,
            startMs: Long,
            endMs: Long
    ): List<Program> {
        val programs = mutableListOf<Program>()
        channel?.let { chan ->
            val epgId = chan.internalProviderData.get("epgId").toString()
            val start: String = getFloatDate(Date(startMs))
            val end: String = getFloatDate(Date(endMs))

            val url = "https://tv.baun.lan/api/epg.html".toHttpUrl().newBuilder()
                    .addQueryParameter("utf8", "1")
                    .addQueryParameter("lvl", "2")
                    .addQueryParameter("channel", epgId)
                    .addQueryParameter("start", start)
                    .addQueryParameter("end", end)
                    .build()
            val request = Request.Builder()
                    .url(url)
                    .build()

            val client = HTTPUtil.getHttpClient()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.byteStream()?.use { input ->
                    val entries = epgHandler.parse(input)
                    entries.forEach { entry ->
                        val builder = Program.Builder()
                                .setChannelId(chan.id)
                                .setTitle(entry.title)
                                .setDescription(entry.description)
                                .setStartTimeUtcMillis(entry.start!!.time)
                                .setEndTimeUtcMillis(entry.end!!.time)
                                .setInternalProviderData(chan.internalProviderData)
                        programs.add(builder.build())
                    }
                }
            }
        }

        return programs
    }

}