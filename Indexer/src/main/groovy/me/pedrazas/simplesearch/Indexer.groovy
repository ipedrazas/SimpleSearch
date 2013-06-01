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


import com.mongodb.Mongo;
import com.mongodb.BasicDBObject;

class Indexer{
    def mongo = new Mongo()
    def db = mongo.getDB("simpleSearch")
    static main(args) {
        def indexer = new Indexer()
        indexer.run()
    }

    def run(){
        // content_type: "text\html",
        def links = db.getCollection("links");
        links.find(new BasicDBObject("indexed", false)).each{
            try{
                def file = SimpleSearchUtils.fetchContent(it.url)
                if(file!=null){
                    ElasticSearchSender.send('simple', 'webpage', it.oid, it.url, it.content_type, file)
                    println "Indexed OK! ${it._id} - ${it.url}"
                    // update doc
                    def doc = new BasicDBObject()
                    doc.append("\$set", new BasicDBObject().append("indexed", true).append("indexed_date", new Date()))
                    BasicDBObject searchQuery = new BasicDBObject()
                    searchQuery.put("_id", it.id)
                    links.update(searchQuery, doc)
                }
            }catch(FileNotFoundException){
                FileNotFoundException.printStackTrace()
                println "File not found: ${it.url}"
            }catch(Exception){
                Exception.printStackTrace()
            }
        }
    }
}
