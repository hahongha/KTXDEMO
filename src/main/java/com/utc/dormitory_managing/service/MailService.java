package com.utc.dormitory_managing.service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.dto.ContractDTO;
import com.utc.dormitory_managing.dto.MailDTO;
import com.utc.dormitory_managing.dto.StaffDTO;
import com.utc.dormitory_managing.dto.StudentDTO;
import com.utc.dormitory_managing.entity.Contract;
import com.utc.dormitory_managing.entity.Staff;
import com.utc.dormitory_managing.entity.Student;
import com.utc.dormitory_managing.repository.ContractRepo;
import com.utc.dormitory_managing.repository.StaffRepo;
import com.utc.dormitory_managing.repository.StudentRepo;
import com.utc.dormitory_managing.utils.Utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.NoResultException;


public interface MailService {
	void sendEmail(StudentDTO studentDTO, String subject, StaffDTO staffDTO);
	
	void sendMailByNewAccount(StudentDTO studentDTO);
	
	void sendMailByContract(Contract contractDTO);
	// New method for OTP
	void sendOtpEmail(String toEmail, String subject, String otpMessage);
}

@Service
class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private StudentRepo studentRepo;	

	@Autowired
	private StaffRepo staffRepo;
	
	@Autowired
	private ContractRepo contractRepo;
	
	@Async
	@Override
	public void sendEmail(StudentDTO studentDTO, String subject, StaffDTO staffDTO) {
		MailDTO mailDTO = new MailDTO();

		try {
			Student student = studentRepo.findById(studentDTO.getStudentId()).orElseThrow(NoResultException::new);
			Staff staff = staffRepo.findById(staffDTO.getStaffId()).orElseThrow(NoResultException::new);
			
			String senderName = staff.getFullname();
			String senderEmail = staff.getStaffEmail();

			
			String receiverName = student.getFullname();
			String receiverEmail = student.getStudentEmail();

			mailDTO.setSubject(subject);

			MimeMessage email = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(email, StandardCharsets.UTF_8.name());

			mailDTO.setContent("test");

			
			// Load template email with content
			Context context = new Context();
			context.setVariable("senderName", senderName);
			context.setVariable("senderEmail", senderEmail);
			context.setVariable("receiverName", receiverName);

			String html = templateEngine.process("email", context);

			// Send email
			helper.setTo(receiverEmail);
			helper.setText(html, true);
			helper.setSubject(mailDTO.getSubject());
			helper.setFrom(senderEmail);

			javaMailSender.send(email);
			

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	@Async
	@Override
	public void sendMailByNewAccount(StudentDTO studentDTO) {
		MailDTO mailDTO = new MailDTO();

		try {
			Student student = studentRepo.findById(studentDTO.getStudentId()).orElseThrow(NoResultException::new);
			
			String receiverName = student.getFullname();
			String receiverEmail = student.getStudentEmail();

			mailDTO.setSubject("Chào mừng đến với KTX trường GTVT");

			MimeMessage email = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(email, StandardCharsets.UTF_8.name());

			mailDTO.setContent("Chào mừng đến với Ký túc xá");

			
			// Load template email with content
			Context context = new Context();
			context.setVariable("tenKTX","GTVT");
			context.setVariable("studentName",student.getFullname());
			context.setVariable("username",student.getStudentId());
			context.setVariable("userpassword", student.getStudentId());

			String html = templateEngine.process("mailForNewStudent", context);

			// Send email
			helper.setTo(receiverEmail);
			helper.setText(html, true);
			helper.setSubject(mailDTO.getSubject());
			helper.setFrom("phamha03122003@gmail.com");

			javaMailSender.send(email);
			

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendMailByContract(Contract contractOp) {
		MailDTO mailDTO = new MailDTO();

		try {
			Student student = contractOp.getStudent();
			String receiverName = contractOp.getStudent().getFullname();
			String receiverEmail = contractOp.getStudent().getStudentEmail();

			mailDTO.setSubject("Thông báo hết hạn hợp đồng");

			MimeMessage email = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(email, StandardCharsets.UTF_8.name());

			mailDTO.setContent("Thông báo hết hạn hợp đồng");
			String html = new String();
			Context context = new Context();
			context.setVariable("receiveName", receiverName);
			context.setVariable("enddate", context);
			// neu ngay het han cua sinh vien truoc ngay cuoi cung cua thang nay thi gui ban het han neu khong thi se gui ban gia han
			if(contractOp.getStudent().getEndDate().before(Utils.getCurrentMonth().getEndDate()))
			{
				html = templateEngine.process("mailContractExpiry", context);
				
			}
			// Load template email with content
			else {
				html = templateEngine.process("mailContractRenew", context);
			}
			// Send email
			helper.setTo(receiverEmail);
			helper.setText(html, true);
			helper.setSubject(mailDTO.getSubject());
			helper.setFrom("phamha03122003@gmail.com");

			javaMailSender.send(email);
			

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void sendOtpEmail(String toEmail, String subject, String otpMessage) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(otpMessage);
		javaMailSender.send(message);
	}
}
