/*
 * Copyright © 2015 dvbviewer-controller Project
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
package de.rayba.dmsinputservice.data
// TODO: Auto-generated Javadoc
/**
 * The Class Channel.
 *
 * @author RayBa
 * @date 07.04.2012
 */
class Channel : Comparable<Channel> {

    var id: Long? = null
    var channelID: Long = 0
    var name: String? = null
    var position: Int? = null
    var epgID: Long = 0
    var logoUrl: String? = null

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    override fun compareTo(other: Channel): Int {
        return position!!.compareTo(other.position!!)
    }


    override fun equals(other: Any?): Boolean {
        return if (other is Channel) {
            channelID == other.channelID
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return channelID.toString().hashCode()
    }

}