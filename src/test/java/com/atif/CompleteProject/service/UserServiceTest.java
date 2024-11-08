package com.atif.CompleteProject.service;

import com.atif.CompleteProject.dto.UserDTO;
import com.atif.CompleteProject.entity.User;
import com.atif.CompleteProject.exception.ResourceNotFoundException;
import com.atif.CompleteProject.exception.UserNotFoundException;
import com.atif.CompleteProject.mapper.UserMapper;
import com.atif.CompleteProject.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).name("John").email("john@example.com").build();
        userDTO = UserDTO.builder().id(1L).name("John").email("john@example.com").build();
    }

    @Test
    public void testCreateUserSuccess() {
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getName(), result.getName());
    }

    @Test
    public void testCreateUserFailure_NullUser() {
        when(userRepository.save(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(NullPointerException.class, () -> {
            userService.createUser(null);
        });
    }

    @Test
    public void testGetUserByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(userDTO.getName(), result.get().getName());
    }

    @Test
    public void testGetUserByIdFailure_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
    }

    @Test
    public void testUpdateUserFailure_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, userDTO);
        });
    }

    @Test
    public void testDeleteUserFailure_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
    }
}
