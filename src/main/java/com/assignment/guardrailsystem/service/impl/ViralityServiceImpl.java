package com.assignment.guardrailsystem.service.impl;

import com.assignment.guardrailsystem.service.ViralityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViralityServiceImpl implements ViralityService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void increaseVirality(Long postId, int score) {

        String key = "post:" + postId + ":virality_score";

        redisTemplate.opsForValue().increment(key, score);

        Object updatedValue = redisTemplate.opsForValue().get(key);

        System.out.println("Updated Redis Score = " + updatedValue);
    }
    @Override
    public Long getViralityScore(Long postId) {

        String key = "post:" + postId + ":virality_score";

        Object value = redisTemplate.opsForValue().get(key);

        System.out.println("Redis value = " + value);

        if (value == null) {
            return 0L;
        }

        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        return Long.parseLong(value.toString());
    }

    @Override
    public Long incrementBotCount(Long postId) {

        String key = "post:" + postId + ":bot_count";

        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public boolean createCooldown(Long botId, Long userId) {

        String key = "cooldown:bot_" + botId + ":user_" + userId;

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "BLOCKED", 10, TimeUnit.MINUTES);

        return Boolean.TRUE.equals(success);
    }
    @Override
    public void handleNotification(Long userId, String message) {

        String cooldownKey = "notif:cooldown:user_" + userId;

        Boolean firstNotification = redisTemplate.opsForValue()
                .setIfAbsent(
                        cooldownKey,
                        "SENT",
                        15,
                        TimeUnit.MINUTES
                );

        if (Boolean.TRUE.equals(firstNotification)) {

            System.out.println(
                    "Push Notification Sent to User: " + message
            );

        } else {

            String listKey = "user:" + userId + ":pending_notifs";

            redisTemplate.opsForList()
                    .rightPush(listKey, message);

            redisTemplate.opsForSet()
                    .add("pending_notification_users", userId.toString());
        }
    }
    @Override
    public void processPendingNotifications() {

        Set<Object> users = redisTemplate.opsForSet()
                .members("pending_notification_users");

        if (users == null || users.isEmpty()) {
            return;
        }

        for (Object userObj : users) {

            String userId = userObj.toString();

            String listKey = "user:" + userId + ":pending_notifs";

            Long size = redisTemplate.opsForList().size(listKey);

            if (size != null && size > 0) {

                Object firstMessage = redisTemplate.opsForList()
                        .leftPop(listKey);

                System.out.println(
                        "Summarized Push Notification: "
                                + firstMessage
                                + " and "
                                + (size - 1)
                                + " others interacted with your posts."
                );

                redisTemplate.delete(listKey);
            }
        }

        redisTemplate.delete("pending_notification_users");
    }
}