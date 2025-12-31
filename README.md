# Microservices-using-Springboot
Microservices using springboot

## Project Structure

### Section 2: Basic Microservices
- Basic Spring Boot project strucuture
- Three independent microservices (Accounts, Cards, Loans)
- Defining entity structure
- Spring Data JPA for database operations, use of in-memory database like H2
- Creating DTOs
- Creating REST controllers
- Service layer implementation
- Repository layer
- Mapper classes for conversion between entity to dto and vice versa
- Exception handling
- Input validation
- Audit columns
- Swagger API documentation

### Section 4: Containerization of services using Docker
- Method 1: Using Dockerfile (Accounts Microservice)
  - Creating jar file using maven command
  - Writing instructions in Dockerfile
  - Generate the docker image
  - Run the docker image

- Method 2: Using buildpacks (Loans Microservice, but it is NOT WORKING, so temporarily using Google Jib to create the image)
  - Paketo buildpacks (developed by Heroku and Pivotal)
  - No need to worry about the security, performance, compressing of the image...buildpacks handles it
  - Add image instruction in pom.xml
  - Create docker image using : mvn spring-boot:build-image
  
- Method 3: Using Google Jib (Cards Microservice)
  - Jib builds optimized Docker and OCI images for your Java applications without a Docker daemon - and without deep mastery of Docker best-practices.
  - Can be used to create imges, even if we don't have Docker installed on our local system
  - It is only for Java based project
  - This will be used throughout this course