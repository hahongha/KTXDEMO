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

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentAPI {


    private final PaymentService paymentService;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request ) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }



//    @GetMapping("/vn-pay-callback")
//    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(
//            HttpServletRequest request, HttpServletRequest response,
//            @RequestParam String vnp_ResponseCode) {
//        String status = request.getParameter("vnp_ResponseCode");
////      get params from vnpay
//// fronend nhận và tạo màn hình thông báo thanh công nếu response code== 00=>> success
////
//
//
//        if (vnp_ResponseCode.equals("00")) {
//            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentDTO.VNPayResponse("00", "Success", ""));
//        } else {
//            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
//        }
//
//


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
            StringBuilder js   = new StringBuilder();
            js.append("Tong tien: "+amount+"\n Nguoi thanh toan "+OrderInfo+"\n Thoi gian thanh toan "+vnp_PayDate);
            transactionDTO.setData(js.toString());
            return new ResponseEntity<>(transactionDTO, HttpStatus.OK);

        }
        else
        {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }

}
