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

class Crawler{

    Map<String, Boolean> linkMap
    def parser
    def formats = ["html", "pdf"]
    def base_urls = ["http://localhost", "http://ivan.pedrazas", "http://simplesearch.pedrazas"]

    static main(args) {
        String[] urls = ["http://localhost/"]
        def c = new Crawler(urls)
        c.run()
    }

    def Crawler(String[] urls){
        this.linkMap = new HashMap<String, Boolean>()
        for(String crawlUrl : urls)
            this.linkMap.put(crawlUrl, false)
    }

    def run(){
        def e = this.linkMap.find {it.getValue() == false}
        while (e != null) {
            def url = e.getKey()
            try {
                println "Parsing ${url}"
                e.setValue(true)
                if(this.isValidUrl(url)){
                    def w = new WebPage(url)
                    w.save()
                    SimpleSearchUtils.extractLinks(url, this.linkMap)
                }

                e = this.linkMap.find {it.getValue() == false}
            }
            catch (Exception) {
                println "ERROR ${Exception.printStackTrace()}"
                System.exit(-1)
            }
        }
    }

    def addUrl(String url){
        this.base_urls.each{ base_url ->
            if(url.startsWith(base_url)){
                return true
            }
        }
        return false
    }
}

