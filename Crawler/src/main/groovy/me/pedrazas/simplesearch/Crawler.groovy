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

import org.cyberneko.html.parsers.SAXParser

class Crawler{

    Map<String, Boolean> linkMap
    def parser
    def formats = ["html", "pdf"]
    def base_urls = ["http://localhost", "http://ivan.pedrazas", "http://simplesearch."]

    static main(args) {
        String[] urls = ["http://localhost/docs/"]
        def c = new Crawler(urls)
        c.run()

    }

    def Crawler(String[] urls){
        println urls
        this.linkMap = new HashMap<String, Boolean>()
        // linkMap.put("http://localhost/docs/index.html", false)
        for(String crawlUrl : urls)
            this.addUrl(crawlUrl)
        this.parser = new SAXParser()
        this.parser.setFeature('http://xml.org/sax/features/namespaces', false)
    }

    def addUrl(String url){
        this.linkMap.put(url, false)
    }


    def run(){
        def e = this.linkMap.find {it.getValue() == false}
        while (e != null) {
            def url = e.getKey()
            try {
                // get URL address from map value

                println "URL: ${url}"
                e.setValue(true)
                if(url.startsWith('http://localhost')){
                    def w = new WebPage(url)
                    w.save()
                }
                // extract links from current URL
                def host = (url =~ /(http:\/\/[^\/]+)\/?.*/)[0][1]
                def base = url[0..url.lastIndexOf('/')]
                def page = new XmlParser(parser).parse(url)
                def links = page.depthFirst().A.grep { it.@href }.'@href'

                //  fix and put all new links to map, visited set to false
                links.each {
                    def newURL = processURL(host, base, it)
                    if(newURL != null)
                        if (this.linkMap.containsKey(newURL) == false)
                            this.linkMap.put(newURL, false)
                }
                println "Adding ${links.size()} URLs. Total: ${linkMap.size()}"
            }
            catch (FileNotFoundException) {
                println "URL Not Found ${url}"
                println "ERROR ${FileNotFoundException.printStackTrace()}"
            }
            catch (Exception) {
                println "ERROR ${Exception.printStackTrace()}"
                System.exit(-1)
            }
            e = this.linkMap.find {it.getValue() == false}
        }
    }

    // fixes relative links and discards unwanted protocols
    def processURL(host, base, url) {
        url = stripQueryStringAndHashFromPath(url.replaceAll(" ","%20"))
        if(host.startsWith('http://localhost')){
            if (url.startsWith('http://') || url.startsWith('https://')){
                return  new URI(url).normalize().toURL().toString()
            }else if (url.startsWith('/')){
                println "(HOST) URL : ${url}"
                return new URI(host + url).normalize().toURL().toString()
            }else{
                println "(BASE) URL : ${url}"
                return new URI(base + url).normalize().toURL().toString()
            }
        }else{
            return null
        }
    }

    def stripQueryStringAndHashFromPath(url) {
        // if(!(url.startsWith('#') || url.startsWith('?'))){
        if(!url.startsWith('#'))
            return url.split("\\?")[0].split("#")[0];
        return ""
    }


}

