# Social Networking Platform

A backend social networking application built with Java and Spring Boot.

## Backend API Documentation

[Swagger UI](https://13.233.194.9:8000/swagger-ui/index.html)

## Tech Stack

- **Backend:** Java, Spring Boot
- **Database:** MySQL
- **Authentication:** JWT, Role-Based Authorization
- **DevOps & Deployment:** Docker, Jenkins, AWS EC2

## Features

- User authentication and authorization
- Social post management and user interactions
- Real-time messaging service

## API Testing

1. Open the [Swagger UI](https://13.233.194.9:8000/swagger-ui/index.html).
2. Select an API endpoint.
3. Review the request parameters and response schema.
4. Click **Try it out** to test the endpoint.

## Build and Run Locally

### Pre-Requisites
- Java 17
- Maven
- MySQL

### 1. Clone the Repository
```bash
git clone <your-repository-url>
cd social
```

### 2. Configure the Database
Update `src/main/resources/application.properties` with your MySQL database credentials.

### 3. Build the Application
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

Alternatively, run the generated JAR:
```bash
java -jar target/check-0.0.1-SNAPSHOT.jar
```

### 5. Access Swagger UI
Once the application is running, open:

http://localhost:8000/swagger-ui/index.html

**Note:** The Swagger UI requires the backend server to be running and accessible.



## Build and Deploy to Production

The application is built and deployed using **Jenkins, Maven, Docker, and AWS EC2**.

### Prerequisites

- Jenkins configured with Maven and the required credentials.
- AWS EC2 instance with Docker and Docker Compose installed.
- Application repository cloned on the EC2 instance in `~/social`.
- `docker-compose.yml` configured in the application directory.

### 1. Checkout Source Code

Jenkins checks out the application source code from the configured Git repository.

### 2. Build the Application

Jenkins builds the Spring Boot application using Maven.

```bash
mvn clean install
```

The generated JAR file is:

```text
target/social-0.0.1-SNAPSHOT.jar
```

### 3. Configure Environment Variables

Jenkins retrieves sensitive configuration values from Jenkins Credentials, including:

- Database username and password
- JWT secret
- SSL keystore file and password
- EC2 SSH private key

The pipeline generates an `.env` file containing the application configuration.

### 4. Transfer Files to EC2

Jenkins uses SCP to transfer the following files to the EC2 instance:

- Spring Boot executable JAR
- `.env` configuration file
- SSL keystore (`keystore.p12`)

### 5. Deploy Using Docker Compose

Jenkins connects to the EC2 instance over SSH and executes the deployment commands.

```bash
cd ~/social
mkdir -p target
mv -f ~/social-0.0.1-SNAPSHOT.jar target/
git pull
docker compose down
docker compose up -d --build
```

Docker Compose rebuilds and starts the application using the updated deployment files.

### 6. Verify Deployment

Check the status of running containers:

```bash
docker compose ps
```

### 7. Access the Application

**Swagger UI:**

https://13.233.194.9:8000/swagger-ui/index.html

### Security Notes

- Store sensitive values in Jenkins Credentials rather than hardcoding them.
- Do not commit `.env` files, private keys, or SSL keystores to the Git repository.
- Restrict SSH and application-port access through AWS Security Groups.
