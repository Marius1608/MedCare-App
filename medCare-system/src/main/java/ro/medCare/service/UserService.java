package ro.medCare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.medCare.exception.ResourceNotFoundException;
import ro.medCare.exception.ValidationException;
import ro.medCare.model.User;
import ro.medCare.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ValidationException("Utilizatorul nu există!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Parolă incorectă!");
        }

        return user;
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ValidationException("Numele de utilizator există deja!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("Utilizatorul nu a fost găsit!");
        }

        Optional<User> duplicateUser = userRepository.findByUsername(user.getUsername());
        if (duplicateUser.isPresent() && !duplicateUser.get().getId().equals(user.getId())) {
            throw new ValidationException("Numele de utilizator există deja pentru alt utilizator!");
        }

        // Dacă parola a fost modificată, criptează noua parolă
        if (!user.getPassword().equals(existingUser.get().getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilizatorul nu a fost găsit!");
        }
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilizatorul nu a fost găsit!"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}