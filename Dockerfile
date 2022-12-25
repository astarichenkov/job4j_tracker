FROM maven:3.6.3-openjdk-15
RUN mkdir job4j_tracker
WORKDIR job4j_tracker
COPY . .
RUN mvn -DskipTests package
CMD ["java", "-jar", "target/memTracker.jar"]