package com.vaibavshri.role.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaibavshri.role.entity.Role;
import com.vaibavshri.role.repository.RoleRepository;
import com.vaibavshri.role.service.RoleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private RoleService service;
    
    @Autowired
    private RoleRepository repository;

 // Display all roles
 	@GetMapping
 	public String findAllRoles(Model model, HttpServletRequest request) {
 	    List<Role> roles = service.getAllRoles();
 	    model.addAttribute("roleList", roles);
 	    return "role_page";
 	}
    
    // Display the form to create a new role   
 	@GetMapping("/create")
 	public String showCreateRoleForm(Model model) {
 	    model.addAttribute("newRole", new Role()); 
 	    return "create_role_page";
 	}


    // Handle form submission for adding a new role
    @PostMapping("/addrole")
    public String addRole(@Valid @ModelAttribute Role newRole, BindingResult result, Model model) {
        // Check for duplicate role name
        if (service.isRolenameDuplicate(newRole.getRoleName())) {
            result.rejectValue("roleName", "error.roleName", "Rolename already exists. Please choose another.");
        }

        // Validate role name using regex
    if (!newRole.getRoleName().matches("[a-zA-Z0-9]*")) {
        	System.out.println("Rolename doesn't match");
            result.rejectValue("roleName", "error.roleName", "Rolename must contain only letters and numbers.");
        }

     // Check for errors
    if (result.hasErrors()) {
	        // Log error count and messages
	        System.out.println("Error count: " + result.getErrorCount());
	        result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
	        
	        model.addAttribute("newRole", newRole); // Retain input data
	        model.addAttribute("error_object", result.getAllErrors());
	        return "create_role_page"; // Return to the form with error messages
	    }

        // Save role if valid
    service.save(newRole);
        return "redirect:/";
    }

    // Fetch role details
    @GetMapping("/role/{roleId}")
    public String getRoleDetails(@PathVariable Integer roleId, Model model) {
        Role role = service.getRoleEntityByID(roleId);
        model.addAttribute("role", role);
        return "role_details";
    }

    // Show the edit form for an existing role
    @GetMapping("edit/{roleId}")
    public String getEditRolePage(Model model, @PathVariable int roleId) {
    	Role byroleId = service.getRoleEntityByID(roleId);
		System.out.println(byroleId.getRoleId() + byroleId.getRoleName());
		model.addAttribute("roleToEdit", byroleId);
		return "edit_role_page";
	}

    // Handle role update
    @PostMapping("/update")
    public String updateRole(@ModelAttribute Role roleToEdit, BindingResult result, Model model) {
        // Check for errors
    	if (result.hasErrors()) {
	        // Log error count and messages
	        System.out.println("Error count: " + result.getErrorCount());
	        result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
	        
	        model.addAttribute("roleToEdit", roleToEdit); // Retain input data
	        return "edit_role_page"; // Return to the form with error messages
	    }

        // Proceed with role update
    service.edit(roleToEdit);
        return "redirect:/";
    }

    // Handle role deletion
    @GetMapping("/delete/{roleId}")
    public String deleteRole(@PathVariable int roleId) {
        service.delete(roleId);
        return "redirect:/";
    }
    
    public void delete(int roleId) {
	    repository.findById(roleId)
	              .orElseThrow(() -> new EntityNotFoundException("Role not found for id: " + roleId));
	    repository.deleteById(roleId);
	}

    // Global exception handler
    @ControllerAdvice
    public static class GlobalExceptionHandler {
        @ExceptionHandler(EntityNotFoundException.class)
        public String handleEntityNotFound(EntityNotFoundException e, Model model) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error_page";
        }
    }
}