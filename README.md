# Villager

> ⚠️ **Disclaimer** : Ce projet est un ancien développement personnellement. Il a été récemment mis à jour et rendu public. Il remonte à cette époque et n'a pas eu de maintenance active depuis.

Plugin Minecraft (Spigot/Paper) pour gerer un systeme de villages, PNJ, quetes et economie.

## Fonctionnalites
- Gestion de villages et joueurs
- PNJ (pathfinding, interactions, dialogues)
- Quetes basiques (ex: WelcomeQuest)
- Economie et monnaie (CoinType)
- Commandes: /money, /npc, /village
- Configuration et messages localises

## Prerequis
- Java 8+ (selon la version cible de votre serveur)
- Spigot ou Paper
- Maven

## Installation
1. Cloner le projet.
2. Compiler:
   ```bash
   mvn clean package
   ```
3. Copier le jar dans le dossier `plugins` du serveur.
4. Demarrer le serveur.

## Configuration
- Fichier principal: `src/main/resources/config.yml`
- Langue: `src/main/resources/fr_fr.lang`
- Plugin: `src/main/resources/plugin.yml`

## Structure rapide
- `doryanbessiere.villager.net`: coeur du plugin
- `commands`: commandes du plugin
- `commons`: outils communs (config, chat, item, npc, etc.)
- `village`: logique et stockage des villages
- `player`: gestion des joueurs

## Developpement
- Importer le projet Maven dans votre IDE.
- Les sources sont sous `src/main/java`.
