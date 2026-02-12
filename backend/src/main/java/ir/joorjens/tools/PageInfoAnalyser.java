package ir.joorjens.tools;

import ir.joorjens.common.Utility;
import ir.joorjens.common.file.FileRW;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductBrandTypeRepository;
import ir.joorjens.dao.interfaces.ProductCategoryTypeRepository;
import ir.joorjens.dao.interfaces.ProductDetailTypeRepository;
import ir.joorjens.dao.interfaces.ProductRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PageInfoAnalyser {

    //---------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(PageInfoAnalyser.class);
    //---------------------------------------------------------------------------
    private static final String FOLDER_READ = "/home/mehdi/Desktop/joorjens_data/json/";
    private static final String FOLDER_READ_IMAGE = "/home/mehdi/Desktop/joorjens_data/images/";
    private static final String FOLDER_WRITE_IMAGE = "/home/mehdi/Desktop/joorjens_data/product_image/";
    private static final String FILE_MAP_CAT = "/home/mehdi/Desktop/joorjens_data/map_cats.csv";
    private static final String NOT_HAVE = "نداریم";
    private static final String SPLITTER = "\\s*#\\s*";
    private static final String PUNCH = "[\\p{Punct}]+";
    //---------------------------------------------------------------------------
    private static final ProductRepository REPO_PRODUCT = (ProductRepository) RepositoryManager.getByEntity(Product.class);
    private static final ProductCategoryTypeRepository REPO_CAT = (ProductCategoryTypeRepository) RepositoryManager.getByEntity(ProductCategoryType.class);
    private static final ProductBrandTypeRepository REPO_BRAND = (ProductBrandTypeRepository) RepositoryManager.getByEntity(ProductBrandType.class);
    private static final ProductDetailTypeRepository REPO_DETAIL = (ProductDetailTypeRepository) RepositoryManager.getByEntity(ProductDetailType.class);
    //---------------------------------------------------------------------------
    private static final Map<String, ProductBrandType> MAP_BRAND = new HashMap<>();
    private static final Map<String, ProductCategoryType> MAP_CATEGORY = new HashMap<>();
    private static final Map<String, ProductDetailType> MAP_DETAIL = new HashMap<>();
    private static final Map<String, String> MAP_CAT = new HashMap<>();
    private static final Map<String, ProductCategoryDetailType> MAP_CAT_DETAIL = new HashMap<>();
    //---------------------------------------------------------------------------

    private static void insertProducts() {
        final List<String> paths = FileRW.getDirectoryContents(FOLDER_READ);
        int counter = 0, failed = 0;
        String read;
        PageInfo info;
        Product product;
        ProductCategoryType catType;
        ProductBrandType brandType;
        for (String path: paths) {
            read = FileRW.read(path, null, false);
            info = (PageInfo) Utility.fromJson(read, PageInfo.class);
            if(info == null || info.code == null) {
                LOGGER.warn(String.format("Counter: %d, info is null. path: %s", counter++, path));
                continue;
            }
            product = new Product(info.code, info.title, info.getPrice());
            catType = getCategory(info.cats);
            if(catType != null) {
                product.setProductCategoryType(catType);
            }
            brandType = getBrand(info.brand);
            if(brandType != null) {
                product.setProductBrandType(brandType);
            }
            product.setProductDetails(getDetails(info.specs, product, catType));
            product.setImage(saveImages(product, catType));
            ++counter;
            try {
                REPO_PRODUCT.persist(product);
            } catch (JoorJensException e) {
                ++failed;
                LOGGER.info(String.format("Exception: counter(%d): %s. Message: %s", counter, path, e.getMessage()));
            }
            if(counter % 50 == 0) {
                System.out.print('.');
            }
        }
        LOGGER.info(String.format("Counter: %d, failed: %d", counter, failed));

    }

    static String saveImages(final Product product, final ProductCategoryType cat) {
        final String from = FOLDER_READ_IMAGE + product.getBarcode() + "/1.jpg";
        if(!FileRW.isExist(from, false, true)) {
            return null;
        }
        final long catId = cat.isFistLevel() ? cat.getId() : cat.getParentId();
        final String image = catId + "/" + product.getBarcode() + ".jpg";
        final String to = FOLDER_WRITE_IMAGE + image;
        FileRW.renameFile(from, to, false);
        return image;
    }

    //---------------------------------------------------------------------------

    private static ProductCategoryType getCategory(final List<String> cats) {
        //["مواد غذایی","ادویه و چاشنی و مخلفات غذا","سس"]
        ProductCategoryType catType = null;
        if(cats != null) {
            String find;
            for (int i = cats.size() - 1; i >= 0; i--) {
                find = MAP_CAT.get(removePunct(cats.get(i)));
                if (find != null) {
                    catType = MAP_CATEGORY.get(find);
                    if (catType != null) {
                        break;
                    }
                }
            } //for
        }
        if(catType == null) {
            catType = MAP_CATEGORY.get(NOT_HAVE);
        }
        return catType;
    }

    private static ProductBrandType getBrand(String brand) {
        ProductBrandType brandType = null;
        if(brand != null) {
            brandType = MAP_BRAND.get(brand.trim());
        }
        if(brandType == null) {
            brandType = MAP_BRAND.get(NOT_HAVE);
        }
        return brandType;
    }

    private static Set<ProductDetail> getDetails(final Map<String, String> specs
            , final Product product, final ProductCategoryType categoryType) {
        final Set<ProductDetail> productDetails = new HashSet<>();
        if (specs != null) {
            String key;
            ProductDetailType detailType;
            ProductCategoryDetailType categoryDetailType;
            for (Map.Entry<String, String> entry : specs.entrySet()) {
                detailType = MAP_DETAIL.get(entry.getKey().trim());
                if (detailType != null) {
                    productDetails.add(new ProductDetail(entry.getValue().trim(), detailType, product));

                    key = getKey(categoryType, detailType);
                    categoryDetailType = MAP_CAT_DETAIL.get(key);
                    if (categoryDetailType == null) {
                        categoryDetailType = new ProductCategoryDetailType(categoryType, detailType);
                        MAP_CAT_DETAIL.put(key, categoryDetailType);
                    }
                    categoryType.addProductDetailType(categoryDetailType);
                }
            } // for
        }
        return productDetails;
    }

    private static void updateCategories() throws JoorJensException {
        //int rowAffected = REPO_CAT.updateBatch(new ArrayList<>(MAP_CAT_DETAIL.values()));
        int rowAffected = REPO_CAT.updateBatch(new ArrayList<>(MAP_CATEGORY.values()));
        LOGGER.info(String.format("updateCategories: %d", rowAffected));
    }
    //---------------------------------------------------------------------------

    private static void load() throws JoorJensException {
        final List<String> lines = FileRW.readLine(FILE_MAP_CAT, null, false);
        for (String line : lines) {
            final String[] split = line.split(SPLITTER);
            MAP_CAT.put(removePunct(split[0]), split[1].trim());
        }
        long catId;
        final Set<String> cats = new HashSet<>(MAP_CAT.values());
        final List<ProductCategoryType> catList = REPO_CAT.getAll(0);
        for (ProductCategoryType cat: catList) {
            cat.setName(cat.getName().trim());
            MAP_CATEGORY.put(cat.getName(), cat);
            cats.remove(cat.getName());

            catId = cat.isFistLevel() ? cat.getId() : cat.getParentId();
            FileRW.mkdir(FOLDER_WRITE_IMAGE + catId, false, false);
        }
        REPO_CAT.updateBatch(catList);
        LOGGER.info(String.format("CategoriesDb(%d=%d), CategoriesMap(%d)", catList.size(), MAP_CATEGORY.size(), MAP_CAT.size()));

        final List<ProductBrandType> brandList = REPO_BRAND.getAll(0);
        for (ProductBrandType brand: brandList) {
            brand.setName(brand.getName().trim());
            MAP_BRAND.put(brand.getName(), brand);
        }
        REPO_BRAND.updateBatch(brandList);
        LOGGER.info(String.format("BrandDb(%d=%d)", brandList.size(), MAP_BRAND.size()));

        final List<ProductDetailType> detailList = REPO_DETAIL.getAll(0);
        for (ProductDetailType detail: detailList) {
            detail.setName(detail.getName().trim());
            MAP_DETAIL.put(detail.getName(), detail);
        }
        REPO_DETAIL.updateBatch(detailList);
        LOGGER.info(String.format("DetailDb(%d=%d)", detailList.size(), MAP_DETAIL.size()));
    }

    //---------------------------------------------------------------------------

    private static String removePunct(final String text) {
        return text.replaceAll(PUNCH, "").replaceAll("\\s+", "").trim();
    }

    private static String getKey(final ProductCategoryType cat, final ProductDetailType detail) {
        return cat.getId() + "#" + detail.getId();
    }

    //---------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws JoorJensException {
        load();
        insertProducts();
        updateCategories();
    }
    //---------------------------------------------------------------------------
}