package com.assignment.guardrailsystem.service;

public interface ViralityService {

    void increaseVirality(Long postId, int score);

    Long getViralityScore(Long postId);

    Long incrementBotCount(Long postId);

    boolean createCooldown(Long botId, Long userId);

    void handleNotification(Long userId, String message);

    void processPendingNotifications();
}