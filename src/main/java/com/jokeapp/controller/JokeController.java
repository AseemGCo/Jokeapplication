package com.jokeapp.controller;

import com.jokeapp.model.Joke;
import com.jokeapp.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class JokeController {
    
    @Autowired
    private JokeService jokeService;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("jokeCount", jokeService.getJokeCount());
        return "index";
    }
    
    @GetMapping("/api/jokes")
    @ResponseBody
    public ResponseEntity<List<Joke>> getAllJokes() {
        List<Joke> jokes = jokeService.getJokeHistory();
        return ResponseEntity.ok(jokes);
    }
    
    @GetMapping("/api/jokes/{id}")
    @ResponseBody
    public ResponseEntity<Joke> getJokeById(@PathVariable String id) {
        Joke joke = jokeService.getJokeById(id);
        if (joke != null) {
            return ResponseEntity.ok(joke);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJokes", jokeService.getJokeCount());
        stats.put("lastUpdated", System.currentTimeMillis());
        stats.put("autoRefreshEnabled", jokeService.isAutoRefreshEnabled());
        stats.put("refreshRate", jokeService.getRefreshRate());
        return ResponseEntity.ok(stats);
    }
    
    @DeleteMapping("/api/jokes")
    @ResponseBody
    public ResponseEntity<Map<String, String>> clearHistory() {
        jokeService.clearHistory();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Joke history cleared successfully");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/api/jokes/refresh")
    @ResponseBody
    public ResponseEntity<Map<String, String>> refreshJokes() {
        Joke joke = jokeService.fetchJokeManually();
        Map<String, String> response = new HashMap<>();
        if (joke != null) {
            response.put("message", "Joke refreshed successfully");
            response.put("jokeId", joke.getId());
        } else {
            response.put("message", "Failed to refresh joke");
        }
        return ResponseEntity.ok(response);
    }
    
    // New endpoints for auto-refresh control
    @GetMapping("/api/autorefresh/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAutoRefreshStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("enabled", jokeService.isAutoRefreshEnabled());
        return ResponseEntity.ok(status);
    }
    
    @PostMapping("/api/autorefresh/toggle")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleAutoRefresh() {
        jokeService.toggleAutoRefresh();
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", jokeService.isAutoRefreshEnabled());
        response.put("message", "Auto-refresh " + (jokeService.isAutoRefreshEnabled() ? "enabled" : "disabled"));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/api/autorefresh/enable")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enableAutoRefresh() {
        jokeService.setAutoRefreshEnabled(true);
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", true);
        response.put("message", "Auto-refresh enabled");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/api/autorefresh/disable")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> disableAutoRefresh() {
        jokeService.setAutoRefreshEnabled(false);
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", false);
        response.put("message", "Auto-refresh disabled");
        return ResponseEntity.ok(response);
    }
    
    // New endpoints for refresh rate management
    @GetMapping("/api/refresh-rate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRefreshRate() {
        Map<String, Object> response = new HashMap<>();
        response.put("refreshRate", jokeService.getRefreshRate());
        response.put("refreshRateSeconds", jokeService.getRefreshRate() / 1000.0);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/api/refresh-rate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setRefreshRate(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Object rateValue = request.get("refreshRate");
            long milliseconds;
            
            if (rateValue instanceof Number) {
                // If it's a number, assume it's in seconds
                double seconds = ((Number) rateValue).doubleValue();
                milliseconds = (long) (seconds * 1000);
            } else if (rateValue instanceof String) {
                // If it's a string, try to parse it
                String rateStr = (String) rateValue;
                if (rateStr.endsWith("s")) {
                    // Remove 's' and parse as seconds
                    double seconds = Double.parseDouble(rateStr.substring(0, rateStr.length() - 1));
                    milliseconds = (long) (seconds * 1000);
                } else {
                    // Try to parse as milliseconds
                    milliseconds = Long.parseLong(rateStr);
                }
            } else {
                throw new IllegalArgumentException("Invalid refresh rate format");
            }
            
            jokeService.setRefreshRate(milliseconds);
            response.put("success", true);
            response.put("refreshRate", milliseconds);
            response.put("refreshRateSeconds", milliseconds / 1000.0);
            response.put("message", "Refresh rate updated successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Failed to update refresh rate: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
