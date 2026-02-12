package ir.joorjens.model.util;

import ir.joorjens.common.Utility;
import ir.joorjens.model.entity.*;

import java.util.*;

public enum TypeEnumeration {

    //-------------------------------------------------------------------------------
    ENUM(-1, null, "Enum", "داده شمارشی", null),
    //-------------------------------------------------------------------------------

    JOORJENS(1, null, "JoorJens", "جورجنس", ENUM),

    APP_TYPE(10, null, "App Type", "نوع نرم افزار", ENUM),

    ACTION(30, null, "Action", "عملیات", ENUM),
    ACTION_DELETE(31, "", "Delete", "حذف", ACTION),
    ACTION_INSERT(32, "", "Insert", "ایجاد", ACTION),
    ACTION_UPDATE(33, "", "Update", "بروزرسانی", ACTION),
    ACTION_BLOCK(34, "", "Block", "غیرفعال‌سازی", ACTION),
    ACTION_UNBLOCK(35, "", "Unblock", "فعال‌سازی", ACTION),
    ACTION_SEARCH(45, "", "Search", "جستجو", ACTION),
    ACTION_STATUS_OK(48, "", "Successful", "موفقیت‌آمیز", ACTION),
    ACTION_STATUS_FAILED(49, "", "Failed", "ناموفق", ACTION),

    DAY_OF_WEEK(50, null, "Day of week", "روزهای هفته", ENUM),
    DW_SATURDAY(51, "1", "Sat", "شنبه", DAY_OF_WEEK),
    DW_SUNDAY(52, "2", "Sun", "یکشنبه", DAY_OF_WEEK),
    DW_MONDAY(53, "3", "Mon", "دوشنبه", DAY_OF_WEEK),
    DW_TUESDAY(54, "4", "Tue", "سه شنبه", DAY_OF_WEEK),
    DW_WEDNESDAY(55, "5", "Wed", "چارشنبه", DAY_OF_WEEK),
    DW_THURSDAY(56, "6", "Thu", "پنج شنبه", DAY_OF_WEEK),
    DW_FRIDAY(57, "7", "Fri", "جمعه", DAY_OF_WEEK),
    //DW_HOLIDAY(58, "Holiday", "تعطیل", DAY_OF_WEEK),

    TIME_STAMP(70, null, "TimeStamp", "مهرزمانی", ENUM),
    TS_YEAR(71, "", "Year", "سالانه", TIME_STAMP),
    TS_MONTH(72, "", "Month", "ماهانه", TIME_STAMP),
    TS_WEEK(73, "", "Week", "هفتگی", TIME_STAMP),
    TS_DAY(74, "", "Day", "روزانه", TIME_STAMP),
    TS_HOUR(75, "", "Hour", "روزانه", TIME_STAMP),

    DB_ORDER_TYPE(90, null, "DbOrderType", "نوع مرتب‌سازی", ENUM),
    DB_OT_ID(91, "", "id", "شناسه", DB_ORDER_TYPE),
    DB_OT_WEIGHT(92, "", "weight", "اولویت", DB_ORDER_TYPE),
    DB_OT_TYPE(93, "", "type", "نوع", DB_ORDER_TYPE),
    DB_OT_NAME(94, "", "name", "نام", DB_ORDER_TYPE),
    DB_OT_RATE(95, "", "rateSum/rateCount", "امتیاز", DB_ORDER_TYPE),
    DB_OT_POSITION(96, "", "position", "جایگاه", DB_ORDER_TYPE),
    DB_OT_NAME_DISTRIBUTOR(97, "", "distributor.name", String.format("نام %s", Distributor.getEN()), DB_ORDER_TYPE),
    DB_OT_NAME_DISTRIBUTOR_PRODUCT(98, "", "product.name", String.format("نام %s", DistributorProduct.getEN()), DB_ORDER_TYPE),
    DB_OT_CHECK(131, "", "supportCheck", "چکی", DB_ORDER_TYPE),
    DB_OT_PRICE(141, "", "price", "قیمت", DB_ORDER_TYPE),
    DB_OT_PRICE_MIN(142, "", "priceMin", "ارزانترین", DB_ORDER_TYPE),
    DB_OT_SALE_MAX(143, "", "saleCount", "پرفروش ترین", DB_ORDER_TYPE),
    DB_OT_DP_PRICE(151, "", "allPriceWithDiscount", "قیمت بسته تخفیفی", DB_ORDER_TYPE),
    DB_OT_DP_PERCENT(152, "", "allDiscountPercent", "تخفیف بسته تخفیفی", DB_ORDER_TYPE),

    CART_PACKDUCT_TYPE(200, null, "CartPackductType", "نوع محصول سبد خرید", ENUM),
    CPT_PRODUCT(211, "", "CPTProduct", "محصول", CART_PACKDUCT_TYPE),
    CPT_PACKAGE(221, "", "CPTPackage", "بسته تخفیفی یا باندلینگ", CART_PACKDUCT_TYPE),

    TRANSACTION_STATE(250, "Transaction State", "وضعیت تراکنش", null, ENUM),
    TRS_NEW(251, "New", "پرداخت نشده", "", TRANSACTION_STATE),
    TRS_PAYED(254, "Payed", "پرداخت شده", "", TRANSACTION_STATE),
    TRS_COMPLETED(257, "Completed", "تکمیل شده", "", TRANSACTION_STATE),
    TRS_CHECK(260, "Check", "بررسی شود", "", TRANSACTION_STATE),
    TRS_RETURN(268, "Bank Return", "برگشت", "", TRANSACTION_STATE),
    TRS_FAILED(269, "Failed", "خطادار", "", TRANSACTION_STATE),

    SHEBA_STATE(290, "Pay Status", "وضعیت پرداخت", null, ENUM),
    SHS_REGISTERED(291, "registered", "ثبت شده", "", SHEBA_STATE),
    SHS_CONFIRMED(293, "confirmed", "تایید شده", "", SHEBA_STATE),
    SHS_SENT(295, "sent", "ارسال شده به بانک مرکزی", "", SHEBA_STATE),
    SHS_REJECTED_CENTRAL(497, "centralbank_rejected", "رد شده توسط بانک مرکزی", "", SHEBA_STATE),
    SHS_REJECTED(499, "rejected", "رد شده توسط بانک مقابل", "", SHEBA_STATE),
    SHS_SUCCESS(501, "sent_recieved", "دریافت شده توسط بانک مرکزی", "", SHEBA_STATE),
    SHS_SUCCESS_BANK(503, "Success", "موفقیت آمیز", "", SHEBA_STATE),
    SHS_UNKNOWN(505, "Unknown", "نامشخص", "", SHEBA_STATE),

    //-------------------------------------------------------------------------------

    ROLE_PERMISSION(1000, null, "RolePermission", "دسترسی‌", ENUM),
    USER_ROLE(1010, null, "UserRole", "نقش کاربر", ENUM),
    UR_CUSTOMER(1011, "", "Customer", "مشتری", USER_ROLE),
    UR_STORE_ADMIN(1031, "", "Store Admin", "مدیر سوپرمارکت", USER_ROLE),
    UR_DISTRIBUTION_ADMIN(1051, "", "Distribution Admin", "مدیر پخش", USER_ROLE),
    UR_DISTRIBUTION_CONTROLLER(1052, "", "Distribution Controller", "ناظر پخش", USER_ROLE),
    UR_DISTRIBUTION_OPERATOR(1053, "", "Distribution Operator", "اپراتور پخش", USER_ROLE),
    UR_DISTRIBUTION_DELIVERER(1054, "", "Distribution Deliverer", "تحویل‌دهنده پخش", USER_ROLE),
    UR_CENTRAL_ADMIN(1071, "", "Central Admin", "مدیر دفتر مرکزی", USER_ROLE),
    UR_CENTRAL_CONTROLLER(1072, "", "Central Controller", "ناظر دفتر مرکزی", USER_ROLE),
    UR_CENTRAL_SUPPORTER(1073, "", "Central Supporter", "پشتیبان دفتر مرکزی", USER_ROLE),
    UR_CENTRAL_OPERATOR(1074, "", "Central Operator", "اپراتور دفتر مرکزی", USER_ROLE),
    UR_CENTRAL_ACCOUNTANT(1075, "", "Central Accountant", "حسابدار دفتر مرکزی", USER_ROLE),

    ORDER_STATUS(1110, null, "Order Status Types", "انواع حالات سفارش", ENUM),
    OS_RECEIVED(1111, "", "Received", "دریافت شده", ORDER_STATUS),
    OS_APPROVE(1114, "", "Approve", "تایید سفارش", ORDER_STATUS),
    OS_PAYED(1117, "", "Payed", "پرداخت شده", ORDER_STATUS),
    OS_PROCESSED(1120, "", "Processed", "پردازش انبار", ORDER_STATUS),
    OS_READY(1123, "", "Ready", "آماده ارسال", ORDER_STATUS),
    OS_POST(1126, "", "Post", "ارسال شده", ORDER_STATUS),
    OS_DELIVERED(1129, "", "Delivered", "تحویل شده", ORDER_STATUS),

    ORDER_SETTLEMENT(1130, null, "Order Settlement Types", "انواع تسویه سفارش", ENUM),
    OST_CHECK(1131, "", "Check", "چکی", ORDER_SETTLEMENT),
    OST_CACHE(1132, "", "Cache", "نقدی", ORDER_SETTLEMENT),
    OST_CREDIT(1133, "", "Credit", "اعتباری", ORDER_SETTLEMENT),

    PRODUCT_DETAIL_TYPE(1210, null, "Product Detail Type", "نوع جزيیات کالا", ENUM),
    PRODUCT_CATEGORY_TYPE(1310, null, "Product Category Type", "نوع دسته‌بندی کالا", ENUM),

    //-------------------------------------------------------------------------------

    CONFIG(2010, null, "Config", "تنظیمات", ENUM),
    CONFIG_TAX(2011, "9", "Config Tax", "مالیات بر ارزش افزوده", CONFIG),
    CONFIG_INSURANCE(2012, "7", "Config Insurance", "بیمه بار", CONFIG),
    CONFIG_FIRST_BUY_SUM(2013, "100000", "Config First Buy Sum", "مجموع خرید برای اعتبار اولیه", CONFIG),
    CONFIG_FIRST_CREDIT(2014, "10000", "Config First Credit", "مقدار اعتبار خرید اولیه", CONFIG),

    CONFIG_PROMOTION_MESSAGE(2021, "در صورت خرید %d تومان، %d تومان اعتبار برای خرید بعدی به شما تعلق خواهد گرفت. برای بهره‌مندی از این اعتبار %d تومان به خرید خود اضافه کنید.", "Config Promotion Message", "پیام پروموشن", CONFIG),

    CONFIG_DISTRIBUTOR_CART_WARN_TITLE(2031, "هشدار تعداد فاکتور باز", "disttributorCartWarnTitle", "عنوان هشدار به تامین کننده به خاطر فاکتورهای باز", CONFIG),
    CONFIG_DISTRIBUTOR_CART_WARN_DESC(2032, "تعداد فاکتور باز شما زیاد است، در صورتی که این تعداد از حد مجاز بیشتر شود منجر به غیرفعال شدن شما خواهد شد.", "disttributorCartWarnDesc", "توضیحات هشدار به تامین کننده به خاطر فاکتورهای باز", CONFIG),
    CONFIG_DISTRIBUTOR_CART_BLOCK_TITLE(2036, "غیرفعال شدن به دلیل تعداد فاکتور باز", "disttributorCartBlockTitle", "عنوان غیرفعالسازی تامین کننده به خاطر فاکتورهای باز", CONFIG),
    CONFIG_DISTRIBUTOR_CART_BLOCK_DESC(2037, "شما به دلیل تعداد فاکتور باز بیش از حد مجاز غیرفعال شده اید.", "disttributorCartBlockDesc", "توضیحات غیرفعالسازی تامین کننده به خاطر فاکتورهای باز", CONFIG),

    CONFIG_CONTRACT(2041, "جناب آقای name با شماره تلفن mobile صاحب ...", "Config Contract", "متن قرارداد شرکت پخش ها", CONFIG),

    CONFIG_DISTRIBUTOR_PACK_SIZE_ALL(2111, "2", "Config Minimum count of Distributor package products", "حداقل تعداد کل کالا در "+ DistributorPackage.getEN(), CONFIG),
    CONFIG_DISTRIBUTOR_PACK_SIZE_BUNDLING(2112, "2", "Config Minimum count of Distributor package bundling products", "حداقل تعداد کالای باندلینگ در "+ DistributorPackage.getEN(), CONFIG),

    //-------------------------------------------------------------------------------

    //AdministrativeDivision
    AD(3010, null, "Administrative Division", "تقسیمات کشوری", ENUM),
    AD_PROVINCE(3011, "", "Province", "استان", AD),
    AD_CITY(3012, "", "City", "شهر", AD),
    AD_ZONE(3013, "", "Zone", "منطقه", AD),
    AD_DISTRICT(3014, "", "District", "محله", AD),
    AD_STREET(3015, "", "Street", "خیابان", AD),

    GENDER(3020, null, "Gender", "جنسیت", ENUM),
    GENDER_MALE(3021, "", "Male", "مرد", GENDER),
    GENDER_FEMALE(3022, "", "Female", "زن", GENDER),

    //TASK_TYPE
    PRIORITY_TYPE(3030, null, "PriorityType", "اولویت", ENUM),
    PRT_LOW(3031, "", "Low", "کم", PRIORITY_TYPE),
    PRT_MEDIUM(3032, "", "Medium", "متوسط", PRIORITY_TYPE),
    PRT_HIGH(3033, "", "High", "بالا", PRIORITY_TYPE),

    TASK_STATUS(3040, null, "TaskStatus", "وظیفه", ENUM),
    TS_NEW(3041, "", "New", "جدید", TASK_STATUS),
    TS_ASSIGNED(3042, "", "Assigned", "محول شده", TASK_STATUS),
    TS_APPROVED(3046, "", "Approved", "تایید شده", TASK_STATUS),
    TS_REJECTED(3047, "", "Rejected", "رد شده", TASK_STATUS),

    //--
    TASK_TYPE(3050, null, "TaskType", "نوع وظیفه", ENUM),
    TT_INSERT_STORE(3051, "", "InsertStore", "ایجاد فروشگاه", TASK_TYPE),
    TT_UPDATE_STORE(3052, "", "UpdateStore", "بروزرسانی فروشگاه", TASK_TYPE),
    //--
    TT_INSERT_DISTRIBUTOR_MAIN(3053, "", "InsertDistributorMain", "ایجاد شرکت پخش", TASK_TYPE),
    TT_UPDATE_DISTRIBUTOR_MAIN(3054, "", "UpdateDistributorMain", "بروزرسانی شرکت پخش", TASK_TYPE),
    TT_INSERT_DISTRIBUTOR_MAIN_BRANCH(3055, "", "InsertDistributorMainBranch", "ایجاد شعبه شرکت پخش", TASK_TYPE),
    TT_UPDATE_DISTRIBUTOR_MAIN_BRANCH(3056, "", "UpdateDistributorMainBranch", "بروزرسانی شعبه شرکت پخش", TASK_TYPE),
    //--
    TT_INSERT_DISTRIBUTOR_EMPLOYEE(3071, "", "InsertEmployee", "ایجاد همکاران", TASK_TYPE),
    TT_UPDATE_DISTRIBUTOR_EMPLOYEE(3072, "", "UpdateEmployee", "بروزرسانی همکاران", TASK_TYPE),
    //--
    TT_INSERT_PRODUCT(3081, "", "InsertProduct", "ایجاد کالا", TASK_TYPE),
    TT_UPDATE_PRODUCT(3082, "", "UpdateProduct", "بروزرسانی کالا", TASK_TYPE),    //--
    //--

    DISTRIBUTOR_TYPE(3120, null, "DistributorType", "نوع شرکت پخش", ENUM),
    DT_MAIN(3121, "", "MainType", "شرکت پخش", DISTRIBUTOR_TYPE),
    DT_MAIN_BRANCH(3122, "", "MainBranchType", "شرکت پخش شعبه", DISTRIBUTOR_TYPE),

    CONTRACT_TYPE(3140, null, "ContractType", "نوع قرارداد", ENUM),
    //CT_SHARE(3141, "", "Share", "حق اشتراکی", CONTRACT_TYPE),
    //CONTRACT_TYPE_SHARE(3160, null, CONTRACT_TYPE.getEn() + CT_SHARE.getEn(), CONTRACT_TYPE.getFa() + CT_SHARE.getFa(), ENUM),
    CTS_WEEKLY(3161, "1000000", "ShareWeekly", "هفتگی(۱ میلیون)", CONTRACT_TYPE),
    CTS_MONTH_1(3162, "3000000", "ShareMonthFirst", "یک ماهه(۳ میلیون)", CONTRACT_TYPE),
    CTS_MONTH_3(3163, "8000000", "ShareMonthThird", "سه ماهه(۸ میلیون)", CONTRACT_TYPE),
    CTS_MONTH_6(3166, "15000000", "ShareMonthSixth", "شش ماه(۱۵ میلیون)", CONTRACT_TYPE),
    //CT_PERCENT(3142, "", "Percent", "درصدی از فروش", CONTRACT_TYPE),
    //CONTRACT_TYPE_PERCENT(3180, null, CONTRACT_TYPE.getEn() + CT_PERCENT.getEn(), CONTRACT_TYPE.getFa() + CT_PERCENT.getFa(), ENUM),
    CTP_0(3181, "2.5", "Percent-0-50", "بین ۰ تا ۵۰ میلیون ۲.۵ درصد", CONTRACT_TYPE),
    CTP_50(3182, "3.5", "Percent-50-200", "بین ۵۰ تا ۲۰۰ میلیون ۳.۵ درصد", CONTRACT_TYPE),
    CTP_200(3183, "4.5", "Percent-200", "بیشتر ار ۲۰۰ میلیون ۴.۵ درصد", CONTRACT_TYPE),
    //....
    CT_FIX(3191, "4", "Fix", "ثابت ۴ درصد", CONTRACT_TYPE),

    PRODUCT_BRAND_TYPE(3220, null, "ProductBrandType", "برند محصول", ENUM),
    PBT_MAIN(3221, "", "PBTMain", "برند مادر", PRODUCT_BRAND_TYPE),
    PBT_PBT(3222, "", "PBTProductBrandType", "برند", PRODUCT_BRAND_TYPE),

    DISCOUNT_TYPE(3240, null, "DiscountType", "نوع تخفیف", ENUM),
    DT_SPECIAL(3241, "", "DTSpecial", "ویژه", DISCOUNT_TYPE),
    DT_CACHE(3242, "", "DTCache", "نقدی", DISCOUNT_TYPE),
    DT_OFFER(3243, "", "DTOffer", "پیشنهادی (آفر)", DISCOUNT_TYPE),

    MESSAGE_TO_TYPE(3250, null, "MessageToType", "نوع گیرنده پیام", ENUM),
    MTT_ORDINAL(3251, "", "MTTOrdinal", "معمولی", MESSAGE_TO_TYPE),
    MTT_CC(3252, "", "MTTCC", "سی‌سی", MESSAGE_TO_TYPE),
    MTT_BCC(3253, "", "MTTBCC", "بی‌سی‌سی", MESSAGE_TO_TYPE),

    CARD_STATUS(3260, null, "CardStatus", "وضعیت کارت", ENUM),
    CS_USED(3261, "", "CardUsed", "استفاده شده", CARD_STATUS),
    CS_EXPIRED(3262, "", "CardExpired", "منقضی شده", CARD_STATUS),
    CS_NEW(3263, "", "CardNew", "استفاده نشده", CARD_STATUS),

    ADVERTISING_TYPE(3300, null, "AdvertisingType", "نوع تبلیغات", ENUM),
    AT_TOP(3301, "120;340", "top", "بالا", ADVERTISING_TYPE),
    AT_TOP_RIGHT(3311, "", "topRight", "بالا راست", ADVERTISING_TYPE),
    AT_TOP_LEFT(3313, "", "topLeft", "بالا چپ", ADVERTISING_TYPE),
    AT_RIGHT_1(3331, "", "right1", "راست - اول", ADVERTISING_TYPE),
    AT_RIGHT_2(3333, "", "right2", "راست - دوم", ADVERTISING_TYPE),
    AT_RIGHT_3(3335, "", "right3", "راست - سوم", ADVERTISING_TYPE),
    AT_RIGHT_4(3337, "", "right4", "راست - چهارم", ADVERTISING_TYPE),
    AT_RIGHT_5(3339, "", "right5", "راست - پنجم", ADVERTISING_TYPE),
    AT_LEFT_1(3351, "", "left1", "گوشه بالا چپ - اول", ADVERTISING_TYPE),
    AT_LEFT_2(3353, "", "left2", "گوشه بالا چپ - دوم", ADVERTISING_TYPE),
    AT_LEFT_3(3355, "", "left3", "گوشه بالا چپ - سوم", ADVERTISING_TYPE),
    AT_LEFT_4(3357, "", "left4", "گوشه بالا چپ - چهارم", ADVERTISING_TYPE),
    AT_LEFT_5(3359, "", "left5", "گوشه بالا چپ - پنجم", ADVERTISING_TYPE),
    AT_MID_1_RIGHT(3371, "", "mid1Right", "وسط اول - راست", ADVERTISING_TYPE),
    AT_MID_1_LEFT(3373, "", "mid1Left", "وسط اول - چپ", ADVERTISING_TYPE),
    AT_MID_2_RIGHT(3381, "", "mid2Right", "وسط دوم - راست", ADVERTISING_TYPE),
    AT_MID_2_LEFT(3383, "", "mid21eft", "وسط دوم - چپ", ADVERTISING_TYPE),
    AT_MID_3_RIGHT(3391, "", "mid3Right", "وسط سوم - راست", ADVERTISING_TYPE),
    AT_MID_3_LEFT(3393, "", "mid3Left", "وسط سوم - چپ", ADVERTISING_TYPE),
    AT_MID_4_RIGHT(3401, "", "mid4Right", "وسط چهارم - راست", ADVERTISING_TYPE),
    AT_MID_4_LEFT(3403, "", "mid4Left", "وسط چهارم - چپ", ADVERTISING_TYPE),
    AT_MID_5_RIGHT(3411, "", "mid5Right", "وسط پنجم - راست", ADVERTISING_TYPE),
    AT_MID_5_LEFT(3413, "", "mid5Left", "وسط پنجم - چپ", ADVERTISING_TYPE),
    AT_DOWN(3451, "", "down", "پایین", ADVERTISING_TYPE),
    AT_DOWN_RIGHT(3461, "", "downRight", "پایین راست", ADVERTISING_TYPE),
    AT_DOWN_LEFT(3463, "", "downLeft", "پایین چپ", ADVERTISING_TYPE),

    //-------------------------------------------------------------------------------
    ;

    private static final Map<Integer, TypeEnumeration> TYPE_MAP = new HashMap<>();
    public static final Map<Integer, String> TYPE_MAP_ID_NAME = new HashMap<>();

    public static final List<TypeEnumeration> TYPE_LIST_MAIN = new ArrayList<>();
    public static final Map<Integer, List<TypeEnumeration>> TYPE_MAP_LIST = new HashMap<>();
    public static final Set<TypeEnumeration> DISTRIBUTOR_EMPLOYEE = new HashSet<>();
    public static final Set<TypeEnumeration> CENTRAL_EMPLOYEE = new HashSet<>();
    public static final List<TypeEnumeration> ROLE_LIST_CENTRAL = new ArrayList<>();
    public static final List<TypeEnumeration> ROLE_LIST_DISTRIBUTOR = new ArrayList<>();

    static {
        //---------------------------------------------------------------------

        CENTRAL_EMPLOYEE.add(UR_CENTRAL_ADMIN);
        CENTRAL_EMPLOYEE.add(UR_CENTRAL_OPERATOR);
        CENTRAL_EMPLOYEE.add(UR_CENTRAL_ACCOUNTANT);
        CENTRAL_EMPLOYEE.add(UR_CENTRAL_SUPPORTER);
        CENTRAL_EMPLOYEE.add(UR_CENTRAL_CONTROLLER);

        DISTRIBUTOR_EMPLOYEE.add(UR_DISTRIBUTION_ADMIN);
        DISTRIBUTOR_EMPLOYEE.add(UR_DISTRIBUTION_DELIVERER);
        DISTRIBUTOR_EMPLOYEE.add(UR_DISTRIBUTION_CONTROLLER);
        DISTRIBUTOR_EMPLOYEE.add(UR_DISTRIBUTION_OPERATOR);

        //---------------------------------------------------------------------

        for (TypeEnumeration type : TypeEnumeration.values()) {
            TYPE_MAP.put(type.id, type);
            TYPE_MAP_ID_NAME.put(type.id, type.getEn());
        }

        //---------------------------------------------------------------------

        ROLE_LIST_CENTRAL.add(UR_CENTRAL_ADMIN);
        ROLE_LIST_CENTRAL.add(UR_CENTRAL_OPERATOR);
        ROLE_LIST_CENTRAL.add(UR_CENTRAL_CONTROLLER);
        ROLE_LIST_CENTRAL.add(UR_CENTRAL_ACCOUNTANT);
        ROLE_LIST_CENTRAL.add(UR_CENTRAL_SUPPORTER);

        //ROLE_LIST_DISTRIBUTOR.add(UR_DISTRIBUTION_ADMIN);
        //ROLE_LIST_DISTRIBUTOR.add(UR_DISTRIBUTION_DELIVERER);
        ROLE_LIST_DISTRIBUTOR.add(UR_DISTRIBUTION_OPERATOR);
        ROLE_LIST_DISTRIBUTOR.add(UR_DISTRIBUTION_CONTROLLER);

        //---------------------------------------------------------------------

        TYPE_LIST_MAIN.add(ACTION);
        List<TypeEnumeration> list = new ArrayList<>();
        TYPE_MAP_LIST.put(ACTION.getId(), list);
        list.add(ACTION_DELETE);
        list.add(ACTION_INSERT);
        list.add(ACTION_UPDATE);
        list.add(ACTION_SEARCH);

        TYPE_LIST_MAIN.add(DAY_OF_WEEK);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(DAY_OF_WEEK.getId(), list);
        list.add(DW_SATURDAY);
        list.add(DW_SUNDAY);
        list.add(DW_MONDAY);
        list.add(DW_TUESDAY);
        list.add(DW_WEDNESDAY);
        list.add(DW_THURSDAY);
        list.add(DW_FRIDAY);
        //list.add(DW_HOLIDAY);

        TYPE_LIST_MAIN.add(TIME_STAMP);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(TIME_STAMP.getId(), list);
        list.add(TS_DAY);
        list.add(TS_WEEK);
        list.add(TS_MONTH);

        TYPE_LIST_MAIN.add(DB_ORDER_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(DB_ORDER_TYPE.getId(), list);
        list.add(DB_OT_ID);
        list.add(DB_OT_WEIGHT);
        list.add(DB_OT_TYPE);
        list.add(DB_OT_NAME);
        list.add(DB_OT_RATE);
        list.add(DB_OT_POSITION);
        list.add(DB_OT_NAME_DISTRIBUTOR);
        list.add(DB_OT_NAME_DISTRIBUTOR_PRODUCT);
        list.add(DB_OT_CHECK);
        list.add(DB_OT_PRICE);
        list.add(DB_OT_PRICE_MIN);
        list.add(DB_OT_SALE_MAX);
        list.add(DB_OT_DP_PRICE);
        list.add(DB_OT_DP_PERCENT);

        TYPE_LIST_MAIN.add(CART_PACKDUCT_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(CART_PACKDUCT_TYPE.getId(), list);
        list.add(CPT_PACKAGE);
        list.add(CPT_PRODUCT);

        TYPE_LIST_MAIN.add(TRANSACTION_STATE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(TRANSACTION_STATE.getId(), list);
        list.add(TRS_NEW);
        list.add(TRS_PAYED);
        list.add(TRS_COMPLETED);
        list.add(TRS_CHECK);
        list.add(TRS_RETURN);
        list.add(TRS_FAILED);

        TYPE_LIST_MAIN.add(SHEBA_STATE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(SHEBA_STATE.getId(), list);
        list.add(SHS_REGISTERED);
        list.add(SHS_CONFIRMED);
        list.add(SHS_SENT);
        list.add(SHS_REJECTED_CENTRAL);
        list.add(SHS_REJECTED);
        list.add(SHS_SUCCESS);
        list.add(SHS_SUCCESS_BANK);
        list.add(SHS_UNKNOWN);

        //---------------------------------------------------------------------

        TYPE_LIST_MAIN.add(ROLE_PERMISSION);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(ROLE_PERMISSION.getId(), list);

        TYPE_LIST_MAIN.add(USER_ROLE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(USER_ROLE.getId(), list);
        list.add(UR_CUSTOMER);
        list.add(UR_STORE_ADMIN);
        list.add(UR_DISTRIBUTION_ADMIN);
        list.add(UR_DISTRIBUTION_CONTROLLER);
        list.add(UR_DISTRIBUTION_OPERATOR);
        list.add(UR_DISTRIBUTION_DELIVERER);
        list.add(UR_CENTRAL_ADMIN);
        list.add(UR_CENTRAL_CONTROLLER);
        list.add(UR_CENTRAL_SUPPORTER);
        list.add(UR_CENTRAL_OPERATOR);
        list.add(UR_CENTRAL_ACCOUNTANT);

        TYPE_LIST_MAIN.add(ORDER_STATUS);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(ORDER_STATUS.getId(), list);
        list.add(OS_RECEIVED);
        list.add(OS_APPROVE);
        list.add(OS_PAYED);
        list.add(OS_PROCESSED);
        list.add(OS_READY);
        list.add(OS_POST);
        list.add(OS_DELIVERED);

        TYPE_LIST_MAIN.add(ORDER_SETTLEMENT);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(ORDER_SETTLEMENT.getId(), list);
        list.add(OST_CHECK);
        list.add(OST_CACHE);
        list.add(OST_CREDIT);

        TYPE_LIST_MAIN.add(PRODUCT_DETAIL_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(PRODUCT_DETAIL_TYPE.getId(), list);

        TYPE_LIST_MAIN.add(PRODUCT_CATEGORY_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(PRODUCT_CATEGORY_TYPE.getId(), list);

        TYPE_LIST_MAIN.add(CONFIG);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(CONFIG.getId(), list);
        list.add(CONFIG_TAX);
        list.add(CONFIG_INSURANCE);
        list.add(CONFIG_FIRST_BUY_SUM);
        list.add(CONFIG_FIRST_CREDIT);
        list.add(CONFIG_PROMOTION_MESSAGE);
        list.add(CONFIG_DISTRIBUTOR_CART_WARN_TITLE);
        list.add(CONFIG_DISTRIBUTOR_CART_WARN_DESC);
        list.add(CONFIG_DISTRIBUTOR_CART_BLOCK_TITLE);
        list.add(CONFIG_DISTRIBUTOR_CART_BLOCK_DESC);
        list.add(CONFIG_CONTRACT);
        list.add(CONFIG_DISTRIBUTOR_PACK_SIZE_ALL);
        list.add(CONFIG_DISTRIBUTOR_PACK_SIZE_BUNDLING);

        //---------------------------------------------------------------------

        TYPE_LIST_MAIN.add(AD);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(AD.getId(), list);
        list.add(AD_PROVINCE);
        list.add(AD_CITY);
        list.add(AD_ZONE);
        list.add(AD_DISTRICT);
        list.add(AD_STREET);

        TYPE_LIST_MAIN.add(GENDER);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(GENDER.getId(), list);
        list.add(GENDER_MALE);
        list.add(GENDER_FEMALE);

        TYPE_LIST_MAIN.add(PRIORITY_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(PRIORITY_TYPE.getId(), list);
        list.add(PRT_LOW);
        list.add(PRT_MEDIUM);
        list.add(PRT_HIGH);

        TYPE_LIST_MAIN.add(TASK_STATUS);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(TASK_STATUS.getId(), list);
        list.add(TS_NEW);
        list.add(TS_ASSIGNED);
        list.add(TS_APPROVED);
        list.add(TS_REJECTED);

        TYPE_LIST_MAIN.add(TASK_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(TASK_TYPE.getId(), list);
        list.add(TT_INSERT_STORE);
        list.add(TT_UPDATE_STORE);
        list.add(TT_INSERT_DISTRIBUTOR_MAIN);
        list.add(TT_UPDATE_DISTRIBUTOR_MAIN);
        list.add(TT_INSERT_DISTRIBUTOR_MAIN_BRANCH);
        list.add(TT_UPDATE_DISTRIBUTOR_MAIN_BRANCH);
        list.add(TT_INSERT_DISTRIBUTOR_EMPLOYEE);
        list.add(TT_UPDATE_DISTRIBUTOR_EMPLOYEE);
        list.add(TT_INSERT_PRODUCT);
        list.add(TT_UPDATE_PRODUCT);

        TYPE_LIST_MAIN.add(DISTRIBUTOR_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(DISTRIBUTOR_TYPE.getId(), list);
        list.add(DT_MAIN);
        list.add(DT_MAIN_BRANCH);

        TYPE_LIST_MAIN.add(CONTRACT_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(CONTRACT_TYPE.getId(), list);
        list.add(CTS_WEEKLY);
        list.add(CTS_MONTH_1);
        list.add(CTS_MONTH_3);
        list.add(CTS_MONTH_6);
        //--
        list.add(CTP_0);
        list.add(CTP_50);
        list.add(CTP_200);
        //--
        list.add(CT_FIX);

        TYPE_LIST_MAIN.add(PRODUCT_BRAND_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(PRODUCT_BRAND_TYPE.getId(), list);
        list.add(PBT_MAIN);
        list.add(PBT_PBT);

        TYPE_LIST_MAIN.add(DISCOUNT_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(DISCOUNT_TYPE.getId(), list);
        list.add(DT_SPECIAL);
        list.add(DT_CACHE);
        list.add(DT_OFFER);

        TYPE_LIST_MAIN.add(MESSAGE_TO_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(MESSAGE_TO_TYPE.getId(), list);
        list.add(MTT_ORDINAL);
        list.add(MTT_CC);
        list.add(MTT_BCC);

        TYPE_LIST_MAIN.add(CARD_STATUS);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(CARD_STATUS.getId(), list);
        list.add(CS_USED);
        list.add(CS_EXPIRED);
        list.add(CS_NEW);

        TYPE_LIST_MAIN.add(ADVERTISING_TYPE);
        list = new ArrayList<>();
        TYPE_MAP_LIST.put(ADVERTISING_TYPE.getId(), list);
        list.add(AT_TOP);
        list.add(AT_TOP_RIGHT);
        list.add(AT_TOP_LEFT);
        list.add(AT_RIGHT_1);
        list.add(AT_RIGHT_2);
        list.add(AT_RIGHT_3);
        list.add(AT_RIGHT_4);
        list.add(AT_RIGHT_5);
        list.add(AT_LEFT_1);
        list.add(AT_LEFT_2);
        list.add(AT_LEFT_3);
        list.add(AT_LEFT_4);
        list.add(AT_LEFT_5);
        list.add(AT_MID_1_RIGHT);
        list.add(AT_MID_1_LEFT);
        list.add(AT_MID_2_RIGHT);
        list.add(AT_MID_2_LEFT);
        list.add(AT_MID_3_RIGHT);
        list.add(AT_MID_3_LEFT);
        list.add(AT_MID_4_RIGHT);
        list.add(AT_MID_4_LEFT);
        list.add(AT_MID_5_RIGHT);
        list.add(AT_MID_5_LEFT);
        list.add(AT_DOWN);
        list.add(AT_DOWN_RIGHT);
        list.add(AT_DOWN_LEFT);
        //---------------------------------------------------------------------
    }

    //----------------------------------------------------------------------------

    private final int id;
    private final String def;
    private final String en;
    private final String fa;
    private final TypeEnumeration parent;

    TypeEnumeration(int id, String def, String en, String fa, TypeEnumeration parent) {
        this.id = id;
        this.def = def;
        this.en = en;
        this.fa = fa;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getDef() {
        return def;
    }

    public String getEn() {
        return en;
    }

    public String getFa() {
        return fa;
    }

    public TypeEnumeration getParent() {
        return parent;
    }

    public static TypeEnumeration get(int id) {
        return TYPE_MAP.get(id);
    }

    public static String getFa(int id) {
        final TypeEnumeration type = get(id);
        return type != null ? type.getFa() : "-";
    }

    public static boolean contains(int id) {
        return TYPE_MAP.containsKey(id);
    }

    public static Map<Integer, TypeEnumeration> getMap() {
        return TYPE_MAP;
    }

    public static Map<Integer, String> getMapIdName() {
        return TYPE_MAP_ID_NAME;
    }

    public static List<TypeEnumeration> getList() {
        return TYPE_LIST_MAIN;
    }

    public static Map<Integer, List<TypeEnumeration>> getMapList() {
        return TYPE_MAP_LIST;
    }

    public static List<TypeEnumeration> getList(int id) {
        return TYPE_MAP_LIST.get(id);
    }

    public static List<TypeEnumeration> getListRoles(int id) {
        if(TypeEnumeration.UR_CENTRAL_ADMIN.getId() == id) {
            return ROLE_LIST_CENTRAL;
        } else if(TypeEnumeration.UR_DISTRIBUTION_ADMIN.getId() == id) {
            return ROLE_LIST_DISTRIBUTOR;
        }
        return new ArrayList<>();
    }

    public static String getOrderByWeightName(final String table) {
        final String orderTypeIds = String.format("%d,%d", TypeEnumeration.DB_OT_WEIGHT.getId(), TypeEnumeration.DB_OT_NAME.getId());
        final String ascDescs = String.format("%s,%s", "false", "true");
        return TypeEnumeration.getOrder(table, orderTypeIds, ascDescs);
    }

    public static String getOrder(String table, final String orderTypeIds, final String ascDescs) {
        if(Utility.isEmpty(orderTypeIds)) {
            return String.format("ORDER BY %s.%s %s", table, TypeEnumeration.DB_OT_ID.getEn(), "desc");
        }

        final String[] orderTypeArr = orderTypeIds.split("\\s*,\\s*");
        final String[] ascDescArr = ascDescs.split("\\s*,\\s*");
        final StringBuilder orderSB = new StringBuilder();
        for (int i = 0; i < orderTypeArr.length; i++) {
            final TypeEnumeration orderType = get(Integer.parseInt(orderTypeArr[i]));
            if (orderType == null || TypeEnumeration.DB_ORDER_TYPE != orderType.getParent()) {
                continue;
            }
            final boolean ascDesc = (i < ascDescArr.length) && Boolean.parseBoolean(ascDescArr[i]);
            if(Utility.isEmpty(table)) {
                table = "t";
            }

            if(i == 0) {
                orderSB.append("ORDER BY ");
            } else {
                orderSB.append(",");
            }
            orderSB.append(String.format("%s.%s %s", table, orderType.getEn(), (ascDesc ? "asc" : "desc")));
        }
        return orderSB.toString();
    }

    public static boolean isCentralEmployee(TypeEnumeration role) {
        return CENTRAL_EMPLOYEE.contains(role);
    }

    public static boolean isDistributorEmployee(TypeEnumeration role) {
        return DISTRIBUTOR_EMPLOYEE.contains(role);
    }

    public static boolean isStoreEmployee(TypeEnumeration role) {
        return UR_STORE_ADMIN == role;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id
                + ",\"fa\":\"" + this.fa
                + "\",\"en\":\"" + this.en + "\"}";
        //+ "\",\"parent\":\"" + this.parent + "\"}";
    }
}