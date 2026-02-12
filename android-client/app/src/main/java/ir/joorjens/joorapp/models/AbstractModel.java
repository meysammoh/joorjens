package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meysammoh on 15.11.17.
 */

public abstract class AbstractModel {

    public  long getId(){
        return id;
    }

    public  void setId(long id){
        this.id=id;
    }

    @SerializedName("block")
    @Expose
    private boolean block = false;
    @SerializedName("id")
    @Expose
    private long id;
    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

}
