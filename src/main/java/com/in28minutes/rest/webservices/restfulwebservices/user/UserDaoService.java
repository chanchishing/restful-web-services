package com.in28minutes.rest.webservices.restfulwebservices.user;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDaoService {

    private static int userCount=0;

    private static List<User> users=new ArrayList<>();
    static {
        users.add(new User(++userCount,"Adam", LocalDate.now().minusYears(30)));
        users.add(new User(++userCount,"Eve", LocalDate.now().minusYears(25)));
        users.add(new User(++userCount,"Jim", LocalDate.now().minusYears(20)));
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(int id) {
        //Predicate<User> predicate =  user->user.getId()==id;
        //return users.stream().filter(predicate).findFirst().get();

        User userFound=users.stream().filter(user->user.getId()==id).findFirst().orElse(null);

        return userFound;

    }

    public User saveUser(User user) {

        user.setId(++userCount);
        users.add(user);
        return  user;

    }

    public void deleteById(int id) {
        Predicate<User> predicate =  user->user.getId()==id;

        User userFound=users.stream().filter(predicate).findFirst().orElse(null);
        if (userFound==null){
            throw new UserNotFoundException("id:"+id);
        } else {
           users.removeIf(predicate);
        }

    }

}
