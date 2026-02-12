package ir.joorjens.joorapp.utils;

/**
 * Created by Mohsen on 10/7/2017.
 */

public class EnumHelper {

    public enum AllEnumNames{
        Gender,
        AdministrativeDivision, // root
        Province, // استان
        City,  // شهر
        Zone, // منطقه
        District, // ناحیه
        Street, // خیابان
        ActivityType,
        //=============== Discount Enums =================
        DiscountType,
        DiscountSpecial,
        DiscountCash,
        DiscountOffer,

        //================ Cart entity types enum ================
        CartPackage,
        CartProduct,

        //================ OrderTypes ====================
        OrderTypeIdPrice,
        OrderTypeIdTime,
        OrderTypeIdSelling,
        OrderTypeIdName,
        OrderTypeIdRating,

        //================ Order Status ==================
        OS_RECEIVED,
        OS_APPROVE,
        OS_PAYED,
        OS_PROCESSED,
        OS_READY,
        OS_POST,
        OS_DELIVERED,


        Role_Store_Admin,
        Role_Distribution_Deliverer,
        Role_Customer,

        // Dashboard time format
        DashboardTimeYear,
        DashboardTimeMonth,
        DashboardTimeWeek,
        DashboardTimeDay,
        DashboardTimeHour

    }

    public static boolean loadAllEnums(){
        return true;
    }

    public static int getEnumCode(AllEnumNames name){
        switch (name){
            case Gender:
                return 3020;
            case AdministrativeDivision:
                return 3010;
            case Province:
                return 3011;
            case City:
                return 3012;
            case Zone:
                return 3013;
            case District:
                return 3014;
            case Street:
                return 3015;
            case ActivityType:
                return 3100;
            //=======================
            case DiscountType:
                return 3240;
            case DiscountSpecial:
                return 3241;
            case DiscountCash:
                return 3242;
            case DiscountOffer:
                return 3243;
            //=======================

            case CartPackage:
                return 221;
            case CartProduct:
                return 211;
            //=======================
            case OrderTypeIdPrice:
                return 142;
            case OrderTypeIdTime:
                return 91;
            case OrderTypeIdName:
                return 94;
            case OrderTypeIdRating:
                return 95;
            case OrderTypeIdSelling:
                return 143;

            //=======================
            case OS_RECEIVED:
                return 1111;
            case OS_APPROVE:
                return 1114;
            case OS_PAYED:
                return 1117;
            case OS_PROCESSED:
                return 1120;
            case OS_READY:
                return 1123;
            case OS_POST:
                return 1126;
            case OS_DELIVERED:
                return 1129;

            //=======================
            case Role_Store_Admin:
                return 1031;
            case Role_Distribution_Deliverer:
                return 1054;
            case Role_Customer:
                return 1011;

            //========================
            case DashboardTimeYear:
                return 71;
            case DashboardTimeMonth:
                return 72;
            case DashboardTimeWeek:
                return 73;
            case DashboardTimeDay:
                return 74;
            case DashboardTimeHour:
                return 75;

            default:
                return -1;
        }
    }
}
