package com.utc.dormitory_managing.apis;

import com.utc.dormitory_managing.dto.*;
import com.utc.dormitory_managing.entity.Payment;
import com.utc.dormitory_managing.service.BillService;
import com.utc.dormitory_managing.service.PaymentService;
import com.utc.dormitory_managing.service.StudentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentAPI {


    private final PaymentService paymentService;
   @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay
    (
            HttpServletRequest request,
            @RequestParam(value = "amount" ) String amount,
            @RequestParam(value = "studentId" ) String studentId,
            @RequestParam(value = "billId" ) String billId
    )
    {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request, amount, studentId,billId));
    }
    @Autowired
    private final BillService billService;
   private final StudentService studentService;

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
    // Get all payments
    @GetMapping("/getAll")
    public ResponseEntity<ResponseObject<List<Payment>>> getAllPayments() {
        try {
            List<Payment> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(
                    new ResponseObject<>(HttpStatus.OK, "Get all payments successfully", payments)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<Payment>> getPaymentById(@PathVariable Long id) {
        try {
            Payment payment = paymentService.getPaymentById(id);
            if (payment != null) {
                return ResponseEntity.ok(
                        new ResponseObject<>(HttpStatus.OK, "Get payment successfully", payment)
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject<>(HttpStatus.NOT_FOUND, "Payment not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

    // Delete payment
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deletePayment(@PathVariable Long id) {
        try {
            boolean deleted = paymentService.deletePayment(id);
            if (deleted) {
                return ResponseEntity.ok(
                        new ResponseObject<>(HttpStatus.OK, "Payment deleted successfully", null)
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject<>(HttpStatus.NOT_FOUND, "Payment not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseObject<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

}
