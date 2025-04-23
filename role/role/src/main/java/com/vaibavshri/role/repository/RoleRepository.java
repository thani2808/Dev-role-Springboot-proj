package com.vaibavshri.role.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaibavshri.role.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Optional<Role> findByRoleName(String roleName);
	
	Role findByRoleId(int roleId);

}
