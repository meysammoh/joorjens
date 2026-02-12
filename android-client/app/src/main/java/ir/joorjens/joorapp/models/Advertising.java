package ir.joorjens.joorapp.models;

/**
 * Created by mohsen on 5/18/2018.
 */

public class Advertising {
    public Advertising(String name, int imgId) {
        this.name = name;
        this.imageId = imgId;
    }

    public String getName() {
        return name;
    }
    public int getImageId(){
        return imageId;
    }

    public void setImageId(int id){
        imageId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int imageId;
}
