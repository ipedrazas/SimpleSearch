

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
}
