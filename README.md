# Filiera Facile

A Spring Boot application for supply chain management.

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

..

## Environment Variables

Set the following environment variables:

```bash
export DB_PASSWORD=your_database_password
export DB_USERNAME=root  # optional, defaults to root
export DB_URL=jdbc:mysql://localhost:3306/filierafacile?createDatabaseIfNotExist=true  # optional
```

## Installation

```bash
git clone https://github.com/emanuelegurini/filierafacile.git
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

## Database

The application uses MySQL. The database will be created automatically if it doesn't exist.

## Testing

```bash
mvn test
```