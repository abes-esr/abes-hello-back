# abes-hello-back

[![build-test-pubtodockerhub](https://github.com/abes-esr/abes-hello-back/actions/workflows/build-test-pubtodockerhub.yml/badge.svg)](https://github.com/abes-esr/abes-hello-back/actions/workflows/build-test-pubtodockerhub.yml) [![Docker Pulls](https://img.shields.io/docker/pulls/abesesr/abes-hello.svg)](https://hub.docker.com/r/abesesr/abes-hello/)

Application exemple type "Hello World" qui illustre la politique de développement de l'Abes avec des modèles pour la partie back : API et batch.

Les fonctionalités mise en oeuvre dans ``abes-hello-back`` sont :
1. Un service web RESTful permettant d'obtenir un message public pour tous les utilisateurs et un message privé pour les utilisateurs authentifiés sur le  service
2. Un service de tâches de fond 

Voir aussi le front ici : https://github.com/abes-esr/abes-hello-front

## Développements

### Utilisation en local

#### Module web (l'API)

Dans l'environnement local, il convient d'utiliser la commande :

    mvn clean package   
    
puis d'exécuter le service web selon :
 
    cd web
    mvn spring-boot:run
    
Il existe 3 profils d'environnement différents que sont 'dev', 'test' et 'prod'. Pour exécuter un environnement en particulier : 

    mvn clean package -Pdev
    cd web
    mvn spring-boot:run -Pdev   

#### Module batch

Batch Spring de base qui illustre l'utilisation d'une tasklet et d'un chunk.
La tasklet lit un fichier texte et place son contenu dans une variable.
La variable est passée au chunk gràce à l'ExecutionContext (accessible dans les Tasklet et les classes du chunk en implémentant l'interface StepExecutionListener).
Le chunk est composé de 3 parties, reader, processor et writer.
Il est configuré pour traiter les données par paquet de 10.
Le processor fait appel à une méthode externe qui illustre l'utilisation du retry (on peut le configurer via l'annotation @Retry, elle permet d'essayer plusieurs fois un traitement qui est susceptible de renvoyer une erreur (par exemple un appel à un service web ou une connexion à une base de données).

TODO : expliquer comment compiler et tester en local

### Utilisation en local avec Docker

#### Génération des images docker

Les images docker sont générées automatiquement à chaque ``git push`` par la [chaîne d'intégration continue](https://github.com/abes-esr/abes-hello-back/actions/workflows/build-test-pubtodockerhub.yml). Les images suivantes sont [disponibles sur dockerhub](https://hub.docker.com/r/abesesr/abes-hello/tags) (idem pour ``batch``) :
- ``abesesr/abes-hello:main-web`` : l'image du dernier git push sur la branche ``main`` ("tag glissant")
- ``abesesr/abes-hellos:develop-web`` : l'image du dernier git push sur la branche ``develop`` ("tag glissant")
- ``abesesr/abes-hello:X.X.X-web`` : l'image dont le n° de version est ``X.X.X``
- ``abesesr/abes-hello:latest-web`` : l'image de la dernière version publiée

Il est aussi possible de générer ces images localement en tapant par exemple les commandes suivantes :
```bash
cd abes-hello-back/
docker build . --target web-image -t abesesr/abes-hello:develop-web
docker build . --target batch-image -t abesesr/abes-hello:develop-batch
```

Remarque : utilisation de ``--target`` car le Dockerfile crée 3 images (multi-stage),
- une première (``build-image``) pour la compilation de tout abes-hello-back (core, web et batch),
- une seconde (``batch-image``) pour l'image du batch qui contient la crontab avec le JAR du batch de abes-hello,
- et une troisième (``web-image``) pour l'image du web qui contient tomcat9 avec le WAR de abes-hello.

#### Démarrage des conteneurs docker

Pour un déploiement dans le SI de l'Abes, il faut se référer aux configurations suivantes :
https://git.abes.fr/depots/abes-hello-docker/ (TODO)

Pour le déployer en local sur sa machine, une fois la génération des images terminée (cf section au dessus), voici les commandes que l'on peut utiliser (TODO, cette partie pourrait être améliorée en proposant un ``docker-compose.yml``):
```bash
cd abes-hello-back/

docker run -d \
  --name abes-hello-web \
  -e SPRING_PROFILES_ACTIVE=localhost \
  -e SPRING_CONFIG_LOCATION=/config/ \
  -v $(pwd)/web/src/main/resources/application-localhost.properties:/config/application-localhost.properties \
  -p 8080:8080 \
  abesesr/abes-hello:develop-web

docker run -d \
  --name abes-hello-batch \
  -e ABESHELLO_BATCH_CRON="0 * * * *" \
  -e ABESHELLO_BATCH_AT_STARTUP="1" \
  -e SPRING_PROFILES_ACTIVE=localhost \
  -e SPRING_CONFIG_LOCATION=/config/ \
  -v $(pwd)/batch/src/main/resources/application-localhost.properties:/config/application-localhost.properties \
  abesesr/abes-hello:develop-batch
```
Les fichiers de configurations spring sont injectés via un volume docker. Vous pouvez les modifier [ici pour le web](https://github.com/abes-esr/abes-hello-back/blob/main/batch/src/main/resources/application-localhost.properties) et [ici pour le batch](https://github.com/abes-esr/abes-hello-back/blob/main/batch/src/main/resources/application-localhost.properties) et relancer le conteneur pour qu'ils soient pris en compte.


Pour consulter les logs des deux conteneurs :
```bash
docker logs -n 100 -f abes-hello-web
docker logs -n 100 -f abes-hello-batch
```



## Publier une nouvelle release de l'application

Se référer à la procédure commune à toutes les applications opensource de l'Abes ici :  
https://github.com/abes-esr/abes-politique-developpement/blob/main/01-Gestion%20du%20code%20source.md#publier-une-nouvelle-release-dune-application
