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
package me.pedrazas.simplesearch;

import com.gmongo.GMongo
import org.apache.commons.codec.binary.Base64OutputStream

class WebPage {

    def mongo = new GMongo()
    def db = mongo.getDB("simpleSearch")

    String url
    String contentType
    int oid
    Date created
    Date indexedDate
    long modified
    boolean updated = false
    boolean indexed = false

    def formats = ["text/html", "application/pdf"]
    def base_urls = ["http://localhost", "http://ivan.pedrazas", "http://simplesearch."]


    WebPage(String websiteAddress){
        this.url = websiteAddress
        this.created = new Date()
        try{
            def conn = new URL(websiteAddress).openConnection()
            if (conn.responseCode == 200 || conn.responseCode == 201){
                this.contentType = conn.contentType
                this.modified = conn.lastModified
              } else {
                println "Error Connecting to " + websiteAddress
              }
        }catch (Exception) {
            Exception.printStackTrace()
        }
    }

    def next(){
        def counter = db.counters.findAndModify([_id: "seq"], [$inc: [c: 1]])
        if (counter==null){
            counter = [_id: 'seq', c: 2]
            db.counters.insert(counter)
            return 1
        }
        return counter.c
    }

    def save(){
            // oid: this.next() here because we don't want to count the not found errors
        if(this.isIndexable()){
            def page = [url: this.url, content_type: this.contentType, oid: this.next(), modified: this.modified, added: this.created, indexed: this.indexed]
            this.db.links.save(page)
        }
    }


    def isIndexable(){
        def flag = false;
        this.formats.each{
            if(this.contentType.startsWith(it))
                flag = true
        }
        this.base_urls.each{
            if(this.url.startsWith(it))
                flag = true
        }
        return flag
    }

    static String fetchContent(String url){
        println "Fetching ${url}"
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Base64OutputStream b64os = new Base64OutputStream(byteOut);
        b64os << new URL(url).openStream()
        return new String(byteOut.toByteArray());
    }
}
