# 🧠 PIM-AI-Connector

**PIM-AI-Connector** ist eine moderne Microservice-Plattform zur KI-gestützten Analyse, Anreicherung und Optimierung von Produktinformationen. Sie basiert auf dem Projekt [MyBookstore Microservices](https://github.com/thanhtuanh/mybookstore-microservices) und erweitert dieses um intelligente Services im PIM-Kontext.

🔗 **Live-Demo**: [pim-ai-connector-demo.onrender.com](https://pim-ai-connector-demo.onrender.com)

🕐 *Hinweis: Das Projekt wird auf Render.com (Free-Tier) gehostet – der erste Ladevorgang kann daher einige Sekunden dauern.*

> ⚠️ **Dieses Projekt dient ausschließlich der persönlichen Weiterbildung und Innovation im Rahmen meiner beruflichen Weiterentwicklung. Es wird nicht kommerziell oder im Freelance-Kontext genutzt.**

---

## 🏗️ Architekturübersicht

Die Anwendung besteht aus mehreren entkoppelten Services:

- 🔐 `auth-service` – Benutzerverwaltung & Authentifizierung (JWT)
- 📘 `book-service` – Verwaltung und Suche von Büchern
- 🔍 `product-intelligence-service` – KI-basierte Analyse von Produktbildern und -daten
- 🌐 `frontend` – Angular-Frontend mit KI-Dashboards

### PostgreSQL-Datenbanken:

- 🟢 `postgres-auth`
- 🟢 `postgres-book`
- 🟢 `postgres-intelligence`

Alle Services sind über REST-APIs verbunden und werden per Docker Compose orchestriert.

---

## 🚀 Projekt lokal starten

### Voraussetzungen

- Docker & Docker Compose
- Java 17 (für Spring Boot Services)
- Node.js + Angular CLI (für Frontend)
- API-Schlüssel für OpenAI und Google Vision

### `.env`-Datei anlegen

Pfad: `pim-ai-connector-service/.env`

```env
# KI & Vision
OPENAI_API_KEY=your_openai_api_key
GOOGLE_APPLICATION_CREDENTIALS=path_to_your_credentials.json
GCP_PROJECT_ID=your_gcp_project_id

# Frontend
WEB_DOMAIN=http://localhost:4200
