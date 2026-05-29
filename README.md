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
    ![Alt Text](./images/docker-push.png)
    
- **Docker compose**
  To run multi-container applications
    - Create docker-compose.yaml file
    - Run all the containers at once using ```docker compose up``` command

      ![Alt Text](./images/docker-compose-up.png)

      ![Alt Text](./images/docker-desktop.png)

  - Stop all the containers at once using ```docker compose down``` command

    ![Alt Text](./images/docker-compose-down.png)

### Section 6: Configuration Management in Microservices

- **Challenges:**
  - How do we separate the configuration/properties from the microservices so that the same docker image can be deployed in different environments
  - How do we inject the configuration/properties needed by the microservice during the startup of the service
  - How do we maintain configuration/properties in a centralized repository along with versioning

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