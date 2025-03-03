# Microservices E-Commerce Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2022.0.0-blue)](https://spring.io/projects/spring-cloud)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A scalable cloud-native e-commerce platform built with **Spring Boot 3** and **Spring Cloud**, designed for high availability and distributed transactions.

## ✨ Features
- **Core Services**: Product catalog, payment processing, order management, notifications
- **Resilience**: Circuit breakers, retry mechanisms, distributed tracing
- **Observability**: Integrated with Zipkin for request tracking
- **Security**: JWT authentication, network isolation
- **Event-Driven**: Async communication via message broker
- **CI/CD Ready**: Pre-configured build pipelines

## 🛠️ Tech Stack
| Category          | Technologies                          |
|--------------------|---------------------------------------|
| **Framework**      | Spring Boot 3, Spring Cloud           |
| **Database**       | MySQL (Relational DB), MongoDB            |
| **Messaging**      | RabbitMQ/Kafka                        |
| **Monitoring**     | Zipkin, Spring Cloud Sleuth           |
| **Infrastructure** | Docker, Kubernetes, AWS/GCP (optional)|
| **CI/CD**          | GitHub Actions, Jenkins               |

## 🏗️ Architecture
### Network Segregation
| **Public Network**              | **Private Network**             |
|----------------------------------|----------------------------------|
| API Gateway                      | Order Service                    |
| Customer Service                 | Notification Service             |
| Product Service                  | Source Server (On-prem)          |
| Payment Service                  | Cloud Server (Cloud deployment) |
| Sandbox Payment Confirmation     | Relational(Mysql) DB (Primary Database) |     |

### Core Components
- **Acquisitions**: Customer onboarding and order initiation
- **Products**: Inventory management and catalog services
- **Payments**: Transaction processing with sandbox testing
- **Notification**: Real-time alerts via email/SMS
- **Message Broker**: Event streaming between services
- **ZIPKIN**: End-to-end request tracing

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Docker 20.10+
- MySQL 8.0+
- Maven 3.8+

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/fazla23/microservice_e-commerce.git
   cd microservice_e-commerce  
2. Start infrastructure: `docker-compose up -d mysql rabbitmq zipkin`  
3. Build services: `mvn clean package`  
4. Run services sequentially:  
   - Service Registry: `java -jar service-registry/target/*.jar`  
   - API Gateway: `java -jar api-gateway/target/*.jar`  
   - Repeat for other services (products, payments, etc.)  

**Key Workflow**: Customer order → Acquisitions Service → Product Service (inventory check) → Payment Service (transaction) → Notification Service (confirmation) → ZIPKIN tracing.  

**Contributors**: [Fazla Rabbi](https://github.com/fazla23)   

API docs via Swagger UI: `http://localhost:8080/swagger-ui.html`.  
