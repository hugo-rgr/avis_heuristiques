# avis_heuristiques2

Ce dépôt est maintenant structuré en deux parties :

- `backend/` : API Java (Spring Boot / Maven)
- `frontend/` : application React (Vite + TypeScript)

## Prérequis

- Java (version selon le `pom.xml`)
- Node.js (>= 18 recommandé)

## Lancer le backend

Depuis `backend/` :

```bat
mvnw.cmd test
mvnw.cmd spring-boot:run
```

Le backend écoute typiquement sur `http://localhost:8080`.

## Lancer le frontend

Depuis `frontend/` :

```bat
npm install
npm run dev
```

Le frontend écoute sur `http://localhost:5173`.