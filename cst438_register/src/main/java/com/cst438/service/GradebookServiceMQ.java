package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		 
		// TODO 
		// create EnrollmentDTO and send to gradebookQueue
		EnrollmentDTO enrollDTO = new EnrollmentDTO();
		enrollDTO.studentName = student_name;
		enrollDTO.studentEmail = student_email;
		enrollDTO.course_id = course_id;
		rabbitTemplate.convertAndSend(gradebookQueue.getName(), enrollDTO);
		
		System.out.println("Message send to gradbook service for student "+ student_email +" " + course_id);  
		
	}
	
	@RabbitListener(queues = "registration-queue")
	public void receive(CourseDTOG courseDTOG) {
		System.out.println("Receive enrollment :" + courseDTOG);

		//TODO 
		// for each student grade in courseDTOG,  find the student enrollment entity, update the grade and save back to enrollmentRepository.
		for(CourseDTOG.GradeDTO gdto: courseDTOG.grades) {
			Enrollment enroll = enrollmentRepository.findByEmailAndCourseId(gdto.student_email, courseDTOG.course_id);
			enroll.setCourseGrade(gdto.grade);
			enrollmentRepository.save(enroll);
		}
	}

}
