package nz.ac.auckland.mail

import org.junit.Ignore
import org.junit.Test

/**
 * @author Marnix Cook
 */
class MailFetcherTest extends GroovyTestCase {

    public static final String BASE_URL = "http://localhost/poc/remote-email/getMail.php?id="

    /**
     * Test that the remote fetching and json marshalling is working
     */
    @Ignore
    public void getFromRemote() {

        // retrieve mail content
        RemoteMailFetcher f = [
                getEndpoint : {
                    return BASE_URL
                }
            ] as RemoteMailFetcher

        MailContent content = f.getEmailById("remote.test.mail")

        // assertions
        assertNotNull(content)
        assertEquals("remote.test.mail", content.id)
        assertEquals("Hi {{name}},\nThis is my email.\n\nIt is awesome.", content.plain)
        assertEquals("<h2>Hi {{name}}<h2><h1>This is my email.</h1><p>It is awesome</p><img src=\"https://www.google.co.nz/images/srpr/logo11w.png\" alt=\"Google Logo\">", content.html)
    }

}
