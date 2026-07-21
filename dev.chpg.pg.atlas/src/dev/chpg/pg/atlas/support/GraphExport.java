package dev.chpg.pg.atlas.support;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.ensoftcorp.atlas.core.db.graph.Edge;
import com.ensoftcorp.atlas.core.db.graph.Graph;
import com.ensoftcorp.atlas.core.db.graph.Node;
import com.ensoftcorp.atlas.core.db.notification.NotificationMap;

import dev.chpg.pg.exporter.DgbExporter;
import dev.chpg.pg.exporter.ExportAttributeValue;
import dev.chpg.pg.exporter.ExportEdge;
import dev.chpg.pg.exporter.ExportGraph;
import dev.chpg.pg.exporter.ExportNode;

public class GraphExport {
	
	public static void exportGraph(Graph graph, File outputFile) throws Exception {
        DgbExporter exporter = new DgbExporter();
        exporter.export(new ExportGraphAdapter(graph), outputFile.getAbsolutePath());
	}
	
	 private static class ExportGraphAdapter implements ExportGraph {
	        private final IdGenerator idGenerator;
	        private final IntIntMap idMap;
	        private final List<ExportNode> nodes;
	        private final List<ExportEdge> edges;

	        ExportGraphAdapter(Graph graph) {
	            this.idGenerator = new IdGenerator();
	            long numElements = graph.nodes().size() + graph.edges().size();
	            if (numElements > Integer.MAX_VALUE) {
	                throw new RuntimeException("Atlas graph exceeds max DGB graph size.");
	            }
	            this.idMap = new IntIntMap((int) numElements);
	            this.nodes = StreamSupport.stream(graph.nodes().spliterator(), false /* DO NOT PARALLELIZE -- idGenerator/idMap are not thread safe */)
	                .map(node -> new ExportNodeAdapter(node, idMap, idGenerator))
	                .collect(Collectors.toList());
	            this.edges = StreamSupport.stream(graph.edges().spliterator(), false /* DO NOT PARALLELIZE -- idGenerator/idMap are not thread safe */)
                    .map(edge -> new ExportEdgeAdapter(edge, idMap, idGenerator))
                    .collect(Collectors.toList());
	        }

	        @Override
	        public Iterable<? extends ExportNode> nodes() {
	            return nodes;
	        }

	        @Override
	        public Iterable<? extends ExportEdge> edges() {
	            return edges;
	        }
	    }

	    private static class ExportNodeAdapter implements ExportNode {
	        private final Node node;
	        private final int id;

	        ExportNodeAdapter(Node node, IntIntMap idMap, IdGenerator idGenerator) {
	            this.node = node;
	            this.id = idGenerator.createNodeId();
	            idMap.put(node.addressBits(), this.id);
	        }

	        @Override
	        public int id() {
	            return id;
	        }

	        @Override
	        public Iterable<String> tags() {
	            return node.tags();
	        }

	        @Override
	        public Map<String, ExportAttributeValue> attributes() {
	            return convertAttributes(node.attr());
	        }
	    }

	    private static class ExportEdgeAdapter implements ExportEdge {
	        private final Edge edge;
	        private final int id;
	        private final IntIntMap idMap;

	        ExportEdgeAdapter(Edge edge, IntIntMap idMap, IdGenerator idGenerator) {
	            this.edge = edge;
	            this.id = idGenerator.createNodeId();
	            idMap.put(edge.addressBits(), this.id);
	            this.idMap = idMap;
	        }

	        @Override
	        public int id() {
	            return id;
	        }

	        @Override
	        public int sourceId() {
	            return idMap.get(edge.from().addressBits());
	        }

	        @Override
	        public int targetId() {
	            return idMap.get(edge.to().addressBits());
	        }

	        @Override
	        public Iterable<String> tags() {
	            return edge.tags();
	        }

	        @Override
	        public Map<String, ExportAttributeValue> attributes() {
	            return convertAttributes(edge.attr());
	        }
	    }

	    private static Map<String, ExportAttributeValue> convertAttributes(NotificationMap<String, Object> attrs) {
	        Map<String, ExportAttributeValue> expAttrs = new HashMap<>();
	        for (String key : attrs.keys()) {
	            Object val = attrs.get(key);
	            if (val instanceof String) {
	                expAttrs.put(key, ExportAttributeValue.ofString((String) val));
	            } else if (val instanceof Integer) {
	                expAttrs.put(key, ExportAttributeValue.ofInt((Integer) val));
	            } else if (val instanceof Long) {
	                expAttrs.put(key, ExportAttributeValue.ofLong((Long) val));
	            } else if (val instanceof Double) {
	                expAttrs.put(key, ExportAttributeValue.ofDouble((Double) val));
	            } else if (val instanceof Boolean) {
	                expAttrs.put(key, ExportAttributeValue.ofBoolean((Boolean) val));
	            } else if (val instanceof byte[]) {
	                expAttrs.put(key, ExportAttributeValue.ofByteArray((byte[]) val));
	            } else {
	            	// default to just toString
	            	expAttrs.put(key, ExportAttributeValue.ofString(val.toString()));
	            }
	        }
	        return expAttrs;
	    }
//	
//	public static Graph exportSchema() {
//		GlobalGraph schema = new GlobalGraph();
//		
//		Map<String,Node> tags = new HashMap<String,Node>();
//		for(String tag : XCSG.HIERARCHY.registeredTags()) {
//			Node node = schema.createNode();
//			node.tags().add(tag);
//			tags.put(tag, node);
//			schema.addNode(node);
//		}
//		
//		// create XCSG tag hierarchy
//		for(String childTag : tags.keySet()) {
//			Node childNode = tags.get(childTag);
//			for(String ancestorTag : XCSG.HIERARCHY.ancestorsSet(childTag)) {
//				Node ancestorNode = tags.get(ancestorTag);
//				Edge edge = schema.createEdge(ancestorNode, childNode);
//				schema.addEdge(edge);
//			}
//		}
//		
//		return schema;
//	}
	
	
}
