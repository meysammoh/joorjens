package ir.joorjens.common.response;

import ir.joorjens.model.entity.*;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import java.util.HashMap;
import java.util.Map;

public enum ExceptionCode {

    //-------------------------------------------------------------------------------
    //uniqueKey constraints
    UK_CUSTOMER_MOBILE(1001, 409, "UK_CUSTOMER__mobileNumber", String.format(AbstractModel.UK_MSQ, "شماره موبایل")),
    UK_CUSTOMER_NATIONAL_IDENTIFIER(1002, 409, "UK_CUSTOMER__nationalIdentifier", String.format(AbstractModel.UK_MSQ, "شماره ملی")),
    UK_AREA_PARENT_AD_NAME(1011, 409, "UK_AREA__parent_ad_name", String.format(AbstractModel.UK_MSQ, "نام محدوده")),
    UK_ORDER_STATUS_TYPE_NAME(1021, 409, "UK_ORDER_STATUS_TYPE__name", String.format(AbstractModel.UK_MSQ, "نام نوع حالت سفارش")),
    UK_DISTRIBUTOR_DISCONTENT_TYPE(1031, 409, "UK_DISTRIBUTOR_DISCONTENT_TYPE__name", String.format(AbstractModel.UK_MSQ, "نام نوع نارضایتی از شرکت پخش")),
    UK_PRODUCT_CATEGORY_TYPE_PARENT_NAME(1041, 409, "UK_PRODUCT_CATEGORY_TYPE__parent_name", String.format(AbstractModel.UK_MSQ, "نام نوع دسته‌بندی کالا")),
    UK_PRODUCT_DETAIL_TYPE_PARENT_NAME(1051, 409, "UK_PRODUCT_DETAIL_TYPE__parent_name", String.format(AbstractModel.UK_MSQ, "نام نوع جزئیات کالا")),
    UK_PRODUCT_RETURN_TYPE_NAME(1061, 409, "UK_PRODUCT_RETURN_TYPE__name", String.format(AbstractModel.UK_MSQ, "نام نوع برگشت کالا")),
    UK_VEHICLE_BRAND_TYPE_NAME(1071, 409, "UK_VEHICLE_BRAND_TYPE__name", String.format(AbstractModel.UK_MSQ, "نام نوع برند وسیله نقلیه")),
    UK_CONFIG_FIELD_NAME(1081, 409, "UK_CONFIG_FIELD__name", String.format(AbstractModel.UK_MSQ, "نام پارامتر تنظیمات")),
    UK_ORDER_SETTLEMENT_TYPE_NAME(1091, 409, "UK_ORDER_SETTLEMENT_TYPE__name", String.format(AbstractModel.UK_MSQ, "نام نوع تسویه سفارش")),
    UK_VEHICLE_LICENSE_PLATE(1101, 409, "UK_VEHICLE__licensePlate", String.format(AbstractModel.UK_MSQ, "پلاک وسیله نقلیه")),
    UK_DISTRIBUTOR_PRODUCT_ID_DIS_PRO(1111, 409, "UK_DISTRIBUTOR_PRODUCT__id_dis_product", String.format(AbstractModel.UK_MSQ, DistributorProduct.getEN())),
    UK_DISTRIBUTOR_PRODUCT_PACKAGE_COUNT(1112, 409, "UK_DISTRIBUTOR_PRODUCT_PACKAGE__product_count", String.format(AbstractModel.UK_MSQ, DistributorProductPackage.getEN())),
    UK_DISTRIBUTOR_PRODUCT_DISCOUNT_TYPE(1113, 409, "UK_DISTRIBUTOR_PRODUCT_DISCOUNT__product_type", String.format(AbstractModel.UK_MSQ, DistributorProductDiscount.getEN())),
    UK_PRODUCT_BARCODE(1211, 409, "UK_PRODUCT__barcode", String.format(AbstractModel.UK_MSQ, "سریال محصول")),
    UK_PRODUCT_DETAILS_PAIR(1221, 409, "UK_PRODUCT_DETAILS__product_type", String.format(AbstractModel.UK_MSQ, ProductDetail.getEN())),
    UK_BANNER_LINK(1231, 409, "UK_BANNER__link", String.format(AbstractModel.UK_MSQ, "آدرس بنر")),
    UK_COLOR_TYPE_NAME(1251, 409, "UK_COLOR_TYPE__name", String.format(AbstractModel.UK_MSQ, "نام نوع رنگ")),
    UK_DICTIONARY_NAME(1261, 409, "UK_DICTIONARY__name", String.format(AbstractModel.UK_MSQ_2, "نام", Dictionary.getEN())),
    UK_BONUS_CARD_DETAIL_NUMBER(1271, 409, "UK_BONUS_CARD_DETAIL__number", String.format(AbstractModel.UK_MSQ_2, "شماره", BonusCardDetail.getEN())),
    UK_VALID_IP_ROLE(1281, 409, "UK_VALID_IP__ip_role", String.format(AbstractModel.UK_MSQ, ValidIP.getEN())),
    UK_STORE_MANAGER(1282, 409, "UK_STORE__manager", String.format(AbstractModel.UK_MSQ, Store.getEN())),

    UK_CUSTOMER_SESSION_PARAMS(1601, 409, "UK_CUSTOMER_SESSION__ip_userAgent_customer_role", String.format(AbstractModel.UK_MSQ, "پارامترهای جلسه کاربر")),
    UK_PERMISSION_URL(1602, 409, "UK_PERMISSION__url", String.format(AbstractModel.UK_MSQ, "آدرس دسترسی")),
    UK_PERMISSION_KEY(1603, 409, "UK_PERMISSION__key", String.format(AbstractModel.UK_MSQ, "کلید دسترسی")),
    UK_PRODUCT_CATEGORY_DETAIL_TYPE_FOREIGN_KEYS(1604, 409, "UK_PRODUCT_CATEGORY_DETAIL_TYPE__foreignKeys", String.format(AbstractModel.UK_MSQ, "کلید‌های انواع جزئیات یک نوع دسته بندی")),
    UK_ROLE_NAME(1605, 409, "UK_ROLE__name", String.format(AbstractModel.UK_MSQ, "نام نقش")),
    UK_DISTRIBUTOR_SERIAL(1606, 409, "UK_DISTRIBUTOR__serial", String.format(AbstractModel.UK_MSQ, "کد شناسایی شرکت پخش")),

    UK_CART_DIST_PACKDUCT_PACKAGE(1701, 409, "UK_CART_DIST_PACKDUCT__package", String.format(AbstractModel.UK_MSQ, CartDistributorPackduct.getEN())),
    UK_CART_DIST_PACKDUCT_PRODUCT(1702, 409, "UK_CART_DIST_PACKDUCT__product", String.format(AbstractModel.UK_MSQ, CartDistributorPackduct.getEN())),
    UK_CART_DISTRIBUTOR_RATE(1706, 409, "UK_DISTRIBUTOR_RATE__cart_distributor", String.format("شما قبلا به %s این %s نظر داده اید.", Distributor.getEN(), CartDistributorPackduct.getEN())),

    UK_(1100, 409, "UK_", "داده تکراری"), //must be always at the end of list of UK_s

    //-------------------------------------------------------------------------------
    //foreignKey constraints
    FK_(2001, 406, "FK_", "این داده در جایی استفاده شده است و قابل حذف نیست"),
    FK_PERMISSION(2002, 406, "FK_PERMISSION", "سطح دسترسی در جایی استفاده شده است!"),
    FK_NOT_FOUND(2003, 406, "Unable to find ir.joorjens.model.entity", "حداقل یکی از موارد مورد نظر یافت نشده است."),
    FK_BONUS_CARD(2021, 406, "at least one of bonus card detail is used!", "حداقل یکی از کارتها توسط مشتری استفاده شده است."),

    //-------------------------------------------------------------------------------

    OP_FORBIDDEN(3001, 403, "operation_forbidden", "دسترسی شما به این قسمت تعریف نشده است!"),
    OP_NOT_LOGIN(3002, 401, "not_login", "ابتدا باید وارد سیستم شوید!"),
    OP_NOT_USER_PASSWORD(3003, 401, "not_user_password", "نام کاربری یا رمز عبور اشتباه است!"),
    OP_NOT_PASSWORD_MATCH(3004, 401, "not_password_match", "رمز عبور قبلی اشتباه است!"),
    OP_USER_BANDED(3005, 401, "user_banded", "کاربر مورد نظر فعال نیست!"),
    OP_NO_UCLAIM(3006, 401, "no_uclaim", "کاربر با موفقیت از سیستم خارج شد! اما ورود شما معتبر نبوده است!"),
    OP_USER_IP(3007, 401, "user_ip", "کاربر محترم، شما اجازه دسترسی از این شبکه را ندارید!"),
    OP_FORBIDDEN_FILTER(3011, 403, "operation_forbidden_filter", "دسترسی شما به این قسمت تعریف نشده است!"),
    OP_ENTITY_BLOCKED(3021, 403, "entity_blocked", "داده مورد نظر غیرفعال شده است!"),

    EXCEPTION(4001, 500, "error", "خطایی رخ داده است"),
    NOT_FOUND(4004, 404, "not_found", "پافت نشد"),
    NOT_FOUND_PARAM(4005, 404, "not_found", "%s یافت نشد"),
    NOT_FOUND_ID(4006, 404, "not_found_id", "داده با شناسه %d یافت نشد!"),

    INVALID_JSON(4011, 400, "invalid_json", "رشته نامعتبر"),
    INVALID_OBJECT(4012, 400, "invalid", "شی نامعتبر"),
    INVALID_OBJECT_PARAM(4013, 400, "invalid", "%s نامعتبر است"),
    INVALID_PARAM(4015, 406, "invalid_data", "پارامتر نامعتبر"),
    INVALID_PARAM_PARAM(4016, 406, "invalid_data", "%s نامعتبر است"),
    INVALID_PARAM_OBJECT(4017, 406, "invalid_data", "%s در %s نامعتبر است"),
    INVALID_CHILD_SIZE(4018, 406, "invalid_child_size", "تعداد %s نباید کمتر از %.0f باشد."),
    INVALID_MANAGER(4019, 406, "invalid_manager", "کاربر گرامی، شما نمی‌توانید مدیر %s شوید!"),
    INVALID_TIME(4020, 406, "invalid_time", "زمان(ها) درست وارد نشده‌اند!"),

    INVALID_PASSWORD_EMPTY(4021, 406, "password_empty", "رمز عبور نباید خالی باشد!"),
    INVALID_PASSWORD_LENGTH(4022, 406, "password_length", "رمز عبور باید حداقل از ۶ حرف تشکیل شده باشد!"),
    INVALID_PASSWORD_NUMBER(4023, 406, "password_number", "رمز عبور باید حداقل یک عدد داشته باشد!"),
    INVALID_PASSWORD_LETTER(4024, 406, "password_letter", "رمز عبور باید حداقل یک حرف انگلیسی کوچک داشته باشد!"),
    INVALID_PASSWORD_LETTER_CAPS(4025, 406, "password_letter_caps", "رمز عبور باید حداقل یک حرف انگلیسی بزرگ داشته باشد!"),
    INVALID_PASSWORD_PUNCTUATION(4026, 406, "password_punctuation", "رمز عبور باید حداقل یک حرف اضافه مانند & یا @ و ... داشته باشد!"),
    INVALID_PASSWORD_NOT_EQUAL(4027, 406, "password_not_equal", "رمز عبور و تکرار آن یکسان نیستند!"),

    CUSTOMER_IN_CARTABLE(4051, 406, "customer_in_cartable", "شما اطلاعاتی در صف انتظا دارید و باید منتظر بمانید!"),
    TASK_WAS_DONE(4052, 406, "task_was_done", "وظیفه مورد نظر قبلا انجام شده است!"),
    PRODUCT_IN_CARTABLE(4053, 406, "product_in_cartable", "کالای مورد نظر قبلا ثبت شده و منتظر تایید است!"),
    TASK_IS_NEW(4054, 406, "task_is_new", "وظیفه مورد نظر جدید است!"),

    TYPE_UNDEFINED(4061, 406, "type_undefined", "نوع %s درست نیست!"),

    PRODUCT_DETAIL_UNDEFINED(4071, 406, "product_detail_undefined", "نوع %s برای این دسته تعریف نشده است!"),
    PRODUCT_DETAIL_MANDATORY(4072, 406, "product_detail_mandatory", String.format("بعضی از %s تعریف نشده است!", ProductDetailType.getEN())),

    DISCOUNT_UNKNOWN(4080, 406, "discount_type_unknown", String.format("%s تعریف نشده است!", TypeEnumeration.DISCOUNT_TYPE.getFa())),
    DISCOUNT_ONE_INT(4081, 406, "discount_one_int_needed", "مقدار خواسته شده را وارد کنید!"),
    DISCOUNT_BOTH_INT(4082, 406, "discount_both_int_needed", "باید دو مقدار را وارد کنید!"),

    PACKAGE_PRODUCT_INVALID_DISCOUNT(4086, 406, "package_product_invalid_discount", "مقدار تخفیف در بسته تخفیفی نامعتبر است!"),

    BONUS_CARD_DETAIL_DIGIT(4090, 406, "bonus_card_detail_digit_mismatch", "تعداد ارقام باید %s رقم باشد."),

    CART_EMPTY(4110, 404, "cart_is_empty", String.format("%s خالی است!", Cart.getEN())),
    CART_NOT_FOUND_PRODUCT(4111, 404, "cart_not_found_product", String.format("کالای مورد نظر در %s یافت نشد!", Cart.getEN())),
    CART_MIN_ORDER(4121, 406, "cart_min_order", "حداقل باید %d مورد از این کالا سفارش دهید."),
    CART_MAX_ORDER(4122, 406, "cart_max_order", "حداکثر می توانید %d مورد از این کالا سفارش دهید."),
    CART_DISTRIBUTOR_STOCK(4123, 406, "cart_distributor_stock", String.format("موجودی این %s کافی نیست از %s دیگری سفارش دهید!", Distributor.getEN(), Distributor.getEN())),
    CART_PACK_TIME(4131, 406, "cart_pack_time_not_valid", "زمان سفارش این بسته به اتمام رسیده است!"),
    CART_RATE_NOT_YOURS(4141, 406, "cart_is_not_belong_to_you", String.format("%s متعلق به شما نیست!", Cart.getEN())),
    CART_COMPLETED(4142, 406, "cart_state_is_completed", "%s بسته شده است!"),
    CART_NOT_OK(4143, 406, "cart_can_not_change", "%s نمی‌تواند به این حالت تغییر یابد!"),
    CART_DIST_DELIVERER(4151, 406, "cart_dist_deliverer_orderStatus", "حداقل باید یک کالا %s باشد!"),

    INTERNAL_SERVER_ERROR(5001, 500, "server error", "خطای داخلی رخ داده است!"),;

    private final static Map<String, ExceptionCode> CODE_MAP = new HashMap<>();

    static {
        for (ExceptionCode ex : ExceptionCode.values()) {
            CODE_MAP.put(ex.messageKey.toLowerCase(), ex);
        }
    }

    private final int httpCode;
    private final int errorCode;
    private final String messageKey;
    private final String message;

    ExceptionCode(int errorCode, int httpCode, String messageKey, String message) {
        this.httpCode = httpCode;
        this.errorCode = errorCode;
        this.messageKey = messageKey;
        this.message = message;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getMessage() {
        return message;
    }

    public static ExceptionCode get(String key) {
        return CODE_MAP.get(key.toLowerCase());
    }

    public static ExceptionCode getContains(String key) {
        for (ExceptionCode ex : ExceptionCode.values()) {
            if (key.contains(ex.getMessageKey().toLowerCase()))
                return ex;
        }
        return null;
    }
}