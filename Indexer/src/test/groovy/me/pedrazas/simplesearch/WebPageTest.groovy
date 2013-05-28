
package me.pedrazas.simplesearch

import groovy.util.GroovyTestCase
import me.pedrazas.simplesearch.WebPage
import org.apache.commons.codec.binary.Base64

class WebPageTest extends GroovyTestCase {
    void testBigFileToBase64() {
        def url = "http://localhost/books/Closure_%20The%20Definitive%20Guide/Closure_%20The%20Definitive%20Guide.PDF"
        // def url = "http://localhost/docs/index.html"
        def w = new WebPage(url)
        def file = WebPage.fetchContent(url)
        assertNotNull file
    }
    void testUrl() {
        def url = "http://localhost/grails-docs-2.2.2/api/org/codehaus/groovy/grails/web/converters/configuration/ConvertersConfigurationHolder.html"
        def w = new WebPage(url)
        def file = WebPage.fetchContent(url)
        assertNotNull file
    }

    void testFetchContent(){
        def url = "http://localhost/grails-docs-2.2.2/api/org/codehaus/groovy/grails/web/converters/configuration/ConvertersConfigurationHolder.html"
        def file = WebPage.fetchContent(url)
        assertNotNull file
        byte[] decoded = Base64.decodeBase64(file);
        println "Base 64 Decoded  String : " + new String(decoded)
    }
}
