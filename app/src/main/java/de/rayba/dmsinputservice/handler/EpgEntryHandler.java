/*
 * Copyright (C) 2012 dvbviewer-controller Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.rayba.dmsinputservice.handler;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.math.NumberUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.rayba.dmsinputservice.data.EpgEntry;

import static de.rayba.dmsinputservice.date.DMSDateConverterKt.stringToDate;

/**
 * The Class EpgEntryHandler.
 *
 * @author RayBa
 */
public class EpgEntryHandler extends DefaultHandler {

    List<EpgEntry> epgList = null;
    EpgEntry currentEPG = null;
	public static final String DATEFORMAT_RS_EPG = "yyyyMMddHHmmss";


	public List<EpgEntry> parse(InputStream inputStream) throws SAXException, IOException {
        Xml.parse(inputStream, Xml.Encoding.UTF_8, getContentHandler());
        return epgList;
    }

    @NonNull
    private ContentHandler getContentHandler() {
        RootElement root = new RootElement("epg");
        Element programmeElement = root.getChild("programme");
        Element eventId = programmeElement.getChild("eventid");
        Element pdc = programmeElement.getChild("pdc");
        Element titles = programmeElement.getChild("titles");
        Element title = titles.getChild("title");
        Element descriptions = programmeElement.getChild("descriptions");
        Element description = descriptions.getChild("description");
        Element events = programmeElement.getChild("events");
        Element event = events.getChild("event");

        root.setStartElementListener(new StartElementListener() {

            @Override
            public void start(Attributes attributes) {
                epgList = new ArrayList<EpgEntry>();
            }
        });

        programmeElement.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                currentEPG = new EpgEntry();
                currentEPG.setEpgID(NumberUtils.toLong(attributes.getValue("channel")));
                currentEPG.setStart(stringToDate(attributes.getValue("start"), DATEFORMAT_RS_EPG));
                currentEPG.setEnd(stringToDate(attributes.getValue("stop"), DATEFORMAT_RS_EPG));
            }
        });

        programmeElement.setEndElementListener(new EndElementListener() {

            @Override
            public void end() {
                epgList.add(currentEPG);
            }
        });

        eventId.setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                currentEPG.setEventId(body);
            }
        });
        pdc.setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                currentEPG.setPDC(body);
            }
        });
        title.setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                currentEPG.setTitle(body);
            }
        });
        event.setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                currentEPG.setSubTitle(body);
            }
        });

        description.setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                currentEPG.setDescription(body);
            }
        });
        return root.getContentHandler();
    }

}
