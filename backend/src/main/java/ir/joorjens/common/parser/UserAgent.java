package ir.joorjens.common.parser;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import ir.joorjens.common.Utility;

/**
 * Created by mehdi on 6/28/17.
 */
public class UserAgent {

    public String browserManufacturer, browserGroup;
    public String browserVersion;
    public String osManufacturer, osGroup;
    public String device;

    public static UserAgent getUserAgent(final String userAgentString) {
        eu.bitwalker.useragentutils.UserAgent ua = eu.bitwalker.useragentutils.UserAgent.parseUserAgentString(userAgentString);

        UserAgent userAgent = new UserAgent();
        Browser browser = ua.getBrowser();
        if (!"UNKNOWN".equals(browser.toString().toUpperCase())) {
            if (ua.getBrowserVersion() != null) {
                userAgent.browserVersion = ua.getBrowserVersion().toString().toUpperCase();
            }
            if (browser.getManufacturer() != null) {
                userAgent.browserManufacturer = browser.getManufacturer().toString().toUpperCase();
                if (!"OTHER".equals(userAgent.browserManufacturer)) {
                    userAgent.browserManufacturer = Utility.toFirstUpper(userAgent.browserManufacturer);
                } else {
                    userAgent.browserManufacturer = null;
                }
            }
            if (browser.getGroup() != null) {
                userAgent.browserGroup = browser.getGroup().toString().toUpperCase();
                if (!"OTHER".equals(userAgent.browserGroup)) {
                    userAgent.browserGroup = Utility.toFirstUpper(userAgent.browserGroup);
                } else {
                    userAgent.browserGroup = null;
                }
            }
        }

        OperatingSystem os = ua.getOperatingSystem();
        if (!"UNKNOWN".equals(os.toString().toLowerCase())) {
            if (os.getManufacturer() != null) {
                userAgent.osManufacturer = os.getManufacturer().toString().toUpperCase();
                if (!"OTHER".equals(userAgent.osManufacturer)) {
                    userAgent.osManufacturer = Utility.toFirstUpper(userAgent.osManufacturer);
                } else {
                    userAgent.osManufacturer = null;
                }
            }
            if (os.getGroup() != null) {
                userAgent.osGroup = os.getGroup().toString().toUpperCase();
                if (!"OTHER".equals(userAgent.osManufacturer)) {
                    userAgent.osGroup = Utility.toFirstUpper(userAgent.osGroup);
                } else {
                    userAgent.osGroup = null;
                }
            }
            if (os.getDeviceType() != null) {
                userAgent.device = os.getDeviceType().toString().toUpperCase();
                if (!"OTHER".equals(userAgent.device)) {
                    userAgent.device = Utility.toFirstUpper(userAgent.device);
                } else {
                    userAgent.device = null;
                }
            }
        }

        return userAgent;
    }

    public static void main(String[] args) {
        String[] arr = {"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3"
                , "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
                , "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.4 (KHTML, like Gecko) Chrome/5.0.375.999 Safari/533.4"
                , "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/533.4 (KHTML, like Gecko) Chrome/5.0.375.99 Safari/533.4"
                , "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.5) Gecko/2008121718 Gentoo Firefox/3.0.5"
        };

        for (String userAgentString : arr) {
            UserAgent userAgent = getUserAgent(userAgentString);
            System.out.println(userAgent.browserManufacturer + " " + userAgent.browserGroup + " " + userAgent.browserVersion);
            System.out.println(userAgent.osManufacturer + " " + userAgent.osGroup);
            System.out.println(userAgent.device);
            System.out.println();
        }

    }
}
