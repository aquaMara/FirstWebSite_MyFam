package org.aquam.springsecurity2.service;

import org.aquam.springsecurity2.models.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface DefaultHomeRepository extends JpaRepository<Home, Long> {

    Optional<Home> findByHomename(String homename);
    Optional<Home> findHomeByHomeId(Long homeId);

}
