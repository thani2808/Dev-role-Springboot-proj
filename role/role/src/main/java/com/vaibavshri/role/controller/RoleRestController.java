package com.vaibavshri.role.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.vaibavshri.role.entity.Role;
import com.vaibavshri.role.repository.RoleRepository;
import com.vaibavshri.role.service.RoleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@Controller
/*@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleRestController {

	@Autowired
	private RoleService service;
	
	@Autowired
	private RoleRepository repository;
	
	//Display all roles
	@GetMapping
	public ResponseEntity<List<Role>> findAllRoles() {
		List<Role> roles = service.getAllRoles();
		return ResponseEntity.ok(roles);
	}
	
	//Get user by ID
	@GetMapping("/{roleId}")
	public ResponseEntity<?> getRoleDetails(@PathVariable Integer roleId){
		try {
			Role role = service.getRoleEntityByID(roleId);
			return ResponseEntity.ok(role);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
		}
	}
	
	//Create a new role
	@PostMapping("/create")
	public ResponseEntity<?> addRole(@Valid @RequestBody Role newRole){
		if (newRole == null) {
			return ResponseEntity.badRequest().body("Request body is missing or invalid");
		}
		
		String validationError = validateRole(newRole);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }
		
		service.save(newRole);
		return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
	}

	//Update an existing role
	@PutMapping("/edit/{roleId}")
	public ResponseEntity<?> updateRole(@PathVariable Integer roleId,
										@RequestBody Role roleToRole) {
		Role existingRole = service.getRoleEntityByID(roleId);
		
		if (existingRole == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
		}
		
		existingRole.setRoleName(roleToRole.getRoleName());
		service.save(existingRole); // Save the updated role
        
        return ResponseEntity.ok(existingRole);
	}

	// Delete a role
	@DeleteMapping("/delete/{roleId}")
	public ResponseEntity<?> deleteRole(@PathVariable Integer roleId) {
		service.delete(roleId);
		return ResponseEntity.noContent().build();
	}
	
	// Helper method to validate role
	private String validateRole(Role role) {
	    if (role == null) {
	        return "Role cannot be null.";
	    }

	    if (role.getRoleId() == 0) { 
	        if (service.isRolenameDuplicate(role.getRoleName())) {
	            return "Rolename already exists. Please choose another.";
	        }
	    }

	    if (!role.getRoleName().matches("[a-zA-Z0-9]*")) {
	        return "Rolename must contain only letters and numbers.";
	    }

	    return null;
	}
}

	@RestControllerAdvice
	class GlobalExceptionHandler {
	
		@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
	        String message = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
	            ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());
	        return ResponseEntity.badRequest().body(message);
	    }
	}*/