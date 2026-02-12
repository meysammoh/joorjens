package ir.joorjens.aaa;

import ir.joorjens.model.util.TypeEnumeration;

/**
 * temp class for abstraction!!! and names
 * ex: instead of {@link ir.joorjens.model.util.TypeEnumeration#UR_CENTRAL_ADMIN#getId()} -> {@link #ca} :)
 * this class will be used in {@link ir.joorjens.aaa.UrlRolesType}
 */
abstract class T {
    //--------------------------------------------------------------------------------------------------

    static final int ca = TypeEnumeration.UR_CENTRAL_ADMIN.getId()//
            , cc = TypeEnumeration.UR_CENTRAL_CONTROLLER.getId()//
            , cs = TypeEnumeration.UR_CENTRAL_SUPPORTER.getId()//
            , co = TypeEnumeration.UR_CENTRAL_OPERATOR.getId()//
            , cac = TypeEnumeration.UR_CENTRAL_ACCOUNTANT.getId();
    //--------------------------------------------
    static final int da = TypeEnumeration.UR_DISTRIBUTION_ADMIN.getId()//
            , dc = TypeEnumeration.UR_DISTRIBUTION_CONTROLLER.getId()//
            , dop = TypeEnumeration.UR_DISTRIBUTION_OPERATOR.getId()//
            , dd = TypeEnumeration.UR_DISTRIBUTION_DELIVERER.getId();
    //--------------------------------------------
    static final int sa = TypeEnumeration.UR_STORE_ADMIN.getId();
    //--------------------------------------------
    static final int c = TypeEnumeration.UR_CUSTOMER.getId();
    //--------------------------------------------

    static final int[] all = new int[]{ca, cc, cs, co, cac, da, dc, dop, dd};

    //--------------------------------------------------------------------------------------------------

    static final String insert = "افزودن" //
            , update = "ویرایش"//
            , del = "حذف"//
            , search = "جستجوی"//
            , view = "مشاهده"//
            , child = "فرزندان"//
            , pair = "جفت"//
            , block = TypeEnumeration.ACTION_BLOCK.getFa()//
            ;
}