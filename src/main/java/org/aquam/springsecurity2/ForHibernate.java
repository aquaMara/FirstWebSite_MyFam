package org.aquam.springsecurity2;


import org.aquam.springsecurity2.models.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class ForHibernate {

    public static void main(String[] args) {
        //String name, String username, String password, AppUserRole appUserRole, boolean locked, boolean enabled

        DefaultUser marina = new DefaultUser("Marina", "dog", "nikita", AppUserRole.ADMIN, true, true);
        DefaultUser nikita = new DefaultUser("Nikita", "cat", "marina", AppUserRole.PERSON, true, true);
        DefaultUser mom = new DefaultUser("Irina", "mom", "dadmom", AppUserRole.PERSON, true, true);
        DefaultUser dad = new DefaultUser("Sergei", "dad", "momdad", AppUserRole.PERSON, true, true);

        Home famHome = new Home("fam");
        Home coupHome = new Home("coup");

        marina.addHomesForDefaultUser(famHome);
        mom.addHomesForDefaultUser(famHome);
        dad.addHomesForDefaultUser(famHome);
        marina.addHomesForDefaultUser(coupHome);
        nikita.addHomesForDefaultUser(coupHome);

        famHome.addDefaultUsersForHome(marina);
        famHome.addDefaultUsersForHome(mom);
        famHome.addDefaultUsersForHome(dad);
        coupHome.addDefaultUsersForHome(marina);
        coupHome.addDefaultUsersForHome(nikita);

        Room kitchen = new Room("Kitchen");
        Room bedroom = new Room("Bedroom");
        Room bathroom = new Room("Bathroom");

        Room balcony = new Room("balcony");

        famHome.addRoomsForHome(kitchen);
        famHome.addRoomsForHome(bedroom);
        famHome.addRoomsForHome(bathroom);
        coupHome.addRoomsForHome(balcony);

        Note kitchenNote1 = new Note("for kitchen", "buy milk");
        Note kitchenNote2 = new Note("for kitchen also", "buy meet");
        Note bathroomNote1 = new Note("for bathroom", "take a shower");

        kitchen.addNotesForRoom(kitchenNote1);
        kitchen.addNotesForRoom(kitchenNote2);
        bathroom.addNotesForRoom(bathroomNote1);

        //********************************************************************************************************************

        Configuration cfg=new Configuration().configure().addAnnotatedClass(Room.class).addAnnotatedClass(Home.class).addAnnotatedClass(DefaultUser.class).addAnnotatedClass(Note.class);
        StandardServiceRegistryBuilder builder= new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());
        SessionFactory sf= cfg.buildSessionFactory(builder.build());
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();

        session.save(marina);
        session.save(mom);
        session.save(dad);
        session.save(nikita);
        session.save(famHome);
        session.save(coupHome);

        session.save(kitchen);
        session.save(bedroom);
        session.save(bathroom);
        session.save(balcony);

        session.save(kitchenNote1);
        session.save(kitchenNote2);
        session.save(bathroomNote1);



        transaction.commit();
        session.close();


    }

}
