package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();
    private final Logger log = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try(BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")) {
            try(ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getDate("created").toLocalDate()));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")) {
            try(ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"),
                            it.getInt("city_id")));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE post SET name = (?), description = (?), created = (?)" +
                     "WHERE id = (?)")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(post.getCreated()));
            ps.setInt(4, post.getId());
            ps.execute();
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
    }

    private void create(Post post) {
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name, description, created) " +
                    "VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(post.getCreated()));
            ps.execute();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE \"user\" SET name = (?), email = (?), password = (?) WHERE id = (?)")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.execute();
        } catch (SQLException e) {
            log.error("Exception in PsqlStore:", e);
            throw new IllegalArgumentException("Данный email зарегистрирован");
        }
    }

    private void create(User user) {
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO \"user\"(name, email, password) " +
                    "VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            log.error("Exception in PsqlStore:", e);
                throw new IllegalArgumentException("Данный email зарегистрирован");
        }
    }
    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = (?), city_id = ? WHERE id = (?)")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity_id());
            ps.setInt(3, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
    }

    private void create(Candidate candidate) {
        try(Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("INSERT INTO candidate(name, city_id) VALUES (?,?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity_id());
            ps.execute();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    candidate.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
    }

    @Override
    public Post findPostById(int id) {
        try(Connection cn = pool.getConnection();
            PreparedStatement st = cn.prepareStatement("select * from post where id = (?)")) {
            st.setInt(1, id);
            try(ResultSet it = st.executeQuery()) {
                if (it.next()) {
                    return new Post(id, it.getString("name"), it.getString("description"), it.getDate("created").toLocalDate());
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        try(Connection cn = pool.getConnection();
            PreparedStatement st = cn.prepareStatement("select * from candidate where id = (?)")) {
            st.setInt(1, id);
            try(ResultSet it = st.executeQuery()) {
                if (it.next()) {
                    return new Candidate(id, it.getString("name"), it.getInt("city_id"));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return null;
    }

    @Override
    public User findUserById(int id) {
        try(Connection cn = pool.getConnection();
            PreparedStatement st = cn.prepareStatement("select * from \"user\" where id = (?)")) {
            st.setInt(1, id);
            try(ResultSet it = st.executeQuery()) {
                if (it.next()) {
                    return new User(id, it.getString("name"),
                            it.getString("email"), it.getString("password"));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return null;
    }

    @Override
    public User findUserByMail(String mail) {
        try(Connection cn = pool.getConnection();
            PreparedStatement st = cn.prepareStatement("select * from \"user\" where email = (?)")) {
            st.setString(1, mail);
            try(ResultSet it = st.executeQuery()) {
                if (it.next()) {
                    return new User(it.getInt("id"), it.getString("name"),
                            it.getString("email"), it.getString("password"));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return null;
    }

    @Override
    public void removeCandidate(int id) {
        try(Connection cn = pool.getConnection();
            PreparedStatement st = cn.prepareStatement("delete from candidate where id = (?)")) {
            st.setInt(1, id);
            st.execute();
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
    }

    @Override
    public Collection<String> findAllCities() {
        List<String> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city")) {
            try(ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(it.getString("name"));
                }
            }
        } catch (Exception e) {
            log.error("Exception in PsqlStore:", e);
        }
        return cities;
    }
}
