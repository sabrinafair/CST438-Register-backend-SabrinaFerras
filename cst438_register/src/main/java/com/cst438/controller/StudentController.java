package com.cst438.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	GradebookService gradebookService;	

	
	@PostMapping("/student/add")
	public StudentDTO createNewStudent(@RequestBody StudentDTO studentDTO) {
		
		
		Student student = studentRepository.findByEmail(studentDTO.email);
		if (student != null) {	//if there is already this student inn the db throw error
			System.out.println("/student student email already taken. "+ studentDTO.email);
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student email already taken." );

		} else {
			//setting new student info
			Student st = new Student();
			st.setEmail(studentDTO.email);
			st.setName(studentDTO.name);
			st.setStatusCode(0);
			st.setStatus("No Holds");
			Student savedStudent = studentRepository.save(st);	//saving student
			StudentDTO result = createStudentDTO(savedStudent);	//making student dto
			return result;
		}
	}
	

	
	private StudentDTO createStudentDTO(Student s) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.student_id = s.getStudent_id();
		studentDTO.name = s.getName();
		studentDTO.email = s.getEmail();
		studentDTO.statusCode = s.getStatusCode();
		studentDTO.status = s.getStatus();

		return studentDTO;
	}
	
	@PutMapping("/placeHold/{student_id}")
	private StudentDTO placeHold(@PathVariable int student_id) {
		Student student = studentRepository.findById(student_id);
		if (student != null) {	//to make sure student exists
			//placing hold
			student.setStatus("HOLD");	
			student.setStatusCode(1);
			Student savedStudent = studentRepository.save(student); //saving updated student
			StudentDTO result = createStudentDTO(student);	
			return result;
			
		} else {	//for if student is not in db
			System.out.println("/student student with id "+ student_id + "is not in database");
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student is not in database." );
		}
	}
	
	//logic just opposite of placeHold
	@PutMapping("/removeHold/{student_id}")
	private StudentDTO removeHold(@PathVariable int student_id) {
		Student student = studentRepository.findById(student_id);
		if (student != null) {
			student.setStatus("No Holds");
			student.setStatusCode(0);
			Student savedStudent = studentRepository.save(student);
			StudentDTO result = createStudentDTO(student);
			return result;
			
		} else {
			System.out.println("/student student with id "+ student_id + "is not in database");
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student is not in database." );
		}
	}
	
}
