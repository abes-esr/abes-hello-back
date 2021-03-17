FROM maven:3-jdk-11 as build
WORKDIR /applis
COPY abes-hello-back ./abes-hello-back
#COPY batch .
COPY core ./core
COPY pom.xml .
COPY web ./web
## RUN mvn dependency:go-offline
## RUN mvn package -DskipTests
WORKDIR /applis/web
RUN mvn -Dmaven.test.skip=true clean package
## RUN mvn -f pom.xml clean package
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /applis/web/target/*.jar)

FROM openjdk:11
COPY --from=build /applis/web/target/*.jar /usr/local/lib/demo.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]
#VOLUME /tmp
#ARG DEPENDENCY=/applis/web/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /applis/lib
#COPY --from=build ${DEPENDENCY}/META-INF /applis/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /applis
#ENTRYPOINT ["java","-cp","applis:applis/lib/*","FULL_NAME_OF_SPRING_BOOT_MAIN_CLASS"]
