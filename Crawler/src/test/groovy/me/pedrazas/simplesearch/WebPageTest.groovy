

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
