package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.Collection;

public interface Store {
    Collection<Post> findAllPosts();
    Collection<Candidate> findAllCandidates();
    void save(Post post);
    void save(Candidate candidate);
    void save(User user);
    Post findPostById(int id);
    Candidate findCandidateById(int id);
    User findUserById(int id);
    User findUserByMail(String mail);
    Collection<String> findAllCities();
    void removeCandidate(int id);
}
