FROM node:18-alpine

WORKDIR /app

COPY gate-simulator/package*.json ./

RUN npm ci --only=production

COPY gate-simulator/ ./

EXPOSE 9999

CMD ["npm", "start"]