package com.cst438.domain;

import java.util.Objects;

public class StudentDTO {

	public int student_id;
	public String name;
	public String email;
	public int statusCode;
	public String status;
	
	// student.status
			// = 0  ok to register
			// != 0 hold on registration.  student.status may have reason for hold.
	
	public StudentDTO() {
		
		this.student_id = 0;
		this.name = null;
		this.email = null;
		this.statusCode = 0;
		this.status = null;
	}
	
	public StudentDTO(String name, String email, int statusCode, String status) {
		
		this.name = name;
		this.email = email;
		this.statusCode = statusCode;
		this.status = status;
	}
	


	@Override
	public String toString() {
		return "StudentDTO [student_id=" + student_id + ", name=" + name + ", email=" + email + ", statusCode="
				+ statusCode + ", status=" + status + "]";
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentDTO other = (StudentDTO) obj;
		return Objects.equals(email, other.email) && Objects.equals(name, other.name)
				&& Objects.equals(status, other.status) && statusCode == other.statusCode
				&& student_id == other.student_id;
	}
	
	


}
