package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;

@Entity
@Table(name = "permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"url"}, name = "UK_PERMISSION__url"),
        @UniqueConstraint(columnNames = {"key_"}, name = "UK_PERMISSION__key")})
public class Permission extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "دسترسی";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "key_", nullable = false)
    private String key;
    @Column(nullable = false)
    private String name;
    private String url;
    @Column(columnDefinition = "boolean default false")
    private boolean justLogin;
    @Column(columnDefinition = "boolean default false")
    private boolean noLogin;

    public Permission() {
    }

    public Permission(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isJustLogin() {
        return justLogin;
    }

    public void setJustLogin(boolean justLogin) {
        this.justLogin = justLogin;
    }

    public boolean isNoLogin() {
        return noLogin;
    }

    public void setNoLogin(boolean noLogin) {
        this.noLogin = noLogin;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = !Utility.isEmpty(this.key) && !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public boolean isMenu() {
        return url == null || url.isEmpty();
    }

    //-----------------------------------------------------------------------------------------------------------
}