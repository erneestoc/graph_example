package com.example;

import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.Scope;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.GremlinDsl;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Map;

@GremlinDsl(traversalSource = "com.example.ExampleTraversalSourceDsl")
public interface ExampleTraversalDsl<S, E> extends GraphTraversal.Admin<S, E> {
    public default GraphTraversal<S, Vertex> friends() {
        return out("friendship");
    }

    public default GraphTraversal<S, Map<Object, Long>> suggestedFriends() {
        GraphTraversal<S, Vertex> traversal = out("friendship")
                .as("myFriends");

        return traversal
    		.out("friendship")
    		.as("friendsOfFriends")
    		.groupCount()
            .by("email")
            .order(Scope.local)
            .by(Column.values, Order.decr);
    }
}
