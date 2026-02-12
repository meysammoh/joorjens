package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "valid_ip", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ip", "id_role"}, name = "UK_VALID_IP__ip_role")})
public class ValidIP extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "ip";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String ip;
    private String note;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", foreignKey = @ForeignKey(name = "FK_VALID_IP__ROLE__role"))
    @JsonIgnore
    private Role role;
    //------------------------------------------------

    public ValidIP() {
    }

    public ValidIP(long id) {
        this.id = id;
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        if(!Utility.validIP(ip)) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, "ip");
        }
        boolean OK = role != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }
}