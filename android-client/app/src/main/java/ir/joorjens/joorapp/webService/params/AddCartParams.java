package ir.joorjens.joorapp.webService.params;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meysammoh on 15.03.18.
 */

public class AddCartParams {
    private Integer type;
    private Integer count;
    private Boolean buyByCheck;
    private Integer distributorProductId;
    private Integer distributorPackageId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getBuyByCheck() {
        return buyByCheck;
    }

    public void setBuyByCheck(Boolean buyByCheck) {
        this.buyByCheck = buyByCheck;
    }

    public Integer getDistributorProductId() {
        return distributorProductId;
    }

    public void setDistributorProductId(Integer distributorProductId) {
        this.distributorProductId = distributorProductId;
    }

    public Integer getDistributorPackageId() {
        return distributorPackageId;
    }

    public void setDistributorPackageId(Integer distributorPackageId) {
        this.distributorPackageId = distributorPackageId;
    }

    public AddCartParams(Integer type, Integer count, Boolean buyByCheck,
                         Integer distributorProductId, Integer distributorPackageId) {
        this.type = type;
        this.count = count;
        this.buyByCheck = buyByCheck;
        this.distributorProductId = distributorProductId;
        this.distributorPackageId = distributorPackageId;
    }
}
