import java.util.HashMap;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphView<N extends Displayable> extends Pane {
  private Graph<N> graph;
  public Map<N,Circle> map = new HashMap<>();
  
  public GraphView(Graph<N> graph) {
    this.graph = graph;
    
    // Draw vertices
    java.util.List<N> vertices 
      = graph.getVertices();    
    for (int i = 0; i < graph.getSize(); i++) {
      int x = vertices.get(i).getX();
      int y = vertices.get(i).getY();
      String name = vertices.get(i).getName();
      
      Circle c = new Circle(x, y, 16);
      c.setFill(Color.CORNFLOWERBLUE);
      c.setStroke(Color.BLACK);
      map.put(vertices.get(i), c);
      Text n = new Text(x - 8, y - 18, name);
      DoubleProperty d1 = new SimpleDoubleProperty(-8);
      DoubleProperty d2 = new SimpleDoubleProperty(-20);
      n.xProperty().bind(c.centerXProperty().add(d1));
      n.yProperty().bind(c.centerYProperty().add(d2));
      c.setOnMouseDragged(e -> {
    	  c.setCenterX(e.getX());
    	  c.setCenterY(e.getY());
      });
      getChildren().addAll(c, n); // Display a vertex
//      getChildren().add(new Text(x - 8, y - 18, name)); 
    }
    
    // Draw edges for pairs of vertices
    for (int i = 0; i < graph.getSize(); i++) {
      java.util.List<Integer> neighbors = graph.getNeighbors(i);
      Circle c2 = map.get(vertices.get(i));
      int x1 = graph.getVertex(i).getX();
      int y1 = graph.getVertex(i).getY();
      for (int v: neighbors) {
    	Circle c3 = map.get(vertices.get(v));
        int x2 = graph.getVertex(v).getX();
        int y2 = graph.getVertex(v).getY();
        
        // Draw an edge for (i, v)
        Line l = new Line(x1, y1, x2, y2);
        getChildren().add(l); 
        c2.centerXProperty().bindBidirectional(l.startXProperty());
        c2.centerYProperty().bindBidirectional(l.startYProperty());
        c3.centerXProperty().bindBidirectional(l.endXProperty());
        c3.centerYProperty().bindBidirectional(l.endYProperty());
      }
    }
  }
}
