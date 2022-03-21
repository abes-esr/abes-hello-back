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



## Publier une nouvelle release de l'application

Pour publier une nouvelle release (version) de l'application, voici comment procéder:
1. Se rendre sur l'onglet "Actions" sur le dépôt github  
   ![image](https://user-images.githubusercontent.com/328244/159044287-67c7131f-8663-4452-b7fa-55aa8c695692.png)
2. Cliquer sur le workflow "Create release"  
   ![image](https://user-images.githubusercontent.com/328244/159044427-d36ae0d6-51cc-4f69-a855-097c162ba100.png)
3. Cliquez ensuite sur "Run workflow" sur la droite  
   ![image](https://user-images.githubusercontent.com/328244/159044539-57b57fba-15b8-440d-94e7-1ee859566a04.png)
4. Indiquez ensuite le numéro de la version à générer (doit respecter le sementic versionning) après avoir vérifié que votre numéro de version n'existe pas déjà dans la [liste des tags](./tags)  
   ![image](https://user-images.githubusercontent.com/328244/159044729-e9cc0d7a-abe3-401f-a246-84e577670493.png)
5. Validez et attendez que le build se termine dans le [workflow "build-test-pubtodockerhub"](./actions/workflows/build-test-pubtodockerhub.yml), ce qui aura pour conséquence  de générer et [publier sur dockerhub une image docker](https://hub.docker.com/r/abesesr/) ayant comme tag le numéro de version de votre release.
