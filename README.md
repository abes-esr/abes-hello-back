# abes-hello-back

abes-hello-back est une application d'exemple qui a pour objectif d'illustrer la politique de développement au sein de l'ABES.
Les fonctionalités mise en oeuvre dans 'abes-hello-back' sont :
1. Un service web RESTful permettant d'obtenir un message public pour tous les utilisateurs et un message privé pour les utilisateurs authentifiés sur le  service.
2. Un service de tâches de fond 

## 1. Service web RESTful

Le service web RESTful permet d'exposer un message public de bienvenue et un message privé aux utilisateurs authentifiés sur le service.
Les échanges s'opèrent via des requêtes HTTP contenant des fichiers JSON dans le corps des requêtes (Body) et une clé 'Authorization' dans l'entête des requêtes pour les appels à des services sécurisés. 

### 1.1 Routes d'accès public
 
| Méthode | Route | Description | Entrées | Sorties | 
|:---------|:------|:-------------|:------|:------|
| GET | / | Retourne un message de bienvenue.|  |@Body `json {"response": "Hello from ABES - PUBLIC API PAGE" }` |
| POST | /register | Enregistrer un nouvel utilisateur. |@Body  `json { "userName" : "identifiant","passWord" : "monmotdepasse"}` | @Body `json {"id": "number","userName": "identifiant","passWord": "monmotdepassehash"}` |
| POST | /login | Service d'authentification d'un utilisateur. Ce service retourne un token. |@Body  `json { "userName" : "identifiant","passWord" : "monmotdepasse"}` | @Body `raw montoken` |

### 1.2 Routes d'accès privée
 
| Méthode | Route | Description | Entrés | Sorties | 
|:--------|:------|:------------|:------|:------|
| GET | /secured | Retourne un message privé. | @Header `Authorization = "Bearer montoken"` | @Body `raw Hello from Abes - Voici est private API` |


## 2. Partie batch

Batch Spring de base qui illustre l'utilisation d'une tasklet et d'un chunk.
La tasklet lit un fichier texte et place son contenu dans une variable.
La variable est passée au chunk gràce à l'ExecutionContext (accessible dans les Tasklet et les classes du chunk en implémentant l'interface StepExecutionListener).
Le chunk est composé de 3 parties, reader, processor et writer.
Il est configuré pour traiter les données par paquet de 10.
Le processor fait appel à une méthode externe qui illustre l'utilisation du retry (on peut le configurer via l'annotation @Retry, elle permet d'essayer plusieurs fois un traitement qui est susceptible de renvoyer une erreur (par exemple un appel à un service web ou une connexion à une base de données).


