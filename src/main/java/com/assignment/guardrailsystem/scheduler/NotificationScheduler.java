package com.assignment.guardrailsystem.scheduler;

import com.assignment.guardrailsystem.service.ViralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final ViralityService viralityService;

    @Scheduled(fixedRate = 300000)
    public void processNotifications() {

        viralityService.processPendingNotifications();
    }
}