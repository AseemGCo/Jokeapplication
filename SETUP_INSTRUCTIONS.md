# Quick Setup Guide - AI Joke Generator

## 🚀 How to Run the Application

### Option 1: Using the Batch File (Easiest)
1. Double-click `run.bat` in the project directory
2. Wait for the application to start
3. Open your browser and go to: `http://localhost:8080`

### Option 2: Using Command Line
1. Open Command Prompt or PowerShell in the project directory
2. Run: `.\mvnw.cmd spring-boot:run`
3. Open your browser and go to: `http://localhost:8080`

### Option 3: Using the JAR File
1. Build the project: `.\mvnw.cmd clean install`
2. Run the JAR: `java -jar target\joke-generator-1.0.0.jar`
3. Open your browser and go to: `http://localhost:8080`

## 🎯 What You'll See

- **Futuristic UI**: Modern design with gradient animations and glass morphism effects
- **Real-time Jokes**: New jokes appear every 2 seconds automatically
- **Live Statistics**: Total jokes fetched, refresh rate, and uptime
- **Joke History**: Scrollable list of all previous jokes
- **Interactive Controls**: Manual refresh, clear history, toggle auto-refresh

## 🔧 Features

✅ **Automatic Joke Fetching**: Every 2 seconds from icanhazdadjoke.com  
✅ **WebSocket Real-time Updates**: Instant joke delivery  
✅ **Joke Storage**: Map-based storage for previous jokes  
✅ **RESTful API**: Multiple endpoints for data access  
✅ **Responsive Design**: Works on desktop and mobile  
✅ **Fallback Mechanism**: Works even if API is down  

## 📱 API Endpoints

- `GET /` - Main application page
- `GET /api/jokes` - Get all jokes from history
- `GET /api/stats` - Get application statistics
- `POST /api/jokes/refresh` - Manually trigger joke refresh
- `DELETE /api/jokes` - Clear joke history

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Frontend**: HTML5, CSS3, JavaScript
- **Real-time**: WebSocket (STOMP)
- **API**: icanhazdadjoke.com (No API key required)
- **Build Tool**: Maven
- **Java Version**: 17+

## 🎨 UI Highlights

- **Gradient Animations**: Dynamic color shifts
- **Glass Morphism**: Modern glass-like UI elements
- **Live Status Indicators**: Real-time connection status
- **Custom Scrollbars**: Styled for better UX
- **Responsive Grid**: Adapts to different screen sizes

## 🚨 Troubleshooting

1. **Port 8080 already in use**: Change port in `application.properties`
2. **Java not found**: Ensure Java 17+ is installed and JAVA_HOME is set
3. **Maven issues**: Use the provided Maven wrapper (`mvnw.cmd`)

## 🎉 Enjoy Your Jokes!

The application is now running and will automatically fetch new jokes every 2 seconds. The futuristic UI provides a great user experience with real-time updates and beautiful animations.

---

**Built with ❤️ using Spring Boot & Modern Web Technologies**
