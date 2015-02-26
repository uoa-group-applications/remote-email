package nz.ac.auckland.mail

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * @author Marnix Cook
 *
 * The JSON Object that is received from the remote server.
 */
class MailContent {

    /**
     * The ID
     */
    String id

    /**
     * The mail subject
     */
    String subject;

    /**
     * The plain text version
     */
    String plain

    /**
     * The HTML version
     */
    String html

    @JsonIgnore
    Map<String, String> headers = [:];

}
