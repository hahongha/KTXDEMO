//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        // Cấu hình broker cho các tin nhắn gửi tới /topic
//        config.enableSimpleBroker("/topic");
//        // Cấu hình cho các yêu cầu từ client tới server với prefix /app
//        config.setApplicationDestinationPrefixes("/app");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        // Đăng ký điểm cuối WebSocket cho client kết nối
//        registry.addEndpoint("/spring-boot-tutorial").withSockJS();
//    }
//}
