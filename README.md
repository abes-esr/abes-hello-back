# abes-hello-back

abes-hello-back est une application d'exemple qui a pour objectif d'illustrer la politique de développement au sein de l'ABES.
Les fonctionalités mise en oeuvre dans 'abes-hello-back' sont :
1. Un service web RESTful permettant d'obtenir un message public pour tous les utilisateurs et un message privé pour les utilisateurs authentifiés sur le  service.
2. Un service de tâches de fond 

## 1. Usage

Dans l'environnement local, il convient d'utiliser la commande :

    mvn clean package   
    
puis d'exécuter le service web selon :
 
    cd web
    mvn spring-boot:run
    
Il existe 3 profils d'environnement différents que sont 'dev', 'test' et 'prod'. Pour exécuter un environnement en particulier : 

    mvn clean package -Pdev
    cd web
    mvn spring-boot:run -Pdev   

## 2. Partie batch

Batch Spring de base qui illustre l'utilisation d'une tasklet et d'un chunk.
La tasklet lit un fichier texte et place son contenu dans une variable.
La variable est passée au chunk gràce à l'ExecutionContext (accessible dans les Tasklet et les classes du chunk en implémentant l'interface StepExecutionListener).
Le chunk est composé de 3 parties, reader, processor et writer.
Il est configuré pour traiter les données par paquet de 10.
Le processor fait appel à une méthode externe qui illustre l'utilisation du retry (on peut le configurer via l'annotation @Retry, elle permet d'essayer plusieurs fois un traitement qui est susceptible de renvoyer une erreur (par exemple un appel à un service web ou une connexion à une base de données).


