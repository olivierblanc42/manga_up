# Utiliser une image Node.js pour le développement
FROM node:18-alpine

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste du code source
COPY . .

# Exposer le port d'Angular
EXPOSE 4200

# Démarrer Angular avec HMR
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll", "2000", "--hmr"]
