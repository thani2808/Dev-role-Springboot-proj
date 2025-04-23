package com.vaibavshri.privilege.service;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.vaibavshri.privilege.entity.Privilege;
import com.vaibavshri.privilege.repository.PrivilegeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PrivilegeService {

	@Autowired
	private PrivilegeRepository repository;
	
	public List<Privilege> getAllPrivilege(){
		List<Privilege> privilegeList = repository.findAll();
		return privilegeList;
	}
	
	public Privilege getPrivilegeByPrivilegeId(int privilegeId) {
		return repository.findByPrivilegeId(privilegeId);
	}
	
	public void save(Privilege privilege) {
		repository.save(privilege);
	}
	
	public Privilege findByPrivilegeIdAndDelete(int privilegeId) {
		List<Privilege> privilegeList = repository.findAll();
		Privilege privilege = privilegeList.stream()
				.filter(it -> (it.getPrivilegeId() == privilegeId))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		privilegeList.remove(privilege);
		repository.deleteById(privilegeId);
		return privilege;
	}

	public Privilege getPrivilegeEntityByID(int privilegeId) {
		return repository.findById(privilegeId)
				.orElseThrow(() -> new RuntimeException("Privilege not found for id : " + privilegeId));
	}
	
	
	public Privilege edit(Privilege privilege) {
	    return repository.findById(privilege.getPrivilegeId())
	            .map(existingPrivilege -> {
	                existingPrivilege.setPrivilegeName(privilege.getPrivilegeName());
	                existingPrivilege.setStatus(privilege.getStatus());
	                existingPrivilege.setRoleId(privilege.getRoleId());
	                return repository.save(existingPrivilege);
	            })
	            .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + privilege.getPrivilegeId() + " not found."));
	}
	
	public void delete(int privilegeId) {
		if (repository.existsById(privilegeId)) {
	        repository.deleteById(privilegeId);
	    } else {
	        throw new EntityNotFoundException("Privilege not found for id: " + privilegeId);
	    }
	}
	
	// Method to check for duplicate privilegename
    public boolean isPrivilegenameDuplicate(String privilegeName) {
        // Return true if a privilege with the same privilegename exists in the database
    	return repository.findByPrivilegeName(privilegeName).isPresent();
    }

    @Cacheable("getroleId")
	public List<Privilege> getPrivilegesByRoleId(int roleId) {
		// TODO Auto-generated method stub
		return repository.findByRoleId(roleId);
	}
    
    
    
//    public List<Privilege> getPrivilegesByRoleId(int roleId) {
//    	return repository.findByRoleId(roleId);
//    }
}