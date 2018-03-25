package com.example;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.TraversalStrategies;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;

public class ExampleTraversalSourceDsl extends GraphTraversalSource {

    public ExampleTraversalSourceDsl(final Graph graph, final TraversalStrategies traversalStrategies) {
        super(graph, traversalStrategies);
    }

    public ExampleTraversalSourceDsl(final Graph graph) {
        super(graph);
    }

    public GraphTraversal<Vertex, Vertex> user(String email) {
        GraphTraversalSource clone = this.clone();
        return clone.V().has("email", email);
    }
    public GraphTraversal<Vertex, Vertex> users() {
        GraphTraversalSource clone = this.clone();
        return clone.V().has("email");
    }
}


