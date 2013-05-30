/*
 * Copyright 2013 Ivan Pedrazas
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.pedrazas.simplesearch

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.JSON


class ElasticSearchSender{


    static void send(String index, String objectType, int oid, String url, String contentType, String file){
        def UrlES = "http://localhost:9200/${index}/${objectType}/${oid}"
        println "Url to Index: ${url}"
        try{
            def http = new HTTPBuilder(UrlES)
            http.request( POST, JSON ) { req ->
                body = [
                  webpage : [
                    url: url,
                    content_type: contentType,
                    content: file
                  ]
                ]

                response.success = { resp, json ->
                    println resp
                    println json
                }

                // handler for any failure status code:
              response.failure = { resp ->
                println "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
              }
            }
        }catch(Exception){
            Exception.printStackTrace()
        }
    }
}
