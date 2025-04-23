package com.vaibavshri.privilege.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaibavshri.privilege.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

}
