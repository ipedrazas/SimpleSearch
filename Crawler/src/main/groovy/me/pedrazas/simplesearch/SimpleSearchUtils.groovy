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
}
