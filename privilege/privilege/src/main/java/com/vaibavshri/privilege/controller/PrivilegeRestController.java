/*package com.vaibavshri.privilege.controller;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaibavshri.privilege.entity.Privilege;
import com.vaibavshri.privilege.repository.PrivilegeRepository;
import com.vaibavshri.privilege.service.PrivilegeService;
import com.vaibavshri.privilege.service.RoleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/privilege")
@RequiredArgsConstructor
public class PrivilegeRestController {

	@Autowired
	private PrivilegeService service;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeRepository repository;
	
	// Display all privileges
	@GetMapping
	public ResponseEntity<List<Privilege>> findAllPrivileges() {
		List<Privilege> privileges = service.getAllPrivilege();
		return ResponseEntity.ok(privileges);
	}
	
	//Get privilege by ID
	@GetMapping("/{privilegeId}")
	public ResponseEntity<?> getPrivilegeDetails(@PathVariable Integer privilegeId) {
		try {
			Privilege privilege = service.getPrivilegeByPrivilegeId(privilegeId);
			return ResponseEntity.ok(privilege);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Privilege not found.");
		}
	}
	
	//Create a new privilege
	@PostMapping("/create")
	public ResponseEntity<?> addPrivilege(@Valid @RequestBody Privilege newPrivilege) {
	    // Validate privilege object
	    String validationError = validatePrivilege(newPrivilege);
	    if (validationError != null) {
	        return ResponseEntity.badRequest().body(validationError);
	    }

	    // Save privilege
	    service.save(newPrivilege);
	    return ResponseEntity.status(HttpStatus.CREATED).body(newPrivilege);
	}

	private String validatePrivilege(Privilege privilege) {
	    if (privilege == null) {
	        return "Privilege cannot be null.";
	    }

	    if (privilege.getPrivilegeName() == null || privilege.getPrivilegeName().trim().isEmpty()) {
	        return "Privilege name cannot be null or empty.";
	    }

	    if (!privilege.getPrivilegeName().matches("[a-zA-Z0-9 ]*")) {
	        return "Privilege name must contain only letters, numbers, or spaces.";
	    }

	    if (service.isPrivilegenameDuplicate(privilege.getPrivilegeName())) {
	        return "Privilege name already exists.";
	    }

	    return null;
	}

	// Update an existing privilege
	@PutMapping("/edit/{privilegeId}")
	public ResponseEntity<?> updatePrivilege(@PathVariable Integer privilegeId,
											 @RequestBody Privilege privilegeToEdit) {
		Privilege existingPrivilege = service.getPrivilegeEntityByID(privilegeId);
		
		if (existingPrivilege == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Privilege not found");
		}
		
		String validationError = validatePrivilege(privilegeToEdit);
		if (validationError != null) {
			return ResponseEntity.badRequest().body(validationError);
		}
		
		//existingPrivilege.setPrivilegeName(privilegeToEdit.getPrivilegeName());
		existingPrivilege.setRoleId(privilegeToEdit.getRoleId());
		Privilege updatedPrivilege = service.edit(existingPrivilege);
		
		return ResponseEntity.ok(updatedPrivilege);
		
	}
	
	// Delete a privilege
	@DeleteMapping("/delete/{privilegeId}")
	public ResponseEntity<?> deletePrivilege(@PathVariable Integer privilegeId) {
		service.delete(privilegeId);
		return ResponseEntity.noContent().build();
	}
	
	//Get users by privilege
	@GetMapping("/role/{roleId}")
	public ResponseEntity<List<Privilege>> getPrivilegesByRole(@PathVariable int roleId) {
	    List<Privilege> privileges = service.getPrivilegesByRoleId(roleId);

	    if (privileges == null || privileges.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    return ResponseEntity.ok(privileges);
	}
}*/