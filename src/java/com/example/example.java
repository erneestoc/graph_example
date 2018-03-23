package com.example;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.JanusGraphManagement;


class Example {
    public static void main(String[] args) {
        JanusGraph graph = getGraph();
        buildIndexFor(graph);
        
        graph.addVertex("name", "ernesto", "email", "ernesto@fond.co");

        GraphTraversal<Vertex, Vertex> traversal = graph
                .traversal()
                .V()
                .has("email", "ernesto@fond.co");

        Vertex v = traversal.next();

        System.out.print("Hola " + v.property("name").value());
    }

    private static final JanusGraph getGraph() {
        return JanusGraphFactory.build().set("storage.backend", "inmemory").open();
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