package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.CustomUserDetailsService;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import net.engineeringdigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDb = userService.getUserByName(userName);

        if (StringUtils.hasLength(user.getUsername())) {
            userInDb.setUsername(user.getUsername());
        }

        if (StringUtils.hasLength(user.getPassword())) {
            userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            userInDb.setRoles(user.getRoles());
        }

        if (StringUtils.hasLength(user.getEmail())) {
            userInDb.setEmail(user.getEmail());
        }

        if (user.getSentimentAnalysis() != null) {
            userInDb.setSentimentAnalysis(user.getSentimentAnalysis());
        }

        userService.saveUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/greeting/{city}")
    public ResponseEntity<?> greeting(@PathVariable String city) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        WeatherResponse weatherResponse = weatherService.getWeather(city);
        StringBuilder message = new StringBuilder("Hi " + userName + " the temperature is : "
                + weatherResponse.getCurrent().getTemperature())
                .append("\nAnd uv index is: ").append(weatherResponse.getCurrent().getUv())
                .append("\nAt location: ").append(weatherResponse.getLocation().getName());

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Login failed", e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect username or password");
        }
    }
}
