package com.vaibavshri.privilege.controller;

import java.util.List;

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

import com.vaibavshri.privilege.entity.Privilege;
import com.vaibavshri.privilege.entity.Role;
import com.vaibavshri.privilege.repository.PrivilegeRepository;
import com.vaibavshri.privilege.service.PrivilegeService;
import com.vaibavshri.privilege.service.RoleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PrivilegeController {
	
	@Autowired
	private PrivilegeService service;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PrivilegeRepository repository;
	
	// Display all privileges
	 	@GetMapping
	 	public String findAllPrivileges(Model model, HttpServletRequest request) {
	 	    List<Privilege> privileges = service.getAllPrivilege();
	 	    model.addAttribute("privilegeList", privileges);
	 	    return "privilege_page";
	 	}
	    
	    // Display the form to create a new privilege
	    /*@GetMapping("/create")
	    public String showCreatePrivilegeForm(Model model) {
	    	List<Role> privilegeRoles = roleService.getAllRoles();
	        model.addAttribute("newPrivilege", new Privilege());
	        model.addAttribute("privilegeRoles", privilegeRoles);
	        return "create_privilege_page";
	    }*/
	 	
	 	@GetMapping("/create")
	 	public String showCreatePrivilegeForm(Model model) {
	 	    List<Role> privilegeRoles = roleService.getAllRoles();
	 	    
	 	    // Debugging Output
	 	    System.out.println("Roles fetched: " + (privilegeRoles != null ? privilegeRoles.size() : "null"));
	 	    
	 	    model.addAttribute("newPrivilege", new Privilege());
	 	    model.addAttribute("privilegeRoles", privilegeRoles != null ? privilegeRoles : List.of()); // Ensure a non-null list
	 	    
	 	    return "create_privilege_page"; // This should exactly match the template file name
	 	}


	    // Handle form submission for adding a new privilege
	    @PostMapping("/addprivilege")
	    public String addPrivilege(@Valid @ModelAttribute Privilege newPrivilege, BindingResult result, Model model) {
	        // Check for duplicate privilege name
	        if (service.isPrivilegenameDuplicate(newPrivilege.getPrivilegeName())) {
	            result.rejectValue("privilegeName", "error.privilegeName", "Privilegename already exists. Please choose another.");
	        }

	        // Validate privilege name using regex
	        if (!newPrivilege.getPrivilegeName().matches("[a-zA-Z0-9]*")) {
	        	System.out.println("Privilegename doesn't match");
	            result.rejectValue("privilegeName", "error.privilegeName", "Privilegename must contain only letters and numbers.");
	        }

	     // Check for errors
		    if (result.hasErrors()) {
		        // Log error count and messages
		        System.out.println("Error count: " + result.getErrorCount());
		        result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
		        
		        model.addAttribute("newPrivilege", newPrivilege); // Retain input data
		        model.addAttribute("error_object", result.getAllErrors());
		        return "create_privilege_page"; // Return to the form with error messages
		    }

	        // Save privilege if valid
	        service.save(newPrivilege);
	        return "redirect:/";
	    }

	    // Fetch privilege details
	    @GetMapping("/privilege/{privilegeId}")
	    public String getPrivilegeDetails(@PathVariable Integer privilegeId, Model model) {
	    	Privilege privilege = service.getPrivilegeEntityByID(privilegeId);
	        model.addAttribute("privilege", privilege);
	        return "privilege_details";
	    }

	    // Show the edit form for an existing privilege
	    @GetMapping("edit/{privilegeId}")
	    public String getEditPrivilegePage(Model model, @PathVariable int privilegeId) {
	    	Privilege byprivilegeId = service.getPrivilegeEntityByID(privilegeId);
			System.out.println(byprivilegeId.getPrivilegeId() + byprivilegeId.getPrivilegeName());
			List<Role> roles = roleService.getAllRoles();
			model.addAttribute("privilegeToEdit", byprivilegeId);
			model.addAttribute("privilegeRoles", roles);
			return "edit_privilege_page";
		}

	    // Handle privilege update
	    @PostMapping("/update")
	    public String updatePrivilege(@ModelAttribute Privilege privilegeToEdit, BindingResult result, Model model) {
	    	System.out.println(privilegeToEdit.getPrivilegeId() + privilegeToEdit.getPrivilegeName() + privilegeToEdit.getStatus() + privilegeToEdit.getRoleId());
	    	// Check for errors
	    	if (result.hasErrors()) {
		        // Log error count and messages
		        System.out.println("Error count: " + result.getErrorCount());
		        result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
		        
		        model.addAttribute("privilegeToEdit", privilegeToEdit); // Retain input data
		        return "edit_privilege_page"; // Return to the form with error messages
		    }

	        // Proceed with privilege update
	        service.edit(privilegeToEdit);
	        return "redirect:/";
	    }

	    // Handle privilege deletion
	    @GetMapping("/delete/{privilegeId}")
	    public String deletePrivilege(@PathVariable int privilegeId) {
	        service.delete(privilegeId);
	        return "redirect:/";
	    }
	    
	    public void delete(int privilegeId) {
		    repository.findById(privilegeId)
		              .orElseThrow(() -> new EntityNotFoundException("Privilege not found for id: " + privilegeId));
		    repository.deleteById(privilegeId);
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