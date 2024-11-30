package com.utc.dormitory_managing.service;

import java.util.Date;
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
import com.utc.dormitory_managing.dto.BillFormDTO;
import com.utc.dormitory_managing.dto.ClockDTO;
import com.utc.dormitory_managing.dto.RoomDTO;
import com.utc.dormitory_managing.entity.Bill;
import com.utc.dormitory_managing.entity.Clock;
import com.utc.dormitory_managing.entity.Room;
import com.utc.dormitory_managing.entity.Services;
import com.utc.dormitory_managing.repository.BillRepo;
import com.utc.dormitory_managing.repository.ClockRepo;
import com.utc.dormitory_managing.repository.RoomRepo;
import com.utc.dormitory_managing.repository.ServiceRepo;
import com.utc.dormitory_managing.utils.Utils;
import com.utc.dormitory_managing.utils.Utils.DateRange;

public interface BillService {
	BillDTO create(BillDTO billDTO);
	BillDTO update(BillDTO billDTO);
	
	BillDTO updateStatus(BillDTO billDTO, String preStatus, String newStatus);
	void updateStatus2(BillDTO billDTO, String preStatus, String newStatus);
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
	private ClockRepo clockRepo;
	
	@Autowired
	private ServiceRepo serviceRepo;
	
	@Autowired
	private ClockService clockService;
	
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
			Bill Bill = mapper.map(BillDTO, Bill.class);
			billRepo.save(Bill);
			return BillDTO;
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
			return mapper.map(BillOptional.get(), BillDTO.class);
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
			List<Bill> Bills = billRepo.findAll();
			return Bills.stream().map(s -> mapper.map(s, BillDTO.class))
					.collect(Collectors.toList());
		} catch (ResourceAccessException e) {
			throw Problem.builder().withStatus(Status.EXPECTATION_FAILED).withDetail("ResourceAccessException").build();
		} catch (HttpServerErrorException | HttpClientErrorException e) {
			throw Problem.builder().withStatus(Status.SERVICE_UNAVAILABLE).withDetail("SERVICE_UNAVAILABLE").build();
		}
	}

	@Override
	public BillDTO createMonth(BillFormDTO billFormDTO) {
		ModelMapper mapper = new ModelMapper();
		Bill bill = new Bill();
		Services serviceE = serviceRepo.findByServiceName("ELECTRONIC").get();
		Services serviceW = serviceRepo.findByServiceName("WATER").get();
		//lay ki han tinh toan cua hoa don cua hoa don
		Optional<Room> roomOp = roomRepo.findById(billFormDTO.getRoomDTO().getRoomId());
		
		if(roomOp.isEmpty()) throw new BadRequestAlertException("Not Found Room", "room", "missingId");
		RoomDTO room2 = mapper.map(roomOp.get(), RoomDTO.class);
		
		//tinh toan tien dien va tien nuoc cua hoa don
		ClockDTO water = new ClockDTO();
		water.setClockName(NameClockRef.WATER.toString());
		DateRange dateRange = Utils.getCurrentMonth();
		water.setStartDate(dateRange.getStartDate());
		water.setEndDate(dateRange.getEndDate());
		water.setPreviosIndex(billFormDTO.getWaterPreIndex());
		water.setLastIndex(billFormDTO.getWaterIndex());
		Long value =(long) (water.getLastIndex()- water.getPreviosIndex())* serviceW.getServicePrice();
		water.setRoom(room2);
		clockService.create(water);
		
		
		
		ClockDTO electronic = new ClockDTO();
		electronic.setClockName(NameClockRef.ELECTRONIC.toString());
		electronic.setStartDate(dateRange.getStartDate());
		electronic.setEndDate(dateRange.getEndDate());
		electronic.setPreviosIndex(billFormDTO.getEPreIndex());
		electronic.setLastIndex(billFormDTO.getEIndex());
		electronic.setRoom(room2);
		Long value2 =(long) (electronic.getLastIndex()- water.getPreviosIndex())* serviceE.getServicePrice();
		
		clockService.create(electronic);
		
		bill.setBillId(UUID.randomUUID().toString());
		bill.setBillName(roomOp.get().getRoomName()+" thang "+Utils.getMonth(dateRange.getEndDate()));
		System.err.println(bill.getBillName());
		bill.setRoom(roomOp.get());
		bill.setBillValue(value+ value2);
		bill.setBillStatus(StatusBilltRef.WAITING.toString());
		bill.setBillDescription("electronic:"+ electronic.toString()+","+ "water:"+water.toString());
		billRepo.save(bill);
		return mapper.map(bill, BillDTO.class);
	}

	@Override
	public BillDTO updateStatus(BillDTO billDTO, String preStatus, String newStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus2(BillDTO billDTO, String preStatus, String newStatus) {
		// TODO Auto-generated method stub
		
	}
	
}