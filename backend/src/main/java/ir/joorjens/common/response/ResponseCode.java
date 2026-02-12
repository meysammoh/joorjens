package ir.joorjens.common.response;

import ir.joorjens.model.entity.BonusCard;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public enum ResponseCode {

    //-------------------------------------------------------------------------------
    DONE(1001, 200, "Done!", "انجام شد!"),
    DONE_DELETE(1002, 200, "Deleted!", "%s مورد نظر با موفقیت حذف شد!"),
    DONE_UPDATE(1003, 200, "Updated!", "%s مورد نظر با موفقیت ویرایش شد!"),
    DONE_NO_CHANGE(1004, 200, "NotChanged!", "تغییری صورت نگرفت!"),
    DONE_BLOCK(1006, 200, "BlockDone!", String.format("%s با موفقیت انجام شد!", TypeEnumeration.ACTION_BLOCK.getFa())),
    DONE_UNBLOCK(1007, 200, "UnblockDone!", String.format("%s با موفقیت انجام شد!", TypeEnumeration.ACTION_UNBLOCK.getFa())),
    LOGOUT(1011, 200, "User logout!", "کاربر با موفقیت از سیستم خارج شد!"),
    ACTIVATION_CODE_MOBILE(1021, 200, "Activation code was sent to your Mobile!", "کد فعالسازی به گوشی همراه‌تان فرستاده شد!"),
    ACTIVATION_CODE_MOBILE_EMAIL(1022, 200, "Activation code was sent to your Mobile and Email!", "کد فعالسازی به گوشی همراه و آدرس الکترونیکی‌تان فرستاده شد!"),
    RESET_CODE_MOBILE(1031, 200, "Reset code was sent to your Mobile!", "کد تغییر رمز عبور به گوشی همراه‌تان فرستاده شد!"),
    RESET_CODE_MOBILE_EMAIL(1032, 200, "Reset code was sent to your Mobile and Email!", "کد تغییر رمز عبور به گوشی همراه و آدرس الکترونیکی‌تان فرستاده شد!"),
    PASSWORD_CHANGED(1041, 200, "Password was successfully changed!", "رمز عبور با موفقیت تغییر یافت!"),
    CARD_USED(1051, 200, "your credit was increased!", String.format("اعتبار شما با %s افزایش یافت.", BonusCard.getEN())),

    CARTABLE_APPROVED(1101, 200, "Task was approved!", "وظیفه مورد نظر با موفقیت انجام شد."),
    CARTABLE_REJECTED(1102, 200, "Task was rejected!", "وظیفه مورد نظر با موفقیت رد شد."),
    CARTABLE_RENEWED(1103, 200, "Task was renewed!", "وظیفه مورد نظر با موفقیت تجدید شد!"),
    CARTABLE_ADDED_DISTRIBUTOR(1106, 200, "DistributorInfo was added to cartable", "کاربر گرامی %s پس از بررسی مدارک، کد شناسایی به شرکت پخش شما اختصاص یافته و از طریق پیام‌ها در این پنل، پیامک به همراه شما و ایمیل اعلام شده در هنگام ثبت‌نام، ارسال خواهد شد."),
    CARTABLE_ADDED_STORE(1107, 200, "StoreInfo was added to cartable", "کاربر گرامی %s پس از بررسی مدارک، کد شناسایی به فروشگاه شما اختصاص یافته و از طریق پیام ها در این پنل و پیامک به شماره همراه شما ارسال خواهد شد"),
    CARTABLE_ADDED_PRODUCT(1108, 200, "ProductInfo was added to cartable", "کاربر گرامی %s اطلاعات کالای %s ثبت شد و پس از تایید توسط کارشناسان ما امکان استفاده از آن کالا برای شما میسر خواهد بود."),

    ADD_REMOVE_DELETE(1151, 200, "2 things were added/removed!", "%d مورد %s %s شد!"),

    //-------------------------------------------------------------------------------
    ;

    private final static Map<String, ResponseCode> CODE_MAP = new HashMap<>();

    static {
        for (ResponseCode ex : ResponseCode.values()) {
            CODE_MAP.put(ex.messageKey.toLowerCase(), ex);
        }
    }

    private final int httpCode;
    private final int code;
    private final String messageKey;
    private final String message;

    ResponseCode(int code, int httpCode, String messageKey, String message) {
        this.httpCode = httpCode;
        this.code = code;
        this.messageKey = messageKey;
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCode get(String key) {
        return CODE_MAP.get(key.toLowerCase());
    }

    public static ResponseCode getContains(String key) {
        for (ResponseCode ex : ResponseCode.values()) {
            if (key.contains(ex.getMessageKey().toLowerCase()))
                return ex;
        }
        return null;
    }

    public static String getMessage(int count, String name, TypeEnumeration action) {
        return String.format(ADD_REMOVE_DELETE.getMessage(), count, name, action.getFa());
    }
}