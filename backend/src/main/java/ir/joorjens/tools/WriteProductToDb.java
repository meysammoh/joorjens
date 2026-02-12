package ir.joorjens.tools;

import ir.joorjens.common.file.FileRW;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.ProductCategoryTypeRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.model.entity.ProductCategoryType;

/**
 * Created by mehdi on 5/16/18.
 */
public class WriteProductToDb {


    private static final ProductCategoryTypeRepository REPO = (ProductCategoryTypeRepository) RepositoryManager.getByEntity(ProductCategoryType.class);

    public static void main(String[] args) throws JoorJensException {
        final long fakeId = ProductCategoryType.getFakeId();
        final ProductCategoryType parent = new ProductCategoryType(fakeId);
        final String content = FileRW.read("/home/mehdi/Desktop/cats.txt", null, false);
        final String[] split = content.split("\\s*###\\s*");
        for (String line : split) {
            final String[] catSub = line.split("\\s*##\\s*");
            int childCount = 0;
            final ProductCategoryType mainCat = new ProductCategoryType(catSub[0].trim(), parent);
            REPO.persist(mainCat);
            for (String sub : catSub[1].split("\\s*#\\s*")) {
                ++childCount;
                final ProductCategoryType subCat = new ProductCategoryType(sub.trim(), mainCat);
                REPO.persist(subCat);
            }
            mainCat.setChildCount(childCount);
            REPO.update(mainCat);
        }
    }
}