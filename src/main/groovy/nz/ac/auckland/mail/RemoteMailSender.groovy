package nz.ac.auckland.mail

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import nz.ac.auckland.common.config.ConfigKey
import nz.ac.auckland.common.stereotypes.UniversityComponent
import org.apache.commons.mail.ImageHtmlEmail
import org.apache.commons.mail.resolver.DataSourceUrlResolver

/**
 * @author Marnix Cook
 *
 * This class is able to send the mail From: a specific address To: a specific address using
 * the context as retrieved from the external server, using the context that parses the moustache
 * template.
 */
@UniversityComponent
public class RemoteMailSender {

	@ConfigKey("mail.smtp.host")
	private String smtpHost;

	@ConfigKey("mail.smtp.port")
	private Integer smtpPort;

	@ConfigKey("mail.remote.assetbase")
	private String assetBaseUrl;

	/**
	 * Send this email.
	 *
	 * @param mail is the mail to send
	 * @param from is the originator
	 * @param to is the user to send it to
	 * @param context contains the information that is to be merged into the email
	 */
	public void send(MailContent mail, String from, String to, Map<String, String> context = [:]) {
		ImageHtmlEmail imageMail = new ImageHtmlEmail();

		imageMail.dataSourceResolver = new DataSourceUrlResolver(getAssetBaseURL(), true)

		// setup host
		imageMail.hostName = getSmtpHost()
		imageMail.smtpPort = getSmtpPort()

		// setup mail content

		imageMail.textMsg = compileText(mail.plain, context)
		imageMail.htmlMsg = compileText(mail.html, context)
		imageMail.from = from
		imageMail.addTo(to);

		// set the headers
		imageMail.headers = mail.headers;

		imageMail.subject = mail.subject;

		// send the e-mail
		imageMail.send()
	}

	/**
	 * Compile the template using mustache.
	 *
	 * @param contents is the contents to parse
	 * @param context is the context to parse the template with
	 * @return the compiled string
	 */
	protected String compileText(String contents, Map<String, String> context) {
		MustacheFactory mFactory = new DefaultMustacheFactory()
		Mustache mTemplate = mFactory.compile(new StringReader(contents), "mailTemplate")

		StringWriter stringWriter = new StringWriter()
		mTemplate.execute(stringWriter, context)
		return stringWriter.toString()
	}

	/**
	 * @return the smtp port
	 */
	protected int getSmtpPort() {
		return this.smtpPort
	}

	/**
	 * @return the smtp host
	 */
	protected String getSmtpHost() {
		return this.smtpHost
	}

	/**
	 * @return asset base url
	 */
	protected URL getAssetBaseURL() {
		return new URL(assetBaseUrl)
	}

}


