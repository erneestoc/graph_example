package com.example;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.util.ArrayList;


class Example {
    public static void main(String[] args) {
        JanusGraph graph = getGraph();

        graph.addVertex("name", "ernesto", "email", "ernesto@fond.co");

        GraphTraversal<Vertex, Vertex> traversal = graph
                .traversal()
                .V()
                .has("email", "ernesto@fond.co");

        Vertex v = traversal.next();

        System.out.println("Hola " + v.property("name").value());

        ArrayList<String> userList = Sample.data(graph);
        System.out.println("[Example] - Generated sample data.");
        Long userSize = graph.traversal(ExampleTraversalSource.class)
            .users().count().next();
        System.out.println("[Example] - Evaluating for "+userSize+" users.");
        Long friendsSize = graph.traversal(ExampleTraversalSource.class)
            .users().friends().count().next();
        System.out.println("[Example] - Evaluating for "+friendsSize+" friendships.");
        String email1 = userList.get(1);
        ExampleTraversal<Vertex, Vertex> user1Traversal = graph.traversal(ExampleTraversalSource.class)
                            .user(email1);
        String name = (String) user1Traversal.next().property("name").value();
        Long friendSize = graph.traversal(ExampleTraversalSource.class)
                            .user(email1).friends().count().next();
        System.out.println("[Example] - User "+name+" has "+friendSize+" friends.");
        String friendsOfFriends = graph.traversal(ExampleTraversalSource.class)
                            .user(email1).suggestedFriends().next().toString();
        System.out.println(friendsOfFriends);
        System.exit(0);
    }

    public static final JanusGraph getGraph() {
        JanusGraph g = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
        buildIndexFor(g);
        return g;
    }

    private static final void buildIndexFor(JanusGraph graph) {
        JanusGraphManagement mgmt = graph.openManagement();

        PropertyKey emailKey = mgmt.makePropertyKey("email")
                .dataType(String.class)
                .cardinality(Cardinality.SINGLE)
                .make();

        mgmt.buildIndex("byEmailUnique", Vertex.class)
                .addKey(emailKey)
                .unique()
                .buildCompositeIndex();

        mgmt.commit();
    }
}