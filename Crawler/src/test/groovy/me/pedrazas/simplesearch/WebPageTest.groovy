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

import groovy.util.GroovyTestCase
import com.gmongo.GMongo
import me.pedrazas.simplesearch.WebPage


import com.foursquare.fongo.Fongo;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;

class WebPageTest extends GroovyTestCase {

    Fongo fongo = new Fongo("mongo server 1");
    DB db = fongo.getDB("mydb");

    void testBigFileToBase64() {
        def url = "http://www.fsa.usda.gov/Internet/FSA_File/tech_assist.pdf"
        def w = new WebPage(url, db)
    }
    void testWebPage() {
        def url = "http://ivan.pedrazas.me/?p=122"
        def w = new WebPage(url, db)
    }

    void testSaveWebPage() {
        def url = "http://ivan.pedrazas.me/?p=122"
        def w = new WebPage(url, db)
        w.save()
    }
}
