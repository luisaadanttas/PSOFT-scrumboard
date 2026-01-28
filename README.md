# PSOFT Scrum Board

## Introduction

This application was developed as a final project for the "Software Design" course.

## Running the Application

### Prerequisites

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) installed

### Getting Started

1. Clone the repository:

    ```bash
    git clone [repository URL]
    cd PSOFT-scrumboard
    ```

2. Run the Spring Boot application:

    ```bash
    ./mvnw spring-boot:run
    ```

    to test:
    ```bash
    mvn -Dtest=UserControllerTest test
    ```

3. Access the Swagger UI:

    Open your web browser and navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to interact with the API.

## Additional Information

- The application will be running at [http://localhost:8080](http://localhost:8080).
- The Swagger UI provides a user-friendly interface to explore and test the API endpoints.


## Notes

- The project includes Maven Wrapper files in the repository, allowing you to use the Maven Wrapper without installing Maven separately.
- If you encounter any issues, ensure that you have Java and Maven installed.
- If you make changes to the Maven configuration, commit the updated Maven Wrapper files to your version control system:

    ```bash
    git add .mvn/wrapper/*
    git commit -m "Update Maven Wrapper files"
    ```

