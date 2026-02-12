package ir.joorjens.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.ComparatorInterface;
import ir.joorjens.model.util.TypeEnumeration;
import org.jetbrains.annotations.Nullable;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.ResponseTransformer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Utility {
    private static final Logger log = LoggerFactory.getLogger(Utility.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    //-------------------------- JSON --------------------------

    public static Set<Long> getSetLong(final String str, final String split) {
        final Set<Long> set = new HashSet<>();
        if (!isEmpty(str)) {
            final String[] splitArr = str.split("\\s*" + split + "\\s*");
            for (String elem : splitArr) {
                set.add(Long.valueOf(elem));
            }
        }
        return set;
    }

    public static Set<Integer> getSetInteger(final String str, final String split) {
        final Set<Integer> set = new HashSet<>();
        if (!isEmpty(str)) {
            final String[] splitArr = str.split("\\s*" + split + "\\s*");
            for (String elem : splitArr) {
                set.add(Integer.valueOf(elem));
            }
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    public static Object fromJson(String json, Class clazz) {
        try {
            //return new Gson().fromJson(json, clazz);
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Exception@fromJson:" + json + " Message:" + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object fromJson(String json, TypeReference javaType) {
        try {
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Exception@fromJson:" + json + " Message:" + e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(String json) {
        try {
            return MAPPER.readValue(json, HashMap.class);
        } catch (IOException e) {
            log.error("Exception@getMap:" + json + " Message:" + e.getMessage());
        }
        return new HashMap<>();
    }

    public static String toJson(Object obj) {

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Exception@toJson:" + obj + " Message:" + e.getMessage());
        }
        return "{}";
    }

    public static String toXml(Object obj) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Exception@toXml:" + obj + " Message:" + e.getMessage());
        }
        return null;
    }

    public static Object fromXml(String xml, Class clazz) {
        if (xml != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                return jaxbUnmarshaller.unmarshal(new StringReader(xml)); //new File("C:\\file.xml")
            } catch (JAXBException e) {
                log.error("Exception@fromXml:" + xml + " Message:" + e.getMessage());
            }
        }
        return null;
    }

    public static ResponseTransformer json() {
        return Utility::toJson;
    }

    public static ResponseTransformer xml() {
        return Utility::toXml;
    }

    //-------------------------- JSON --------------------------

    //-------------------------- URI --------------------------
    public static String getRequestInfo(Request request, boolean header) {
        if (request == null) {
            return "No url";
        }
        StringBuilder urlSB = new StringBuilder(request.uri());
        if (request.queryString() != null) {
            urlSB.append('?').append(request.queryString());
        }
        if (header) {
            urlSB.append('#');
            int i = 0;
            for (String headerKey : request.headers()) {
                if (++i > 1) {
                    urlSB.append('&');
                }
                urlSB.append(headerKey).append('=').append(request.headers(headerKey));
            }
        }
        return urlSB.toString();

    }
    //-------------------------- URI --------------------------

    //-------------------------- UUID --------------------------
    public static UUID getUUID() {
        return UUID.randomUUID();
    }
    //-------------------------- UUID --------------------------

    //----------------------- Validation -----------------------
    public static boolean validIP(final String ip) {
        try {
            if (isEmpty(ip)) {
                return false;
            }
            final String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            for (String s : parts) {
                final int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean validateNationalIdentifier(final String nationalIdentifier) {
        if (nationalIdentifier == null || !nationalIdentifier.matches("\\d{10}")) { //{8,10}
            return false;
        }
        final char[] chars = nationalIdentifier.toCharArray();
        final int length = chars.length;
        int sum = 0;
        for (int i = 0, j = length; i < length - 1; i++, j--) {
            sum += j * Character.getNumericValue(chars[i]);
        }
        final int remain = sum % 11;
        final int control = Character.getNumericValue(chars[length - 1]);
        final boolean OK;
        if (remain < 2) {
            OK = (control == remain);
        } else {
            OK = (control == (11 - remain));
        }
        return OK;
    }

    public static boolean validateMobileNumber(String mobileNumber) {
        return mobileNumber != null && mobileNumber.matches("\\d{11}")
                && mobileNumber.charAt(0) == '0' && mobileNumber.charAt(1) == '9';
    }

    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches("\\d+");
    }

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateEmail(String email) {
        return email != null && email.matches(EMAIL_PATTERN);
    }

    public static String normalizeMobileNumber(String mobileNumber) {
        if (mobileNumber.charAt(0) == '0')
            return mobileNumber;
        else if (mobileNumber.startsWith("98"))
            return '0' + mobileNumber.substring(2);
        if (mobileNumber.startsWith("+98"))
            return '0' + mobileNumber.substring(3);
        else if (mobileNumber.charAt(0) == '9')
            return '0' + mobileNumber;
        else
            return mobileNumber;
    }

    public static String toLowerCase(String str) {
        if (str != null) {
            return str.trim().toLowerCase();
        }
        return null;
    }

    public static String toFirstUpper(String str) {
        if (str != null) {
            if (str.length() == 1) {
                return str.toUpperCase();
            } else if (str.length() > 1) {
                return str.substring(0, 1).toUpperCase()
                        + str.substring(1).toLowerCase();
            }
        }
        return str;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    //----------------------- Validation -----------------------

    //------------------------- Hashing -------------------------
    private static final String SALT = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";
    private static final HashFunction hf = Hashing.murmur3_128();
    private static final HashFunction HF_SHA = Hashing.sha1();

    public static String generatePassword(String password) {
        HashCode hc = Hashing.sha1().hashString(SALT + password + SALT, Charsets.UTF_8);
        return BaseEncoding.base64().encode(hc.asBytes());
    }

    public static String generatePassword(String password, String passwordRepeat) throws JoorJensException {
        if (isEmpty(password) || isEmpty(passwordRepeat)) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_EMPTY);
        }
        if (!password.equals(passwordRepeat)) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_NOT_EQUAL);
        }
        if (password.length() < 6) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_LENGTH);
        }
        /*
        if (!password.matches(".*\\d+.*")) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_NUMBER);
        }
        if (!password.matches(".*[a-z]+.*")) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_LETTER);
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_LETTER_CAPS);
        }
        if (!password.matches(".*\\p{Punct}.*")) {
            throw new JoorJensException(ExceptionCode.INVALID_PASSWORD_PUNCTUATION);
        }
        */
        return generatePassword(password);
    }

    public static String generateHash(String str) {
        return hf.newHasher()
                .putString(str, Charsets.UTF_8)
                .hash().toString();
    }

    public static String generateHashSha(String str) {
        return HF_SHA.newHasher()
                .putString(str, Charsets.UTF_8)
                .hash().toString();
    }

    public static byte[] compress(@Nullable String str) throws IOException {
        Preconditions.checkNotNull(str, "@compress: str is null");
        ByteArrayOutputStream out = new ByteArrayOutputStream(str.length());
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes(Charsets.UTF_8));
        gzip.close();
        byte[] bytes = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            log.warn(String.format("IOException@compress. Message: %s", e.getMessage()));
        }
        return bytes;
    }

    public static String decompress(@Nullable byte[] bytes) throws IOException {
        Preconditions.checkNotNull(bytes, "@decompress: bytes is null");
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream gzip = new GZIPInputStream(in);
        String ret = new String(ByteStreams.toByteArray(gzip), Charsets.UTF_8);
        try {
            gzip.close();
            in.close();
        } catch (IOException e) {
            log.warn(String.format("IOException@decompress. Message: %s", e.getMessage()));
        }
        return ret;
    }


    public static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encodeBase64(String str) {
        return encodeBase64(str.getBytes());
    }

    public static byte[] decodeBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static String decodeBase64Str(String str) {
        return new String(decodeBase64(str));
    }

    //------------------------- Hashing -------------------------

    public static double round(double value, int places) {
        if (places < 0) {
            places = 2;
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double getShare(long price, float percent) {
        return price * (percent / 100);
    }

    public static float getPrice(long price, float percent) {
        final float priceOff = price * (percent / 100);
        return price - priceOff;
    }

    public static float getPercent(long allPrice, long price) {
        return (float) ((price * 100.0) / (allPrice * 1.0));
    }

    //-------------------------- Time ---------------------------
    public static int getCurrentTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String getDate(Date date, String format) {
        SimpleDateFormat dt = new SimpleDateFormat(format);
        return dt.format(date);
    }

    public static float getHourMinute(long timeInSec) {
        Calendar calendar = Calendar.getInstance();
        if (timeInSec > 0) {
            calendar.setTimeInMillis(timeInSec * 1000);
        }
        int hour = calendar.get(Calendar.HOUR);
        if (Calendar.PM == calendar.get(Calendar.AM_PM)) {
            hour += 12;
        }
        int minute = calendar.get(Calendar.MINUTE);
        return getHour(hour, minute);
    }

    public static int getDay(long timeInSec, boolean jalali) {
        Calendar calendar = Calendar.getInstance();
        if (timeInSec > 0) {
            calendar.setTimeInMillis(timeInSec * 1000);
        }
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (jalali) {
            if (day == 7) { // 7 is Saturday, 1 is Sunday, ..., 6 is Friday
                day = 0;
            }
            return ++day;
        }
        return day;
    }

    public static int getYear(long timeInSec, boolean jalali) {
        Calendar calendar = Calendar.getInstance();
        if (timeInSec > 0) {
            calendar.setTimeInMillis(timeInSec * 1000);
        }
        if (jalali) {
            JalaliCalendar.YearMonthDate _date = new JalaliCalendar.YearMonthDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            JalaliCalendar.YearMonthDate jalali_date = JalaliCalendar.gregorianToJalali(_date);
            return jalali_date.getYear();
        }
        return calendar.get(Calendar.YEAR);
    }

    public static TypeEnumeration getDayJalali(long timeInSec) {
        int day = getDay(timeInSec, true) + TypeEnumeration.DAY_OF_WEEK.getId();
        return TypeEnumeration.get(day);
    }

    public static boolean isCurrentDayEvenJalali() { //Shanbe: 1, 2Shanbe: 3, 4Shanbe: 5
        return getDay(0, true) % 2 == 1;
    }

    private static float getHour(int hour, int minute) {
        return hour + (minute / 60.0f);
    }

    public static float getHour(String str) {
        float number = -1;
        try {
            if (!isEmpty(str)) {
                String[] h = str.split(":");
                int hour = Integer.parseInt(h[0]);
                int minute = 0;
                if (h.length > 1) {
                    minute = Integer.parseInt(h[1]);
                }
                number = getHour(hour, minute);
            }
        } catch (Exception e) {
            log.error("@getHour" + e.getMessage());
        }
        return number;
    }

    public static String setHour(float hour) {
        int hourInt = (int) hour;
        return String.format("%02d:%02d", hourInt, (int) ((hour - hourInt) * 60));
    }

    public static String getTime(final int second) {
        final int day = second / Config.TIME_DAY_SEC;
        int tmp = second % Config.TIME_DAY_SEC;
        final int hour = tmp / Config.TIME_HOUR;
        tmp = tmp % Config.TIME_HOUR;
        final int min = tmp / Config.TIME_MIN;
        final int sec = tmp % Config.TIME_MIN;
        return String.format("%02d:%02d:%02d:%02d", day, hour, min, sec);
    }

    public static int getTimeRound(int time, TypeEnumeration timeStamp) {
        Calendar cal = Calendar.getInstance();
        if (time > 0) {
            cal.setTimeInMillis(time * 1000L);
        } else {
            cal.setTimeInMillis(System.currentTimeMillis());
        }
        String format = "%04d/%02d/%02d %02d:%02d:%02d";
        String dateFormat;
        switch (timeStamp) {
            case TS_HOUR:
                dateFormat = String.format(format
                        , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), 0, 0);
                break;
            case TS_DAY:
                dateFormat = String.format(format
                        , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                break;
            case TS_MONTH:
                dateFormat = String.format(format
                        , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
                break;
            case TS_YEAR:
            default:
                dateFormat = String.format(format
                        , cal.get(Calendar.YEAR), 1, 1, 0, 0, 0);
                break;
        }
        format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern(format);
        try {
            Date date = simpleDateFormat.parse(dateFormat);
            time = (int) (date.getTime() / 1000);
        } catch (ParseException e) {
            log.warn(String.format("ParseException@getTimeStampDay. Message: %s", e.getMessage()));
        }
        return time;
    }

    public static int getTimeStamp(int time, TypeEnumeration timeStamp, boolean jalali) {
        Calendar cal;
        if (jalali) {
            if (time > 0) {
                cal = new JalaliCalendar(time * 1000L);
            } else {
                cal = new JalaliCalendar();
            }
        } else {
            cal = Calendar.getInstance();
            if (time > 0) {
                cal.setTimeInMillis(time * 1000L);
            }
        }
        String ts;
        switch (timeStamp) {
            case TS_DAY:
                ts = String.format("%04d%02d%02d",
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                break;
            case TS_WEEK:
                ts = String.format("%04d%02d",
                        cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR));
                break;
            case TS_MONTH:
                ts = String.format("%04d%02d",
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                break;
            case TS_YEAR:
                ts = String.format("%04d", cal.get(Calendar.YEAR));
                break;
            default:
                ts = "0";
                break;
        }
        return Integer.parseInt(ts);
    }

    public static int getTime(String time, TypeEnumeration timeStamp, boolean jalali) {
        int year = 0, month = 1, day = 1, week = 0;
        switch (timeStamp) {
            case TS_DAY:
                day = Integer.parseInt(time.substring(6, 8));
            case TS_MONTH:
                month = Integer.parseInt(time.substring(4, 6));
            case TS_YEAR:
                year = Integer.parseInt(time.substring(0, 4));
                break;
            case TS_WEEK:
                week = Integer.parseInt(time.substring(4, 6));
                year = Integer.parseInt(time.substring(0, 4));
                break;
        }
        Calendar cal;
        if (jalali) {
            cal = new JalaliCalendar(year, month - 1, day);
        } else {
            cal = Calendar.getInstance();
            cal.set(year, month - 1, day, 0, 0, 0);
        }
        if (week > 0) {
            cal.set(Calendar.WEEK_OF_YEAR, week);
        }

        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static int getTime(String d, String format) {
        try {
            SimpleDateFormat dt = new SimpleDateFormat(format);
            Date date = dt.parse(d);
            return (int) date.getTime();
        } catch (Exception e) {
            return getCurrentTime();
        }
    }

    public static Pair<Integer, Integer> getTimeFromTo(String time, TypeEnumeration timeStamp, boolean jalali) {
        int year = 0, month = 1, day = 1, hour = 0, week = 0;
        switch (timeStamp) {
            case TS_HOUR:
                day = Integer.parseInt(time.substring(8, 10));
            case TS_DAY:
                day = Integer.parseInt(time.substring(6, 8));
            case TS_MONTH:
                month = Integer.parseInt(time.substring(4, 6));
            case TS_YEAR:
                year = Integer.parseInt(time.substring(0, 4));
                break;
            case TS_WEEK:
                week = Integer.parseInt(time.substring(4, 6));
                year = Integer.parseInt(time.substring(0, 4));
                break;
        }
        Calendar cal;
        if (jalali) {
            cal = new JalaliCalendar(year, month - 1, day, hour, 0);
        } else {
            cal = Calendar.getInstance();
            cal.set(year, month - 1, day, hour, 0, 0);
        }
        if (week > 0) {
            cal.set(Calendar.WEEK_OF_YEAR, week);
        }

        int timeFrom = (int) (cal.getTimeInMillis() / 1000);
        int timeTo;
        switch (timeStamp) {
            case TS_HOUR:
                timeTo = timeFrom + Config.TIME_HOUR;
                break;
            case TS_DAY:
                timeTo = timeFrom + Config.TIME_DAY_SEC;
                break;
            case TS_MONTH:
                int dayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                timeTo = timeFrom + Config.TIME_DAY_SEC * dayOfMonth;
                break;
            case TS_YEAR:
                int dayOfYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                timeTo = timeFrom + Config.TIME_DAY_SEC * dayOfYear;
                break;
            case TS_WEEK:
                int firstDayOfYear = Utility.getTimeFirstDayOfYear(timeFrom, jalali);
                int weekOfYear = cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
                if(jalali) {
                    firstDayOfYear = Utility.getDayOfWeek(firstDayOfYear, true);
                    if(week == weekOfYear) { // هفته آخر سال
                        timeFrom = timeFrom - Config.TIME_DAY_SEC * (7 - firstDayOfYear);
                        timeTo = timeFrom + Config.TIME_WEEK_SEC;
                    } else if(week == 1) { //هفته اول سال
                        int firstDayOfLastYear = Utility.getTimeFirstDayOfYear(timeFrom - Config.TIME_MONTH_SEC, true);
                        firstDayOfLastYear = Utility.getDayOfWeek(firstDayOfLastYear, true);
                        timeFrom = timeFrom - Config.TIME_DAY_SEC * (7 - firstDayOfLastYear + 1);
                        timeTo = timeFrom + Config.TIME_WEEK_SEC;
                    } else if(week > 1) {
                        int dayOfWeek = getDayOfWeek(timeFrom, true);
                        if (dayOfWeek >= firstDayOfYear) {
                            timeFrom -= Config.TIME_DAY_SEC * (firstDayOfYear - 1);
                        }
                        timeTo = timeFrom + Config.TIME_WEEK_SEC;
                    } else {
                        timeTo = timeFrom + Config.TIME_DAY_SEC * firstDayOfYear;
                    }
                } else {
                    //firstDayOfYear = Utility.getDayOfWeek(firstDayOfYear, false);
                    timeTo = timeFrom + Config.TIME_DAY_SEC * 7;
                }
                break;
            default:
                timeTo = timeFrom;
                break;
        }

        return new Pair<>(timeFrom, timeTo);
    }

    public static int getTimeFromZero(String time) {
        String[] hourMinSec = time.split(":");
        int seconds = 0;
        for (int i = 0, j = hourMinSec.length - 1; i < hourMinSec.length; i++, j--) {
            seconds += (Integer.parseInt(hourMinSec[i].trim()) * Math.pow(60, j));
        }
        return seconds;
    }

    /**
     * <ul>
     * This function return remained milli seconds util a specified time
     * <li>It is correct for time lower than hour</li>
     * <li>for times larger than hour use {@link #getTimeUntilNextDay()} or {@link #getTimeRoundOfNextMonth()}</li>
     * </ul>
     * <ul>
     * for ex. if current time is 14:35:59.0, and you want to know the remained milli seconds until 14:40:0.0
     * <li>@periodInMilliSec: 5 * 60 * 1000. because your period is 5 minutes</li>
     * <li>@return: 241_000</li>
     * </ul>
     *
     * @param periodInMilliSec time < 60 * 60 * 1000
     * @return remained time until secondInMilliSec
     */
    public static long getTimeUntil(int periodInMilliSec) {
        return periodInMilliSec - (System.currentTimeMillis() % periodInMilliSec);
    }

    public static int getTimeUntilNextHour() {
        int currentTime = getCurrentTime();
        int nextHour = getTimeRound(currentTime + Config.TIME_HOUR, TypeEnumeration.TS_HOUR);
        return nextHour - currentTime;
    }

    public static int getTimeUntilNextDay() {
        int currentTime = getCurrentTime();
        int nextDay = getTimeRound(currentTime + Config.TIME_DAY_SEC, TypeEnumeration.TS_DAY);
        return nextDay - currentTime;
    }

    public static int getDayOfWeek(int timeInSec, boolean jalali) {
        Calendar calendar = Calendar.getInstance();
        if (timeInSec > 0) {
            calendar.setTimeInMillis(timeInSec * 1000L);
        }
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (jalali) {
            if (day == 7) { // 7 is Saturday, 1 is Sunday, ..., 6 is Friday
                day = 0;
            }
            return ++day;
        }
        return day;
    }

    public static int getDayOfMonth(int timeInSec, boolean jalali) {
        final Calendar cal;
        if (jalali) {
            if (timeInSec > 0) {
                cal = new JalaliCalendar(timeInSec * 1000L);
            } else {
                cal = new JalaliCalendar();
            }
        } else {
            cal = Calendar.getInstance();
            if (timeInSec > 0) {
                cal.setTimeInMillis(timeInSec * 1000L);
            }
        }
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getTimeRoundOfNextMonth() {
        int currentTime = getCurrentTime();
        int currentMonth = getTimeRound(currentTime, TypeEnumeration.TS_MONTH);
        currentTime += Config.TIME_MONTH_SEC;
        int nextMonth = getTimeRound(currentTime, TypeEnumeration.TS_MONTH);
        if (nextMonth == currentMonth) {
            currentTime += 2 * Config.TIME_DAY_SEC;
            nextMonth = getTimeRound(currentTime, TypeEnumeration.TS_MONTH);
        }
        return nextMonth;
    }

    public static int getTimeFirstDayOfYear(int time, boolean jalali) {
        Calendar cal;
        if (jalali) {
            if (time > 0) {
                cal = new JalaliCalendar(time * 1000L);
            } else {
                cal = new JalaliCalendar();
            }
        } else {
            cal = Calendar.getInstance();
            if (time > 0) {
                cal.setTimeInMillis(time * 1000L);
            }
        }
        cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    /**
     * @return Jalali
     */
    public static boolean isFirstOfMonth(boolean jalali) {
        final int one = getDayOfMonth(getCurrentTime(), jalali);
        return one == 1;
    }

    public static boolean isNight() {
        return isNight(getHourMinute(0));
    }

    public static boolean isNight(float hourMinute) {
        return hourMinute >= Config.hourNightFrom || hourMinute < Config.hourNightTo;
    }

    //-------------------------- Time ---------------------------

    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static String decode(String str) {
        try {
            return URLDecoder.decode(str, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMoneyTomanStr(double amount) {
        DecimalFormat dFormat = new DecimalFormat("####,###,###");
        return dFormat.format((long) amount) + " تومان";
    }

    //-------------------------------------------------------------------------------------------------

    public static <T extends ComparatorInterface> List<T> sortList(final Collection<T> c, final boolean ascOrDesc, final int oneOrTwo) {
        final List<T> list = new ArrayList<>(c);
        if(ascOrDesc) {
            if(1 == oneOrTwo) {
                list.sort((a, b) -> a.getOrder1().compareTo(b.getOrder1()));
            } else {
                list.sort((a, b) -> a.getOrder2().compareTo(b.getOrder2()));
            }
        } else {
            if(1 == oneOrTwo) {
                list.sort((a, b) -> b.getOrder1().compareTo(a.getOrder1()));
            } else {
                list.sort((a, b) -> b.getOrder1().compareTo(a.getOrder1()));
            }
        }
        return list;
    }

    public static boolean isFromToOk(final Collection<? extends ComparatorInterface> collection) {
        boolean ok = true;
        if(collection != null && collection.size() > 0) {
            ComparatorInterface ftFirst = null;
            for (ComparatorInterface ft: sortList(collection, true, 1)) {
                if(ft.getOrder1() > ft.getOrder2()) {
                    ok = false;
                    break;
                }
                if(ftFirst == null) {
                    ftFirst = ft;
                    continue;
                }
                if(ftFirst.getOrder2() > ft.getOrder1()) {
                    ok = false;
                    break;
                }
                ftFirst = ft;
            } //for
        } //if
        return ok;
    }

    //-------------------------------------------------------------------------------------------------
    private static final String NUMBER_FIRST = "123456789";
    private static final String NUMBER_OTHER = "01234567890123456789012345678901234567890123456789";
    private static final java.util.Random RANDOM = new java.util.Random();

    public static long randomNumber(int count) {
        final StringBuilder builder = new StringBuilder();
        final int first = count - 1;
        while (count-- != 0) {
            if(first != count) {
                int character = (int) (Math.random() * NUMBER_OTHER.length());
                builder.append(NUMBER_OTHER.charAt(character));
            } else {
                int character = (int) (Math.random() * NUMBER_FIRST.length());
                builder.append(NUMBER_FIRST.charAt(character));
            }
        }
        return Long.parseLong(builder.toString());
    }

    public static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    //-------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println(getShare(10000, 3.0f));
        System.out.println(getPrice(10000, 3.0f));
        System.out.println(getPercent(10000, 600));
    }
}