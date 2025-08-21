package com.jokeapp.service;

import com.jokeapp.model.Joke;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ScheduledFuture;

@Service
public class JokeService {
    
    private static final Logger logger = LoggerFactory.getLogger(JokeService.class);
    
    @Value("${joke.api.url:https://icanhazdadjoke.com/}")
    private String jokeApiUrl;
    
    @Value("${joke.api.key:}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final TaskScheduler taskScheduler;
    private final Map<String, Joke> jokeHistory = new ConcurrentHashMap<>();
    private final AtomicBoolean autoRefreshEnabled = new AtomicBoolean(true);
    private final AtomicLong currentRefreshRate = new AtomicLong(2000); // Default 2 seconds
    private ScheduledFuture<?> scheduledTask;
    
    public JokeService(RestTemplate restTemplate, SimpMessagingTemplate messagingTemplate, TaskScheduler taskScheduler) {
        this.restTemplate = restTemplate;
        this.messagingTemplate = messagingTemplate;
        this.taskScheduler = taskScheduler;
        startScheduledTask();
    }
    
    private void startScheduledTask() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
        scheduledTask = taskScheduler.scheduleAtFixedRate(this::fetchRandomJoke, currentRefreshRate.get());
    }
    
    public void setRefreshRate(long milliseconds) {
        if (milliseconds < 1000) {
            throw new IllegalArgumentException("Refresh rate must be at least 1000 milliseconds (1 second)");
        }
        if (milliseconds > 300000) {
            throw new IllegalArgumentException("Refresh rate cannot exceed 300000 milliseconds (5 minutes)");
        }
        
        currentRefreshRate.set(milliseconds);
        if (autoRefreshEnabled.get()) {
            startScheduledTask();
        }
        logger.info("Refresh rate updated to {} milliseconds", milliseconds);
    }
    
    public long getRefreshRate() {
        return currentRefreshRate.get();
    }
    
    public void fetchRandomJoke() {
        // Only fetch jokes if auto-refresh is enabled
        if (autoRefreshEnabled.get()) {
            try {
                Joke joke = fetchJokeFromApi();
                if (joke != null) {
                    joke.setTimestamp(LocalDateTime.now());
                    jokeHistory.put(joke.getId(), joke);
                    
                    // Send to WebSocket clients
                    messagingTemplate.convertAndSend("/topic/jokes", joke);
                    
                    logger.info("Fetched new joke: {}", joke.getJoke().substring(0, Math.min(50, joke.getJoke().length())) + "...");
                }
            } catch (Exception e) {
                logger.error("Error fetching joke: {}", e.getMessage());
            }
        }
    }
    
    private Joke fetchJokeFromApi() {
        try {
            // Using icanhazdadjoke.com API (no API key required)
            return restTemplate.getForObject("https://icanhazdadjoke.com/", Joke.class);
        } catch (Exception e) {
            logger.error("Failed to fetch joke from API: {}", e.getMessage());
            return createFallbackJoke();
        }
    }
    
    private Joke createFallbackJoke() {
        Joke fallbackJoke = new Joke();
        fallbackJoke.setId("fallback-" + System.currentTimeMillis());
        fallbackJoke.setJoke("Why don't scientists trust atoms? Because they make up everything!");
        fallbackJoke.setStatus("success");
        fallbackJoke.setTimestamp(LocalDateTime.now());
        return fallbackJoke;
    }
    
    public List<Joke> getJokeHistory() {
        return new ArrayList<>(jokeHistory.values());
    }
    
    public Joke getJokeById(String id) {
        return jokeHistory.get(id);
    }
    
    public int getJokeCount() {
        return jokeHistory.size();
    }
    
    public void clearHistory() {
        jokeHistory.clear();
    }
    
    // Auto-refresh control methods
    public boolean isAutoRefreshEnabled() {
        return autoRefreshEnabled.get();
    }
    
    public void setAutoRefreshEnabled(boolean enabled) {
        autoRefreshEnabled.set(enabled);
        logger.info("Auto-refresh {}: {}", enabled ? "enabled" : "disabled");
    }
    
    public void toggleAutoRefresh() {
        boolean newState = autoRefreshEnabled.getAndSet(!autoRefreshEnabled.get());
        logger.info("Auto-refresh toggled: {}", !newState);
    }
    
    // Manual joke fetch method
    public Joke fetchJokeManually() {
        try {
            Joke joke = fetchJokeFromApi();
            if (joke != null) {
                joke.setTimestamp(LocalDateTime.now());
                jokeHistory.put(joke.getId(), joke);
                
                // Send to WebSocket clients
                messagingTemplate.convertAndSend("/topic/jokes", joke);
                
                logger.info("Manually fetched new joke: {}", joke.getJoke().substring(0, Math.min(50, joke.getJoke().length())) + "...");
            }
            return joke;
        } catch (Exception e) {
            logger.error("Error manually fetching joke: {}", e.getMessage());
            return null;
        }
    }
}
