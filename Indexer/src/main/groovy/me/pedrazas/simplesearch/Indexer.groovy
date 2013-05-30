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

import com.gmongo.GMongo

class Indexer{
    def mongo = new GMongo()
    def db = mongo.getDB("simpleSearch")

    static main(args) {
        def indexer = new Indexer()
        indexer.run()
    }

    def run(){
        // content_type: "text\html",
        db.links.find(indexed: false).each{
            try{
                def file = SimpleSearchUtils.fetchContent(it.url)
                if(file!=null){
                    ElasticSearchSender.send('simple', 'webpage', it.oid, it.url, it.content_type, file)

                    println "Indexed OK! ${it._id} - ${it.url}"
                    db.links.update(
                                        [_id: it._id], //
                                        [$set:
                                            [indexed: true,                 //
                                             indexed_date: new Date()
                                             ],
                                             $inc: [ indexed_count: 1]     //
                                        ]
                                    )
                }
            }catch(FileNotFoundException){
                // FileNotFoundException.printStackTrace()
                println "File not found: ${it.url}"
            }catch(Exception){
                Exception.printStackTrace()
            }
        }
    }
}
