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
import org.apache.commons.codec.binary.Base64OutputStream

class SimpleSearchUtils{

    static def isValidFormat(String url, List<String> formats){
        def flag = false
        if(url.contains('.'))
            formats.each{
                if(url.contains('.' + it))
                    flag = true
            }
        return flag
    }

    static def isValidBaseUrl(String url, List<String> base_urls){
        def flag = false
        base_urls.each{
            if(url.startsWith(it))
                flag = true
        }
        return flag
    }

    static def isIndexable(String url, List<String> formats, List<String> base_urls){
        def flag = false
        if(isValidFormat(url, formats))
            flag = true
        if(isValidBaseUrl(url, base_urls))
            flag = true
        return flag
    }

    // fixes relative links and discards unwanted protocols
    static def processURL(host, base, url) {
        url = stripQueryStringAndHashFromPath(url.replaceAll(" ","%20")) //
        if (url.startsWith('http://') || url.startsWith('https://')){
            return  new URI(url).normalize().toURL().toString()
        }else if (url.startsWith('/')){
            return new URI(host + url).normalize().toURL().toString()
        }else{
            return new URI(base + url).normalize().toURL().toString()
        }
    }

    static def stripQueryStringAndHashFromPath(url) {
        // if(!(url.startsWith('#') || url.startsWith('?'))){
        if(!url.startsWith('#'))
            return url.split("\\?")[0].split("#")[0];
        return ""
    }

    static def extractLinks(String url, Map<String, Boolean> linkMap){
        def parser = new SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        try{
            // extract links from current URL
            url = addSlash(url)
            def host = (url =~ /(http:\/\/[^\/]+)\/?.*/)[0][1]
            def base = url[0..url.lastIndexOf('/')]
            //base = new URI(base).normalize().toURL().toString()
            def page = new XmlParser(parser).parse(url)
            def links = page.depthFirst().A.grep { it.@href }.'@href'
            if(links){
                //  fix and put all new links to map, visited set to false
                links.each {
                    def newURL = SimpleSearchUtils.processURL(host, base, it)
                    if(newURL != null){
                        if (linkMap.containsKey(newURL) == false){
                            linkMap.put(newURL, false)
                        }
                    }
                }
                println "Url Candidates ${links.size()} URLs. Total: ${linkMap.size()}"
            }else
                println "No links found"
        }catch (FileNotFoundException) {
            println "URL Not Found ${url}"
            println "ERROR ${FileNotFoundException.printStackTrace()}"
        }
    }

    // normalise urls finishing on a directory
    static def addSlash(String url){
        def ini = url.lastIndexOf('/')
        def end = url.length()-1
        if(ini<end){
            if(!url[ini..end].contains('.')){
                return url + '/'
            }
        }
        return url
    }

    static String fetchContent(String url){
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Base64OutputStream b64os = new Base64OutputStream(byteOut);
        b64os << new URL(url).openStream()
        return new String(byteOut.toByteArray());
    }
}
