# Filiera Facile

A Spring Boot application for supply chain management.

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+ (see Database Setup section for installation instructions)

## Environment Variables

Set the following environment variables:

```bash
export DB_PASSWORD=your_database_password
export DB_USERNAME=root  # optional, defaults to root
export DB_URL=jdbc:mysql://localhost:3306/filierafacile?createDatabaseIfNotExist=true  # optional
```

## Installation

```bash
git clone <repository-url>
cd filierafacile
mvn clean install
```

## Running the Application

### Development

```bash
mvn spring-boot:run
```

### Production

```bash
java -jar target/filierafacile-*.jar
```

### With custom environment variables

```bash
DB_PASSWORD=yourpass mvn spring-boot:run
```

The application will start on `http://localhost:8081`

## Database Setup

### Installing MySQL Server

#### macOS (using Homebrew)
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
Download and install MySQL Server from [MySQL Downloads](https://dev.mysql.com/downloads/mysql/)

### Database Configuration

1. **Create a MySQL user** (optional, you can use root):
```bash
mysql -u root -p
CREATE USER 'filiera_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON filierafacile_db.* TO 'filiera_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

2. **Set the database password**:
The application expects the password `Lmlyraa200kh!` by default, or set your own via environment variables.

### Database Migrations

The application uses **Flyway** for database migrations. Migrations run automatically on startup:

- **Automatic Migration**: When you start the application, all migrations in `src/main/resources/db/migration/` are executed automatically
- **Migration Files**: V1 through V13 create tables and insert sample data
- **Clean Database**: If you need to reset, drop the database and restart the application

#### Sample Data Included
- Users, companies, and products
- Sample events, shopping carts, and tickets
- Ready-to-test data for API endpoints

The database `filierafacile_db` will be created automatically if it doesn't exist.

## API Documentation

Once the application is running, you can access:

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **API Docs**: `http://localhost:8081/api-docs`

### Sample API Endpoints

- `GET /api/carrelli` - Get all shopping carts
- `GET /api/carrelli/{utenteId}` - Get cart for specific user
- `GET /api/eventi` - Get all events
- `GET /api/biglietti` - Get all tickets

## Testing

```bash
mvn test
```