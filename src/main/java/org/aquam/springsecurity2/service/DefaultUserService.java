package org.aquam.springsecurity2.service;

import org.aquam.springsecurity2.models.AppUserRole;
import org.aquam.springsecurity2.models.DefaultUser;
import org.aquam.springsecurity2.models.Home;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DefaultUserService implements UserDetailsService {

    private static final String DEFAULT_USER_NOT_FOUND = "Person with username %s not found";
    private final DefaultUserRepository defaultUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final DefaultHomeRepository defaultHomeRepository;

    @Autowired
    public DefaultUserService(DefaultUserRepository defaultUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, DefaultHomeRepository defaultHomeRepository) {
        this.defaultUserRepository = defaultUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.defaultHomeRepository = defaultHomeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return defaultUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(DEFAULT_USER_NOT_FOUND, username)));

    }

    public List<DefaultUser> getAllDefaultUsers() {
        List<DefaultUser> allUsers = defaultUserRepository.findAll();
        return  allUsers;
    }

    public void saveUser(DefaultUser defaultUser) {

        String encodedPassword = bCryptPasswordEncoder.encode(defaultUser.getPassword());
        defaultUser.setPassword(encodedPassword);
        defaultUser.setAppUserRole(AppUserRole.PERSON);

        defaultUserRepository.save(defaultUser);

        Home defaultHome = new Home("default_" + defaultUser.getUsername());
        defaultUser.addHomesForDefaultUser(defaultHome);
        defaultHome.addDefaultUsersForHome(defaultUser);

        defaultHomeRepository.save(defaultHome);
        // save user ?

    }

    public boolean defaultUserExists(DefaultUser defaultUser) {
        boolean defaultUserExists = defaultUserRepository.findByUsername(defaultUser.getUsername()).isPresent();
        return  defaultUserExists;
    }

    // addHomeForUser
    public void addHomeForUser(DefaultUser defaultUser, Home home) {

        Home loadedHome = null;
        String homenameTemp = defaultUser.getHomesForDefaultUser().get(0).getHomename();
        int homeArrSize = defaultUser.getHomesForDefaultUser().size();

        if(homenameTemp.equals("default_" + defaultUser.getUsername())) {
            loadedHome = defaultUser.getHomesForDefaultUser().get(0);
            String newHomename = home.getHomename();
            loadedHome.setHomename(newHomename);

            defaultHomeRepository.save(loadedHome); // save changes
            defaultUserRepository.save(defaultUser);

        } else {

            defaultUser.addHomesForDefaultUser(home);
            home.addDefaultUsersForHome(defaultUser);

            defaultHomeRepository.save(home);   // save home
            defaultUserRepository.save(defaultUser);

        }

    }


}

