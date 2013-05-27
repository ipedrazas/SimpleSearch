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
import me.pedrazas.simplesearch.WebPage

class WebPageTest extends GroovyTestCase {
    void testBigFileToBase64() {
        def url = "http://localhost/docs/books/Learning%20iOS%20Programming,%202nd%20Edition/Learning%20iOS%20Programming,%202nd%20Edition.pdf"
        def w = new WebPage(url)
    }
    void testWebPage() {
        def url = "http://localhost/docs/index.html"
        def w = new WebPage(url)
    }

    void testSaveWebPage() {
        def url = "http://localhost/docs/index.html"
        def w = new WebPage(url)
        w.save()
    }
}
