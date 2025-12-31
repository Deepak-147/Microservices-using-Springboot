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
- **Three ways of creating docker images**
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

- **Creating docker images using one of the 3 ways**

- **Running the docker images as containers**

    ```bash
    docker run -d -p 8090:8090 ldeepak/loans:s4
    docker run -d -p 8080:8080 ldeepak/accounts:s4
    docker run -d -p 9000:9000 ldeepak/cards:s4
    ```

- **Listing the images**

    ![Alt Text](./section4/images/list-images.png)
    
- **Listing the containers**

    ![Alt Text](./section4/images/list-containers.png)
  
- **Pushing the docker images to docker repository using Docker desktop UI**
    ![Alt Text](./section4/images/docker-push.png)
    
- **Docker compose**
  To run multi-container applications
    - Create docker-compose.yaml file
    - Run all the containers at once using ```docker compose up``` command

      ![Alt Text](./section4/images/docker-compose-up.png)

      ![Alt Text](./section4/images/docker-desktop.png)

  - Stop all the containers at once using ```docker compose down``` command

    ![Alt Text](./section4/images/docker-compose-down.png)
