package ir.joorjens.model.interfaces;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import static ir.joorjens.common.Utility.getCurrentTime;

@MappedSuperclass
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class AbstractModel implements Serializable, Validate {
    protected static final long serialVersionUID = 1395L;
    public static final String UK_MSQ = "%s با این مشخصات قبلا در سیستم ثبت شده است!";
    public static final String UK_MSQ_2 = "%s %s با این مشخصات قبلا در سیستم ثبت شده است!";

    //no need to userId(who created this object), because we will log every thing :)
    public abstract long getId();

    public abstract void setId(long id);

    @Column(updatable = false)
    @JsonIgnore
    private int createdTime;
    @JsonIgnore
    private int updatedTime;
    @Column(columnDefinition = "boolean default false")
    private boolean block = false;

    public int getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(int createdTime) {
        this.createdTime = createdTime;
    }

    public void setCreatedTime() {
        createdTime = getCurrentTime();
    }

    public int getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(int updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void setUpdatedTime() {
        updatedTime = getCurrentTime();
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void setEdit(AbstractModel am) {
        setId(am.getId());
        setCreatedTime(am.getCreatedTime());
        setBlock(am.isBlock());
        if(getId() > 0) {
            setUpdatedTime();
        }
    }
}