package ir.joorjens.model.businessEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryHierarchical implements Serializable {

    public final long id;
    public final String name;
    public final List<CategoryHierarchical> child = new ArrayList<>();

    public CategoryHierarchical() {
        this(0, null);
    }

    public CategoryHierarchical(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
