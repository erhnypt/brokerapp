package com.brokerapp.service.impl;

import com.brokerapp.dto.UserDTO;
import com.brokerapp.model.User;
import com.brokerapp.repository.UserRepository;
import com.brokerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Kullanıcı servis arabiriminin uygulaması
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User createUser(UserDTO userDTO) {
        if (!isUsernameAvailable(userDTO.getUsername())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor: " + userDTO.getUsername());
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .active(userDTO.isActive())
                .build();

        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID ile kullanıcı bulunamadı: " + id));

        // If username has changed and new username is used by another user
        if (!existingUser.getUsername().equals(userDTO.getUsername())
                && !isUsernameAvailable(userDTO.getUsername())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor: " + userDTO.getUsername());
        }

        existingUser.setUsername(userDTO.getUsername());

        // If password has changed, encrypt it
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        existingUser.setActive(userDTO.isActive());

        return userRepository.save(existingUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("ID ile kullanıcı bulunamadı: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
