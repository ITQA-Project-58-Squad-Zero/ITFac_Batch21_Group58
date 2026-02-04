# API Testing Guide

## Overview

This project now includes comprehensive API testing capabilities using REST Assured and Serenity BDD. The infrastructure is designed to be reusable and easy to extend for all team members.

## Project Structure

```
src/test/java/
├── api/
│   ├── client/
│   │   ├── BaseApiClient.java          # Base class with reusable HTTP methods
│   │   └── SalesApiClient.java         # Sales-specific API client
│   └── models/
│       ├── SaleRequest.java            # Request model for sales
│       └── SaleResponse.java           # Response model for sales
├── steps/
│   └── api/
│       └── SalesApiSteps.java          # Cucumber step definitions for API tests
└── ...

src/test/resources/
└── features/
    └── api/
        └── sales_api.feature           # API test scenarios
```

## How to Use

### 1. Running API Tests

**Run all API tests:**

```bash
mvn clean verify -Dcucumber.filter.tags="@api"
```

**Run specific test scenario:**

```bash
mvn clean verify -Dcucumber.filter.tags="@SM_API_01"
```

**Run all tests (UI + API):**

```bash
mvn clean verify
```

### 2. Creating New API Tests

#### Step 1: Create API Client (if needed)

Extend `BaseApiClient` for your module:

```java
public class YourModuleApiClient extends BaseApiClient {
    private static final String ENDPOINT = "/your-endpoint";

    public Response getAllItems(String username, String password) {
        return getWithAuth(ENDPOINT, username, password);
    }
}
```

#### Step 2: Create Request/Response Models

```java
public class YourRequest {
    private String field1;
    private Integer field2;
    // getters and setters
}
```

#### Step 3: Create Step Definitions

```java
public class YourModuleApiSteps {
    private YourModuleApiClient client = new YourModuleApiClient();
    private Response response;

    @When("I send a GET request to your endpoint")
    public void sendGetRequest() {
        response = client.getAllItems(username, password);
    }
}
```

#### Step 4: Write Feature File

```gherkin
Feature: Your Module API Testing

  @your_tag
  Scenario: Test your endpoint
    Given I am authenticated as an admin user
    When I send a GET request to your endpoint
    Then the response status code should be 200
```

### 3. Available Reusable Methods in BaseApiClient

**HTTP Methods:**

- `get(String endpoint)` - GET request without auth
- `getWithAuth(String endpoint, String username, String password)` - GET with auth
- `post(String endpoint, Object body)` - POST request
- `postWithAuth(String endpoint, Object body, String username, String password)` - POST with auth
- `put(String endpoint, Object body)` - PUT request
- `putWithAuth(String endpoint, Object body, String username, String password)` - PUT with auth
- `delete(String endpoint)` - DELETE request
- `deleteWithAuth(String endpoint, String username, String password)` - DELETE with auth

**Request Builders:**

- `getBaseRequest()` - Basic request with JSON content type
- `getAuthenticatedRequest(String username, String password)` - Request with Basic Auth
- `getRequestWithHeaders(Map<String, String> headers)` - Request with custom headers

### 4. Available Step Definitions

**Authentication:**

- `Given I am authenticated as an admin user`
- `Given I am authenticated as a regular user`
- `Given I am not authenticated`

**Request Preparation:**

- `Given I have a valid sale request with plantId {string}, quantity {int}, and totalPrice {double}`
- `Given I have a sale request with invalid quantity {int}`
- `Given I have a sale request with missing plantId`

**API Calls:**

- `When I send a GET request to retrieve all sales`
- `When I send a GET request to retrieve sale with id {string}`
- `When I send a POST request to create a new sale`
- `When I send a PUT request to update sale with id {string}`
- `When I send a DELETE request to delete sale with id {string}`

**Response Validation:**

- `Then the response status code should be {int}`
- `Then the response should contain a list of sales`
- `Then the response should contain sale details`
- `Then the response should contain an error message`
- `Then the response content type should be {string}`
- `Then the response time should be less than {int} milliseconds`
- `Then the response should contain field {string} with value {string}`

## Configuration

### API Base URL

Configured in `src/test/resources/serenity.conf`:

```hocon
environments {
    default {
        api.base.url = "http://localhost:8080/api"
    }
    dev {
        api.base.url = "http://localhost:8080/api"
    }
    staging {
        api.base.url = "http://staging-server:8080/api"
    }
}
```

### Credentials

Configured in `src/test/resources/serenity.conf`:

```hocon
credentials {
    admin {
        username = "admin"
        password = "admin123"
    }
    user {
        username = "testuser"
        password = "test123"
    }
}
```

## Sample Test Scenarios Included

The `sales_api.feature` file includes 11 sample scenarios:

1. **SM_API_01** - Get all sales (200 OK)
2. **SM_API_02** - Get sale by ID (200 OK)
3. **SM_API_03** - Create sale with valid data (201 Created)
4. **SM_API_04** - Update existing sale (200 OK)
5. **SM_API_05** - Delete sale (204 No Content)
6. **SM_API_06** - Invalid quantity zero (400 Bad Request)
7. **SM_API_07** - Invalid negative quantity (400 Bad Request)
8. **SM_API_08** - Missing required field (400 Bad Request)
9. **SM_API_09** - Non-existent sale (404 Not Found)
10. **SM_API_10** - Unauthorized access (401 Unauthorized)
11. **SM_API_11** - Non-admin user forbidden (403 Forbidden)

## Best Practices

1. **Always use authentication** - Use the provided auth methods for secure endpoints
2. **Validate response structure** - Check status codes, content types, and response bodies
3. **Test error scenarios** - Include negative test cases for validation
4. **Keep tests independent** - Each test should be able to run standalone
5. **Use meaningful tags** - Tag tests for easy filtering and execution
6. **Log requests/responses** - Already configured in BaseApiClient for debugging

## Troubleshooting

**Issue: Tests fail with connection refused**

- Ensure the backend application is running on the configured URL
- Check `serenity.conf` for correct API base URL

**Issue: Authentication fails**

- Verify credentials in `serenity.conf` match your backend
- Check if the API uses a different auth mechanism (JWT, OAuth, etc.)

**Issue: Tests pass but no data returned**

- Ensure database has test data
- Check API endpoint paths match your backend routes

## Next Steps

1. Update the API base URL in `serenity.conf` to match your actual backend
2. Adjust authentication mechanism if not using Basic Auth
3. Add more test scenarios based on your requirements
4. Create API clients for other modules (Plants, Users, etc.)
5. Integrate with CI/CD pipeline

## Support

For questions or issues, refer to:

- [Serenity BDD Documentation](https://serenity-bdd.github.io/theserenitybook/latest/index.html)
- [REST Assured Documentation](https://rest-assured.io/)
- Project README.md
