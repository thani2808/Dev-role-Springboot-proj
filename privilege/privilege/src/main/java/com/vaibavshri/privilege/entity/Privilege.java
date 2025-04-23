package com.vaibavshri.privilege.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "privilege")

public class Privilege {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_seq")
	@SequenceGenerator(name = "privilege_seq", sequenceName = "privilege_seq", allocationSize = 1)
	@Column(name = "privilege_id")
	private int privilegeId;
	@Column(name = "privilege_name", nullable = false, length = 15)
	@NotBlank(message = "Privilegename is required")
	private String privilegeName;
	@Column(name = "status", nullable = false)
	private int status = 1;
	@Column(name = "role_id")
	private int roleId;
	
	public Privilege() {
		
	}
	
	public Privilege(int privilegeId, String privilegeName, int status, int roleId) {
		this.privilegeId = privilegeId;
		this.privilegeName = privilegeName;
		this.status = status;
		this.roleId = roleId;
	}

	public int getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
