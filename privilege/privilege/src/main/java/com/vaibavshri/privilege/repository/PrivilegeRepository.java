package com.vaibavshri.privilege.repository;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaibavshri.privilege.entity.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{
	
	Optional<Privilege> findByPrivilegeName(String privilegeName);
	
	Privilege findByPrivilegeId(int privilegeId);
	List<Privilege> findByRoleId(int roldId);

}
