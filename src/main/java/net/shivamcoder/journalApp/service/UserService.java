package net.shivamcoder.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.shivamcoder.journalApp.entity.User;
import net.shivamcoder.journalApp.repository.UserRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final PasswordEncoder encoder= new BCryptPasswordEncoder();
    private final UserRepo userRepository;
    @Autowired
    public UserService(UserRepo userRepository){
        this.userRepository=userRepository;
    }

    public boolean saveNewEntry(User user){
        try{
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;

        }catch (Exception e){
            log.error("Failed to create user :",e);
            return false;

        }

    }
    public void saveAdmin(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }


    public void saveEntry(User user){
        userRepository.save(user);
    }
    public List<User>getAll(){
       return userRepository.findAll();
    }
    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }
    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }
    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

}
