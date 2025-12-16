# abes-hello-back

[![build-test-pubtodockerhub](https://github.com/abes-esr/abes-hello-back/actions/workflows/build-test-pubtodockerhub.yml/badge.svg)](https://github.com/abes-esr/abes-hello-back/actions/workflows/build-test-pubtodockerhub.yml) [![Docker Pulls](https://img.shields.io/docker/pulls/abesesr/abes-hello.svg)](https://hub.docker.com/r/abesesr/abes-hello/)

Application backend à trois modules (Core, Web et Batch) qui illustre la politique de développement de l'Abes pour une API Web RESTful. L'application front se trouve sur le dépôt suivant : [Github abes-hello-front](https://github.com/abes-esr/abes-hello-front)

Les fonctionnalités misent en œuvre dans ``abes-hello-back`` sont :
1. Un service web RESTful permettant d'obtenir un message public pour tous les utilisateurs
2. Un service web RESTful permettant aux utilisateurs authentifiés sur le service de recevoir un message privé et de consulter l'historique des commandes passées
3. Un batch permettant de lire des données en base de données puis d'effectuer un traitement dessus 

Version: 2.0.1-SNAPSHOT

## Développements

### Module Core

### Module web (l'API)

### Module batch

SpringBatch de base qui illustre l'utilisation d'une tasklet et d'un chunk.
La tasklet lit un fichier texte et place son contenu dans une variable.
La variable est passée au chunk gràce à l'ExecutionContext (accessible dans les Tasklet et les classes du chunk en implémentant l'interface StepExecutionListener).
Le chunk est composé de 3 parties, reader, processor et writer.
Il est configuré pour traiter les données par paquet de 10.
Le processor fait appel à une méthode externe qui illustre l'utilisation du retry (on peut le configurer via l'annotation @Retry, elle permet d'essayer plusieurs fois un traitement qui est susceptible de renvoyer une erreur (par exemple un appel à un service web ou une connexion à une base de données).

Configuration du lancement du batch en local avec IntelliJ :

Avec IntelIJ : 
- Ajouter la variable : `spring.batch.job.name=jobTraitement` dans la configuration du lancement du batch `Environnment variables`   
- Si nécessaire, ajouter les variables `fr.abes.helloabes.batch.*` et/ou `fr.abes.helloabes.batch.BatchConfiguration` dans la configuration du lancement du batch `Code Coverage`

### Utilisation en local avec Docker

Démarrer l'application abes-hello-back sur votre poste local à l'aide du fichier Dockerfile situé à la racine du projet. 
Pour se faire, configurez d'abord votre IDE de manière à inclure cette option de démarrage (`run option` avec IntelIJ) : `-p 8081:8081`

Pour démarrer l'ensemble de l'application abes-hello sur votre poste local, voir le dépôt Github [Github abes-hello-docker](https://github.com/abes-esr/abes-hello-docker) qui propose les configurations docker de l'ensemble de l'API Web RESTful Abes-Hello pour déployer en local ou sur des env de dev, test ou prod.

### Publier une nouvelle release de l'application

Se référer au chapitre `publier-une-nouvelle-release-dune-application` de la [politique informatique de l'Abes](https://politique-informatique.abes.fr/docs/dev/development/source-code/#publier-une-nouvelle-release-dune-application)
