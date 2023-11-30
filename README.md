# PSOFT Scrum Board

## Introduction

This application was developed as a final project for the "Software Project" class at UFCG.

## Running the Application

### Prerequisites

- [Java](https://www.oracle.com/java/technologies/javase-downloads.html) installed
- [Maven](https://maven.apache.org/download.cgi) installed

### Getting Started

1. Clone the repository:

    ```bash
    git clone https://github.com/luisaadanttas/PSOFT-scrumboard.git
    cd PSOFT-scrumboard
    ```

2. Run the Spring Boot application:

    ```bash
    ./mvnw spring-boot:run
    ```

3. Access the Swagger UI:

    Open your web browser and navigate to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to interact with the API.

## Additional Information

- The application will be running at [http://localhost:8080](http://localhost:8080).
- The Swagger UI provides a user-friendly interface to explore and test the API endpoints.

## Project Details

- This project was developed as a final project for the class "Software Project" at the Universidade Federal de Campina Grande (UFCG).

## Notes

- The project includes Maven Wrapper files in the repository, allowing you to use the Maven Wrapper without installing Maven separately.
- If you encounter any issues, ensure that you have Java and Maven installed.
- If you make changes to the Maven configuration, commit the updated Maven Wrapper files to your version control system:

    ```bash
    git add .mvn/wrapper/*
    git commit -m "Update Maven Wrapper files"
    ```
