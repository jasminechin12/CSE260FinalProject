import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Visualize extends Application{
	private Scene scene, graphScene;
	private Label title, label1, errorMessage, graphTitle, mutualFriends;
	private TextField fileName, person;
	private Stage primaryStage;
	private String[] line;
	private List<Name> vertices = new ArrayList<>();
	private List<AbstractGraph.Edge> edges = new ArrayList<>();
	private Graph<Name> graph;
	private double width = 1100;
	private double height = 800;
	private int x = 0;
	private int y = 0;
	private GraphView gv;
	private StackPane graphPane;
	
	@Override
	public void start(Stage primaryStage) {
		title = new Label("Analyze Your Friend Network");
		title.setFont(Font.loadFont("file:src/customfont/digital-7(mono).ttf", 65));
		title.setTextFill(Color.BLUE);
		
		Image image = new Image("images/network1.gif");
		ImageView background = new ImageView(image);
		background.setFitHeight(height);
		background.setFitWidth(width);
		background.setOpacity(.85);
		
		label1 = new Label("File Name:");
		label1.setFont(Font.font("Cambria", 30));
		fileName = new TextField();
		fileName.setMaxSize(600, 20);
		Button displayFriendNetwork = new Button();
		displayFriendNetwork.setText("Display");
		displayFriendNetwork.setOnAction(e -> changeScene(primaryStage));
		HBox hb = new HBox();
		hb.setMaxSize(500, 200);
		hb.getChildren().addAll(label1, fileName, displayFriendNetwork);
		hb.setSpacing(10);
		errorMessage = new Label("Please enter a valid file name");
		errorMessage.setFont(Font.font("Cambria", 40));
		errorMessage.setTextFill(Color.BLUE);
		Button exit = new Button();
		exit.setText("Exit");
		exit.setOnAction(e -> {
			System.exit(0);
		});
		
		StackPane pane = new StackPane();
		pane.getChildren().addAll(background, title, hb, errorMessage, exit);
		scene = new Scene(pane, width, height);
		position(hb, 20, 150, 1, 1);
		position(errorMessage, 0, 180, 1, 1);
		position(exit, 490, 350, 1, 1);
		
		primaryStage.setTitle("Analyze Your Friend Network");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void changeScene(Stage primaryStage) {
		try (Scanner input = new Scanner(new File(fileName.getText()))){
			while (input.hasNext()) {
				line = input.nextLine().split(" - ");
				Name q = new Name(line[0], 70 + (int)(Math.random()*900+1), 70 + (int)(Math.random()*630+1));
				if (!vertices.contains(q)) {
					vertices.add(q);
				}
				String[] friends = line[1].split(", ");
				for (String a : friends) {
					Name r = new Name(a, 70 + (int)(Math.random()*900+1), 70 + (int)(Math.random()*630+1));
					if (!vertices.contains(r))
						vertices.add(r);
					int n = vertices.indexOf(q);
					int f = vertices.indexOf(r);
					AbstractGraph.Edge e = new AbstractGraph.Edge(n,f);
					AbstractGraph.Edge e2 = new AbstractGraph.Edge(f, n);
					edges.add(e);
					edges.add(e2);
				}
			}
			
			graph = new UnweightedGraph<>(vertices,edges);
			
			graphTitle = new Label("Your Friend Network");
			graphTitle.setFont(Font.loadFont("file:src/customfont/digital-7(mono).ttf", 30));
			graphTitle.setTextFill(Color.CORNFLOWERBLUE);
			Button back = new Button();
			back.setText("Back");
			back.setOnAction(e -> start(primaryStage));
			Button graphExit = new Button();
			graphExit.setText("Exit");
			graphExit.setOnAction(e -> {
				System.exit(0);
			});
			mutualFriends = new Label("Your mutual friends will be shown here");
			mutualFriends.setFont(Font.loadFont("file:src/customfont/digital-7(mono).ttf", 30));
			mutualFriends.setTextFill(Color.CORNFLOWERBLUE);
			mutualFriends.setPrefWidth(900);
			Button getMutualFriends = new Button();
			getMutualFriends.setText("Find Me Friends!");
			getMutualFriends.setMaxHeight(30);
			person = new TextField();
			person.setMaxSize(100, 30);
			
			HBox hb2 = new HBox();
			hb2.setHgrow(mutualFriends, Priority.ALWAYS);
			hb2.getChildren().addAll(mutualFriends, getMutualFriends);
			hb2.setSpacing(10);
			
			getMutualFriends.setOnAction(e -> getMutualFriends());
			
			Image image2 = new Image("images/network.jpg");
			ImageView background2 = new ImageView(image2);
			background2.setFitHeight(height);
			background2.setFitWidth(width);
			background2.setOpacity(.3);
			
			graphPane = new StackPane();
			gv = new GraphView(graph);
			graphPane.getChildren().addAll(background2, graphTitle, gv, back, graphExit, person, hb2);
			position(back, -490, -380, 1, 1);
			position(graphTitle, 0, -380, 1, 1);
			position(graphExit, 490, -380, 1, 1);
			position(person, 450, 300, 1, 1);
			position(hb2, 20, 750, 1, 1);
			graphScene = new Scene(graphPane, width, height);
			primaryStage.setTitle("Analyze Your Friend Network");
			primaryStage.setResizable(false);
			primaryStage.setScene(graphScene);
			primaryStage.show();
			
		} catch (FileNotFoundException e) {
			errorMessage.setText("That file does not exist!");
			errorMessage.setTextFill(Color.RED);
		}
	}
	
	public void getMutualFriends() {
		Iterator it = gv.map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)(it.next());
			((Circle)(pair.getValue())).setStroke(Color.BLACK);
			((Circle)(pair.getValue())).setStrokeWidth(1);
		}
		mutualFriends.setText("Your mutual friends will be shown here");
		mutualFriends.setTextFill(Color.CORNFLOWERBLUE);
		
		ArrayList<String> sug = new ArrayList<String>();
		String suggested = "";
		List<Integer> friendsneighbors = new ArrayList<>();
		int k = graph.getIndex(new Name(person.getText()));
		if (k == -1) {
			mutualFriends.setText("The name you have entered is not valid!");
			mutualFriends.setTextFill(Color.RED);
		}
		List<Integer> myneighbors = graph.getNeighbors(k); 
		for (Integer i : myneighbors) 
			friendsneighbors.addAll(graph.getNeighbors(i));
		for (Integer i : myneighbors) {
			if (friendsneighbors.contains(i))
				friendsneighbors.removeAll(Collections.singleton(i));
		}
		if (friendsneighbors.contains(k))
			friendsneighbors.removeAll(Collections.singleton(k));
		for (Integer j : friendsneighbors) {
			if (!suggested.contains(graph.getVertex(j).toString()))
				suggested = suggested + graph.getVertex(j).toString() + ",";
		}
		for (Integer j : friendsneighbors) {
			Circle c = (Circle) gv.map.get(graph.getVertex(j));
			c.setStroke(Color.RED);
			c.setStrokeWidth(5);
		}
	
		
		mutualFriends.setText(suggested);
		mutualFriends.setFont(Font.loadFont("file:src/customfont/digital-7(mono).ttf", 30));
		
		Image image3 = new Image("images/balloons.gif");
		ImageView balloons = new ImageView(image3);
		balloons.setFitHeight(height);
		balloons.setFitWidth(width);
		FadeTransition ft = new FadeTransition(Duration.millis(10000), balloons);
		ft.setFromValue(0.9);
		ft.setToValue(0.0);
		ft.setCycleCount(1);
		ft.play();
		
		graphPane.getChildren().add(balloons);
	}
	
	static class Name implements Displayable {
	    private int x, y;
	    private String name;
	    
	    Name(String name){
	    	this.name = name;
	    }
	    
	    Name(String name, int x, int y) {
	      this.name = name;
	      this.x = x;
	      this.y = y;
	    }
	    
	    @Override 
	    public int getX() {
	      return x;
	    }
	    
	    @Override 
	    public int getY() {
	      return y;
	    }
	    
	    @Override 
	    public String getName() {
	      return name;
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	    	if (this == o)
	    		return true;
	    	if (o == null || o.getClass() != this.getClass())
	    		return false;
	    	Name z = (Name)o;
	    	return (z.name.equals(this.name) && z.hashCode() == this.hashCode());
	    }
	    
	    @Override
	    public int hashCode() {
	    	return 0;
	    }
	    
	    public String toString() {
	    	return name + "";
	    }
	  }
	
	public void position(Node node, double x, double y, double x2, double y2) {
		node.setTranslateX(x);
		node.setTranslateY(y);
		node.setScaleX(x2);
		node.setScaleY(y2);
	}

	public static void main(String[] args) {
		launch(args);

	}

}
