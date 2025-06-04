package com.vaibavshri.role.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaibavshri.role.entity.Role;
import com.vaibavshri.role.repository.RoleRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository repository;
	
	public List<Role> getAllRoles(){
		List<Role> roleList = repository.findAll();
		return roleList;
	}
	
	public Role getRoleByRoleId(int roleId) {
        return repository.findByRoleId(roleId);
    }
	
	public void save(Role role) {
		repository.save(role);
	}
	
	
	public Role findByRoleIdAndDelete(int roleId) {
		List<Role> roleList = repository.findAll();
		Role role = roleList.stream()
				.filter(it -> (it.getRoleId() == roleId))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		roleList.remove(role);
		repository.deleteById(roleId);
		return role;
	}

	public Role getRoleEntityByID(int roleId) {
		return repository.findById(roleId)
				.orElseThrow(() -> new RuntimeException("Role not found for id : " + roleId));
	}
	
	
	public Role edit(Role role) {
	    return repository.findById(role.getRoleId())
	            .map(existingRole -> {
	                existingRole.setRoleName(role.getRoleName());
	                return repository.save(existingRole);
	            })
	            .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + role.getRoleId() + " not found."));
	}
	
	public void delete(int roleId) {
		if (repository.existsById(roleId)) {
	        repository.deleteById(roleId);
	    } else {
	        throw new EntityNotFoundException("Role not found for id: " + roleId);
	    }
	}
	
	// Method to check for duplicate rolename
    public boolean isRolenameDuplicate(String roleName) {
        // Return true if a role with the same rolename exists in the database
    	return repository.findByRoleName(roleName).isPresent();
    }
}
