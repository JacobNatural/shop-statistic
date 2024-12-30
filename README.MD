
# Shop Statistic Application

![Static Badge](https://img.shields.io/badge/Build-passing-flat)
[![Static Badge](https://img.shields.io/badge/Coverage-99%25-flat)](https://jacobnatural.github.io/shop-statistic/jacoco/index.html)
[![Static Badge](https://img.shields.io/badge/docs-blue)](https://jacobnatural.github.io/shop-statistic/apidocs/index.html)
## Overview
The Shop Application is a backend solution built using Spring Boot. It leverages Spring Security with JWT tokens 
to provide robust authentication and authorization. The application allows users to manage their customers, products, 
and orders while providing insightful statistics. This solution is ideal for businesses looking to streamline order 
management and gain valuable data-driven insights.

## Technology Stack

- Backend: Java, Spring Boot

- Security: Spring Security with JWT (JSON Web Token)

- Database: (MySQL)

- Build Tool: Maven 

- Other Tools: Hibernate, Lombok

- Containerization: Docker

## Getting Started


### Prerequisites
- **Java Development Kit (JDK) 21+**
- **Apache Maven 3.9.6+**
- **Docker**

### Cloning the repository:
- To clone the repository and navigate into the project directory, run:

```bash
git clone https://github.com/JacobNatural/shop-statistic.git
cd shop-statistic
```

### Running the application:
- To build the application and run it using Docker Compose:
```bash
docker-compose up -d
```

### Admin User Credentials
- After running the application, an admin user is automatically created with full access to all sections and the ability to manage other users. You can log in with the following credentials:
```bash
- Username: admin
- Password: admin123
``` 
- Use these credentials to access the admin panel and start managing users, roles, products, and orders. Admin has full control over the system, including assigning roles to other users.

### Running Tests
- To execute the tests, ensure that your database configuration for tests is set up in 
the docker-compose-test.yml file and then run:
```bash
docker-compose -f docker-compose-test.yml up -d 
mvn clean test
```

### Access to the Swagger UI:
```bash
http://localhost:8080/swagger-ui/index.html
```
## How It Works

- **During application startup, an `Admin` user is automatically created**, who has access to all endpoints of the application. This user can also manage other users and modify their roles.

- **Every newly registered user is automatically assigned the `Worker` role**, which grants access to the **Orders** and **Statistics** sections only.

### Available Roles and Their Permissions:

- **Admin**:  
  The **Admin** has full access to all sections and endpoints of the application. The **Admin** is the only user who can modify other users' roles and assign them to different roles (e.g., converting a **Worker** into a **Leader**). The **Admin** can manage **Clients**, **Products**, **Orders**, and **Statistics**.

- **Leader**:  
  The **Leader** role is assigned by the **Admin**. A **Leader** has access to **Clients**, **Products**, **Orders**, and **Statistics** sections. They can manage **Products**, **Clients**, and **Orders**, and also view business statistics. However, a **Leader** cannot modify roles of other users.

- **Worker**:  
  A **Worker** has limited access. They can view and manage **Orders** and access **Statistics**. However, they do not have permissions to manage **Clients** or **Products**. The **Worker** role is assigned by default upon registration, and only an **Admin** can change it.
## Contributing
We welcome contributions to improve the Shop Statistic Application. Here's how you can contribute:

1. Fork the repository on GitHub.
2. Make enhancements or fix issues in your forked copy.
3. Submit a pull request to merge your changes into the main repository.

Please ensure your code adheres to our coding standards and is thoroughly tested before submitting a pull request.

