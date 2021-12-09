package org.aquam.springsecurity2.models;

import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long homeId;
    //@NotEmpty(message = "Homename should not be empty")
    @Column(unique = true)
    private String homename;

    public List<DefaultUser> getDefaultUsersForHome() {
        return defaultUsersForHome;
    }

    public void setDefaultUsersForHome(List<DefaultUser> defaultUsersForHome) {
        this.defaultUsersForHome = defaultUsersForHome;
    }

    @ManyToMany(mappedBy = "homesForDefaultUser")
    private List<DefaultUser> defaultUsersForHome = new ArrayList<>();

    public void addDefaultUsersForHome(DefaultUser defaultUser) {
        defaultUsersForHome.add(defaultUser);
    }



    // one home has many rooms
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // little letter!!!
    @JoinColumn(name = "homeId")
    private List<Room> roomsForHome = new ArrayList<>();

    public void addRoomsForHome(Room room) {
        roomsForHome.add(room);
    }

    public Home() {}

    public Home(String homename) {
        this.homename = homename;
    }

    public Home(DefaultUser defaultUser, String homename) {
        this.homename = homename;
        defaultUser.setId(defaultUser.getId());
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public String getHomename() {
        return homename;
    }

    public void setHomename(String homename) {
        this.homename = homename;
    }

    public List<Room> getRoomsForHome() {
        return roomsForHome;
    }

    public void setRoomsForHome(List<Room> roomsForHome) {
        this.roomsForHome = roomsForHome;
    }

    @Override
    public String toString() {
        return "Home{" +
                "homeId=" + homeId +
                ", homename='" + homename;
    }
}

