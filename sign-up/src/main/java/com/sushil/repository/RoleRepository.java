package com.sushil.repository;

import com.sushil.model.ERole;
import com.sushil.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(ERole name);
}
