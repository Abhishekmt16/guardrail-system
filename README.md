# Guardrail System

A Spring Boot microservice implementing Redis-based concurrency guardrails, virality tracking, notification throttling, and distributed cooldown protection.

This project demonstrates backend engineering fundamentals including atomic Redis operations, stateless architecture, distributed rate limiting, scheduled background processing, and PostgreSQL persistence.

---

# Features

## Core API Features

* Create Posts
* Add Comments
* Nested Reply System
* Like Posts
* Global Exception Handling
* DTO-based request validation

---

# Redis Guardrail Engine

## Real-Time Virality Score

Each interaction updates a Redis-based virality score instantly.

| Interaction Type | Score |
| ---------------- | ----- |
| Bot Reply        | +1    |
| Human Like       | +20   |
| Human Comment    | +50   |

Redis Key Example:

```text
post:{id}:virality_score
```

---

## Horizontal Cap (Concurrency Protection)

A single post cannot exceed 100 bot replies.

Implemented using Redis atomic increment operations:

```text
post:{id}:bot_count
```

This prevents race conditions during concurrent bot requests.

If the limit exceeds 100:

```http
429 TOO MANY REQUESTS
```

is returned.

---

## Vertical Cap (Thread Depth Protection)

Comment threads are restricted to a maximum nesting depth of 20.

This prevents:

* Infinite reply chains
* Rendering complexity
* Spam thread abuse

---

## Cooldown Cap (Bot-Human Interaction Protection)

A specific bot cannot interact with the same human more than once within 10 minutes.

Implemented using Redis TTL:

```text
cooldown:bot_{id}:user_{id}
```

This uses Redis `SETNX` behavior via:

```java
setIfAbsent()
```

which guarantees atomic distributed locking.

---

# Notification Engine

## Smart Notification Throttling

To avoid notification spam:

* First interaction sends an immediate notification
* Additional interactions within 15 minutes are batched in Redis Lists

Redis Structures Used:

```text
user:{id}:pending_notifs
```

and:

```text
pending_notification_users
```

---

## Scheduled Notification Sweeper

A Spring `@Scheduled` task runs every 5 minutes.

The scheduler:

* scans pending notification users
* summarizes interactions
* logs grouped notifications
* clears processed Redis lists

Example:

```text
Summarized Push Notification: Bot X and 4 others interacted with your posts.
```

---

# Tech Stack

| Technology        | Usage                        |
| ----------------- | ---------------------------- |
| Java 17           | Core language                |
| Spring Boot 3     | Backend framework            |
| PostgreSQL        | Persistent database          |
| Redis             | Distributed state management |
| Spring Data JPA   | ORM layer                    |
| Spring Data Redis | Redis integration            |
| Docker            | Infrastructure containers    |
| Maven             | Dependency management        |

---

# Project Architecture

```text
Controller Layer
        ↓
Service Layer
        ↓
Redis Guardrails + PostgreSQL
        ↓
Repository Layer
```

The system is fully stateless.

All:

* counters
* cooldowns
* notification queues
* concurrency locks

are stored in Redis instead of Java memory.

---

# API Endpoints

## Create Post

```http
POST /api/posts
```

---

## Add Comment

```http
POST /api/posts/{postId}/comments
```

---

## Like Post

```http
POST /api/posts/{postId}/like
```

---

## Get Virality Score

```http
GET /api/posts/{postId}/virality
```

---

# Running the Project

## 1. Clone Repository

```bash
git clone https://github.com/Abhishekmt16/guardrail-system.git
```

---

## 2. Start Docker Containers

```bash
docker-compose up -d
```

This starts:

* PostgreSQL
* Redis

---

## 3. Run Spring Boot Application

```bash
mvn spring-boot:run
```

OR run directly from IntelliJ.

---

# PostgreSQL Configuration

| Property | Value        |
| -------- | ------------ |
| Database | guardrail_db |
| Username | postgres     |
| Password | postgres     |
| Port     | 5432         |

---

# Redis Configuration

| Property | Value     |
| -------- | --------- |
| Host     | localhost |
| Port     | 6379      |

---

# Concurrency & Thread Safety

The system guarantees concurrency safety using Redis atomic operations.

Key techniques used:

* Redis `INCR`
* Redis `SETNX`
* Redis TTL-based distributed locks
* Stateless request processing

This ensures:

* no race conditions
* safe concurrent bot requests
* exact guardrail enforcement
* database integrity

The horizontal bot-reply cap was tested against concurrent requests to verify that the database never exceeds the configured maximum limit.

---

# Docker Support

The project includes a complete `docker-compose.yml` setup for local development.

Containers:

* PostgreSQL 16
* Redis 7

---
# Tested Scenarios

The system was tested for:

* Concurrent bot spam protection
* Redis atomic counter consistency
* Notification batching
* Cooldown enforcement
* Maximum nested reply depth
* Virality score calculation
* Race-condition prevention

The horizontal concurrency cap successfully prevented database writes beyond the configured bot reply threshold.

---
# Future Improvements

* JWT Authentication
* Kafka-based event streaming
* Redis Streams
* WebSocket notifications
* Distributed tracing
* API Gateway integration
* Kubernetes deployment

---

# Author

Abhishek M T

Backend Engineering Internship Assignment
