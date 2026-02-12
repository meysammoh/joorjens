package ir.joorjens.common.email;

import ir.joorjens.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailSend implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSend.class);
    private static EmailInfo[] emailInfoArr;
    private static String codeReplacement, activationSubject, activationBody;
    private static String resetSubject, resetBody;

    public static void loadEmailInfo() throws IOException {
        final Properties prop = new Properties();
        final URL address = EmailSend.class.getClassLoader().getResource("email.properties");
        if (address == null) {
            throw new IOException("email.properties not found");
        }
        final URLConnection connConfig = address.openConnection();
        prop.load(new InputStreamReader(connConfig.getInputStream(), Charset.forName("UTF-8")));

        codeReplacement = prop.getProperty("codeReplacement");
        activationSubject = prop.getProperty("activationSubject");
        activationBody = prop.getProperty("activationBody");
        resetSubject = prop.getProperty("resetSubject");
        resetBody = prop.getProperty("resetBody");
        final String json = prop.getProperty("emails");
        final EmailInfo[] emailInfoArr = (EmailInfo[]) Utility.fromJson(json, EmailInfo[].class);
        final List<EmailInfo> emailInfoList = new ArrayList<>();
        for (EmailInfo ei : emailInfoArr) {
            if (ei.isValid()) {
                emailInfoList.add(ei);
            }
        }
        if (emailInfoList.size() == 0) {
            throw new IOException(String.format("%s is not valid, please check config file!", EmailInfo.class.getName()));
        }
        LOGGER.info(String.format("EmailInfo list size: %d", emailInfoList.size()));
        EmailSend.emailInfoArr = emailInfoList.toArray(new EmailInfo[emailInfoList.size()]);
    }

    //------------------------------------------------------------------------------------------------------------------

    private static int counter = 0;

    public static synchronized int getCounter() {
        return ++counter % emailInfoArr.length;
    }

    public static boolean send(final Email email) {
        boolean status = true;
        if (email == null || email.isEmptyMailTo()) {
            LOGGER.error("MailTo is empty.");
            return false;
        }
        LOGGER.info("Sending " + email.getMailToSize() + " email ...");

        final EmailInfo emailInfo = emailInfoArr[getCounter()];
        final MimeMessage message = new MimeMessage(emailInfo.getSession());
        try {
            message.setFrom(new InternetAddress(emailInfo.getEmail()));
            message.setSubject(email.getSubject());
            final Multipart multipart = new MimeMultipart();
            final BodyPart bodyPart = new MimeBodyPart();
            switch (email.getEmailType()) {
                case HTML:
                case HTML_UTF8:
                    bodyPart.setContent(email.getBody(), email.getEmailType().getName());
                    break;
                case TEXT:
                default:
                    bodyPart.setText(email.getBody());
                    break;
            }
            multipart.addBodyPart(bodyPart);
            if (!email.isEmptyAttachment()) {
                MimeBodyPart bodyPartAttachment = new MimeBodyPart();
                DataSource source = new FileDataSource(email.getAttachment());
                bodyPartAttachment.setDataHandler(new DataHandler(source));
                bodyPartAttachment.setFileName(email.getAttachment());
                multipart.addBodyPart(bodyPartAttachment);
            }
            message.setContent(multipart);
        } catch (MessagingException e) {
            LOGGER.error("Creating Message. Message: " + e.getMessage());
            return false;
        }

        for (String mailTo : email.getMailTo()) {
            try {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            } catch (MessagingException e) {
                LOGGER.error("Setting Recipient of Message to " + mailTo + ". Message: " + e.getMessage(), EmailSend.class);
                status = false;
                continue;
            }

            if (email.isEmptySubject()) {
                LOGGER.warn("Subject is empty. MailTo: " + mailTo);
            }
            if (email.isEmptyBody()) {
                LOGGER.warn("Body is empty. MailTo: " + mailTo);
            }

            try {
                Transport.send(message);
            } catch (MessagingException e) {
                LOGGER.error("Sending Message to " + mailTo + ". Message: " + e.getMessage());
                status = false;
            }
            LOGGER.info("Message was sent to " + mailTo + " successfully.");
        }

        return status;
    }

    public static void sendEmail(String emailAddress, int code, boolean activationOrPass) {
        final String subject = (activationOrPass) ? activationSubject : resetSubject;
        String body = (activationOrPass) ? activationBody : resetBody;
        body = body.replaceAll(codeReplacement, code + "");
        final Email email = new Email(EmailType.HTML_UTF8, subject, body);
        email.addMailTo(emailAddress);
        boolean send = EmailSend.send(email);
        if (send) {
            LOGGER.info(String.format("Email(%s) was sent to %s with Code: %d", activationOrPass, emailAddress, code));
        } else {
            LOGGER.error(String.format("Email(%s) was sent to %s with Code: %d", activationOrPass, emailAddress, code));
        }
    }

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws IOException {
        LOGGER.info("Sending Email ....");
        EmailSend.loadEmailInfo();
        URL resource = EmailSend.class.getClassLoader().getResource("email.properties");
        String subject = "راهنمای فعال‌سازی حساب کاربری", //
                body = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        " <head>\n" +
                        "  <style type='text/css'>\n" +
                        "    fieldset {\n" +
                        "         display: inline-block;\n" +
                        "    }\n" +
                        "    .first {\n" +
                        "         width: 20%;\n" +
                        "    }\n" +
                        "    .second {\n" +
                        "         width: 40%;\n" +
                        //"         text-align: center;\n" +
                        "    }\n" +
                        "  </style>\n" +
                        " </head>\n" +
                        " <body dir=\"rtl\">\n" +
                        "  <p><b>خوش آمدید!</b></p>" +
                        "  <p><b>بابت ثبت‌نام شما در فروشگاه جورجنس سپاسگزاریم!</b></p>" +
                        "  <br>" +
                        "  <fieldset>\n" +
                        "   <legend>حساب کاربری شما ایجاد شده است و شما می‌توانید با استفاده از اطلاعات زیر حساب‌تان را فعال کنید:</legend>" +
                        "   <table> \n" +
                        "    <tr>\n" +
                        "     <td class='first'><b>کد و آدرس فعال‌سازی:</b></td>\n" +
                        "     <td class='second'><b><a href=\"http://joorjens.ir/activate?code=10000\">10000</a></b></td>\n" +
                        "    </tr>\n" +
                        "   </table>\n" +
                        "  </fieldset>\n" +
                        "  <br>" +
                        "  <br>" +
                        "  <p><b>با سپاس</b></p>" +
                        "  <p><b>فروشگاه جورجنس</b></p>" +
                        " </body>\n" +
                        "</html>", //
                attachment = resource != null ? resource.getPath() : null;
        Email email = new Email(EmailType.HTML_UTF8, subject, body, attachment);
        email.addMailTo("khalil.alijani@gmail.com");
//		email.addMailTo("art_z_72@yahoo.com");
//		email.addMailTo("zeynab.qorbansiyahi@gmail.com");
        email.addMailTo("khalil.alijani@yahoo.com");
        boolean send = EmailSend.send(email);
        if (send) {
            LOGGER.info("email was sent!");
        } else {
            LOGGER.error("email was not sent!");
        }
    }

}