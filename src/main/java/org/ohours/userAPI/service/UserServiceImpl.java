package org.ohours.userAPI.service;

import org.ohours.userAPI.controller.exception.UserIsMinorException;
import org.ohours.userAPI.controller.exception.UsernameTakenException;
import org.ohours.userAPI.model.MyUser;
import org.ohours.userAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    @Override
    public MyUser createUser(MyUser user) {
        if (repository.existsByUsername(user.getUsername()))
            throw new UsernameTakenException(user.getUsername());
        if (LocalDate.now().isBefore(user.getBirthdate().plusYears(18)))
            throw new UserIsMinorException();
        return repository.save(user);
    }

    @Override
    public Optional<MyUser> getUser(String username) {
        return repository.findByUsername(username);
    }
}
