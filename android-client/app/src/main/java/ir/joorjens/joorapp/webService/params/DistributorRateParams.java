package ir.joorjens.joorapp.webService.params;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistributorRateParams {

    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("cartId")
    @Expose
    private Integer cartId;
    @SerializedName("distributorDiscontentTypes")
    @Expose
    private List<DistributorDiscontentType> distributorDiscontentTypes = null;

    public DistributorRateParams(Double rate, String comment, Integer distributorId,
                                 Integer cartId, List<DistributorDiscontentType> distributorDiscontentTypes) {
        this.rate = rate;
        this.comment = comment;
        this.distributorId = distributorId;
        this.cartId = cartId;
        this.distributorDiscontentTypes = distributorDiscontentTypes;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public List<DistributorDiscontentType> getDistributorDiscontentTypes() {
        return distributorDiscontentTypes;
    }

    public void setDistributorDiscontentTypes(List<DistributorDiscontentType> distributorDiscontentTypes) {
        this.distributorDiscontentTypes = distributorDiscontentTypes;
    }

}