# Broker Application User Guide

This is a Java Spring Boot application.

## Getting Started

Run the following commands in your terminal:
```
mvn clean install
mvn spring-boot:run
```

## Important Notes

- All tests can be performed using the Swagger UI interface
- You can view data in the H2 database
- Due to the use of H2 database, all data will be lost when the application is shut down

## Authentication

Admin login credentials are available in the application.properties file:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

To authenticate:
1. Navigate to `POST /api/alt-auth/admin-login` endpoint in Swagger UI
2. Enter the credentials above in the request body
3. Click the "Execute" button
4. You will receive a 200 success response
5. Copy the token from the response body
6. Click the "Authorize" button in the top-right corner of the Swagger UI
7. Enter the token as "Bearer + token" (note: there must be a space between "Bearer" and the token)
8. Click "Authorize" to apply the authentication

## Admin Privileges

After successful login, administrators have the following privileges:

1. User creation
2. Customer creation
3. Asset creation

### Admin Endpoints

- `POST /api/admin/users` - Create new users
- `POST /api/admin/customers` - Create new customers
- `POST /api/admin/assets` - Create new assets
- `GET /api/admin/users` - List all users
- `GET /api/admin/customers` - List all customers
- `GET /api/admin/assets` - List all assets

## Asset Purchasing

Assets can be purchased in two ways:

1. **Admin Purchase**: When logged in as an admin, you can purchase assets on behalf of any customer
2. **Customer Purchase**: Customers can log in and purchase assets directly

Both admin and customer users can perform asset purchase operations using the appropriate endpoints in Swagger UI.

## Order Operations

To manage orders, you can use the Order Controller endpoints:

- `GET /api/orders` - View all orders
- `GET /api/orders/customer/{customerId}/status/{status}` - Filter orders by customer ID and status

### Admin Order Management

Administrators can manage the entire order process and have permissions to:
- Match pending orders
- Cancel pending orders
- View order status for any customer

### Customer Order Placement

If you want a customer to place orders directly:

1. Log out from admin account
2. Navigate to `POST /api/auth/customer-login` endpoint
3. Enter the customer's username and password
4. Obtain the authorization token
5. Apply the authorization as described in the Authentication section
6. The customer can now place orders directly 