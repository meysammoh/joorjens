package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "advertising")
public class Advertising extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "تبلیغات";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String link, image;
    private String title, note;
    private int type = TypeEnumeration.ADVERTISING_TYPE.getId();
    private int weight;
    private boolean app = false;
    private int fromTime, toTime;
    //info about pricePerClick?
    //------------------------------------------------
    //@JsonIgnore
    private long clickCount;
    //------------------------------------------------
    @JsonIgnore
    private UUID uuid = Utility.getUUID();
    //------------------------------------------------

    public Advertising() {
    }

    public Advertising(long id) {
        this.id = id;
    }

    public void setEdit(final Advertising banner) {
        super.setEdit(banner);
        this.uuid = banner.uuid;
        this.clickCount = banner.clickCount;
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        final TypeEnumeration gt = TypeEnumeration.get(type);
        if (gt != null && TypeEnumeration.ADVERTISING_TYPE == gt.getParent()) {
            this.type = type;
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isApp() {
        return app;
    }

    public void setApp(boolean app) {
        this.app = app;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = !Utility.isEmpty(this.link) && !Utility.isEmpty(this.image);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getTypeName() {
        TypeEnumeration gt = TypeEnumeration.get(this.type);
        return (gt != null) ? gt.getFa() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

}