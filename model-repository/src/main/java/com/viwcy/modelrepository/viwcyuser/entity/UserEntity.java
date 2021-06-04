package com.viwcy.modelrepository.viwcyuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viwcy.modelrepository.entity.AbstractBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description TODO
 * @Date 2020/9/1 16:29
 * @Author Fuqiang
 * <p>
 *
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserEntity extends AbstractBaseEntity<UserEntity> implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;
    private String headPhoto;
    private String phone;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField("password")
    private String password;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @TableField(exist = false)
    private List<RoleEntity> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<RoleEntity> roles = this.getRoles();
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
