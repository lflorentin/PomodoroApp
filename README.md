# PomodoroApp

![Angular](https://img.shields.io/badge/Angular-Frontend-dd0031?style=for-the-badge\&logo=angular\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-Backend-6db33f?style=for-the-badge\&logo=springboot\&logoColor=white)
![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge\&logo=openjdk\&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791?style=for-the-badge\&logo=postgresql\&logoColor=white)
![JWT](https://img.shields.io/badge/Security-JWT-black?style=for-the-badge\&logo=jsonwebtokens)
![REST API](https://img.shields.io/badge/API-REST-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Build-Maven-c71a36?style=for-the-badge\&logo=apachemaven\&logoColor=white)

## Productivity & Focus Management Platform

---

## Executive Summary

PomodoroApp is a full-stack productivity application designed to help users improve concentration, task organization, and time management through Pomodoro-based workflows.

The project combines a modern Angular frontend with a secure Spring Boot backend architecture exposing RESTful APIs.

The platform was designed with a scalable enterprise-oriented architecture focusing on:

* Secure authentication
* Task management workflows
* Productivity tracking
* REST API design
* Modern frontend UX
* Modular backend organization
* Scalable full-stack architecture

---

# Solution Architecture

```text
PomodoroApp
├── pomodoroapp_ngfront-main   → Angular frontend application
└── pomodoroapp-main           → Spring Boot backend API
```

---

# Enterprise Technology Stack

## Frontend

* Angular
* TypeScript
* Angular Router
* RxJS
* Angular Material
* SCSS

## Backend

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Maven
* REST API
* MapStruct
* Lombok

## Database

* PostgreSQL
* JPA / Hibernate

---

# Core Business Features

## Authentication & Security

* JWT authentication system
* Secure login workflow
* User registration
* Protected API routes
* Spring Security integration

## Productivity Management

* Pomodoro session management
* Task organization workflows
* Productivity-oriented user experience
* Time management features

## Backend API Services

* RESTful API architecture
* DTO & mapper-based architecture
* Layered backend organization
* Validation and middleware security
* Modular service structure

---

# Backend Architecture

```text
pomodoroapp-main/
├── src/main/java/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── mapper/
│   ├── repository/
│   ├── security/
│   ├── service/
│   └── config/
├── src/main/resources/
├── pom.xml
└── mvnw
```

---

# Frontend Architecture

```text
pomodoroapp_ngfront-main/
├── src/
│   ├── app/
│   ├── components/
│   ├── services/
│   ├── pages/
│   ├── models/
│   └── guards/
├── angular.json
└── package.json
```

---

# Installation

## Backend Setup

```bash
cd pomodoroapp-main
./mvnw spring-boot:run
```

## Frontend Setup

```bash
cd pomodoroapp_ngfront-main
npm install
ng serve
```

---

# Security & API Design

The backend implements a secure authentication workflow based on JWT tokens and Spring Security.

The application architecture follows modern backend development principles:

* DTO separation
* Layered architecture
* API-first communication
* Modular services
* Entity mapping abstraction
* Secure authentication middleware

---

# Technical & Product Objectives

* Improve personal productivity workflows
* Build a scalable full-stack application
* Implement enterprise-grade backend architecture
* Practice secure API development
* Design maintainable frontend architecture
* Strengthen full-stack engineering practices

---

# CI/CD & DevOps Integration

The project already includes a containerized CI pipeline designed to automate:

* Application build workflows
* Backend validation
* Automated testing processes
* Containerized execution environments
* Continuous integration practices

The deployment stage has not yet been fully implemented, but the project architecture was designed to support future automated deployment workflows.

Potential future improvements:

* Production-ready Docker orchestration
* Automated deployment stage implementation
* Monitoring & observability
* Cloud deployment automation
* Real-time session tracking
* Mobile responsive optimization
* Unit & integration testing expansion
* Kubernetes deployment experimentation

---

# Key Technical Highlights

* Existing containerized CI pipeline

* Automated build & test workflows

* Full-stack Angular + Spring Boot architecture

* JWT-based authentication & authorization

* PostgreSQL relational database integration

* RESTful API implementation

* Secure backend design with Spring Security

* DTO / Mapper abstraction pattern

* Modular scalable project structure

* Enterprise-oriented architecture practices

---

# Author

Lucas Florentin

Full Stack Developer — Java / Spring Boot / Angular / DevOps
