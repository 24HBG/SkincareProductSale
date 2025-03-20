package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.request.OrderDetailRequest;
import com.example.demo.entity.request.OrderRequest;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.PaymentMethod;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.DiscountRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.utils.AccountUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
     DiscountRepository discountRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountUtils accountUtils;

    private static final float SHIPPING_FEE = 30000;

    public String checkout(OrderRequest orderRequest, String paymentMethod, Long discountId) throws Exception {
        Account account = accountUtils.getCurrentAccount();

        // ✅ Kiểm tra thông tin địa chỉ và số điện thoại từ orderRequest
        if (orderRequest.getCustomerAddress() == null || orderRequest.getCustomerAddress().trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập địa chỉ giao hàng.");
        }
        if (orderRequest.getPhoneNumber() == null || orderRequest.getPhoneNumber().trim().isEmpty()) {
            throw new RuntimeException("Vui lòng nhập số điện thoại.");
        }

        float total = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setOrderDetails(orderDetails);
        order.setAccount(account);
        order.setStatus(OrderStatusEnum.IN_PROCESS); // Đặt hàng ở trạng thái chờ xác nhận

        // ✅ Gán số điện thoại và địa chỉ vào đơn hàng
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setCustomerAddress(orderRequest.getCustomerAddress());

        for (OrderDetailRequest orderDetailRequest : orderRequest.getDetails()) {
            Product product = productRepository.findProductById(orderDetailRequest.getProductId());
            if (product.getStock() >= orderDetailRequest.getQuantity()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setQuantity(orderDetailRequest.getQuantity());
                orderDetail.setPrice(product.getPrice() * orderDetailRequest.getQuantity());
                orderDetail.setOrder(order);
                orderDetails.add(orderDetail);

                // Giảm số lượng tồn kho
                product.setStock(product.getStock() - orderDetailRequest.getQuantity());
                productRepository.save(product);

                total += orderDetail.getPrice();
            } else {
                throw new RuntimeException("Số lượng sản phẩm không đủ.");
            }
        }

        // ✅ Tính tổng tiền với phí ship và discount (nếu có)
        float discountValue = 0;
        if (discountId != null) {
            Discount discount = discountRepository.findById(discountId)
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không hợp lệ."));
            discountValue = discount.getDiscountValue(); // Giả sử discount tính bằng VNĐ
        }

        float finalTotal = total + SHIPPING_FEE - discountValue;
        if (finalTotal < 0) {
            finalTotal = 0; // Không cho phép tổng tiền âm
        }

        order.setTotal(finalTotal);
        Order newOrder = orderRepository.save(order);

        // ✅ Xử lý thanh toán
        if ("VNPay".equalsIgnoreCase(paymentMethod)) {
            order.setPaymentMethod("VNPay");
            order.setPaymentStatus(PaymentStatus.PENDING); // Chờ thanh toán
            orderRepository.save(order);
            return createURLPayment(order); // Chuyển hướng đến VNPay
        } else if ("COD".equalsIgnoreCase(paymentMethod)) {
            order.setPaymentMethod("COD");
            order.setPaymentStatus(PaymentStatus.PENDING); // Chờ thanh toán khi giao hàng
            orderRepository.save(order);
            return "Đơn hàng đã được tạo thành công. Vui lòng thanh toán khi nhận hàng!";
        } else {
            throw new RuntimeException("Phương thức thanh toán không hợp lệ.");
        }
    }

    public String createURLPayment(Order order) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);
        String orderId = UUID.randomUUID().toString().substring(0, 6);
        String tmnCode = "2ISVDF73";
        String secretKey = "KJSH7IFLIKCLUQPDV72PEY20IKEB6P6D";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnURL = "http://localhost:8080/?orderId=" + order.getId();
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount", (int) order.getTotal() + "00");
        vnpParams.put("vnp_ReturnUrl", returnURL);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "167.99.74.201");

        String signData = vnpParams.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String signed = generateHMAC(secretKey, signData);
        vnpParams.put("vnp_SecureHash", signed);

        return vnpUrl + "?" + signData + "&vnp_SecureHash=" + signed;
    }


    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));

        byte[] hmacBytes = mac.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        // Chuyển đổi byte[] -> hex string nhanh hơn
        return IntStream.range(0, hmacBytes.length)
                .mapToObj(i -> String.format("%02x", hmacBytes[i]))
                .collect(Collectors.joining());
    }

    public List<Order> getOrdersByUser() {
        Account account = accountUtils.getCurrentAccount();
        return orderRepository.findAllByAccountId(account.getId());
    }

    public Order updateStatus(OrderStatusEnum orderStatusEnum, long id) {
        Order order = orderRepository.findOrderById(id);
        order.setStatus(orderStatusEnum);
        return orderRepository.save(order);
    }

    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
