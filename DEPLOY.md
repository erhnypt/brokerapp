# Broker Application Deployment Guide

This document describes how to build, deploy, and run the Broker Application for different environments.

## System Requirements

- Java 17 or higher
- Maven 3.6.x or higher
- Git

## Building the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/brokerApp.git
   cd brokerApp
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```
   This will create a JAR file in the `target` directory: `broker-app-0.0.1-SNAPSHOT.jar`

## Running the Application

### Running JAR File Directly

```bash
java -jar target/broker-app-0.0.1-SNAPSHOT.jar
```

By default, the application uses the H2 in-memory database and runs on port 8080.

### Running with Specific Profile

The application supports different profiles for various environments:

```bash
java -jar target/broker-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

Available profiles:
- `dev`: Development environment (default)
- `test`: Testing environment
- `prod`: Production environment

## Environment Configuration

The application uses different configuration files for different environments:

- `application.properties`: Default configuration
- `application-dev.properties`: Development environment
- `application-test.properties`: Test environment
- `application-prod.properties`: Production environment

To customize configurations:

1. Create a new file: `application-custom.properties`
2. Override any configuration properties
3. Run the application with the custom profile:
   ```bash
   java -jar target/broker-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=custom
   ```

## Database Configuration

### H2 Database (Default)

The application uses an H2 in-memory database by default. The H2 console is accessible at http://localhost:8080/h2-console with these credentials:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `` (empty)
-YOU CAN SEE THE TRANSACTIONS MADE HERE

### External Database Configuration

To use an external database, add the following properties to your profile-specific application properties file:

```properties
spring.datasource.url=jdbc:mysql://your-database-server:3306/broker_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

## Security Configuration

Default administrator credentials are:
- Username: `admin`
- Password: `admin123`

To change the admin credentials, modify the following properties:

```properties
admin.username=custom_admin
admin.password=custom_password
```

## API Documentation (Swagger UI)

The application includes comprehensive API documentation through Swagger UI, accessible at http://localhost:8080/swagger-ui/ when the application is running.

### Using Swagger UI

1. Start the application
2. Open a web browser and navigate to http://localhost:8080/swagger-ui/
3. You will see a list of all available API endpoints organized by controller
4. Click on any endpoint to expand it and see details including:
   - Required parameters
   - Request body structure (for POST and PUT requests)
   - Response codes and body structures
   - Authentication requirements

### Authentication in Swagger UI

For endpoints that require authentication:
1. Click on the "Authorize" button at the top of the page
2. Enter your JWT token in the format: `Bearer your-token-here`
3. Click "Authorize" and close the dialog
4. Now you can test secured endpoints directly from the UI

### Testing Endpoints

1. Select an endpoint
2. Fill in the required parameters or request body
3. Click "Execute"
4. View the server response below

The Swagger UI provides a convenient way to explore and test all available API endpoints without needing additional tools like Postman.

## Monitoring and Health Checks

Health check endpoint: http://localhost:8080/actuator/health

## Logging

The application logs are stored in the `logs` directory with daily rotation. The logging level can be adjusted in the application properties:

```properties
logging.level.root=INFO
logging.level.com.brokerapp=DEBUG
```

## Troubleshooting

### Common Issues

1. **Application fails to start**
   - Check if port 8080 is already in use
   - Verify Java version: `java -version`
   - Ensure database connectivity if using an external database

2. **Authentication issues**
   - Verify that JWT tokens are properly configured
   - Check that the secret key is consistent across instances

3. **Database connectivity issues**
   - Verify database credentials
   - Ensure the database server is running
   - Check network connectivity

For additional help, please contact support at support@brokerapp.com 