package nz.ac.auckland.mail

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import nz.ac.auckland.common.config.ConfigKey
import nz.ac.auckland.common.stereotypes.UniversityComponent

/**
 * @author Marnix Cook
 *
 * The mail fetcher class retrieves content from a remote server and marshals the json content
 * into a {@link MailContent} instance.
 */
@CompileStatic
@UniversityComponent
class RemoteMailFetcher {

    /**
     * Base URL
     */
    @ConfigKey("remote.mail.endpoint")
    private String endpoint;

    /**
     * Get the email from a remote source
     *
     * @param remoteId is the remote id to retrieve from the server
     * @return the mail content object that has the json values from the endpoint mapped to it
     */
    public MailContent getEmailById(String remoteId) {
        URL jsonEndpointUrl = getUrlForId(remoteId);
        URLConnection connection = jsonEndpointUrl.openConnection();
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.readValue(connection.inputStream, MailContent);
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
