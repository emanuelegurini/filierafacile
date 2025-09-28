# Filiera Facile

Applicazione Spring Boot per la gestione della filiera con migrazioni automatiche del database e API RESTful.

## Avvio Rapido

### Prerequisiti

- **Java 21** o superiore
- **Maven 3.6+**
- **MySQL 8.0+** (vedi [Configurazione Database](#configurazione-database) per l'installazione)

### Configurazione Iniziale

1. **Clona il repository**
   ```bash
   git clone <repository-url>
   cd filierafacile
   ```

2. **Configura l'applicazione**
   ```bash
   # Copia la configurazione di esempio
   cp src/main/resources/application-example.yaml src/main/resources/application.yaml
   ```

3. **Modifica la tua configurazione**
   Apri `src/main/resources/application.yaml` e personalizza:
   ```yaml
   datasource:
     url: jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME?createDatabaseIfNotExist=true
     username: YOUR_DB_USERNAME
     password: YOUR_DB_PASSWORD
   ```

4. **Compila ed esegui**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

L'applicazione si avvierà su `http://localhost:8081` con il database creato automaticamente e popolato con dati di esempio.

## Configurazione Avanzata

### Variabili d'Ambiente (Alternativa al file di configurazione)

Invece di modificare `application.yaml`, puoi impostare variabili d'ambiente:

```bash
export DB_URL="jdbc:mysql://localhost:3306/your_db_name?createDatabaseIfNotExist=true"
export DB_USERNAME="your_username"
export DB_PASSWORD="your_password"

mvn spring-boot:run
```

### Deployment in Produzione

```bash
# Compila il JAR
mvn clean package

# Esegui in produzione
java -jar target/filierafacile-*.jar
```

### Sicurezza della Configurazione

**Importante**: Il file `application.yaml` è ignorato da Git per proteggere i dati sensibili. Usa sempre il file di esempio come template.

## Configurazione Database

### Installazione MySQL Server

#### macOS (usando Homebrew)
```bash
brew install mysql
brew services start mysql
```

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
sudo systemctl enable mysql
```

#### Windows
Scarica e installa MySQL Server da [MySQL Downloads](https://dev.mysql.com/downloads/mysql/)

### Configurazione Database

1. **Crea un utente MySQL** (raccomandato):
```bash
mysql -u root -p
CREATE USER 'filiera_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON your_database_name.* TO 'filiera_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

2. **Il database verrà creato automaticamente** quando esegui l'applicazione per la prima volta.

## Migrazioni Database

Questa applicazione usa **Flyway** per la gestione automatica dello schema del database:

### Cosa succede automaticamente:
- **Creazione Database**: Il database specificato viene creato se non esiste
- **Migrazione Schema**: Tutti i 13 file di migrazione (V1-V13) vengono eseguiti in sequenza
- **Dati di Esempio**: I dati di test vengono caricati automaticamente per lo sviluppo

### File di Migrazione:
- `V1__Create_base_tables.sql` - Tabelle principali (utenti, aziende, prodotti)
- `V2__Create_additional_tables.sql` - Tabelle di supporto (carrelli, eventi)
- `V3-V13__*` - Correzioni dati, dati di esempio e miglioramenti

### Reset Database:
Se devi resettare il database:
```bash
# Opzione 1: Elimina e ricrea il database
mysql -u root -p -e "DROP DATABASE IF EXISTS your_database_name;"
mvn spring-boot:run  # Ricreerà tutto

# Opzione 2: Usa Flyway clean (se necessario)
mvn flyway:clean flyway:migrate
```

### Dati di Esempio Inclusi:
- **Utenti**: Utenti di test con ruoli diversi
- **Aziende**: Aziende di esempio della filiera
- **Prodotti**: Vari prodotti alimentari con categorie
- **Eventi**: Eventi e attività della filiera
- **Carrelli e Biglietti**: Dati di transazione pronti per il test

## Documentazione API

Una volta che l'applicazione è in esecuzione, puoi accedere a:

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **Documentazione API**: `http://localhost:8081/api-docs`

### Endpoint API di Esempio

- `GET /api/carrelli` - Ottieni tutti i carrelli
- `GET /api/carrelli/{utenteId}` - Ottieni carrello per utente specifico
- `GET /api/eventi` - Ottieni tutti gli eventi
- `GET /api/biglietti` - Ottieni tutti i biglietti
