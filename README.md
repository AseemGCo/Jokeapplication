# AI Joke Generator - Spring Boot Application

A futuristic Spring Boot application that automatically fetches random jokes every 2 seconds from the icanhazdadjoke.com API and displays them in a beautiful, modern UI with real-time updates via WebSocket.

## Features

- 🤖 **Automatic Joke Fetching**: Fetches new jokes every 2 seconds automatically
- 🌐 **Real-time Updates**: WebSocket integration for instant joke delivery
- 📚 **Joke History**: Stores and displays previous jokes in a scrollable history
- 🎨 **Futuristic UI**: Modern, responsive design with gradient animations
- 📊 **Live Statistics**: Real-time stats including total jokes and uptime
- 🔄 **Manual Controls**: Refresh jokes manually, clear history, toggle auto-refresh
- ⏱️ **Dynamic Refresh Rate**: Change refresh rate from 1 second to 5 minutes
- 📱 **Responsive Design**: Works perfectly on desktop and mobile devices

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Real-time Communication**: WebSocket (STOMP)
- **API**: icanhazdadjoke.com (No API key required)
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Internet connection (for API calls)

## Installation & Setup

1. **Clone or download the project**
   ```bash
   git clone <repository-url>
   cd RestAPI
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open your browser and navigate to: `http://localhost:8080`
   - The application will start fetching jokes automatically

## API Endpoints

- `GET /` - Main application page
- `GET /api/jokes` - Get all jokes from history
- `GET /api/jokes/{id}` - Get specific joke by ID
- `GET /api/stats` - Get application statistics
- `POST /api/jokes/refresh` - Manually trigger joke refresh
- `DELETE /api/jokes` - Clear joke history
- `GET /api/refresh-rate` - Get current refresh rate
- `POST /api/refresh-rate` - Update refresh rate (send JSON with "refreshRate" field)
- `GET /api/autorefresh/status` - Get auto-refresh status
- `POST /api/autorefresh/toggle` - Toggle auto-refresh on/off
- `POST /api/autorefresh/enable` - Enable auto-refresh
- `POST /api/autorefresh/disable` - Disable auto-refresh
- `GET /ws` - WebSocket endpoint for real-time updates

## Configuration

The application uses the following configuration in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Joke API Configuration
joke.api.url=https://icanhazdadjoke.com/
joke.api.key=

# WebSocket Configuration
spring.websocket.max-text-message-size=8192
spring.websocket.max-binary-message-size=8192
```

## How It Works

1. **Scheduled Task**: The `JokeService` uses Spring's `@Scheduled` annotation to fetch jokes every 2 seconds
2. **API Integration**: Fetches jokes from icanhazdadjoke.com API (no authentication required)
3. **Data Storage**: Stores jokes in a `ConcurrentHashMap` for thread-safe access
4. **Real-time Updates**: Uses WebSocket to push new jokes to connected clients
5. **Fallback Mechanism**: If API fails, provides fallback jokes

## UI Features

- **Gradient Animations**: Dynamic color shifts and loading animations
- **Glass Morphism**: Modern glass-like UI elements with backdrop blur
- **Responsive Grid**: Adapts to different screen sizes
- **Interactive Elements**: Hover effects and smooth transitions
- **Live Status Indicators**: Real-time connection status and uptime
- **Custom Scrollbars**: Styled scrollbars for better UX

## Project Structure

```
src/
├── main/
│   ├── java/com/jokeapp/
│   │   ├── JokeGeneratorApplication.java    # Main application class
│   │   ├── controller/
│   │   │   └── JokeController.java         # REST controller
│   │   ├── service/
│   │   │   └── JokeService.java            # Business logic
│   │   ├── model/
│   │   │   └── Joke.java                   # Data model
│   │   └── config/
│   │       ├── WebSocketConfig.java        # WebSocket configuration
│   │       └── RestTemplateConfig.java     # HTTP client configuration
│   └── resources/
│       ├── templates/
│       │   └── index.html                  # Main UI template
│       └── application.properties          # Configuration
└── test/                                   # Test files
```

## Customization

### Changing Refresh Rate
The application now supports dynamic refresh rate changes through the UI or API:

**Via UI:**
- Use the refresh rate input field to set a new rate (1-300 seconds)
- Click "Update Rate" or press Enter to apply changes

**Via API:**
```bash
# Get current refresh rate
curl -X GET http://localhost:8080/api/refresh-rate

# Set new refresh rate (e.g., 5 seconds)
curl -X POST http://localhost:8080/api/refresh-rate \
  -H "Content-Type: application/json" \
  -d '{"refreshRate": 5}'
```

**Programmatically:**
```java
// In JokeService.java
jokeService.setRefreshRate(5000); // 5 seconds in milliseconds
```

### Using Different Joke API
To use a different joke API, update the `fetchJokeFromApi()` method in `JokeService.java` and add your API key to `application.properties`.

### UI Customization
The UI is fully customizable through CSS in the `index.html` file. You can modify colors, animations, and layout as needed.

## Troubleshooting

### Common Issues

1. **Port already in use**
   - Change the port in `application.properties`: `server.port=8081`

2. **API not responding**
   - The application includes fallback jokes if the API is unavailable
   - Check your internet connection

3. **WebSocket connection issues**
   - Ensure no firewall is blocking WebSocket connections
   - Check browser console for connection errors

### Logs
Application logs are available in the console. Set log level in `application.properties`:

```properties
logging.level.com.jokeapp=DEBUG
```

## Contributing

Feel free to contribute to this project by:
- Adding new features
- Improving the UI design
- Adding more joke APIs
- Enhancing error handling
- Adding unit tests

## License

This project is open source and available under the MIT License.

## Acknowledgments

- [icanhazdadjoke.com](https://icanhazdadjoke.com/) for providing the joke API
- Spring Boot team for the excellent framework
- The open-source community for various libraries and tools used

---

**Enjoy the jokes! 😄**
