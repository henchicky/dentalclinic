# Dental Clinic Management System

A full-stack application for managing dental clinic appointments, built with Spring Boot and Vue.js.

## Prerequisites

### Required Software
1. **Java Development Kit (JDK) 17**
   - Download from: [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) or [OpenJDK 17](https://adoptium.net/temurin/releases/?version=17)
   - Verify installation: `java -version`

2. **Maven 3.8+**
   - Download from: [Maven Download](https://maven.apache.org/download.cgi)
   - Verify installation: `mvn -version`

3. **Node.js 18+**
   - Download from: [Node.js Download](https://nodejs.org/en/download/)
   - Verify installation: `node --version`

4. **npm 9+**
   - Comes with Node.js
   - Verify installation: `npm --version`

## Project Structure
```
dentalclinic/
├── backend/          # Spring Boot application
└── frontend/         # Vue.js application
```

## Backend Setup and Running

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend server will start at: `http://localhost:8080`

### Backend Features
- RESTful API endpoints
- H2 in-memory database
- Swagger UI documentation at: `http://localhost:8080/swagger-ui.html`
- H2 Console at: `http://localhost:8080/h2-console`

## Frontend Setup and Running

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

The frontend application will start at: `http://localhost:5173`

### Frontend Features
- Vue 3 with TypeScript
- Vite for fast development
- Pinia for state management
- Vue Router for navigation
- Axios for API calls

## Development Workflow

1. Start the backend server:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. In a new terminal, start the frontend development server:
   ```bash
   cd frontend
   npm run dev
   ```

3. Access the application:
   - Frontend: `http://localhost:5173`
   - Backend API: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`

## Troubleshooting

### Backend Issues
1. If Maven build fails:
   - Clean Maven cache: `mvn clean`
   - Update dependencies: `mvn dependency:purge-local-repository`

2. If H2 database connection fails:
   - Check if the server is running
   - Verify the JDBC URL in application.properties

### Frontend Issues
1. If npm install fails:
   - Clear npm cache: `npm cache clean --force`
   - Delete node_modules: `rm -rf node_modules`
   - Reinstall: `npm install`

2. If Vite server fails to start:
   - Check if port 5173 is available
   - Verify Node.js version is 18+

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Vue 3 Documentation](https://vuejs.org/guide/introduction.html)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)
- [Vite Documentation](https://vitejs.dev/guide/) 