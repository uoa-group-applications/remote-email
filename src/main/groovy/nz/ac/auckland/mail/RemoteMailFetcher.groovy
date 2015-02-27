package nz.ac.auckland.mail

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import net.stickycode.stereotype.configured.PostConfigured
import nz.ac.auckland.common.config.ConfigKey
import nz.ac.auckland.common.stereotypes.UniversityComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
* @author Marnix Cook
*
* The mail fetcher class retrieves content from a remote server and marshals the json content
* into a {@link MailContent} instance.
*/
@CompileStatic
@UniversityComponent
public class RemoteMailFetcher {

	private static final Logger LOG = LoggerFactory.getLogger(RemoteMailFetcher)

	/**
	* Cache content
	*/
	private Map<String, String> contentCache = [:];

	/**
	* Mails that should be preloaded
	*/
	@ConfigKey("remote.mail.preload")
	private String preloadThese;

	/**
	* Base URL
	*/
	@ConfigKey("remote.mail.endpoint")
	private String endpoint;

	/**
	* If remote mail identifiers were setup they will be preloaded, or
	* application will fail with exception when they don't exist.
	*/
	@PostConfigured
	public void configurationCompleted() {
		if (!preloadThese) {
			LOG.info("Nothing to preload.")
			return;
		}

		preloadThese.split(",").each { String remoteId ->
			remoteId = remoteId.trim();
			MailContent email = this.getEmailById(remoteId);

			// not loaded properly?
			if (!email) {
				throw new IllegalStateException(
					String.format(
						"The remote mail with identifier `%s` does not exist, will not continue application startup", remoteId
					)
				);
			}
		}
	}

	/**
	* Get the email from a remote source
	*
	* @param remoteId is the remote id to retrieve from the server
	* @return the mail content object that has the json values from the endpoint mapped to it
	*/
	public MailContent getEmailById(String remoteId) {
		ObjectMapper objMapper = new ObjectMapper();

		try {
			String endpointContents = getEndpointContentForId(remoteId)
			MailResponse response = objMapper.readValue(endpointContents, MailResponse);
			return response.fields
		}
		catch (JsonParseException jpEx) {
			LOG.error("The received JSON response was invalid, aborting.")
		}
		return null;
	}

	/**
	* This method retrieves the content from the endpoint
	*
	* @param remoteId is the remote mail ID to retrieve
	* @return the string with content
	* @throws FileNotFoundException when the URL is not found or if the server is offline
	*/
	protected String getEndpointContentForId(String remoteId) throws FileNotFoundException {
		try {
			// try to load it from the URL
			URL jsonEndpointUrl = getUrlForId(remoteId);
			URLConnection connection = jsonEndpointUrl.openConnection();
			String endpointContents = connection.inputStream.readLines()?.join("\n");

			// store latest version in the content cache
			contentCache[remoteId] = endpointContents;

			return endpointContents
		}
		catch (FileNotFoundException fnfEx) {
			LOG.error("404: The URL `${this.getEndpoint()}` does not exist, will try to return existing template content");

			// let's see if there was already some content for this one
			if (contentCache[remoteId]) {
				return contentCache[remoteId];
			}
		}
	}

	/**
	* @return the URL that is queried to get the email from the remote id
	*/
	protected URL getUrlForId(String remoteId) {
		return new URL(this.getEndpoint() + remoteId)
	}

	/**
	* @return the endpoint from which the JSON is fetched
	*/
	protected String getEndpoint() {
		return this.endpoint;
	}
}

