//package com.p2p.oms.config;
//
//import com.p2p.oms.user.entity.User;
//import com.p2p.oms.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.jspecify.annotations.NullMarked;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//
//
//@Component
//@RequiredArgsConstructor
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//
//    @NullMarked
//    @Override
//    public void run(String... args) {
//
//        seedUser();
//    }
//
//    private void seedUser() {
//        if (userRepository.count() > 0) {
//            return;
//        }
//        User us = User.create("ex@gmail.com");
//        us.deposit(BigDecimal.valueOf(5000));
//        userRepository.save(us);
//    }
//}