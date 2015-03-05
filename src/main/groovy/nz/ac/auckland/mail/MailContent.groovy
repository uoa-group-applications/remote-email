package nz.ac.auckland.mail

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Marnix Cook
 *
 * The JSON Object that is received from the remote server.
 */
class MailContent {

	/**
	 * The ID
	 */
	@JsonProperty("Id")
	String id

	/**
	 * The mail subject
	 */
	@JsonProperty("Subject")
	String subject;

	/**
	 * The plain text version
	 */
	@JsonProperty("Plain")
	String plain

	/**
	 * The HTML version
	 */
	@JsonProperty("HTML")
	String html

	@JsonIgnore
	Map<String, String> headers = [:];

}


