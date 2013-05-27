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
import me.pedrazas.simplesearch.SimpleSearchUtils

class SimpleSearchUtilsTest extends GroovyTestCase {

    def formats = ["html", "pdf"]
    def base_urls = ["http://localhost", "http://ivan.pedrazas", "http://simplesearch."]

    void testIndexerPage() {
        def url = "http://alocalhost/docs/index.html"
        def res = SimpleSearchUtils.isIndexable(url, formats, base_urls)
        assertTrue(res)

        url = "http://alocalhost2/docs/index.html?somethinhg=12234"
        res = SimpleSearchUtils.isIndexable(url, formats, base_urls)
        assertTrue(res)

        url = "http://alocalhost/docs/index.pdf?somethinhg=12234"
        res = SimpleSearchUtils.isIndexable(url, formats, base_urls)
        assertTrue(res)

        url = "http://localhost/docs/"
        res = SimpleSearchUtils.isIndexable(url, formats, base_urls)
        assertTrue(res)

    }

    void testProcessURL(){
        def host = "http://localhost"
        def base = "http://localhost/docs/"
        def test_urls = [   "http://localhost/docs/index.html",
                            "./any/page.html",
                            "http://localhost/docs/../root.html",
                            "/something/else.html"
                        ]
        def result = [  "http://localhost/docs/index.html",
                        "http://localhost/docs/any/page.html",
                        "http://localhost/root.html",
                        "http://localhost/something/else.html"

                     ]
        test_urls.each{
            def res = SimpleSearchUtils.processURL(host, base, it)
            assertTrue(res in result)
        }



    }
}
