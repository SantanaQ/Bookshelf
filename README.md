# Bookshelf

Bookshelf is a web-based application for browsing and purchasing books. The project is built using Java and runs on an Apache Tomcat web server with a MySQL database managed via XAMPP.

## Technologies
- **Java** – Backend logic
- **Apache Tomcat** – Web server to host the application
- **MySQL** – Database for storing book and user data
- **XAMPP** – Tool for managing the MySQL database and Apache server

## Installation
### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Apache Tomcat (configured manually)
- XAMPP (for MySQL database management)

### Database Setup
Before starting the application, set up the MySQL database manually using XAMPP:
1. Open XAMPP Control Panel and start **Apache** and **MySQL**.
2. Open **phpMyAdmin** (http://localhost/phpmyadmin/) and create a new database:
   ```sql
   CREATE DATABASE bookshelf;
   ```
3. Create a database user and grant necessary permissions:
   ```sql
   CREATE USER 'user'@'localhost' IDENTIFIED BY 'your-password';
   GRANT ALL PRIVILEGES ON bookshelf.* TO 'user'@'localhost';
   ```
4. Import the provided SQL schema bookshelf.sql into the `bookshelf` database.

### Deployment Steps
1. **Clone the repository**
   ```bash
   git clone https://github.com/SatanaQ/Bookshelf.git
   cd Bookshelf
   ```
2. **Configure database connection**
   Update the `DatabaseConnection` class with your database credentials:
   ```properties
   db.url=jdbc:mysql://localhost:3306/bookshelf
   db.username=user
   db.password=your-password
   ```
3. **Deploy to Apache Tomcat**
   - Place the compiled `.war` file in the `webapps` folder of your Tomcat installation.
   - Start Tomcat and access the application at `http://localhost:8080/OnlineBookstore`.

## Features
- Browse books by category
- View book details
- Add books to the shopping cart
- Purchase books (basic order management system)

## Contributing
Pull requests are welcome! Please create an issue before making significant changes.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.

