# Joorjens - Multi-Store Management System

> **Note:** This is an archived portfolio project from 2018. It is no longer actively maintained.

A complete store management solution with Android client and Java backend for managing inventory, products, and multi-store operations.

## Project Structure

```
Joorjens/
├── android-client/    # Android mobile application
├── backend/           # Java/Spark REST API server
└── resources/         # Icons, fonts, and UI assets
```

## Tech Stack

### Backend
- Java 8
- Spark Framework (REST API)
- Hibernate ORM
- MySQL Database

### Android Client
- Java/Kotlin
- ZXing (Barcode scanning)
- Material Design

## Features

- Multi-store inventory management
- Barcode scanning for products
- Persian/Farsi language support (IRANYekan font)
- RESTful API architecture

## Getting Started

### Backend
```bash
cd backend
mvn clean install
mvn exec:java
```

### Android
Open `android-client/` in Android Studio and build the project.
