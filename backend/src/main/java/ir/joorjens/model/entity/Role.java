package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "UK_ROLE__name")})
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Role extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "نقش";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    private String note;
    //------------------------------------------------
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "role__permission",
            joinColumns = @JoinColumn(name = "id_role", foreignKey = @ForeignKey(name = "FK_ROLE_PERMISSION__ROLE__role")),
            inverseJoinColumns = @JoinColumn(name = "id_permission", foreignKey = @ForeignKey(name = "FK_ROLE_PERMISSION__ROLE__permission")))
    //@JsonIgnore no need to Ignore because Permission is OK!
    private Set<Permission> permissions = new HashSet<>();
    //------------------------------------------------
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ValidIP> validIPs = new HashSet<>();
    //------------------------------------------------

    public Role() {
    }

    public Role(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<ValidIP> getValidIPs() {
        return validIPs;
    }

    public void setValidIPs(Set<ValidIP> validIPs) {
        this.validIPs = validIPs;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.id > 0 && !Utility.isEmpty(this.name) && permissions.size() > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(Role role) {
        super.setEdit(role);
        setValidIPs(role.validIPs);
    }

    //--------------------------------------------------------------------------

    @JsonIgnore
    public TypeEnumeration getRoleType() {
        return TypeEnumeration.get((int) this.id);
    }

    @JsonIgnore
    public Set<String> getPermissionUrls() {
        return permissions.stream()
                .map(Permission::getUrl)
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    public boolean hasIp(final String ip) {
        boolean OK = Utility.isEmpty(ip) || this.validIPs == null || this.validIPs.size() == 0;
        if (!OK) {
            for (ValidIP validIP : validIPs) {
                if (ip.equals(validIP.getIp())) {
                    OK = true;
                    break;
                }
            }
        }
        return OK;
    }

    //--------------------------------------------------------------------------

}