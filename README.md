# BuildTrack — Construction Project Intelligence Platform

BuildTrack is a construction project management web application built with Java and Spring Boot. It helps manage construction projects, monitor task progress, track site issues, and manage material inventory through a centralized dashboard.

## Features

- **Project Management:** Create, view, update, and delete construction projects.
- **Task Management:** Assign tasks to projects, manage priorities and statuses, and monitor completion progress.
- **Site Issue Tracking:** Record and manage site issues with severity and status tracking.
- **Material Inventory:** Track stock quantities, required materials, suppliers, and potential shortages.
- **Project Dashboard:** View project summaries, planned budgets, overdue tasks, upcoming deadlines, and material shortages.
- **Project Health Monitoring:** Rule-based health indicators using overdue deadlines, overdue tasks, critical site issues, and material shortages.

## Screenshots

### Dashboard
<img width="1687" height="927" alt="image" src="https://github.com/user-attachments/assets/4544aec1-2ed1-411b-ad9b-30ec79383cee" />

### Project Management
<img width="1703" height="925" alt="image" src="https://github.com/user-attachments/assets/2a314bde-bb5c-42a0-a823-d6fc4973bc0d" />

### Task Management
<img width="1704" height="923" alt="image" src="https://github.com/user-attachments/assets/e098f54d-2fcf-4ba9-94ae-216757d6bb64" />

### Site Issue Tracking
<img width="1699" height="926" alt="image" src="https://github.com/user-attachments/assets/a6a63b65-a228-4c93-86c6-72588de25b43" />

### Material Inventory
<img width="1698" height="925" alt="image" src="https://github.com/user-attachments/assets/258375fb-c4ee-4994-8aa7-afee8cd29dcd" />


## Tech Stack

- **Language:** Java 17
- **Backend:** Spring Boot, Spring Data JPA
- **Frontend:** Thymeleaf, HTML, Bootstrap 5
- **Database:** H2 for demo mode; MySQL configuration for development
- **Build Tool:** Apache Maven

## Run Locally

### Prerequisites
- Java 17 or later
- Apache Maven

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/dhruvakalambe/BuildTrack.git
   cd BuildTrack
   ```

2. Start the application in demo mode:

   ```bash
   mvn spring-boot:run "-Dspring-boot.run.profiles=demo"
   ```

3. Open the application in your browser:

   http://localhost:8082

Demo mode uses an H2 file-based database. Application data may persist between runs.

## Project Structure

```text
src/main/java/com/example/buildtrack/
├── BuildTrackApplication.java
├── controller/
├── entity/
├── repository/
└── service/

src/main/resources/
└── templates/
```

## Future Improvements

- User authentication and role-based access
- Construction expense tracking and reporting
- Advanced analytics and visual reports
- Deployment to a cloud platform

## Author

**Dhruva Kalambe**

[GitHub Repository](https://github.com/dhruvakalambe/BuildTrack)
