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
      - Generate the docker image using the command: ```docker build <dockerfile-path> -t <author>/<project-name>:<tag-name>```
      - Run the docker image

    - Method 2: Using buildpacks (Loans Microservice, but it is NOT WORKING, so temporarily using Google Jib to create the image)
      - Paketo buildpacks (developed by Heroku and Pivotal)
      - No need to worry about the security, performance, compressing of the image...buildpacks handles it
      - Add image instruction in pom.xml
      - Create docker image using the command: ```mvn spring-boot:build-image```
  
    - Method 3: Using Google Jib (Cards Microservice)
      - Jib builds optimized Docker and OCI images for your Java applications without a Docker daemon - and without deep mastery of Docker best-practices.
      - Can be used to create imges, even if we don't have Docker installed on our local system
      - It is only for Java based project
      - This will be used throughout this course
      - Create docker image using the command: ```mvn compile jib:dockerBuild```

- **Running the docker images as containers**

    ```bash
    docker run -d -p 8090:8090 ldeepak/loans:s4
    docker run -d -p 8080:8080 ldeepak/accounts:s4
    docker run -d -p 9000:9000 ldeepak/cards:s4
    ```

- **Listing the images**

    ![Alt Text](./images/list-images.png)
    
- **Listing the containers**

    ![Alt Text](./images/list-containers.png)
  
- **Pushing the docker images to docker repository using Docker desktop UI**

    ![Docker-image-push-ui](./images/docker-push.png)

    OR

    Using Command: ```docker image push docker.io/<account-name>/<image>:<tag>```

    ![Docker-image-push-cmd](./images/image-push.png)

    
- **Docker compose**
  To run multi-container applications
    - Create docker-compose.yaml file
    - Run all the containers at once using ```docker compose up -d``` command

      ![Alt Text](./images/docker-compose-up.png)

      ![Alt Text](./images/docker-desktop.png)

  - Stop all the containers at once using ```docker compose down``` command

    ![Alt Text](./images/docker-compose-down.png)

### Section 6: Configuration Management in Microservices

- **Challenges:**
  - How do we separate the configuration/properties from the microservices so that the same docker image can be deployed in different environments ? <br>(Answer: Configuring Spring Boot with properties and profiles)

  - How do we inject the configuration/properties needed by the microservice during the startup of the service ? <br>(Answer: Applying external configuration with Spring Boot)

  - How do we maintain configuration/properties in a centralized repository along with versioning <br>(Answer: Implementing a configuration server with Spring Cloud Config server (RECOMMENDED))

- **Solutions:**
  - **Configuring Spring Boot with properties and profiles**
    - **Properties:**
      - Using @Value Annotation
      - Using Environment interface
      - Using @ConfigurationProperties (RECOMMENDED as it avoids hard coding property keys)
    - **Profiles:**
      - The default profile is active. But we can create another profiles for each environment and activate it based on our requirements

  - **Applying external configuration with Spring Boot**
    - **Command line arguments:**
      - Command-line args are automatically converted to key/value pairs
      - Has Highest precedence
      - Ex: ```java -jar <jar-file-name> --key=value```<br><br>

        ![Alt Text](./images/cmd-args.png)

    - **JVM system properties:**
      - It is prefixed with -D
      - Lower precendence than the command line args, but greater than application property files
      - Ex: ```java -Dkey=value -jar <jar-file-name>```<br><br>

        ![Alt Text](./images/vm-options.png)

    - **Environment variables:**
      - Universally supported
      - Lower precedence than the JVM properties, but greater than application property files
      - Ex: ```KEY=value java -jar <jar-file-name>```<br><br>

        ![Alt Text](./images/env-vars.png)

      <br>

      So the order or precedence is:

      Command line args (Highest) > JVM properties > Environment variables > Application property files (Lowest)

      Drawbacks:
      - Involves executing separate commands and manually setting up the application, which can introduce potential errors during deployment
      - How do we know which configurations were used in a release as there is no tracking of revisions
      - All the configurations are part of the source code which is not recommended
      - No control of access to configuration data
      - Neither properties nor environment variables support encryption of the configuration
      - After modifying the configuration, the application needs to be restarted


  - **Implementing a configuration server with Spring Cloud Config server (RECOMMENDED):**<br><br>

    ![Spring Cloud](./images/spring-cloud.png)
    <br><br>
    
    A centralized configuration server with Spring Cloud Config provides server and client-side support for externalized configuration in a distributed system

    Centralized configurations have two core elements:
    - **Central repository** or a data store where properties are stored
        
        It could be:

      - **File system/classpath**: Configurations are stored on the server file system

      - **Github (RECOMMENDED)**: Configurations are hosted on a Git repo

      - **Database**
    
    - **A server** that reads the config data from the central repository
      
      - Create a new project and add dependencies for Config server (spring-cloud-config-server)

      - For individual microservices, add dependency for Config client (spring-cloud-starter-config)<br><br>
    
    **Refresh Configurations**
    
    Microservice will get the configurations from Config Server at the startup only. But what if the configurations are changed in the Config Server and you want those changes to be reflected in the microservice without restarting it?<br><br>

    Solutions: 
      - **Using Refresh Actuator**<br><br>

        ![Refresh actuator](./images/refresh.png)<br><br>

        - Add actuator dependency (spring-boot-starter-actuator) in pom.xml of the microservices
        - Enable management acuator endpoints for the microservices by adding properties in application.yaml of the microservices
        - Add @RefreshScope annotation to the controller to refresh the configuration properties at runtime without restarting the application
        - Make a POST call to ```/actuator/refresh``` actuator endpoint to refresh the config changes in the microservice<br><br>

        Drawback: 
        We need to make the call manually. If there are 100s of microservices, then making this call manually is cumbersome.<br><br>
    
      - **Using Spring Cloud Bus**<br><br>

        ![Bus Refresh actuator](./images/bus-refresh.png)<br><br>
        
        You need to add Spring Cloud Bus along with a message broker like RabbitMQ or Kafka.
        
        RabbitMQ is a powerful, enterprise grade open source messaging and streaming broker that enables efficient, reliable and versatile communication for applications — perfect for distributed microservices, real-time data, and IoT.<br><br>

        - Add actuator dependency (spring-boot-starter-actuator) in pom.xml of the microservices
        - Enable management acuator endpoints for the microservices by adding properties in application.yaml of the microservices
        - Run rabbitmq as Docker container in local system:
          
          Docker image: https://hub.docker.com/_/rabbitmq

          ```docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management```

        - Add ```spring-cloud-starter-bus-amqp``` in pom.xml of the microservices
        - Add rabbitmq connection properties in application.yaml of the microservices
        - Make a POST call to ```/actuator/busrefresh``` actuator endpoint on any microservice to communicate the config changes to all the microservices registered with rabbitmq<br><br>

        Drawback: 
        Even tough we can communicate the message between multiple microservices with a single API call. But still we have to invoke it manually.<br><br>

      - **Using Spring Cloud Config Monitor**<br><br>

        ![Monitor](./images/monitor.png)<br><br>

        - Spring cloud config offers the monitor library, which enables the triggering of configuration change events in the config service. By exposing the ```/monitor``` endpoint, it facilitates the propagation of these events to all listening application via the bus. The Monitor library allows push notifications from popular code repository providers such as GitHub, GitLab, Bitbucket. You can configure webhooks in these services to automatically send a POST request to the config server after each new push to the configuration repository.

        - Add ```spring-cloud-config-monitor``` and ```spring-cloud-starter-bus-amqp``` in pom.xml of the config server

        - Enable management acuator endpoints for the microservices by adding properties in application.yaml of the config server

        - Add rabbitmq connection properties in application.yaml of the config server

        - Create a Github webhook: https://github.com/Deepak-147/microservices-config/settings/hooks/new

          - Whenever there are any updates in the git repo, the hook will invoke our configured url.

          - The url we want to configure is our /monitor endpoint (http://localhost:8071/monitor). 
            
            But since it is a localhost endpoint, the hook will not be able to resolve this and will fail. For this purpose we will be using a service like Hookdeck (https://hookdeck.com/), which will give us a public url

          - Setup Hookdeck on your local (https://console.hookdeck.com/)

          - Finally execute this command: ```hookdeck listen 8071 Source --cli-path /monitor```

            ![Alt Text](./images/hookdeck-cli.png)

          - Take the public url and add it to the webhook

            ![Alt Text](./images/github-add-webhook.png)

          - Now whenever there is any update to the confiurations in the git repo, the webhook will get triggered and our configured url is hit

            ![Alt Text](./images/github-webhook-requests.png)

            ![Alt Text](./images/hookdeck-cli-requests.png)

### Section 7: MySQL DB integration
  - To simplify the future development, removing the bus refresh functionality as it requires running rabbitmq and requires creating multiple container.

    Removed ```spring-cloud-starter-bus-amqp``` in pom.xml of the microservices.

    Removed rabbitmq connection properties in application.yaml of the microservices.

  - **Running MySQL DB as Docker container**

    We will be using Docker image to run mysql in a docker container. Also we require 3 databases one for each of the service.

    **AccountsDB:**
    
    ```docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql```

    Breakdown of the command:

    - ```-p 3306:3303```: Maps host port 3306 -> container port 3306 (MySQL's default port). This lets your local machine connect to MySQL inside the container.

    - ```--name accountsdb```: Assigns a friendly name to the container, instead of a random one.

    - ```-e```: Sets environment variable inside the container

    - ```mysql```: The Docker image to use (pulled from Docker Hub if not present in local)

    **LoansDB:**

    ```docker run -p 3307:3306 --name loansdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=loansdb -d mysql```

    Notice the use of different host port number 3307, since 3306 is already consumed by accountsdb

    **CardsDB:**

    ```docker run -p 3308:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb -d mysql```

    Notice the use of different host port number 3308, since 3307 is already consumed by loansdb

    ![MySQL DB Containers](./images/mysql-db-containers.png)

  - We will use SQLectron GUI client to work with SQL and add 3 connections one for each DB.

    ![SQLectron-connection-add](./images/sqlelectron-add-connection.png)

    <br>

    ![SQLectron](./images/sqlelectron.png)

  - Remove H2 DB dependency from Microservices and add MySQL dependency

  - Remove H2 related properties in application.yaml of microservices and update mysql connection url

  - Now run all the services, along with config server. The schema.sql should execute and create the schema for the tables.

    ![schema](./images/schema.png)

  > ⚠️ **Warning:** DO NOT delete the containers. You can stop them, but do not delete them, as you will lose all the data.

  - Verify the API endpoints locally

  > **Note:** We will NOT be pushing s7 to DockerHub. So below steps are not required. <br>This section was introduction to db integration. Section 8 will be copied from Section 6 with H2 db.

  - Generate Docker images after updating the image tag for each service from s6 to s7

  - Update Docker files (common-config.yaml and docker-compose.yaml) with below changes:
    - Remove rabbitmq configs and dependencies
    - Add db services
    - Microservices should now depend on db service health

### Section 8: Service Discovery & Service Registration in Microservices

  - **Challenges:**
    
    - How do services locate each other inside a network? <br>(Answer: Service discovery)

    - How do the new service instance enter into the network? <br>(Answer: Service registration)

    - How do a specific service information is shared across the network and how to load balance b/w multiple microservice instances ? <br>(Answer: Load balancing)

    ![cloud-native](./images/cloud-native-problem-solutions.png)
    
    <br>

    ![central-server](./images/central-server.png)

    <br>

    ![client-side-sd-sr-lb](./images/client-side-sd-sr-lb.png)

    <br>

    ![sd-sr-lb](./images/sd-sr-lb.png)

    <br>

    ![sd](./images/sd.png)

    <br>

    ![lb](./images/lb.png)

    <br>

    ![spring-cloud-support](./images/spring-cloud-support.png)


  - **Setup Eureka Server**:

    - Download a new project from spring initializer (start.spring.io) with following dependencies:

      - ```spring-cloud-starter-netflix-eureka-server```
      - ```spring-cloud-starter-config```
      - ```spring-boot-starter-actuator```
    
    - Configure properties in application.yml file

    - Add @EnableEurekaServer annotation to main class

    - Build and run eureka server on ```http://localhost:8070/```

      ![eureka-dashboard](./images/eureka-dashboard.png)

  - **Setup Eureka Client (Individual microservices)**:

    - Add ```spring-cloud-starter-netflix-eureka-client``` dependency in pom.xml
    
    - Configure properties in application.yml file

    - Build and run eureka server on ```http://localhost:8070/```

      ![eureka-dashboard-with-clients](./images/eureka-dashboard-with-clients.png)

    - Graceful Shutdown using actuator endpoint: ```http://localhost:8080/actuator/shutdown```. This will de-register our services from Eureka and terminate them.

  - **Communication between Microservices**:

    - Add ```spring-cloud-starter-openfeign``` dependency inside pom.xml of the microservice that wants to connect to other microservice (in our case inside accounts)

    - Add interface for feign client (like CardsFeignClient and LoansFeignClient). Copy the method signature from respective microservice. Copy necessary DTO classes as required.

    - Write business logic in the service layer and expose the endpoint as rest controller.

    - ```http://localhost:8080/api/fetchCustomerDetails?mobileNumber=``` is one endpoint which fetches customer details using mobile number by combining data from cards and loans microservice. Internally it communicates with cards and loans microserives using feign client.

  - **Eureka Self preservation**

    ![eureka-self-1](./images/eureka-self-1.png)
    <br><br>
    ![eureka-props](./images/eureka-props.png)
    <br><br>
    ![eureka-warning](./images/eureka-warning.png)
    <br>

  - Generate Docker images after updating the image tag for each service from s6 to s8

    ![s8-images](./images/s8-images.png)

  - Push the images to Docker Hub

    ![Docker-hub](./images/docker-hub.png)

  - Update Docker files (common-config.yaml and docker-compose.yaml) to add eureka server

  - Use Docker compose to Run all the containers at once

  - Verify the API endpoints


### Section 9: Gateway, Routing and Cross cutting concerns

  - **Challenges:**

    - How do we maintain a single entrypoint into microservice network?

    - How do we handle cross cutting concerns like logging, auditing, tracing and security across multiple microservices?

    - How do we route based on custom requirements <br>(Answer to all above: API gateway or Edge server)

    ![gateway-functions](./images/gateway-functions.png) <br><br>

    ![gateway-architecture](./images/gateway-architecture.png)
  
  - **Setup Gateway Server**:

    - Download a new project from spring initializer (start.spring.io) with following dependencies:

      - ```spring-cloud-starter-gateway-server-webflux```
      - ```spring-cloud-starter-netflix-eureka-client```
      - ```spring-cloud-starter-config```
      - ```spring-boot-starter-actuator```
    
    - Configure properties in application.yml file

    - Run the services one by one in order (config server < eureka server < individual microservices < gateway server)

    - Check instances of services registered on Eureka Server```http://localhost:8070/```

      ![eureka-with-gateway](./images/eureka-with-gateway.png)

  - **Custom routing:**

    - We can create a bean which returns RouteLocator and add custom route configuration to this.

  - **Tracing and Logging:**

    This helps in tracing the request across multiple services.

    - Add correlation-id header to requests and responses <br>
    - Add logger statements for debugging <br>
  
  - Generate Docker images after updating the image tag for each service from s8 to s9

  - Push the images to Docker Hub

  - Update Docker files (common-config.yaml and docker-compose.yaml) to add gateway server

  - Use Docker compose to Run all the containers at once

  - Verify the API endpoints

### Section 10: Resiliency in Microservices

  - **Challenges:**

    - How do we avoid cascading failures?

    - How do we handle failures gracefully with fallbacks?

    - How to make our services self-healing?

      (Answer to all above: Resilience4J, a fault-tolerance library for Java)

    ![resilience4j](./images/resilience4j.png) <br>

    ![circuit-breaker-1](./images/circuit-breaker-1.png) <br>

    ![circuit-breaker-2](./images/circuit-breaker-2.png) <br>

    ![circuit-breaker-3](./images/circuit-breaker-3.png) <br>
  
  - Circuit breaker pattern in Gateway server

    - Add ```spring-cloud-starter-circuitbreaker-reactor-resilience4j``` dependency in pom.xml

    - Add ```.circuitBreaker()``` and ```.setFallbackUri()``` in the main application file

    - Configure properties in application.yaml

    - Actuator urls:
      
      - Circuit breaker configs: ```http://localhost:8072/actuator/circuitbreakers```

      - Circuit breaker events: ```http://localhost:8072/actuator/circuitbreakerevents?name=accountsCircuitBreaker```

    - Intentionally fail an API by introducing a break-point to never let it return. Keep a check at the above actuator urls and notice the circuit breaker state transitions.

  - Circuit breaker pattern in Accounts microservice

    - Add ```spring-cloud-starter-circuitbreaker-resilience4j``` dependency in pom.xml (not sure why the reactor one is not used)

    - Configure properties in application.yaml

    - Update feign clients to support fallback.

    - Now call ```http://localhost:8072/eazybank/accounts/api/fetchCustomerDetails?mobileNumber=```. It returns the data with all the card and loan details

    - What happens if the service is not available or is down? (Answer: Fallback)
    
      For this we can intentionally stop cards or loans service, this time the fallback is triggered and ```null``` response is passed for respective service call. The overall response is still success, just that the cards or loans data will be empty. So the fallback works and the service is not impacted.



    