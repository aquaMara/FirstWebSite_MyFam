package org.aquam.springsecurity2.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "defaultuser")
public class DefaultUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotEmpty(message = "Username should not be empty")
    @Column(unique=true)
    private String username;
    @NotEmpty(message = "Password should not be empty")
    @Length(min=5, message = "Password should be at least 5 characters")
    private String password;
    private AppUserRole appUserRole;
    private boolean locked = false;
    private boolean enabled = true;

    @ManyToMany // (fetch = FetchType.EAGER)
    @JoinTable(
            name="built_homes",
            joinColumns = @JoinColumn(name="id"),
            inverseJoinColumns = @JoinColumn(name="homeId")
    )
    private List<Home> homesForDefaultUser = new ArrayList<>();

    public void addHomesForDefaultUser(Home home) {
        homesForDefaultUser.add(home);
    }

    public DefaultUser(String name, String username, String password, AppUserRole appUserRole, boolean locked, boolean enabled) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = true;
    }

    public DefaultUser() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singleton(authority); //Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AppUserRole getAppUserRole() {
        return appUserRole;
    }

    public void setAppUserRole(AppUserRole appUserRole) {
        this.appUserRole = appUserRole;
    }


    public List<Home> getHomesForDefaultUser() {
        return homesForDefaultUser;
    }

    public void setHomesForDefaultUser(List<Home> homesForPerson) {
        this.homesForDefaultUser = homesForDefaultUser;
    }

    @Override
    public String toString() {
        return "DefaultUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", appUserRole=" + appUserRole +
                ", locked=" + locked +
                ", enabled=";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUser that = (DefaultUser) o;
        return locked == that.locked && enabled == that.enabled && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && appUserRole == that.appUserRole && Objects.equals(homesForDefaultUser, that.homesForDefaultUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, password, appUserRole, locked, enabled, homesForDefaultUser);
    }
}
