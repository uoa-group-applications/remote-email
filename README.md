# remote-email artifact

Loads email templates from remote server. 

Features:

* Images are automatically inlined.
* Moustache integration to deal with dynamic e-mails.
 
Expects the following configuration properties to be set:

* `mail.smtp.host`: the host of the SMTP server
* `mail.smtp.port`: the port of the SMTP server
* `mail.remote.assetbase`: if any images with relative paths are present in the template load them from this URL.
* `mail.remote.endpoint`: the JSON endpoint that is called appended with the remote id (something like: `http://myemailtemplate.auckland.ac.nz/?id=`)

The class below loads and sends a remote mail:

    @CompileStatic
    @UniversityComponent
    public class MyMailSender {
    
        @Inject private RemoteMailFetcher fetcher;
        @Inject private RemoteMailSender sender;
    
        public void sendModeratorEmail(String moderatorName, String moderatorMail) {
            
            MailContent content = fetcher.getEmailById("moderator.accepted");
            sender.send(
                content,
                 "noreply@auckland.ac.nz",
                 moderatorMail,
                 [
                    name: moderatorName
                 ]
            )
            
        }
        
    }

       

