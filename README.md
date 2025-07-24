# 📚 Library Management System

Sistema di gestione biblioteca realizzato con **Spring Boot 3.4.5** e **React**, utilizzando **JHipster 8.11.0**.

## 🚀 Funzionalità

- **📖 Gestione Libri** - CRUD completo per libri con autori
- **👤 Gestione Autori** - Profili autori e associazioni libri
- **⭐ Sistema Recensioni** - Recensioni e valutazioni utenti
- **🔐 Autenticazione** - Sicurezza JWT con ruoli utente
- **📱 UI Responsiva** - Frontend React moderno
- **📊 API REST** - API RESTful con documentazione OpenAPI

## 🏗️ Architettura

- **Backend**: Spring Boot 3.4.5 + MySQL + MapStruct
- **Frontend**: React 18.3.1 + TypeScript + Bootstrap
- **Database**: MySQL 8.0+ con migrazioni Liquibase
- **Sicurezza**: OAuth2 + JWT
- **Testing**: JUnit 5 + Jest

## 📦 Entità

- **Autore**: `id`, `nome`
- **Libro**: `id`, `titolo`, `prezzo`, `autore`
- **Recensione**: `id`, `descrizione`, `libro`, `user`

## 🛠️ Requisiti

- **Java 17+**
- **Node.js 22.15.0+**
- **MySQL 8.0+** sulla porta 3306

## 🚀 Avvio Rapido

### 1. Setup Database

```sql
CREATE DATABASE Library;
CREATE USER 'giovanni'@'localhost' IDENTIFIED BY 'B@RGHa86$b';
GRANT ALL PRIVILEGES ON Library.* TO 'giovanni'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Avvio Applicazione

```bash
# Backend
./mvnw spring-boot:run

# Frontend (terminal separato)
./npmw start
```

### 3. Accesso

- **App**: http://localhost:8080
- **API Docs**: http://localhost:8080/api-docs
- **Login**: `admin`/`admin` o `user`/`user`

## 📊 API Endpoints

### Autori (`/api/autores`)

- `GET /api/autores` - Lista autori
- `POST /api/autores` - Crea autore
- `PUT /api/autores/{id}` - Modifica autore
- `DELETE /api/autores/{id}` - Elimina autore

### Libri (`/api/libros`)

- `GET /api/libros` - Lista libri
- `POST /api/libros` - Crea libro
- `PUT /api/libros/{id}` - Modifica libro
- `DELETE /api/libros/{id}` - Elimina libro

### Recensioni (`/api/recensiones`)

- `GET /api/recensiones` - Lista recensioni
- `POST /api/recensiones` - Crea recensione
- `PUT /api/recensiones/{id}` - Modifica recensione
- `DELETE /api/recensiones/{id}` - Elimina recensione

## 🔧 Comandi Utili

```bash
# Build
./mvnw clean compile

# Test
./mvnw test

# Test specifico
./mvnw -Dtest=AutoreMapperTest test

# Formattazione codice
./mvnw spotless:apply

# Build produzione
./mvnw -Pprod clean verify

# Frontend
./npmw install
./npmw test
./npmw run build
```

## 🧪 Testing

```bash
# Unit test backend
./mvnw test

# Test filtrati (esclude test problematici)
./mvnw test -Dtest=!*IT,!*MapperTest,!WebConfigurerTest

# Test frontend
./npmw test
```

## 🐛 Risoluzione Problemi

### Connessione Database

- Verificare MySQL attivo: `mysql -u giovanni -p`
- Controllare configurazione in `src/main/resources/config/application-dev.yml`
- Assicurarsi che il database `Library` esista

### Conflitti Porta

- Applicazione: porta 8080
- MySQL: porta 3306

### Test Falliti

- Alcuni test di integrazione richiedono setup TestContainers
- Usare comando filtrato per test di sviluppo

## 📁 Struttura Progetto

```
src/
├── main/
│   ├── java/com/giodad/todolist/
│   │   ├── domain/          # Entità JPA
│   │   ├── repository/      # Repository Spring Data
│   │   ├── service/         # Logica business + DTO
│   │   ├── web/rest/        # Controller REST
│   │   └── config/          # Configurazioni
│   ├── resources/
│   │   └── config/          # File configurazione
│   └── webapp/              # Frontend React
└── test/                    # Test JUnit + Jest
```
