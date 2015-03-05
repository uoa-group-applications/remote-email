package nz.ac.auckland.mail

import org.junit.Ignore
import org.junit.Test

/**
 * @author Marnix Cook
 *
 * Test the sending of the meail
 */
class MailSenderTester extends GroovyTestCase {

	public static final String PLAIN_TEXT = "Test"
	public static final String HTML_TEXT = "<p>Test</p>"

	public static final String BASE_URL = "http://localhost/poc/remote-email/getMail.php?id="

	@Ignore
	public void sendMail() {
		RemoteMailSender s = [

			getSmtpPort : {-> 2025 },
			getSmtpHost : {-> "localhost" },
			getAssetBaseURL : {-> new URL("http://someurlwedontcareabout.com") }

		] as RemoteMailSender;

		// retrieve mail content
		RemoteMailFetcher f = [
				getEndpoint : {
					return BASE_URL
				}
		] as RemoteMailFetcher

		MailContent content = f.getEmailById("test.id");

		s.send(
			content,
			"No reply <noreply@auckland.ac.nz>",
			"Marnix Cook <m.cook@auckland.ac.nz>", [
					name: "Marnix"
			]
		)
	}

	protected MailContent getTestMailContent(String remoteId) {
		MailContent content = new MailContent()
		content.id = remoteId;
		content.plain = PLAIN_TEXT;
		content.html = HTML_TEXT
		return content;
	}

}


