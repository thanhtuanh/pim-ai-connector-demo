# 🧠 PIM-AI-Connector

**PIM-AI-Connector** ist eine moderne Microservice-Plattform, die auf dem Projekt [MyBookstore Microservices](https://github.com/thanhtuanh/mybookstore-microservices) basiert. Sie erweitert dieses um KI-gestützte Services zur Analyse, Anreicherung und Optimierung von Produktinformationen.

🔗 **Live-Demo**: [pim-ai-connector-demo.onrender.com](https://pim-ai-connector-demo.onrender.com)
🕐 *Hinweis: Da die Render.com-Domain im Free-Tier gehostet wird, kann der erste Ladevorgang einige Sekunden dauern.*

---

## 🏗️ Architekturübersicht

Die Anwendung besteht aus mehreren voneinander getrennten Services:

- 🔐 `auth-service` – Benutzerverwaltung & Login mit JWT
- 📘 `book-service` – Verwaltung und Suche von Büchern
- 🔍 `product-intelligence-service` – KI-Analyse für Produktbilder und -daten
- 🌐 `frontend` – Angular-Frontend mit Dashboards & KI-Steuerung

### Datenbanken (jeweils PostgreSQL):
- 🟢 `postgres-auth`
- 🟢 `postgres-book`
- 🟢 `postgres-intelligence`

Alle Komponenten werden mit Docker Compose orchestriert und kommunizieren über REST-APIs in einem privaten Netzwerk.

---

## 🚀 Projekt starten

### Voraussetzungen

- Docker & Docker Compose
- Java 17 (für lokale Spring Boot Tests)
- Node.js + Angular CLI (wenn Frontend lokal getestet wird)
- API-Schlüssel für OpenAI und Google Cloud Vision

### .env Datei erstellen

Im Hauptverzeichnis `pim-ai-connector-service/` folgende Datei erstellen:

```env
# KI & Vision
OPENAI_API_KEY=your_openai_api_key
GOOGLE_APPLICATION_CREDENTIALS=path_to_your_credentials.json
GCP_PROJECT_ID=your_gcp_project_id

# Frontend
WEB_DOMAIN=http://localhost:4200
```

### Projekt starten (lokal)

```bash
cd pim-ai-connector-service
docker-compose up --build
```

Das Frontend ist erreichbar unter: [http://localhost:4200](http://localhost:4200)

---

## 🧠 KI-Funktionalitäten

### 🖼️ Bildanalyse
- Texterkennung aus Buchcovern (OCR)
- Erkennung von Titel, Autor, Genre, Tags

### 📑 Beschreibungsgenerierung
- GPT-basierte Produkttexte
- SEO-optimierte, kanalspezifische Inhalte

### ✨ Metadaten-Intelligenz
- Kategorien-Vorschläge
- Bildqualitätsbewertung
- Erkennung fehlender Felder

---

## 📊 API-Endpoints: Intelligence-Service

- `POST /api/intelligence/book-description` – KI-Buchbeschreibung generieren
- `POST /api/intelligence/cover-text` – Text aus Cover extrahieren
- `POST /api/intelligence/cover-categories` – Kategorien erkennen
- `POST /api/intelligence/cover-metadata` – Titel, Autor, usw. ermitteln

Swagger UI erreichbar unter:  
`http://localhost:8085/api/intelligence/swagger-ui.html`

---

## 🚗 Microservice-Struktur

```plaintext
pim-ai-connector/
├── auth-service/                # Benutzer & JWT
├── book-service/                # Bücher CRUD
├── product-intelligence-service/ # KI-Analyse-Logik
│   ├── controller/           # REST-Schnittstellen
│   ├── dto/                  # Datentransfer-Objekte
│   ├── service/              # OpenAI & Vision API
│   └── exception/           # Fehlerbehandlung
├── frontend/                    # Angular 17 mit KI-Dashboard
├── docker-compose.yml           # Orchestrierung aller Services
├── .env                         # zentrale Umgebungsvariablen
└── README.md
```

---

## 👤 Test-Account für Login

```txt
Username: admin
Passwort: password
```

---

## ✨ Technologien im Einsatz

- **Backend:** Java 17, Spring Boot 3, Spring Security 6, JPA, JWT
- **Frontend:** Angular 17, TypeScript, Bootstrap
- **Datenhaltung:** PostgreSQL
- **KI:** OpenAI GPT-4, Google Cloud Vision
- **Tools:** Docker, Docker Compose, Swagger (springdoc-openapi)

---

## 📆 Roadmap (geplant)

- ✅ OCR-Verbesserung
- ♻️ Produkttypen-Erweiterung
- ⚙️ Mehrsprachige Generierung
- ☁️ Domänenspezifisches Training
- 📊 Erweiterte Dashboards & Reports

---

## 💻 Autor

> Erstellt von **Duc Thanh Nguyen**  
> Senior Java Fullstack Entwickler mit Fokus auf PIM, DAM und KI-Systeme  
> Kontakt: [LinkedIn / GitHub / E-Mail einfügen]

---

## 📄 Lizenz

MIT License – frei nutzbar & erweiterbar

