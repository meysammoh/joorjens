package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meysammoh on 05.05.18.
 */

public class CartStatusUpdateResponse {
    @SerializedName("cartDistributorPackduct")
    @Expose
    private Boolean cartDistributorPackduct;
    @SerializedName("doneCount")
    @Expose
    private Integer doneCount;
    @SerializedName("cartDistributor")
    @Expose
    private Boolean cartDistributor;
    @SerializedName("cart")
    @Expose
    private Boolean cart;

    public Boolean getCartDistributorPackduct() {
        return cartDistributorPackduct;
    }

    public void setCartDistributorPackduct(Boolean cartDistributorPackduct) {
        this.cartDistributorPackduct = cartDistributorPackduct;
    }

    public Integer getDoneCount() {
        return doneCount;
    }

    public void setDoneCount(Integer doneCount) {
        this.doneCount = doneCount;
    }

    public Boolean getCartDistributor() {
        return cartDistributor;
    }

    public void setCartDistributor(Boolean cartDistributor) {
        this.cartDistributor = cartDistributor;
    }

    public Boolean getCart() {
        return cart;
    }

    public void setCart(Boolean cart) {
        this.cart = cart;
    }
}
