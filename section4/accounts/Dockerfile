#Start with a base image containing Java runtime
FROM eclipse-temurin:21-jdk

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER eazybytes.com
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="ldeepak11"

# Add the application's jar to the image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# execute the application. This is the command to run the application: java -jar target/<your-jar-file>
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]