package com.brokerapp.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brokerapp.dto.UserDTO;
import com.brokerapp.model.User;
import com.brokerapp.repository.UserRepository;
import com.brokerapp.service.impl.UserServiceImpl;

/**
 * UserService için birim testleri
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Test için kullanılacak veri nesnelerini hazırla
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setActive(true);

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .active(true)
                .build();
    }

    @Test
    void createUser_Success() {
        // Mock davranışı ayarla
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Metodu çağır
        User result = userService.createUser(userDTO);

        // Sonuçları doğrula
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.isActive());

        // Mock metotların çağrıldığını doğrula
        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_DuplicateUsername() {
        // Mock davranışı ayarla
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // Metot çağrıldığında exception fırlatılmalı
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userDTO);
        });

        // Exception mesajını doğrula
        assertTrue(exception.getMessage().contains("Bu kullanıcı adı zaten kullanılıyor"));

        // save metodunun çağrılmadığını doğrula
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        // Mock davranışı ayarla
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Metodu çağır
        Optional<User> result = userService.getUserById(1L);

        // Sonuçları doğrula
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());

        // Mock metotların çağrıldığını doğrula
        verify(userRepository).findById(1L);
    }

    @Test
    void getAllUsers_Success() {
        // Mock davranışı ayarla
        User user2 = User.builder().id(2L).username("anotheruser").password("pass").active(true).build();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        // Metodu çağır
        List<User> result = userService.getAllUsers();

        // Sonuçları doğrula
        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("anotheruser", result.get(1).getUsername());

        // Mock metodun çağrıldığını doğrula
        verify(userRepository).findAll();
    }
}
