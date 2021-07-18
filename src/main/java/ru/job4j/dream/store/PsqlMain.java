package ru.job4j.dream.store;

import ru.job4j.dream.model.Post;

import java.time.LocalDate;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java", "Java", LocalDate.now()));
        store.save(new Post(0, "Java1", "Java1", LocalDate.now()));
        store.save(new Post(0, "Java2", "Java3", LocalDate.now()));
        store.save(new Post(0, "Java3", "Java3", LocalDate.now()));

        for (Post post : store.findAllPosts()) {
            System.out.println(post);
        }

        store.save(new Post(3, "edited", "edited", LocalDate.now().minusDays(3)));

        for (Post post : store.findAllPosts()) {
            System.out.println(post);
        }
    }
}
