package com.atif.CompleteProject.service;

import com.atif.CompleteProject.dto.UserDTO;
import com.atif.CompleteProject.entity.User;
import com.atif.CompleteProject.exception.InvalidUserException;
import com.atif.CompleteProject.exception.UserNotFoundException;
import com.atif.CompleteProject.mapper.UserMapper;
import com.atif.CompleteProject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        logger.debug("Fetching user with id: {}", id);
        return Optional.ofNullable(userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public UserDTO createUser(UserDTO userDTO) {
        logger.info("Creating new user: {}", userDTO.getName());

        if (userDTO.getName() == null || userDTO.getEmail() == null) {
            throw new InvalidUserException("User name and email must not be null");
        }

        return Optional.of(userDTO)
                .map(userMapper::toEntity)
                .map(userRepository::save)
                .map(userMapper::toDto)
                .orElseThrow(() -> new InvalidUserException("Unable to create user"));
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        logger.info("Updating user with id: {}", id);

        if (userDTO.getName() == null || userDTO.getEmail() == null) {
            throw new InvalidUserException("User name and email must not be null");
        }

        return Optional.ofNullable(userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(userDTO.getName());
                    existingUser.setEmail(userDTO.getEmail());
                    return userRepository.save(existingUser);
                })
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    public boolean deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public Optional<UserDTO> updateAndCreateUser(Long idToUpdate, UserDTO updatedUserDTO, UserDTO newUserDTO) {
        logger.info("Starting transaction to update and create a user");

        if (newUserDTO.getName() == null || newUserDTO.getEmail() == null) {
            throw new InvalidUserException("New user name and email must not be null");
        }

        Optional<User> updatedUser = Optional.ofNullable(userRepository.findById(idToUpdate)
                .map(existingUser -> {
                    existingUser.setName(updatedUserDTO.getName());
                    existingUser.setEmail(updatedUserDTO.getEmail());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new UserNotFoundException(idToUpdate)));

        User createdUser = Optional.of(newUserDTO)
                .map(userMapper::toEntity)
                .map(userRepository::save)
                .orElseThrow(() -> new InvalidUserException("Unable to create new user"));

        return updatedUser.map(userMapper::toDto);
    }
}
