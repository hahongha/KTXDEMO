package com.utc.dormitory_managing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import com.utc.dormitory_managing.apis.error.BadRequestAlertException;
import com.utc.dormitory_managing.configuration.ApplicationProperties.NameClockRef;
import com.utc.dormitory_managing.configuration.ApplicationProperties.StatusBilltRef;
import com.utc.dormitory_managing.dto.BillDTO;
import com.utc.dormitory_managing.dto.BillDetailDTO;
import com.utc.dormitory_managing.dto.BillFormDTO;
import com.utc.dormitory_managing.dto.ClockDTO;
import com.utc.dormitory_managing.dto.RoomDTO;
import com.utc.dormitory_managing.entity.Bill;
import com.utc.dormitory_managing.entity.BillDetail;
import com.utc.dormitory_managing.entity.Room;
import com.utc.dormitory_managing.entity.Services;
import com.utc.dormitory_managing.entity.Student;
import com.utc.dormitory_managing.repository.BillDetailRepo;
import com.utc.dormitory_managing.repository.BillRepo;
import com.utc.dormitory_managing.repository.RoomRepo;
import com.utc.dormitory_managing.repository.ServiceRepo;
import com.utc.dormitory_managing.repository.StudentRepo;
import com.utc.dormitory_managing.utils.Utils;
import com.utc.dormitory_managing.utils.Utils.DateRange;

import jakarta.persistence.NoResultException;

public interface BillService {
	BillDTO create(BillDTO billDTO);
	BillDTO update(BillDTO billDTO);
	Boolean delete(String id);
	BillDTO get(String id);
	List<BillDTO> getAll();
	BillDTO createMonth(BillFormDTO billFormDTO);
	
}
@Service
class BillServiceImpl implements BillService {

	@Autowired
	private BillRepo billRepo;
	
	@Autowired
	private RoomRepo roomRepo;
	
	@Autowired
	private ServiceRepo serviceRepo;
	
	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private BillDetailRepo billDetailRepo;
	
	@Override
	public BillDTO create(BillDTO BillDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Bill Bill = mapper.map(BillDTO, Bill.class);
			Bill.setBillId(UUID.randomUUID().toString());
			billRepo.save(Bill);
			return BillDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public BillDTO update(BillDTO BillDTO) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Bill> BillOptional = billRepo.findById(BillDTO.getBillId());
			if(BillOptional.isEmpty()) throw new BadRequestAlertException("Not Found Bill", "Bill", "missing");
			Bill bill = BillOptional.get();
			Student studentpay = new Student();
//			System.err.println(bill.toString());
			if(BillDTO.getStudentPay()!=null) {
				studentpay = studentRepo.findById(BillDTO.getStudentPay().getStudentId()).orElseThrow(NoResultException::new);
				if(studentpay.getRoom()!= bill.getRoom()) throw new BadRequestAlertException("Not Match Room", "bill", "match");
				bill.setStudentPay(studentpay);
				bill.setBillStatus(StatusBilltRef.COMPLETE.toString());
				bill.setDayPay(new Date());
			}
			billRepo.save(bill);
			BillDTO billDTO2 = mapper.map(bill, BillDTO.class);
			System.err.println(bill.toString());
			System.err.println(billDTO2.toString());
			return billDTO2;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public Boolean delete(String id) {
		try {
			Optional<Bill> BillOptional = billRepo.findById(id);
			if(BillOptional.isEmpty()) return false;
			billRepo.deleteById(id);
			return true;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public BillDTO get(String id) {
		try {
			ModelMapper mapper = new ModelMapper();
			Optional<Bill> BillOptional = billRepo.findById(id);
			if(BillOptional.isEmpty()) throw new BadRequestAlertException("Not Found Bill", "Bill", "missing");
			BillDTO billDTO = mapper.map(BillOptional.get(), BillDTO.class);
			
			List<BillDetail> bds = billDetailRepo.findByBill(BillOptional.get().getBillId());
			if(bds.size()>0) {
				List<BillDetailDTO> billDetailDTOs = new ArrayList<BillDetailDTO>();
				for (BillDetail billDetail : bds) {
					billDetailDTOs.add(mapper.map(billDetail, BillDetailDTO.class));
				}
				billDTO.setBillDetails(billDetailDTOs);
			}
			
			return billDTO;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}

	@Override
	public List<BillDTO> getAll() {
		
		try {
			ModelMapper mapper = new ModelMapper();
			List<BillDTO> b = new ArrayList<BillDTO>();
			List<Bill> Bills = billRepo.findAll();
			if(Bills.size()>0) {
				for (Bill bill : Bills) {
					b.add(get(bill.getBillId()));
				}
			}
			return b;
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public BillDTO createMonth(BillFormDTO billFormDTO) {
		try {
		ModelMapper mapper = new ModelMapper();
		Bill bill = new Bill();
		bill.setBillId(UUID.randomUUID().toString());
		bill.setBillStatus(StatusBilltRef.WAITING.toString());
		List<BillDetail> bds = new ArrayList<BillDetail>();
		Services serviceE = serviceRepo.findByServiceName("ELECTRONIC").get();
		Services serviceW = serviceRepo.findByServiceName("WATER").get();
		//lay ki han tinh toan cua hoa don cua hoa don
		Optional<Room> roomOp = roomRepo.findById(billFormDTO.getRoomDTO().getRoomId());
		if(roomOp.isEmpty()) throw new BadRequestAlertException("Not Found Room", "room", "missingId"); 
		Room room = roomOp.get();
		int preE = room.getLastElectronic();
		int lE = billFormDTO.getEIndex();
		int preW = room.getLastWater();
		int lW = billFormDTO.getWaterIndex();
		if(lW< preW || lE< preE) throw new BadRequestAlertException("Last index < pre index", "bill", "logic");
		room.setLastWater(lW);
		room.setPreWater(preW);
		roomRepo.save(room);
		billRepo.save(bill);
		
		
		room.setPreElectronic(preE);
		room.setLastElectronic(lE);
		
		BillDetail bd1 = new BillDetail();
		bd1.setServices(serviceE);
		bd1.setNumber(lE-preE);
		bd1.setValue(serviceE.getServicePrice()* bd1.getNumber());
		bd1.setBill(bill);
		bds.add(bd1);
		
		BillDetail bd2 = new BillDetail();
		bd2.setServices(serviceW);
		bd2.setNumber(lW-preW);
		bd2.setValue(serviceW.getServicePrice()* bd2.getNumber());
		bds.add(bd2);
		bd2.setBill(bill);
		billDetailRepo.save(bd2);
		billDetailRepo.save(bd1);
		
		return mapper.map(bill, BillDTO.class);
		
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
		
	}
	
}