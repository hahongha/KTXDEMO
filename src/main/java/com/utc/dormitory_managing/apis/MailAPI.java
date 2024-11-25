package com.utc.dormitory_managing.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utc.dormitory_managing.dto.MailRequestDTO;
import com.utc.dormitory_managing.service.MailService;

@RestController
@RequestMapping("/email")
public class MailAPI {

	@Autowired
	private MailService mailService;

	@PostMapping("")
	public ResponseEntity<String> sendNotification(@RequestBody MailRequestDTO mailRequestDTO) {

		try {
				mailService.sendEmail(mailRequestDTO.getStudentDTO(), mailRequestDTO.getSubject(), mailRequestDTO.getStaffDTO());
			return ResponseEntity.status(HttpStatus.OK).body("Email sent successfully");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email");
		}

	}

}

