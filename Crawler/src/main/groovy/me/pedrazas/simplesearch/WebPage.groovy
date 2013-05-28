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

import com.mongodb.DB

class WebPage {

    def db
    String url
    String contentType
    int oid
    Date created
    long modified
    boolean updated = false
    boolean indexed = false
    int responseCode = 0


    WebPage(String websiteAddress, DB db){
        this.url = websiteAddress
        this.created = new Date()
        this.db = db
        try{
            def conn = new URL(websiteAddress).openConnection()
            if (conn.responseCode == 200 || conn.responseCode == 201){
                this.responseCode = conn.responseCode
                this.contentType = conn.contentType
                this.modified = conn.lastModified
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
        if (this.responseCode == 200 || this.responseCode == 201){
            def page = [url: this.url, content_type: this.contentType, oid: this.next(), modified: this.modified, added: this.created, indexed: this.indexed]
            this.db.links.save(page)
        }
    }
}
