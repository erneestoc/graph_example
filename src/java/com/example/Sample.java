package com.example;

import com.github.javafaker.Faker;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphVertex;
import java.util.ArrayList;
import java.util.Random;

public class Sample {
    private static final Faker faker = new Faker();

    public static ArrayList<String> data(JanusGraph graph) {
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<JanusGraphVertex> users = generateUsers(graph);
        generateRandomFriendsRelations(graph, users);
        for (JanusGraphVertex u: users) {
            emails.add((String) u.property("email").value());
        }
        return emails;
    }

    private static ArrayList<JanusGraphVertex> generateUsers(JanusGraph graph) {
        System.out.println("[Sample] - Generating users.");
        ArrayList<JanusGraphVertex> vertices = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Integer randomInteger = new Random().nextInt(1000);
            String email = ""+randomInteger+faker.internet().emailAddress();
            JanusGraphVertex v = graph.addVertex("name", faker.name().firstName(), "email", email);
            vertices.add(v);
        }
        return vertices;
    }

    private static void generateRandomFriendsRelations(JanusGraph graph, ArrayList<JanusGraphVertex> users) {
        System.out.println("[Sample] - Generating friendships.");
        int size = users.size();
        for (int i = 0; i < size; i++) {
            for (int x = 0; x < 50; x++) {
                Integer randomInteger1 = new Random().nextInt(size);
                JanusGraphVertex user1 = users.get(randomInteger1);
                Integer randomInteger2 = new Random().nextInt(size);
                JanusGraphVertex user2 = users.get(randomInteger2);
                Boolean emailsAreNotEqual = getEmail(user1) != getEmail(user2);
                Boolean areNotFriends = usersAreNotFriends(graph, user1, user2);
                if (emailsAreNotEqual && areNotFriends) {
                    user1.addEdge("friendship", user2);
                }
            }
        }
        System.out.println("[Sample] - Friendships created.");
    }

    private static String getEmail(JanusGraphVertex v) {
        return (String) v.property("email").value();
    }

    private static Boolean usersAreNotFriends(JanusGraph graph, JanusGraphVertex v1, JanusGraphVertex v2) {
        String email1 = (String) v1.property("email").value();
        String email2 = (String) v2.property("email").value();
        return graph
                .traversal()
                .V()
                .has("email", email1)
                .out("friendship")
                .has("email", email2)
                .count()
                .next() == 0;
    }
}
