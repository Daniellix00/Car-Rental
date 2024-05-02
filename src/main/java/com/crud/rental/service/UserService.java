package com.crud.rental.service;

import com.crud.rental.domain.User;
import com.crud.rental.exception.UserNotFoundException;
import com.crud.rental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<User> getAllUsers(){return  userRepository.findAll();}
    public User getUserById(final Long userId) throws UserNotFoundException{
      return   userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
    public void createUser(User user){ userRepository.save(user);}
    public void deleteUser(final Long userId) throws UserNotFoundException{
        userRepository.delete(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
    }


}
