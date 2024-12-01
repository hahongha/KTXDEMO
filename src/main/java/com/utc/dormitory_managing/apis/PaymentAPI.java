package com.utc.dormitory_managing.apis;

import com.utc.dormitory_managing.dto.PaymentDTO;
import com.utc.dormitory_managing.dto.ResponseObject;
import com.utc.dormitory_managing.dto.TransactionDTO;
import com.utc.dormitory_managing.entity.Payment;
import com.utc.dormitory_managing.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentAPI {


    private final PaymentService paymentService;
//http://localhost:8080/api/payment/vn-pay?amount=100000&bankCode=NCB&studentId=12111111
    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request ) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public ResponseEntity<?> transaction
            (
                    @RequestParam(value = "vnp_Amount" ) String amount,
                    @RequestParam(value = "vnp_OrderInfo" ) String OrderInfo,
                    @RequestParam(value = "vnp_ResponseCode" ) String ResponseCode,
                    @RequestParam(value = "vnp_PayDate" ) String vnp_PayDate
            )
    {
        TransactionDTO transactionDTO = new TransactionDTO();
        if(ResponseCode.equals("00"))
        {
            Payment savedPayment = paymentService.savePaymentResult(amount, OrderInfo);
            transactionDTO.setName("Chi tiet thanh toan");
            transactionDTO.setDescription("Thanh cong");
            // Định dạng thời gian theo chuỗi
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                // Chuyển đổi chuỗi thành đối tượng Date
                Date vnpayDate = formatter.parse(vnp_PayDate);

                // In ra kết quả
                System.out.println("Ngày giờ sau khi ép kiểu: " + vnpayDate);
                transactionDTO.setDate(vnpayDate);
                transactionDTO.setAmount(Long.valueOf(amount));
                transactionDTO.setOrderInfo(OrderInfo);
            } catch (ParseException e) {
                // Xử lý ngoại lệ nếu chuỗi không hợp lệ
                System.out.println("Lỗi định dạng thời gian: " + e.getMessage());
            }
            return new ResponseEntity<>(transactionDTO, HttpStatus.OK);

        }
        else
        {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }

}
