package com.example.mogubackend.scheduler;

import com.example.mogubackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private ProductService productService;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndUpdateProductStatus() {
        productService.updateProductStatus();
    }
}