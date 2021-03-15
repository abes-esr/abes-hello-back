FROM maven:3-jdk-11 as build
WORKDIR /workspace/app
 
COPY mvnw .
#COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
 
COPY src src
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
 
FROM openjdk:11-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","FULL_NAME_OF_SPRING_BOOT_MAIN_CLASS"]
