/*
 * Copyright © 2013 dvbviewer-controller Project
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
package de.rayba.dmsinputservice.okttp3

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object HTTPUtil {

    private var httpClient: OkHttpClient? = null

    fun getHttpClient(): OkHttpClient {
        if (httpClient == null) {
            val trustManager = SSLUtil.trustAllTrustManager
            httpClient = OkHttpClient.Builder()
                    .sslSocketFactory(SSLUtil.getSSLServerSocketFactory(trustManager), trustManager)
                    .hostnameVerifier(SSLUtil.VerifyAllHostnameVerifiyer())
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .build()
        }
        return httpClient!!
    }

}