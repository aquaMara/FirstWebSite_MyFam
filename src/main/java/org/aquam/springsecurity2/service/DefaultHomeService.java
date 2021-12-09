package org.aquam.springsecurity2.service;

import org.aquam.springsecurity2.models.DefaultUser;
import org.aquam.springsecurity2.models.Home;
import org.aquam.springsecurity2.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultHomeService {

    private static final String HOME_NOT_FOUND = "Home with homename %s not found";
    private final DefaultHomeRepository defaultHomeRepository;
    private final DefaultUserRepository defaultUserRepository;
    private final DefaultRoomRepository defaultRoomRepository;
    private final DefaultUserService defaultUserService;

    @Autowired
    public DefaultHomeService(DefaultHomeRepository defaultHomeRepository, DefaultUserRepository defaultUserRepository, DefaultRoomRepository defaultRoomRepository, DefaultUserService defaultUserService) {
        this.defaultHomeRepository = defaultHomeRepository;
        this.defaultUserRepository = defaultUserRepository;
        this.defaultRoomRepository = defaultRoomRepository;
        this.defaultUserService = defaultUserService;
    }

    public Home loadHomeByHomename(String homename) throws UsernameNotFoundException {
        return defaultHomeRepository.findByHomename(homename)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(HOME_NOT_FOUND, homename)));
    }

    public List<Home> getAllHomes() {
        List<Home> allHomes = defaultHomeRepository.findAll();
        return  allHomes;
    }

    public boolean homeExists(Home home) {
        boolean homeExists = defaultHomeRepository.findByHomename(home.getHomename()).isPresent();
        return  homeExists;
    }
 
    public boolean userIsInHome(Home home, String username) {
        boolean userIsInHome = false;
        List<DefaultUser> allHomeUsers = home.getDefaultUsersForHome();
        for(DefaultUser containerValue: allHomeUsers) {
            if(containerValue.getUsername().equals(username))
                userIsInHome = true;
        }
        return userIsInHome;

        //return home.getDefaultUsersForHome().stream().filter(p -> p.getUsername().equals(username)).findAny().orElseThrow(() -> new UsernameNotFoundException("Room not found"));
    }

    public boolean roomIsInHome(Home home, String roomname) {
        //boolean roomIsInHome = false;
        List<Room> allHomeRooms = home.getRoomsForHome();
        for(Room containerValue: allHomeRooms) {
            if(containerValue.getRoomname().equals(roomname)) {
                //roomIsInHome = true;
                return true;
            }

        }
        return false;
    }

    public void addUserForHome(DefaultUser defaultUser, Home home) {

        UserDetails thatUser2 = defaultUserService.loadUserByUsername(defaultUser.getUsername());
        DefaultUser thatUser = (DefaultUser) thatUser2;
        Home thatHome = loadHomeByHomename(home.getHomename());

        thatHome.addDefaultUsersForHome(thatUser);
        thatUser.addHomesForDefaultUser(thatHome);
        defaultUserRepository.save(thatUser);
        defaultHomeRepository.save(thatHome);
    }

    public void addRoomForHome(Home home, Room thatRoom) {
        // save room and then ->
        //Room thatRoom = new Room(room.getRoomname()); // gjhjh

        defaultRoomRepository.save(thatRoom);

        Home thatHome = loadHomeByHomename(home.getHomename());
        thatHome.addRoomsForHome(thatRoom);

        defaultHomeRepository.save(thatHome); // update

    }

}
