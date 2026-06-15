 package mk.ukim.finki.wp.kol2022.g3.service.impl;

import jakarta.websocket.Encoder;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUserType;
import mk.ukim.finki.wp.kol2022.g3.model.Interest;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidForumUserIdException;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.service.ForumUserService;
import mk.ukim.finki.wp.kol2022.g3.service.InterestService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ForumUserServiceImpl implements ForumUserService{
    private final InterestService interestService;
    private final ForumUserRepository forumUserRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<ForumUser> listAll() {
        return forumUserRepository.findAll();
    }

    @Override
    public ForumUser findById(Long id) {
        return forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException::new);
    }

    @Override
    public ForumUser create(String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        List<Interest> interests = new ArrayList<>();

        for(Long id : interestId){
            interests.add(interestService.findById(id));
        }

        ForumUser forumUser = new ForumUser(name,email, password, type, interests, birthday);

        return forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser update(Long id, String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        List<Interest> interests = new ArrayList<>();

        for(Long interestID : interestId){
            interests.add(interestService.findById(interestID));
        }

        ForumUser forumUser = findById(id);

        forumUser.setName(name);
        forumUser.setEmail(email);
        forumUser.setPassword(password);
        forumUser.setType(type);
        forumUser.setInterests(interests);
        forumUser.setBirthday(birthday);

        return forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser delete(Long id) {
        ForumUser forumUser = findById(id);
        forumUserRepository.delete(forumUser);
        return forumUser;
    }

    @Override
    public List<ForumUser> filter(Long interestId, Integer age) {
        if (interestId != null && age != null) {
            Interest interest = interestService.findById(interestId);
            LocalDate date = LocalDate.now().minusYears(age);
            return forumUserRepository.findAllByInterestsContainsAndBirthdayBefore(interest, date);
        } else if (interestId == null && age == null) {
            return listAll();
        } else if (interestId == null) {
            LocalDate date = LocalDate.now().minusYears(age);
            return forumUserRepository.findAllByBirthdayBefore(date);
        } else {
            Interest interest = interestService.findById(interestId);
            return forumUserRepository.findAllByInterestsContains(interest);
        }
    }

}
