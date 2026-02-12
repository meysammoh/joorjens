package ir.joorjens.aaa;

import ir.joorjens.model.entity.*;

public enum UrlRolesType {

    MENU_ORDERS(1, false, false, null, "لیست سفارشات"
            , new int[]{T.ca}),
    MENU_SETTING(2, false, false, null, "تنظیمات"
            , new int[]{T.ca}),
    MENU_DISTRIBUTION(3, false, false, null, "شرکت‌های پخش"
            , new int[]{T.ca}),
    MENU_STORE(4, false, false, null, "فروشگاه‌ها"
            , new int[]{T.ca}),
    MENU_PRODUCT(5, false, false, null, "کالا"
            , new int[]{T.ca}),
    MENU_REPORT(6, false, false, null, "گزارشات"
            , new int[]{T.ca}),
    MENU_SEARCH(7, false, false, null, "%s کالا"
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    MENU_HOME(1010, false, false, null, Banner.getEN()
            , new int[]{T.ca}),

    DASHBOARD_SALE(1011, false, false, "dashboard/sale/", String.format("%s", "داشبورد میزان فروش")
            , new int[]{T.ca}),
    DASHBOARD_ORDER(1012, false, false, "dashboard/order/", String.format("%s", "داشبورد حالت های سفارشات")
            , new int[]{T.ca}),
    DASHBOARD_PRODUCT(1021, false, false, "dashboard/product/", String.format("%s", "داشبورد کالاها")
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_AREA(2010, false, false, null, Area.getEN()
            , new int[]{T.ca}),

    AREA_INSERT(2011, false, false, "area/insert/", String.format("%s %s", T.insert, Area.getEN())
            , new int[]{T.ca}),
    AREA_UPDATE(2012, false, false, "area/update/", String.format("%s %s", T.update, Area.getEN())
            , new int[]{T.ca}),
    AREA_DELETE(2013, false, false, "area/delete/", String.format("%s %s", T.del, Area.getEN())
            , new int[]{T.ca}),
    AREA_SEARCH(2014, false, false, "area/search/", String.format("%s %s", T.search, Area.getEN())
            , new int[]{T.ca}),
    AREA_VIEW(2015, false, false, "area/view/", String.format("%s %s", T.view, Area.getEN())
            , new int[]{T.ca}),
    AREA_CHILD(2016, false, false, "area/child/", String.format("%s %s", T.child, Area.getEN())
            , new int[]{T.ca}),
    AREA_PAIR(2017, true, false, "area/pair/", String.format("%s %s", T.pair, Area.getEN())
            , new int[]{}),
    AREA_BLOCK(2018, false, false, "area/block/", String.format("%s %s", T.block, Area.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_CONFIG(2020, false, false, null, ConfigField.getEN()
            , new int[]{T.ca}),

    CONFIG_UPDATE(2021, false, false, "configField/update/", String.format("%s %s", T.update, ConfigField.getEN())
            , new int[]{T.ca}),
    CONFIG_SEARCH(2022, false, false, "configField/search/", String.format("%s %s", T.search, ConfigField.getEN())
            , new int[]{T.ca}),
    CONFIG_VIEW(2023, false, false, "configField/view/", String.format("%s %s", T.view, ConfigField.getEN())
            , new int[]{T.ca}),
    CONFIG_PAIR(2024, true, false, "configField/pair/", String.format("%s %s", T.pair, ConfigField.getEN())
            , new int[]{}),
    CONFIG_ALL(2026, true, false, "configEnum/all/", String.format("تمامی %s", ConfigField.getEN())
            , new int[]{}),
    CONFIG_MAIN(2027, true, false, "configEnum/main/", String.format("تمامی %s اصلی", ConfigField.getEN())
            , new int[]{}),
    CONFIG_HIERARCHICAL(2028, true, false, "configEnum/hierarchical/", String.format("سلسله مراتب %s", ConfigField.getEN())
            , new int[]{}),
    CONFIG_ROLES(2029, true, false, "configEnum/roles/", "نقش‌های هم رده"
            , new int[]{}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_DISTRIBUTOR_DISCONTENT_TYPE(2030, false, false, null, DistributorDiscontentType.getEN()
            , new int[]{T.ca}),

    DISTRIBUTOR_DISCONTENT_TYPE_INSERT(2031, false, false, "distributorDiscontentType/insert/", String.format("%s %s", T.insert, DistributorDiscontentType.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_DISCONTENT_TYPE_UPDATE(2032, false, false, "distributorDiscontentType/update/", String.format("%s %s", T.update, DistributorDiscontentType.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_DISCONTENT_TYPE_DELETE(2033, false, false, "distributorDiscontentType/delete/", String.format("%s %s", T.del, DistributorDiscontentType.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_DISCONTENT_TYPE_SEARCH(2034, false, false, "distributorDiscontentType/search/", String.format("%s %s", T.search, DistributorDiscontentType.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_DISCONTENT_TYPE_VIEW(2035, false, false, "distributorDiscontentType/view/", String.format("%s %s", T.view, DistributorDiscontentType.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_DISCONTENT_TYPE_PAIR(2036, true, false, "distributorDiscontentType/pair/", String.format("%s %s", T.pair, DistributorDiscontentType.getEN())
            , new int[]{}),
    DISTRIBUTOR_DISCONTENT_TYPE_BLOCK(2038, false, false, "distributorDiscontentType/block/", String.format("%s %s", T.block, DistributorDiscontentType.getEN())
            , new int[]{T.ca}),

    DISTRIBUTOR_RATE_DISCONTENT_UPSERT(2041, false, false, "distributorRateDiscontent/upsert/", String.format("%s %s", "امتیاز به", DistributorRateDiscontent.getEN())
            , new int[]{T.ca, T.sa, T.c}),
    DISTRIBUTOR_RATE_DISCONTENT_PRE(2042, false, false, "distributorRateDiscontent/rated/", String.format("%s %s", "امتیاز داده شده به ", DistributorRateDiscontent.getEN())
            , new int[]{T.ca, T.sa, T.c}),
    DISTRIBUTOR_RATE_DISCONTENT_SEARCH(2044, false, false, "distributorRateDiscontent/search/", String.format("%s %s", T.search, DistributorRateDiscontent.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_RATE_DISCONTENT_VIEW(2045, false, false, "distributorRateDiscontent/view/", String.format("%s %s", T.view, DistributorRateDiscontent.getEN())
            , new int[]{T.ca}),
    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_ORDER_STATUS_TYPE(2050, false, false, null, OrderStatusType.getEN()
            , new int[]{T.ca}),

    ORDER_STATUS_TYPE_UPDATE(2051, false, false, "orderStatusType/update/", String.format("%s %s", T.update, OrderStatusType.getEN())
            , new int[]{T.ca}),
    ORDER_STATUS_TYPE_SEARCH(2052, false, false, "orderStatusType/search/", String.format("%s %s", T.search, OrderStatusType.getEN())
            , new int[]{T.ca}),
    ORDER_STATUS_TYPE_VIEW(2053, false, false, "orderStatusType/view/", String.format("%s %s", T.view, OrderStatusType.getEN())
            , new int[]{T.ca}),
    ORDER_STATUS_TYPE_PAIR(2054, true, false, "orderStatusType/pair/", String.format("%s %s", T.pair, OrderStatusType.getEN())
            , new int[]{}),
    ORDER_STATUS_TYPE_BLOCK(2058, false, false, "orderStatusType/block/", String.format("%s %s", T.block, OrderStatusType.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_ROLE(2060, false, false, null, Role.getEN()
            , new int[]{T.ca}),

    ROLE_UPDATE(2061, false, false, "role/update/", String.format("%s %s", T.update, Role.getEN())
            , new int[]{T.ca}),
    ROLE_SEARCH(2062, false, false, "role/search/", String.format("%s %s", T.search, Role.getEN())
            , new int[]{T.ca}),
    ROLE_VIEW(2063, false, false, "role/view/", String.format("%s %s", T.view, Role.getEN())
            , new int[]{T.ca}),
    ROLE_PAIR(2064, true, false, "role/pair/", String.format("%s %s", T.pair, Role.getEN())
            , new int[]{T.ca}),
    ROLE_PROFILE(2065, true, false, "role/profile/", String.format("%s %s", "پروفایل", Role.getEN())
            , T.all),
    ROLE_IP_UPDATE(2066, false, false, "role/ip/update/", String.format("%s %s", T.update, Role.getEN())
            , new int[]{T.ca}),
    ROLE_BLOCK(2068, false, false, "role/block/", String.format("%s %s", T.block, Role.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_PERMISSION(2070, false, false, null, Permission.getEN()
            , new int[]{T.ca}),

    PERMISSION_INSERT(2071, false, false, "permission/insert/", String.format("%s %s", T.insert, Permission.getEN())
            , new int[]{T.ca}),
    PERMISSION_UPDATE(2072, false, false, "permission/update/", String.format("%s %s", T.update, Permission.getEN())
            , new int[]{T.ca}),
    PERMISSION_DELETE(2073, false, false, "permission/delete/", String.format("%s %s", T.del, Permission.getEN())
            , new int[]{T.ca}),
    PERMISSION_SEARCH(2074, false, false, "permission/search/", String.format("%s %s", T.search, Permission.getEN())
            , new int[]{T.ca}),
    PERMISSION_VIEW(2075, false, false, "permission/view/", String.format("%s %s", T.view, Permission.getEN())
            , new int[]{T.ca}),
    PERMISSION_PAIR(2076, true, false, "permission/pair/", String.format("%s %s", T.pair, Permission.getEN())
            , new int[]{}),
    PERMISSION_BLOCK(2078, false, false, "permission/block/", String.format("%s %s", T.block, Permission.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_PRODUCT_CAT_TYPE(2080, false, false, null, ProductCategoryType.getEN()
            , new int[]{T.ca}),

    PRODUCT_CAT_TYPE_INSERT(2081, false, false, "productCategoryType/insert/", String.format("%s %s", T.insert, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_UPDATE(2082, false, false, "productCategoryType/update/", String.format("%s %s", T.update, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_DELETE(2083, false, false, "productCategoryType/delete/", String.format("%s %s", T.del, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_SEARCH(2084, false, false, "productCategoryType/search/", String.format("%s %s", T.search, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_VIEW(2085, false, false, "productCategoryType/view/", String.format("%s %s", T.view, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_CHILD(2086, false, false, "productCategoryType/child/", String.format("%s %s", T.child, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_PAIR(2087, true, false, "productCategoryType/pair/", String.format("%s %s", T.pair, ProductCategoryType.getEN())
            , new int[]{}),
    PRODUCT_CAT_TYPE_BLOCK(2088, false, false, "productCategoryType/block/", String.format("%s %s", T.block, ProductCategoryType.getEN())
            , new int[]{T.ca}),
    PRODUCT_CAT_TYPE_HIERARCHICAL(2089, true, false, "productCategoryType/hierarchical/", String.format("%s %s", "سلسله مراتب", ProductCategoryType.getEN())
            , new int[]{}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_PRODUCT_DETAIL_TYPE(2090, false, false, null, ProductDetailType.getEN()
            , new int[]{T.ca}),

    PRODUCT_DETAIL_TYPE_INSERT(2091, false, false, "productDetailType/insert/", String.format("%s %s", T.insert, ProductDetailType.getEN())
            , new int[]{T.ca}),
    PRODUCT_DETAIL_TYPE_UPDATE(2092, false, false, "productDetailType/update/", String.format("%s %s", T.update, ProductDetailType.getEN())
            , new int[]{T.ca}),
    PRODUCT_DETAIL_TYPE_DELETE(2093, false, false, "productDetailType/delete/", String.format("%s %s", T.del, ProductDetailType.getEN())
            , new int[]{T.ca}),
    PRODUCT_DETAIL_TYPE_SEARCH(2094, false, false, "productDetailType/search/", String.format("%s %s", T.search, ProductDetailType.getEN())
            , new int[]{T.ca}),
    PRODUCT_DETAIL_TYPE_VIEW(2095, false, false, "productDetailType/view/", String.format("%s %s", T.view, ProductDetailType.getEN())
            , new int[]{T.ca}),
    PRODUCT_DETAIL_TYPE_CHILD(2096, false, false, "productDetailType/child/", String.format("%s %s", T.child, ProductDetailType.getEN())
            , new int[]{T.ca}),
    PRODUCT_DETAIL_TYPE_PAIR(2097, true, false, "productDetailType/pair/", String.format("%s %s", T.pair, ProductDetailType.getEN())
            , new int[]{}),
    PRODUCT_DETAIL_TYPE_BLOCK(2098, false, false, "productDetailType/block/", String.format("%s %s", T.block, ProductDetailType.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_PRODUCT_RETURN_TYPE(2100, false, false, null, ProductReturnType.getEN()
            , new int[]{T.ca}),

    PRODUCT_RETURN_TYPE_INSERT(2101, false, false, "productReturnType/insert/", String.format("%s %s", T.insert, ProductReturnType.getEN())
            , new int[]{T.ca}),
    PRODUCT_RETURN_TYPE_UPDATE(2102, false, false, "productReturnType/update/", String.format("%s %s", T.update, ProductReturnType.getEN())
            , new int[]{T.ca}),
    PRODUCT_RETURN_TYPE_DELETE(2103, false, false, "productReturnType/delete/", String.format("%s %s", T.del, ProductReturnType.getEN())
            , new int[]{T.ca}),
    PRODUCT_RETURN_TYPE_SEARCH(2104, false, false, "productReturnType/search/", String.format("%s %s", T.search, ProductReturnType.getEN())
            , new int[]{T.ca}),
    PRODUCT_RETURN_TYPE_VIEW(2105, false, false, "productReturnType/view/", String.format("%s %s", T.view, ProductReturnType.getEN())
            , new int[]{T.ca}),
    PRODUCT_RETURN_TYPE_PAIR(2107, true, false, "productReturnType/pair/", String.format("%s %s", T.pair, ProductReturnType.getEN())
            , new int[]{}),
    PRODUCT_RETURN_TYPE_BLOCK(2108, false, false, "productReturnType/block/", String.format("%s %s", T.block, ProductReturnType.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_VEHICLE_TYPE(2110, false, false, null, VehicleBrandType.getEN()
            , new int[]{T.ca}),

    VEHICLE_TYPE_INSERT(2111, false, false, "vehicleBrandType/insert/", String.format("%s %s", T.insert, VehicleBrandType.getEN())
            , new int[]{T.ca}),
    VEHICLE_TYPE_UPDATE(2112, false, false, "vehicleBrandType/update/", String.format("%s %s", T.update, VehicleBrandType.getEN())
            , new int[]{T.ca}),
    VEHICLE_TYPE_DELETE(2113, false, false, "vehicleBrandType/delete/", String.format("%s %s", T.del, VehicleBrandType.getEN())
            , new int[]{T.ca}),
    VEHICLE_TYPE_SEARCH(2114, false, false, "vehicleBrandType/search/", String.format("%s %s", T.search, VehicleBrandType.getEN())
            , new int[]{T.ca}),
    VEHICLE_TYPE_VIEW(2115, false, false, "vehicleBrandType/view/", String.format("%s %s", T.view, VehicleBrandType.getEN())
            , new int[]{T.ca}),
    VEHICLE_TYPE_PAIR(2117, true, false, "vehicleBrandType/pair/", String.format("%s %s", T.pair, VehicleBrandType.getEN())
            , new int[]{}),
    VEHICLE_TYPE_BLOCK(2118, false, false, "vehicleBrandType/block/", String.format("%s %s", T.block, VehicleBrandType.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_PRODUCT_BRAND_TYPE(2130, false, false, null, ProductBrandType.getEN()
            , new int[]{T.ca}),

    PRODUCT_BRAND_TYPE_INSERT(2131, false, false, "productBrandType/insert/", String.format("%s %s", T.insert, ProductBrandType.getEN())
            , new int[]{T.ca}),
    PRODUCT_BRAND_TYPE_UPDATE(2132, false, false, "productBrandType/update/", String.format("%s %s", T.update, ProductBrandType.getEN())
            , new int[]{T.ca}),
    PRODUCT_BRAND_TYPE_DELETE(2133, false, false, "productBrandType/delete/", String.format("%s %s", T.del, ProductBrandType.getEN())
            , new int[]{T.ca}),
    PRODUCT_BRAND_TYPE_SEARCH(2134, false, false, "productBrandType/search/", String.format("%s %s", T.search, ProductBrandType.getEN())
            , new int[]{T.ca}),
    PRODUCT_BRAND_TYPE_VIEW(2135, false, false, "productBrandType/view/", String.format("%s %s", T.view, ProductBrandType.getEN())
            , new int[]{T.ca}),
    PRODUCT_BRAND_TYPE_CHILD(2136, false, false, "productBrandType/child/", String.format("%s %s", T.child, ProductBrandType.getEN())
            , new int[]{T.ca}),
    PRODUCT_BRAND_TYPE_PAIR(2137, true, false, "productBrandType/pair/", String.format("%s %s", T.pair, ProductBrandType.getEN())
            , new int[]{}),
    PRODUCT_BRAND_TYPE_BLOCK(2138, false, false, "productBrandType/block/", String.format("%s %s", T.block, ProductBrandType.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_COLOR_TYPE(2170, false, false, null, ColorType.getEN()
            , new int[]{T.ca}),

    COLOR_TYPE_INSERT(2171, false, false, "colorType/insert/", String.format("%s %s", T.insert, ColorType.getEN())
            , new int[]{T.ca}),
    COLOR_TYPE_UPDATE(2172, false, false, "colorType/update/", String.format("%s %s", T.update, ColorType.getEN())
            , new int[]{T.ca}),
    COLOR_TYPE_DELETE(2173, false, false, "colorType/delete/", String.format("%s %s", T.del, ColorType.getEN())
            , new int[]{T.ca}),
    COLOR_TYPE_SEARCH(2174, false, false, "colorType/search/", String.format("%s %s", T.search, ColorType.getEN())
            , new int[]{T.ca}),
    COLOR_TYPE_VIEW(2175, false, false, "colorType/view/", String.format("%s %s", T.view, ColorType.getEN())
            , new int[]{T.ca}),
    COLOR_TYPE_PAIR(2177, true, false, "colorType/pair/", String.format("%s %s", T.pair, ColorType.getEN())
            , new int[]{}),
    COLOR_TYPE_BLOCK(2178, false, false, "colorType/block/", String.format("%s %s", T.block, ColorType.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_BANNER(2190, false, false, null, Banner.getEN()
            , new int[]{T.ca}),

    BANNER_INSERT(2191, false, false, "banner/insert/", String.format("%s %s", T.insert, Banner.getEN())
            , new int[]{T.ca}),
    BANNER_UPDATE(2192, false, false, "banner/update/", String.format("%s %s", T.update, Banner.getEN())
            , new int[]{T.ca}),
    BANNER_DELETE(2193, false, false, "banner/delete/", String.format("%s %s", T.del, Banner.getEN())
            , new int[]{T.ca}),
    BANNER_SEARCH(2194, false, false, "banner/search/", String.format("%s %s", T.search, Banner.getEN())
            , new int[]{T.ca}),
    BANNER_VIEW(2195, false, false, "banner/view/", String.format("%s %s", T.view, Banner.getEN())
            , new int[]{T.ca}),
    BANNER_BLOCK(2198, false, false, "banner/block/", String.format("%s %s", T.block, Banner.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_DICTIONARY(2200, false, false, null, Dictionary.getEN()
            , new int[]{T.ca}),

    DICTIONARY_INSERT(2201, false, false, "dictionary/insert/", String.format("%s %s", T.insert, Dictionary.getEN())
            , new int[]{T.ca}),
    DICTIONARY_UPDATE(2202, false, false, "dictionary/update/", String.format("%s %s", T.update, Dictionary.getEN())
            , new int[]{T.ca}),
    DICTIONARY_DELETE(2203, false, false, "dictionary/delete/", String.format("%s %s", T.del, Dictionary.getEN())
            , new int[]{T.ca}),
    DICTIONARY_SEARCH(2204, false, false, "dictionary/search/", String.format("%s %s", T.search, Dictionary.getEN())
            , new int[]{T.ca}),
    DICTIONARY_VIEW(2205, true, false, "dictionary/view/", String.format("%s %s", T.view, Dictionary.getEN())
            , new int[]{}),
    DICTIONARY_BLOCK(2208, false, false, "dictionary/block/", String.format("%s %s", T.block, Dictionary.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_BONUS_CARD(2230, false, false, null, BonusCard.getEN()
            , new int[]{T.ca}),

    BONUS_CARD_INSERT(2231, false, false, "bonusCard/insert/", String.format("%s %s", T.insert, BonusCard.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_UPDATE(2232, false, false, "bonusCard/update/", String.format("%s %s", T.update, BonusCard.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_DELETE(2233, false, false, "bonusCard/delete/", String.format("%s %s", T.del, BonusCard.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_SEARCH(2234, false, false, "bonusCard/search/", String.format("%s %s", T.search, BonusCard.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_VIEW(2235, false, false, "bonusCard/view/", String.format("%s %s", T.view, BonusCard.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_BLOCK(2238, false, false, "bonusCard/block/", String.format("%s %s", T.block, BonusCard.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_BONUS_CARD_DETAIL(2250, false, false, null, BonusCardDetail.getEN()
            , new int[]{T.ca}),

    BONUS_CARD_DETAIL_INSERT(2251, false, false, "bonusCardDetail/insert/", String.format("%s %s", T.insert, BonusCardDetail.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_DETAIL_UPDATE(2252, false, false, "bonusCardDetail/update/", String.format("%s %s", T.update, BonusCardDetail.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_DETAIL_DELETE(2253, false, false, "bonusCardDetail/delete/", String.format("%s %s", T.del, BonusCardDetail.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_DETAIL_SEARCH(2254, false, false, "bonusCardDetail/search/", String.format("%s %s", T.search, BonusCardDetail.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_DETAIL_VIEW(2255, false, false, "bonusCardDetail/view/", String.format("%s %s", T.view, BonusCardDetail.getEN())
            , new int[]{T.ca}),
    BONUS_CARD_DETAIL_BLOCK(2258, false, false, "bonusCardDetail/block/", String.format("%s %s", T.block, BonusCardDetail.getEN())
            , new int[]{T.ca}),

    BONUS_CARD_DETAIL_USE(2261, true, false, "bonusCardDetail/use/", String.format("%s %s", "استفاده", BonusCardDetail.getEN())
            , new int[]{T.ca, T.sa, T.c}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_ADVERTISING(2270, false, false, null, Advertising.getEN()
            , new int[]{T.ca}),

    ADVERTISING_INSERT(2271, false, false, "advertising/insert/", String.format("%s %s", T.insert, Advertising.getEN())
            , new int[]{T.ca}),
    ADVERTISING_UPDATE(2272, false, false, "advertising/update/", String.format("%s %s", T.update, Advertising.getEN())
            , new int[]{T.ca}),
    ADVERTISING_DELETE(2273, false, false, "advertising/delete/", String.format("%s %s", T.del, Advertising.getEN())
            , new int[]{T.ca}),
    ADVERTISING_SEARCH(2274, false, false, "advertising/search/", String.format("%s %s", T.search, Advertising.getEN())
            , new int[]{T.ca}),
    ADVERTISING_VIEW(2275, false, false, "advertising/view/", String.format("%s %s", T.view, Advertising.getEN())
            , new int[]{T.ca}),
    ADVERTISING_BLOCK(2278, false, false, "advertising/block/", String.format("%s %s", T.block, Advertising.getEN())
            , new int[]{T.ca}),

    ADVERTISING_CLICK(2281, false, false, "advertising/click/", String.format("%s %s", T.insert, Advertising.getEN())
            , new int[]{T.ca, T.sa, T.c}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_PROMOTION(2300, false, false, null, Promotion.getEN()
            , new int[]{T.ca}),

    PROMOTION_INSERT(2301, false, false, "promotion/insert/", String.format("%s %s", T.insert, Promotion.getEN())
            , new int[]{T.ca}),
    PROMOTION_UPDATE(2302, false, false, "promotion/update/", String.format("%s %s", T.update, Promotion.getEN())
            , new int[]{T.ca}),
    PROMOTION_DELETE(2303, false, false, "promotion/delete/", String.format("%s %s", T.del, Promotion.getEN())
            , new int[]{T.ca}),
    PROMOTION_SEARCH(2304, false, false, "promotion/search/", String.format("%s %s", T.search, Promotion.getEN())
            , new int[]{T.ca}),
    PROMOTION_VIEW(2305, true, false, "promotion/view/", String.format("%s %s", T.view, Promotion.getEN())
            , new int[]{}),
    PROMOTION_BLOCK(2308, false, false, "promotion/block/", String.format("%s %s", T.block, Promotion.getEN())
            , new int[]{T.ca}),
    PROMOTION_MESSAGE(2311, true, false, "promotion/message/", String.format("%s %s", T.view, Promotion.getEN())
            , T.all),

    //---------------------------------------------------------------------------------------------------------------

    MENU_SETTING_DISTRIBUTOR_PROMOTION(2400, false, false, null, DistributorPromotion.getEN()
            , new int[]{T.ca}),

    DISTRIBUTOR_PROMOTION_INSERT(2401, false, false, "distributorPromotion/insert/", String.format("%s %s", T.insert, DistributorPromotion.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PROMOTION_UPDATE(2402, false, false, "distributorPromotion/update/", String.format("%s %s", T.update, DistributorPromotion.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PROMOTION_DELETE(2403, false, false, "distributorPromotion/delete/", String.format("%s %s", T.del, DistributorPromotion.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PROMOTION_SEARCH(2404, false, false, "distributorPromotion/search/", String.format("%s %s", T.search, DistributorPromotion.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PROMOTION_VIEW(2405, true, false, "distributorPromotion/view/", String.format("%s %s", T.view, DistributorPromotion.getEN())
            , new int[]{}),
    DISTRIBUTOR_PROMOTION_BLOCK(2408, false, false, "distributorPromotion/block/", String.format("%s %s", T.block, DistributorPromotion.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PROMOTION_MESSAGE(2411, true, false, "distributorPromotion/message/", String.format("%s %s", T.view, DistributorPromotion.getEN())
            , T.all),

    //---------------------------------------------------------------------------------------------------------------

    CARTABLE_APPROVE(2911, false, false, "cartable/approve/", String.format("%s %s", "تایید", Cartable.getEN())
            , new int[]{T.ca, T.cc, T.dc}),
    CARTABLE_REJECT(2912, false, false, "cartable/reject/", String.format("%s %s", "رد", Cartable.getEN())
            , new int[]{T.ca, T.cc, T.dc}),
    CARTABLE_SEARCH(2913, false, false, "cartable/search/", String.format("%s %s", T.search, Cartable.getEN())
            , new int[]{T.ca, T.cc, T.dc}),
    CARTABLE_VIEW(2914, false, false, "cartable/view/", String.format("%s %s", T.view, Cartable.getEN())
            , new int[]{T.ca, T.cc, T.dc}),
    CARTABLE_RENEW(2917, false, false, "cartable/renew/", String.format("%s %s", "تجدید", Cartable.getEN())
            , new int[]{T.ca, T.cc, T.dc}),
    CARTABLE_BLOCK(2918, false, false, "cartable/block/", String.format("%s %s", T.block, Cartable.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MESSAGE_INSERT(2931, false, false, "message/insert/", String.format("%s %s", T.insert, Message.getEN())
            , new int[]{T.ca}),
    MESSAGE_UPDATE(2932, false, false, "message/update/", String.format("%s %s", T.update, Message.getEN())
            , new int[]{T.ca}),
    MESSAGE_DELETE(2933, false, false, "message/delete/", String.format("%s %s", T.del, Message.getEN())
            , new int[]{T.ca}),
    MESSAGE_SEARCH(2934, false, false, "message/search/", String.format("%s %s", T.search, Message.getEN())
            , new int[]{T.ca}),
    MESSAGE_VIEW(2935, false, false, "message/view/", String.format("%s %s", T.view, Message.getEN())
            , new int[]{T.ca}),
    MESSAGE_SEEN_RECEIVER(2936, false, false, "message/receiver/seen/", String.format("%s %s", "خوانده شده", MessageReceiver.getEN())
            , T.all),
    MESSAGE_DELETE_RECEIVER(2937, false, false, "message/receiver/delete/", String.format("%s %s", T.del, MessageReceiver.getEN())
            , T.all),

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    MENU_DISTRIBUTOR_VIEW(3010, false, false, null, Distributor.getEN()
            , new int[]{T.ca, T.da}),

    DISTRIBUTOR_INSERT(3011, false, false, "distributor/insert/", String.format("%s %s", T.insert, Distributor.getEN())
            , new int[]{T.ca, T.da, T.c}),
    DISTRIBUTOR_UPDATE(3012, false, false, "distributor/update/", String.format("%s %s", T.update, Distributor.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_DELETE(3013, false, false, "distributor/delete/", String.format("%s %s", T.del, Distributor.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_SEARCH(3014, false, false, "distributor/search/", String.format("%s %s", T.search, Distributor.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_VIEW(3015, false, false, "distributor/view/", String.format("%s %s", T.view, Distributor.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_PAIR(3016, true, false, "distributor/pair/", String.format("%s %s", T.pair, Distributor.getEN())
            , new int[]{}),
    DISTRIBUTOR_EXIST(3017, false, false, "distributor/exist/", String.format("%s %s", "وجود", Distributor.getEN())
            , new int[]{T.ca, T.da, T.c}),
    DISTRIBUTOR_BLOCK(3018, false, false, "distributor/block/", String.format("%s %s", T.block, Distributor.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_RATE(3021, false, false, "distributor/rate/", String.format("%s %s", "امتیاز به", Distributor.getEN())
            , new int[]{T.ca, T.sa, T.c}),
    DISTRIBUTOR_PROFILE(3024, true, false, "distributor/profile/", String.format("%s %s", "پروفایل", Distributor.getEN())
            , new int[]{T.ca, T.da, T.c}),

    DISTRIBUTOR_INSERT_BRANCH(3031, false, false, "distributor/insertBranch/", String.format("%s %s %s", T.insert, "شعبه ", Distributor.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_DELETE_BRANCH(3032, false, false, "distributor/deleteBranch/", String.format("%s %s %s", T.del, "شعبه ", Distributor.getEN())
            , new int[]{T.ca}),

    VEHICLE_INSERT(3051, false, false, "vehicle/insert/", String.format("%s %s", T.insert, Vehicle.getEN())
            , new int[]{T.ca, T.da, T.c}),
    VEHICLE_UPDATE(3052, false, false, "vehicle/update/", String.format("%s %s", T.update, Vehicle.getEN())
            , new int[]{T.ca, T.da}),
    VEHICLE_DELETE(3053, false, false, "vehicle/delete/", String.format("%s %s", T.del, Vehicle.getEN())
            , new int[]{T.ca}),
    VEHICLE_SEARCH(3054, false, false, "vehicle/search/", String.format("%s %s", T.search, Vehicle.getEN())
            , new int[]{T.ca, T.da}),
    VEHICLE_VIEW(3055, false, false, "vehicle/view/", String.format("%s %s", T.view, Vehicle.getEN())
            , new int[]{T.ca, T.da}),
    VEHICLE_PAIR(3056, true, false, "vehicle/pair/", String.format("%s %s", T.pair, Vehicle.getEN())
            , new int[]{}),
    VEHICLE_BLOCK(3058, false, false, "vehicle/block/", String.format("%s %s", T.block, Vehicle.getEN())
            , new int[]{T.ca}),

    DISTRIBUTOR_DELIVERER_INSERT(3071, false, false, "distributorDeliverer/insert/", String.format("%s %s", T.insert, DistributorDeliverer.getEN())
            , new int[]{T.ca, T.da, T.c}),
    DISTRIBUTOR_DELIVERER_UPDATE(3072, false, false, "distributorDeliverer/update/", String.format("%s %s", T.update, DistributorDeliverer.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_DELIVERER_SEARCH(3074, false, false, "distributorDeliverer/search/", String.format("%s %s", T.search, DistributorDeliverer.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_DELIVERER_VIEW(3075, false, false, "distributorDeliverer/view/", String.format("%s %s", T.view, DistributorDeliverer.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_DELIVERER_PAIR(3076, true, false, "distributorDeliverer/pair/", String.format("%s %s", T.pair, DistributorDeliverer.getEN())
            , new int[]{}),
    DISTRIBUTOR_DELIVERER_BLOCK(3078, false, false, "distributorDeliverer/block/", String.format("%s %s", T.block, DistributorDeliverer.getEN())
            , new int[]{T.ca}),

    DISTRIBUTOR_EMPLOYEE_INSERT(3091, false, false, "distributorEmployee/insert/", String.format("%s %s", T.insert, DistributorEmployee.getEN())
            , new int[]{T.ca, T.da, T.c}),
    DISTRIBUTOR_EMPLOYEE_UPDATE(3092, false, false, "distributorEmployee/update/", String.format("%s %s", T.update, DistributorEmployee.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_EMPLOYEE_SEARCH(3094, false, false, "distributorEmployee/search/", String.format("%s %s", T.search, DistributorEmployee.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_EMPLOYEE_VIEW(3095, false, false, "distributorEmployee/view/", String.format("%s %s", T.view, DistributorEmployee.getEN())
            , new int[]{T.ca, T.da}),
    DISTRIBUTOR_EMPLOYEE_PAIR(3096, true, false, "distributorEmployee/pair/", String.format("%s %s", T.pair, DistributorEmployee.getEN())
            , new int[]{}),
    DISTRIBUTOR_EMPLOYEE_BLOCK(3098, false, false, "distributorEmployee/block/", String.format("%s %s", T.block, DistributorEmployee.getEN())
            , new int[]{T.ca}),


    DISTRIBUTOR_PRODUCT_INSERT(3201, false, false, "distributorProduct/insert/", String.format("%s %s", T.insert, DistributorProduct.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PRODUCT_UPDATE(3202, false, false, "distributorProduct/update/", String.format("%s %s", T.update, DistributorProduct.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PRODUCT_DELETE(3203, false, false, "distributorProduct/delete/", String.format("%s %s", T.del, DistributorProduct.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PRODUCT_SEARCH(3204, false, false, "distributorProduct/search/", String.format("%s %s", T.search, DistributorProduct.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PRODUCT_VIEW(3205, false, false, "distributorProduct/view/", String.format("%s %s", T.view, DistributorProduct.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PRODUCT_PAIR(3206, true, false, "distributorProduct/pair/", String.format("%s %s", T.pair, DistributorProduct.getEN())
            , new int[]{}),
    DISTRIBUTOR_PRODUCT_BLOCK(3208, false, false, "distributorProduct/block/", String.format("%s %s", T.block, DistributorProduct.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PRODUCT_PRICE(3211, false, false, "distributorProduct/price/", String.format("%s %s", "قیمت", DistributorProduct.getEN())
            , new int[]{T.ca}),

    DISTRIBUTOR_PRODUCT_PRICE_HISTORY_SEARCH(3222, false, false, "distributorProductPriceHistory/search/", String.format("%s %s",  T.search, ProductPriceHistory.getEN())
            , new int[]{T.ca}),

    DISTRIBUTOR_PACKAGE_INSERT(3231, false, false, "distributorPackage/insert/", String.format("%s %s", T.insert, DistributorPackage.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PACKAGE_UPDATE(3232, false, false, "distributorPackage/update/", String.format("%s %s", T.update, DistributorPackage.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PACKAGE_DELETE(3233, false, false, "distributorPackage/delete/", String.format("%s %s", T.del, DistributorPackage.getEN())
            , new int[]{T.ca}),
    DISTRIBUTOR_PACKAGE_SEARCH(3234, false, false, "distributorPackage/search/", String.format("%s %s", T.search, DistributorPackage.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PACKAGE_VIEW(3235, false, false, "distributorPackage/view/", String.format("%s %s", T.view, DistributorPackage.getEN())
            , new int[]{T.ca, T.sa}),
    DISTRIBUTOR_PACKAGE_BLOCK(3238, false, false, "distributorPackage/block/", String.format("%s %s", T.block, DistributorPackage.getEN())
            , new int[]{T.ca}),

    DISTRIBUTOR_PACKAGE_PRODUCT_COUNT_IN(3241, false, false, "distributorPackage/productCountIn/", String.format("%s %s", "تعداد محصول در", DistributorPackage.getEN())
            , new int[]{T.ca, T.sa, T.c}),

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    MENU_STORE_VIEW(4010, false, false, null, Store.getEN()
            , new int[]{T.ca, T.sa}),

    STORE_INSERT(4011, false, false, "store/insert/", String.format("%s %s", T.insert, Store.getEN())
            , new int[]{T.ca, T.sa, T.c}),
    STORE_UPDATE(4012, false, false, "store/update/", String.format("%s %s", T.update, Store.getEN())
            , new int[]{T.ca, T.sa}),
    STORE_DELETE(4013, false, false, "store/delete/", String.format("%s %s", T.del, Store.getEN())
            , new int[]{T.ca}),
    STORE_SEARCH(4014, false, false, "store/search/", String.format("%s %s", T.search, Store.getEN())
            , new int[]{T.ca, T.sa}),
    STORE_VIEW(4015, false, false, "store/view/", String.format("%s %s", T.view, Store.getEN())
            , new int[]{T.ca, T.sa}),
    STORE_PAIR(4016, true, false, "store/pair/", String.format("%s %s", T.pair, Store.getEN())
            , new int[]{}),
    STORE_BLOCK(4018, false, false, "store/block/", String.format("%s %s", T.block, Store.getEN())
            , new int[]{T.ca}),
    STORE_PROFILE(4024, true, false, "store/profile/", String.format("%s %s", "پروفایل", Store.getEN())
            , new int[]{T.ca, T.sa}),

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    MENU_PRODUCT_VIEW(5010, false, false, null, Product.getEN()
            , new int[]{T.ca}),

    PRODUCT_INSERT(5011, false, false, "product/insert/", String.format("%s %s", T.insert, Product.getEN())
            , new int[]{T.ca, T.sa, T.c}),
    PRODUCT_UPDATE(5012, false, false, "product/update/", String.format("%s %s", T.update, Product.getEN())
            , new int[]{T.ca, T.sa}),
    PRODUCT_DELETE(5013, false, false, "product/delete/", String.format("%s %s", T.del, Product.getEN())
            , new int[]{T.ca}),
    PRODUCT_SEARCH(5014, false, false, "product/search/", String.format("%s %s", T.search, Product.getEN())
            , new int[]{T.ca, T.sa}),
    PRODUCT_VIEW(5015, false, false, "product/view/", String.format("%s %s", T.view, Product.getEN())
            , new int[]{T.ca, T.sa}),
    PRODUCT_PAIR(5016, true, false, "product/pair/", String.format("%s %s", T.pair, Product.getEN())
            , new int[]{}),
    PRODUCT_EXIST(5017, false, false, "product/exist/", String.format("%s %s", "وجود", Product.getEN())
            , new int[]{T.ca, T.da, T.c}),
    PRODUCT_BLOCK(5018, false, false, "product/block/", String.format("%s %s", T.block, Product.getEN())
            , new int[]{T.ca}),
    PRODUCT_PRICE(5021, false, false, "product/price/", String.format("%s %s", "قیمت", Product.getEN())
            , new int[]{T.ca}),
    PRODUCT_PRICE_BATCH(5022, false, false, "product/price/batch/", String.format("%s %s", "قیمت دسته‌ای", Product.getEN())
            , new int[]{T.ca}),

    PRODUCT_PRICE_HISTORY_SEARCH(5111, false, false, "productPriceHistory/search/", String.format("%s %s",  T.search, ProductPriceHistory.getEN())
            , new int[]{T.ca}),

    CART_SEARCH(5214, false, false, "cart/search/", String.format("%s %s", T.search, Cart.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_VIEW(5215, false, false, "cart/view/", String.format("%s %s", T.view, Cart.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_GET(5221, false, false, "cart/get/", String.format("%s %s", Cart.getEN(), "من")
            , new int[]{T.ca, T.sa, T.c}),
    CART_FAVORITE(5216, false, false, "cart/favorite/", String.format("%s", "سفارشات محبوب من")
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),

    CART_UPDATE(5218, false, false, "cart/update/", String.format("%s %s", T.update, CartDistributorPackduct.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_UPDATE_BATCH_PACKDUCT(5219, false, false, "cart/updateBatch/", String.format("%s دسته ای %s", T.update, CartDistributorPackduct.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_UPDATE_BATCH_DIST(5220, false, false, "cart/updateBatchDist/", String.format("%s دسته ای %s", T.update, CartDistributor.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),

    CART_CLEAR(5222, false, false, "cart/clear/", String.format("%s %s", "پاک کردن", Cart.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_ADD(5224, false, false, "cart/add/", String.format("%s %s", T.insert, Cart.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_REMOVE(5225, false, false, "cart/remove/", String.format("%s %s", T.del, Cart.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_FINALIZE(5228, false, false, "cart/finalize/", String.format("%s %s", "نهایی کردن", Cart.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),

    CART_DIST_UPDATE_DELIVERER(5242, false, false, "cartDistributor/updateDeliverer/", String.format("%s %s %s", T.update, "تحویل دهنده", CartDistributorPackduct.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),
    CART_DIST_SEARCH(5244, false, false, "cartDistributor/search/", String.format("%s %s", T.search, CartDistributor.getEN())
            , new int[]{T.ca, T.co, T.dop, T.sa, T.c}),

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------

    MENU_CUSTOMER(10010, false, false, null, Customer.getEN()
            , new int[]{T.ca}),

    CUSTOMER_UPDATE(10011, false, false, "customer/update/", String.format("%s %s", T.update, Customer.getEN())
            , new int[]{T.ca}),
    CUSTOMER_SEARCH(10012, false, false, "customer/search/", String.format("%s %s", T.search, Customer.getEN())
            , new int[]{T.ca}),
    CUSTOMER_VIEW(10013, false, false, "customer/view/", String.format("%s %s", T.view, Customer.getEN())
            , new int[]{T.ca}),
    CUSTOMER_PAIR(10014, true, false, "customer/pair/", String.format("%s %s", T.pair, Customer.getEN())
            , new int[]{}),
    CUSTOMER_RESET_PASS(10015, true, false, "customer/resetPassword/", String.format("تغییر رمز عبور %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_LOGOUT(10016, true, false, "customer/logout/", String.format("خروج %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_PROFILE(10017, true, false, "customer/profile/", String.format("%s %s", "پروفایل", Customer.getEN())
            , T.all),
    CUSTOMER_BLOCK(10018, false, false, "customer/block/", String.format("%s %s", T.block, Customer.getEN())
            , new int[]{T.ca}),

    CUSTOMER_LOGIN(10031, false, true, "customer/login/", String.format("ورود %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_SIGNUP(10032, false, true, "customer/signup/", String.format("ثبت‌نام %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_ACTIVATE(10033, false, true, "customer/activate/", String.format("فعال‌سازی %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_RESEND_ACTIVATE(10034, false, true, "customer/resendActivationCode/", String.format("فرستادن دوباره کد فعال‌سازی %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_FORGET_PASS(10035, false, true, "customer/forgetPassword/", String.format("فراموشی رمز عبور %s", Customer.getEN())
            , new int[]{}),
    CUSTOMER_FORGET_PASS_VERIFY(10036, false, true, "customer/forgetPasswordVerify/", String.format("تایید فراموشی رمز عبور %s", Customer.getEN())
            , new int[]{}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_CUSTOMER_SESSION(10110, false, false, null, Customer.getEN()
            , new int[]{T.ca}),

    CUSTOMER_SESSION_DELETE(10111, false, false, "customerSession/delete/", String.format("%s %s", T.del, CustomerSession.getEN())
            , new int[]{T.ca}),
    CUSTOMER_SESSION_VIEW(10112, false, false, "customerSession/view/", String.format("%s %s", T.view, CustomerSession.getEN())
            , new int[]{T.ca}),
    CUSTOMER_SESSION_SEARCH(10113, false, false, "customerSession/search/", String.format("%s %s", T.search, CustomerSession.getEN())
            , new int[]{T.ca}),
    CUSTOMER_SESSION_BLOCK(10118, false, false, "customerSession/block/", String.format("%s %s", T.block, CustomerSession.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_CUSTOMER_LOGIN(10120, false, false, null, Customer.getEN()
            , new int[]{T.ca}),

    CUSTOMER_LOGIN_INSERT(10121, false, false, "customerLogin/insert/", String.format("%s %s", T.insert, CustomerLogin.getEN())
            , new int[]{T.ca}),
    CUSTOMER_LOGIN_UPDATE(10122, false, false, "customerLogin/update/", String.format("%s %s", T.update, CustomerLogin.getEN())
            , new int[]{T.ca}),
    CUSTOMER_LOGIN_DELETE(10123, false, false, "customerLogin/delete/", String.format("%s %s", T.del, CustomerLogin.getEN())
            , new int[]{T.ca}),
    CUSTOMER_LOGIN_VIEW(10124, false, false, "customerLogin/view/", String.format("%s %s", T.view, CustomerLogin.getEN())
            , new int[]{T.ca}),
    CUSTOMER_LOGIN_SEARCH(10125, false, false, "customerLogin/search/", String.format("%s %s", T.search, CustomerLogin.getEN())
            , new int[]{T.ca}),
    CUSTOMER_LOGIN_BLOCK(10128, false, false, "customerLogin/block/", String.format("%s %s", T.block, CustomerLogin.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    TRANSACTION_SEARCH(10201, false, false, "transaction/search/", String.format("%s %s", T.search, Transaction.getEN())
            ,T.all),

    //---------------------------------------------------------------------------------------------------------------

    MENU_LOG(20010, false, false, null, Log.getEN()
            , new int[]{T.ca}),

    LOG_SEARCH(20011, false, false, "log/search/", String.format("%s %s", T.search, Log.getEN())
            , new int[]{T.ca}),
    LOG_BLOCK(20018, false, false, "log/block/", String.format("%s %s", T.block, Log.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    MENU_POSITION(10060, false, false, null, Position.getEN()
            , new int[]{T.ca}),

    POSITION_ADD(10061, false, false, "position/add/", String.format("%s %s", T.insert, Position.getEN())
            , new int[]{T.ca}),
    POSITION_SEARCH(10062, false, false, "position/search/", String.format("%s %s", T.search, Position.getEN())
            , new int[]{T.ca}),
    POSITION_BLOCK(10068, false, false, "position/block/", String.format("%s %s", T.block, Position.getEN())
            , new int[]{T.ca}),

    //---------------------------------------------------------------------------------------------------------------

    ;

    private final int id;
    private final boolean justLogin;
    private final boolean noLogin;
    private final String url;
    private final String nameFa;
    private final int[] roles;

    UrlRolesType(int id, boolean justLogin, boolean noLogin, String url, String nameFa, int[] roles) {
        this.id = id;
        this.justLogin = justLogin;
        this.noLogin = noLogin;
        this.url = url;
        this.roles = roles;
        this.nameFa = nameFa;
    }

    public int getId() {
        return id;
    }

    public boolean isJustLogin() {
        return justLogin;
    }

    public boolean isNoLogin() {
        return noLogin;
    }

    public String getUrl() {
        return url;
    }

    public String getNameFa() {
        return nameFa;
    }

    public int[] getRoles() {
        return roles;
    }

    public boolean isMenu() {
        return url == null || url.isEmpty();
    }
}