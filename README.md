# iCritic-users-service

**Java SpringBoot Project**

## Description

The icritic application is a platform to rate and make comments about movies. The project aims to enhance my Java knowledge and experience by developing microservices using Java 11, SpringBoot 2.7.11, PostgreSQL and the cloud. The application will serve as a learning platform where I can implement various Java concepts, design patterns, and best practices.

The icritic-users-service is a microservice from this application which aims to manage all user related requests.
## Features

- User registration and authentication
- CRUD operations on various entities
- RESTful API endpoints
- Database integration with PostgreSQL
- Cloud integratino with Microsoft Azure storage
- Error handling and exception management
- Logging and debugging capabilities
- Unit testing
- Integration testing

## Technologies Used

- Java 11
- SpringBoot 2.7.11
- PostgreSQL

## Prerequisites

To run this project locally, you will need the following:

- Java 11 JDK installed
- Maven build tool
- PostgreSQL database server

## Installation

1. Clone this repository:

2. Open the project in your preferred IDE.

3. Update the database configuration in the application.yaml file.

4. Use the schema file found within the sql/schema.sql directory to establish the database. 

5. If you plan to call any endpoints which upload files you'll also need to set the azure env variable for your storage.

6. Build the project using Maven. Run the following command from the project's root directory:
   ```bash
   mvn clean install
7. Once the build is successful, you can run the application using the following command:
   ```bash
   mvn spring-boot:run

# Contributing
This project is a personal endeavor, and contributions are not accepted at the moment but if you have any suggestions for improvement please feel free to contact me.
