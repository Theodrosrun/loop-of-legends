# Loop of Legends: The Multi-Snake Challenge

"Loop of Legends: The Multi-Snake Challenge" est un jeu de serpent en réseau où les joueurs contrôlent des serpents et essaient de survivre le plus longtemps possible. Ce projet implémente le client du jeu, qui se connecte à un serveur de jeu pour une expérience multijoueur.

## 1. Construction de l'Application

### Prérequis
- Java JDK 11 ou supérieur.

### Compilation
```bash
javac Client.java
```

## 2. Exécution de l'Application

Pour lancer le client :
```bash
java -jar client-1.0-SNAPSHOT.jar [adresse-du-serveur] [port]
```
Exemple :
```bash
java -jar client-1.0-SNAPSHOT.jar 192.168.1.10 20000
```

Pour lancer le serveur :
```bash
java -jar server-1.0-SNAPSHOT.jar
```
Exemple :
```bash
java -jar server-1.0-SNAPSHOT.jar
```

## 3. Protocole d'Application

Le jeu utilise un protocole basé sur TCP pour la communication entre le client et le serveur. Le protocole définit les actions suivantes :

- **INIT** : Le client envoie INIT pour établir la connexion.
- **LOBBY** : Le client vérifie la disponibilité dans le lobby.
- **JOIN** : Le client rejoint le lobby.
- **READY** : Le client signale qu'il est prêt.
- **START**, **UPDATE**, **END** : Le serveur contrôle le flux du jeu.
- **DIR** : Le client envoie la direction du serpent.
