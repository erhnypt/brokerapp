# Broker Application User Guide

This document provides detailed instructions on how to use the Broker Application API for managing customers, assets, and orders.

## Authentication

### Login
```
POST /api/auth/login
```

**Request Body:**
```json
{
  "username": "your_username",
  "password": "your_password"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "your_username",
  "role": "USER"
}
```

Use the returned token in the Authorization header for subsequent requests:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### `Default Admin Credentials`
The system comes with a default administrator account:
```
Username: admin
Password: admin123
```
You can use these credentials to log in with administrative privileges to access all endpoints.

## Swagger UI Documentation

The Broker Application provides a Swagger UI interface for easy API exploration and testing. To use Swagger UI:

1. Start the application
2. Navigate to http://localhost:8080/swagger-ui/ in your browser
3. The interface displays all available API endpoints grouped by controller
4. Click on any endpoint to expand its details
5. For authenticated endpoints:
   - Click the "Authorize" button at the top
   - Enter your JWT token as `Bearer your-token-here`
   - Click "Authorize" to apply authentication
6. Test endpoints directly from the UI by:
   - Entering required parameters
   - Clicking "Execute"
   - Viewing the response

Swagger UI is the recommended way to explore and test the API as it provides detailed documentation for each endpoint, including parameter requirements and response formats.

## Customer Management

### List All Customers (Admin Only)
```
GET /api/customers
```

### Get Customer by ID
```
GET /api/customers/{id}
```
Note: Regular users can only access their own customer record.

### Create New Customer
```
POST /api/customers
```

**Request Body:**
```json
{
  "name": "Customer Name",
  "email": "customer@example.com",
  "phone": "+901234567890"
}
```

### Update Customer
```
PUT /api/customers/{id}
```

**Request Body:**
```json
{
  "name": "Updated Name",
  "email": "updated@example.com",
  "phone": "+901234567890"
}
```

### Delete Customer
```
DELETE /api/customers/{id}
```

## Asset Management

### List Assets for a Customer
```
GET /api/assets/customer/{customerId}
```
Retrieves all assets owned by a specific customer.

### Get Asset by ID
```
GET /api/assets/{id}
```

### Create Asset
```
POST /api/assets
```

**Request Body:**
```json
{
  "customerId": 1,
  "stockId": 1,
  "amount": 10.5
}
```

## Stock Management (Admin Only)

### List All Stocks
```
GET /api/stocks
```

### Get Stock by ID
```
GET /api/stocks/{id}
```

### Get Stock by Name
```
GET /api/stocks/name/{name}
```

### Create New Stock
```
POST /api/stocks
```

**Request Body:**
```json
{
  "name": "STOCKTRY",
  "price": 100.50,
  "amount": 1000
}
```
Note: Stock names must end with "TRY" indicating Turkish Lira.

### Update Stock
```
PUT /api/stocks/{id}
```

**Request Body:**
```json
{
  "name": "STOCKTRY",
  "price": 105.75,
  "amount": 1200
}
```

### Update Stock Price
```
PUT /api/stocks/{id}/price
```

**Request Body:**
```json
{
  "price": 110.25
}
```

### Increase Stock Amount
```
PUT /api/stocks/{id}/amount
```

**Request Body:**
```json
{
  "amount": 500
}
```

### Delete Stock
```
DELETE /api/stocks/{id}
```

## Order Management

### List Orders for a Customer
```
GET /api/orders/customer/{customerId}
```
Retrieves all orders created by a specific customer.

### Get Order by ID
```
GET /api/orders/{id}
```

### Create New Buy Order
```
POST /api/orders/buy
```

**Request Body:**
```json
{
  "customerId": 1,
  "stockId": 1,
  "price": 100.00,
  "amount": 5.0
}
```

### Create New Sell Order
```
POST /api/orders/sell
```

**Request Body:**
```json
{
  "customerId": 1,
  "stockId": 1,
  "price": 105.00,
  "amount": 3.0
}
```

### Cancel Order
```
DELETE /api/orders/{id}
```

## Error Handling

All API endpoints return standard HTTP status codes:
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

Error responses include details about what went wrong:
```json
{
  "timestamp": "2023-06-15T10:15:30.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/orders/buy"
}
```

## Notes on Order Processing

1. When creating buy or sell orders, the system automatically attempts to match them with existing orders.
2. Matched orders are executed immediately and assets are updated accordingly.
3. Partially matched orders remain active for the unmatched portion.
4. Orders are matched based on price and time priority.
5. You can check the status of your orders using the list orders endpoint.

For any additional assistance, please contact support@brokerapp.com. 