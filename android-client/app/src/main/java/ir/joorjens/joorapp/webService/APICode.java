package ir.joorjens.joorapp.webService;

/**
 * Created by Mohsen on 10/9/2017.
 */

public enum APICode {
    login,
    signUp,
    getProfile,
    updateProfile,
    activate,
    resendActivationCode,
    forgetPassword,
    forgetPasswordVerify,
    logout,
    changePassword,
    searchArea,
    getEnumValue,
    insertStore,
    viewStore,
    searchProduct,
    getProduct,
    getNewestProducts,
    getCheapestProducts,
    getTopSellingProducts,
    getDiscountProducts,

    getAllCategories,
    getFirstLevelCategoryPairs,
    getSubCategoryPairsOf,
    getHierarchicalCategories,

    searchDistributorProduct,
    getCheapestDProducts,
    getNewestDProducts,
    getTopSellingDProducts,
    getDiscountDProducts,
    getDistributorProduct,
    getDistributorPackage,
    searchDistributorPackages,
    searchProductDistributors,
    countProductInDPackage,
    getAllDProducts,

    getAllBanners,

    //============== Cart APIs ============
    addCart,
    getCart,
    getCartOffline,
    finalizeCart,
    searchCart,
    removeCart,
    updateCartEntity,
    getFavorites,
    pairStatusTypes,
    //============ Promotion APIs =========
    searchPromotion,
    //============ Brand APIs =========
    pairBrands,
    //============ Distributor APIs =========
    pairDistributors,
    searchDistributors,
    getDistributorDiscontentTypes,
    rateDistributor,

    searchTransaction,

    getSimilarCheapDProducts,
    getSimilarTopSaleDProducts,
    getSimilarTopSaleProducts,
    getSimilarProducts,
    getSimilarNewDProducts,
    getSimilarNewProducts,

    searchAdvertising,

    searchCartDist,

    getDashboardSaleByMonth,

    //======= Message APIs ========
    searchMessages,
    seenMessageReceiver
}
