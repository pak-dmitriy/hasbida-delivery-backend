package com.delivery.habsida.service;

import com.delivery.habsida.dto.EmployeeCreateRequest;
import com.delivery.habsida.dto.EmployeeCreateResponse;
import com.delivery.habsida.entity.*;
import com.delivery.habsida.exception.EmployeeAlreadyAssignedException;
import com.delivery.habsida.exception.RoleNotFoundException;
import com.delivery.habsida.exception.StoreNotFoundException;
import com.delivery.habsida.exception.UserNotFoundException;
import com.delivery.habsida.repository.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final StoreRepository storeRepository;
    private final UserStoreAccessRepository userStoreAccessRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           StoreRepository storeRepository,
                           UserStoreAccessRepository userStoreAccessRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.storeRepository = storeRepository;
        this.userStoreAccessRepository = userStoreAccessRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmployeeCreateResponse createEmployee(EmployeeCreateRequest request) {

        log.debug("Creating employee with email={}, role={}", request.email(), request.role());

        // 1. Создаем и заполняем сущность User
        User user = new User();

        user.setUserName(request.userName());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        // Обязательно шифруем пароль!
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPhone(request.phone());

        // Сохраняем пользователя в базу, чтобы получить его ID
        User savedUser = userRepository.save(user);

        // 2. Ищем роль в базе данных по имени
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + request.role()));

        // 3. Создаем связь в промежуточной таблице user_role
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);
        userRoleRepository.save(userRole);

       log.debug("Employee created with id={}", savedUser.getId());

        // 4. Возвращаем Response DTO
        return new EmployeeCreateResponse(
                savedUser.getId(),
                savedUser.getUserName(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getPhone(),
                role.getName()
        );
    }

    @Transactional
    public void assignEmployeeToStore(Long userId, Long storeId) {
        //Проверяем есть ли сотрудник и магазин в базе
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new StoreNotFoundException("Store not found"));

        if(userStoreAccessRepository.existsByUserIdAndStoreId(user.getId(), storeId)) {
            throw new EmployeeAlreadyAssignedException("Employee is already assigned to this store");
        }

        UserStoreAccess access = new UserStoreAccess();
        access.setUser(user);
        access.setStore(store);

        userStoreAccessRepository.save(access);
    }
}
