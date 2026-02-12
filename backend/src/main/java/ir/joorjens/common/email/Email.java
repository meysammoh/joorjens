package ir.joorjens.common.email;

import ir.joorjens.common.Utility;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Email implements Serializable {

    private String subject, body, attachment;
    private Set<String> mailToList = new HashSet<>();
    private EmailType emailType;

    public Email(String subject, String body) {
        this(EmailType.TEXT, subject, body, null);
    }

    public Email(EmailType emailType, String subject, String body) {
        this(emailType, subject, body, null);
    }

    public Email(String subject, String body, String attachment) {
        this(EmailType.TEXT, subject, body, attachment);
    }

    public Email(EmailType emailType, String subject, String body, String attachment) {
        setSubject(subject);
        setBody(body);
        setAttachment(attachment);
        this.emailType = emailType;
    }

    public boolean isEmptyMailTo() {
        return mailToList.isEmpty();
    }

    public boolean addMailTo(String mailTo) {
        if (Utility.validateEmail(mailTo)) {
            this.mailToList.add(mailTo);
            return true;
        }
        return false;
    }

    public boolean removeFromMailTo(String mailTo) {
        return !Utility.isEmpty(mailTo) && this.mailToList.remove(mailTo);
    }

    public void clearFromMailTo() {
        this.mailToList.clear();
    }

    public int getMailToSize() {
        return mailToList.size();
    }

    public Set<String> getMailTo() {
        return mailToList;
    }

    public boolean isEmptySubject() {
        return Utility.isEmpty(subject);
    }

    public void setSubject(String subject) {
        if (!Utility.isEmpty(subject))
            this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isEmptyBody() {
        return Utility.isEmpty(body);
    }

    public void setBody(String body) {
        if (!Utility.isEmpty(body))
            this.body = body;
    }

    public String getBody() {
        return body;
    }

    public boolean isEmptyAttachment() {
        return Utility.isEmpty(attachment);
    }

    public void setAttachment(String attachment) {
        if (!Utility.isEmpty(attachment))
            this.attachment = attachment;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setEmailType(EmailType emailType) {
        if (emailType != EmailType.OTHER)
            this.emailType = emailType;
    }

    public EmailType getEmailType() {
        return emailType;
    }
}