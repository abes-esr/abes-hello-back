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

Voir https://github.com/abes-esr/abes-hello-docker qui propose les configurations docker pour déployer en local ou sur des env de dev, test ou prod.

### Publier une nouvelle release de l'application

Se référer à la procédure commune à toutes les applications opensource de l'Abes ici :  
https://github.com/abes-esr/abes-politique-developpement/blob/main/01-Gestion%20du%20code%20source.md#publier-une-nouvelle-release-dune-application
