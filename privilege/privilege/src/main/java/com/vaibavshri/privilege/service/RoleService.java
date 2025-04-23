package com.vaibavshri.privilege.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaibavshri.privilege.entity.Role;
import com.vaibavshri.privilege.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	public List<Role> getAllRoles(){
	    return roleRepository.findAll();
	}
}