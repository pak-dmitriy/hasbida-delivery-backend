package com.delivery.habsida.service;

import com.delivery.habsida.dto.EmployeeCreateRequest;
import com.delivery.habsida.dto.EmployeeCreateResponse;
import com.delivery.habsida.entity.Role;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.entity.User;
import com.delivery.habsida.exception.EmployeeAlreadyAssignedException;
import com.delivery.habsida.exception.RoleNotFoundException;
import com.delivery.habsida.exception.StoreNotFoundException;
import com.delivery.habsida.exception.UserNotFoundException;
import com.delivery.habsida.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserStoreAccessRepository userStoreAccessRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeCreateRequest request;
    private User savedUser;
    private Role role;

    @BeforeEach
    void setUp() {
        request = new EmployeeCreateRequest(
                "spider",
                "peter@peter",
                "peter",
                "Peter",
                "Parker",
                "998971356262",
                "MERCHANT"
        );

        savedUser = new User();
        savedUser.setId(10L);
        savedUser.setUserName("spider");
        savedUser.setEmail("peter@peter");
        savedUser.setFirstName("Peter");
        savedUser.setLastName("Parker");
        savedUser.setPhone("998971356262");

        role = new Role();
        role.setId(1L);
        role.setName("MERCHANT");
    }

    // ---------- createEmployee ----------
    @Test
    void createEmployee_shouldHashPasswordReturnResponse_whenRoleExist() {
        when(passwordEncoder.encode("peter")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(roleRepository.findByName("MERCHANT")).thenReturn(Optional.of(role));

        EmployeeCreateResponse response = employeeService.createEmployee(request);

        assertEquals(10L, response.id());
        assertEquals("peter@peter", response.email());
        assertEquals("MERCHANT",response.role());

        // проверяем, что пароль реально был захэширован, а не сохранён как есть
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("hashedPassword", userCaptor.getValue().getPassword());

        verify(userRoleRepository).save(any());
    }

    @Test
    void createEmployee_shouldThrowException_whenRoleDoesntExists() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(roleRepository.findByName("MERCHANT")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, ()-> employeeService.createEmployee(request));

        // связь с ролью не должна создаваться, если роли не существует
        verify(userRoleRepository, never()).save(any());
    }

    // ---------- assignEmployeeToStore ----------
    @Test
    void assignEmployeeToStore_shouldSaveAccess_whenUserAndStoreExistAndNotAssignedYet(){
        User user = new User();
        user.setId(10L);

        Store store = new Store();
        store.setId(5L);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));

        employeeService.assignEmployeeToStore(10L, 5L);

        verify(userStoreAccessRepository).save(any());
    }

    @Test
    void assignEmployeeToStore_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                ()->employeeService.assignEmployeeToStore(10L, 5L));

        verify(storeRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any());
    }

    @Test
    void assignEmployeeToStore_shouldThrowException_whenStoreNotFound() {

        User user = new User();
        user.setId(10L);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class,
                ()->employeeService.assignEmployeeToStore(10L, 5L));

        verify(userStoreAccessRepository, never()).save(any());
    }

    @Test
    void assignEmployeeToStore_shouldThrowException_whenAlreadyAssigned() {
        User user = new User();
        user.setId(10L);

        Store store = new Store();
        store.setId(5L);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(5L)).thenReturn(Optional.of(store));
        when(userStoreAccessRepository.existsByUserIdAndStoreId(10L, 5L)).thenReturn(true);

        assertThrows(EmployeeAlreadyAssignedException.class,
                ()->employeeService.assignEmployeeToStore(10L, 5L));

verify(userStoreAccessRepository, never()).save(any());

    }
}