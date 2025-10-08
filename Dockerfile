###
# Image pour la compilation
FROM maven:3-jdk-11 as build-image
WORKDIR /build/
# Installation et configuration de la locale FR
RUN apt update && DEBIAN_FRONTEND=noninteractive apt -y install locales
RUN sed -i '/fr_FR.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen
ENV LANG fr_FR.UTF-8
ENV LANGUAGE fr_FR:fr
ENV LC_ALL fr_FR.UTF-8


# On lance la compilation Java
# On débute par une mise en cache docker des dépendances Java
# cf https://www.baeldung.com/ops/docker-cache-maven-dependencies
COPY ./pom.xml /build/pom.xml
COPY ./core/pom.xml /build/core/pom.xml
COPY ./web/pom.xml /build/web/pom.xml
COPY ./batch/pom.xml /build/batch/pom.xml
RUN mvn verify --fail-never
# et la compilation du code Java
COPY ./core/   /build/core/
COPY ./web/    /build/web/
COPY ./batch/  /build/batch/
RUN mvn --batch-mode \
        -Dmaven.test.skip=false \
        -Duser.timezone=Europe/Paris \
        -Duser.language=fr \
        package



###
# Image pour le module batch
# Remarque: l'image openjdk:11 n'est pas utilisée car nous avons besoin de cronie
#           qui n'est que disponible sous centos/rockylinux.
FROM rockylinux:8 as batch-image
WORKDIR /scripts/
# systeme pour les crontab
# cronie: remplacant de crond qui support le CTRL+C dans docker (sans ce système c'est compliqué de stopper le conteneur)
# gettext: pour avoir envsubst qui permet de gérer le template tasks.tmpl
RUN dnf install -y cronie gettext && \
    crond -V && rm -rf /etc/cron.*/*
COPY ./docker/batch/tasks.tmpl /etc/cron.d/tasks.tmpl
# Le JAR et le script pour le batch de LN
RUN dnf install -y java-11-openjdk
COPY ./docker/batch/abes-hello-batch1.sh /scripts/abes-hello-batch1.sh
COPY --from=build-image /build/batch/target/*.jar /scripts/abes-hello-batch1.jar

COPY ./docker/batch/docker-entrypoint.sh /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["crond", "-n"]



###
# Image pour le module web
FROM tomcat:9-jdk11 as web-image
COPY --from=build-image /build/web/target/*.war /usr/local/tomcat/webapps/ROOT.war
CMD [ "catalina.sh", "run" ]

# pour avoir le manager de tomcat
# https://hub.docker.com/r/bitnami/tomcat
# FROM bitnami/tomcat:9.0 as web-image
# COPY --from=build-image /build/web/target/*.war /opt/bitnami/tomcat/webapps/ROOT.war

#FROM eclipse-temurin:11-jre as back-server
#COPY --from=web-build /app/web/target/*.jar /app/licences-nationales.jar
#ENTRYPOINT ["java","-jar","/app/licences-nationales.jar"]
