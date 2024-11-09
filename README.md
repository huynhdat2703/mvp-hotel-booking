# Hotel Booking System

This is a Hotel Booking system built with Spring Boot (Java) and PostgreSQL. It includes Docker support for easy setup and deployment. The application allows users to make, update, and cancel hotel bookings.

## Prerequisites

Before running this project, ensure you have the following installed on your machine:

- [Docker](https://www.docker.com/products/docker-desktop) (for creating containers)
- [Docker Compose](https://docs.docker.com/compose/) (for managing multi-container applications)
- [Maven](https://maven.apache.org/) (for building the project)
- [JDK 11](https://adoptopenjdk.net/) (for running the Java application, if not using Docker)

## Getting Started

### 1. Clone the Repository

Clone the repository to your local machine using Git:

```bash
git clone https://your-repository-url.git
cd your-repository-folder
```

### 2. Docker Setup
Step 1: Open terminal at project directory

Step 2: Run this command:
```bash
docker-compose up --build -d
```

This will start the following services:

- PostgreSQL database (available on localhost:5432)

- Java application (available on localhost:8080)

It will automatically initialize the PostgreSQL database with required tables and initial data from the init_db folder.

If you would like to restore data, you can copy script from ```init_db/init_table.sql``` then run it manually.

### 3. Accessing the Application
Once the containers are running, you can access the API at localhost:8080.

You can interact with the hotel booking system via RESTful APIs.

For example:

Get all bookings: ```GET /api/bookings```

Create a booking: ```POST /api/bookings```

Cancel a booking: ```DELETE /api/bookings/{id}```

### 4. Running Tests
To run the unit tests, you can use Maven. Simply run the following command:

```
./mvnw test
```

### 5. Stopping the Containers
To stop the running Docker containers, use the following command:
```
docker-compose down
```
This will stop and remove the containers, but your database data will be persisted in Docker volumes.

### 6. Check custom metrics
To check custom metric counter to count number of create booking, access here:
```
http://localhost:8080/actuator/metrics/custom.counter
```

### 7. Logging
Log will be stored in ```logs/myapp.log```

## License
This project is licensed under the MIT License - see the LICENSE file for details.
