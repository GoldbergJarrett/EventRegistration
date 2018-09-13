package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import alert.Warning;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Registration extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	//NOTE: statements for db
	private Statement stmt;
	private Statement stmt2;

	// NOTE: Graph counters
	int accCount;
	int busCount;
	int compCount;
	int libCount;
	int nurCount;
	int otherCount;

	int femaleCount;
	int maleCount;
	
	//NOTE: Combo box
	final ComboBox statsComboBox = new ComboBox();

	//NOTE: Graph
	final static String computerscience = "Computer Science";
	final static String business = "Business";
	final static String nursing = "Nursing";
	final static String accounting = "Accounting";
	final static String liberalarts = "Liberal Arts";
	final static String other = "Other";

	//NOTE: Textfields
	TextField ecField = new TextField();
	TextField enField = new TextField();
	TextField eventDescField = new TextField();
	TextField studentIdField = new TextField();
	TextField ecGenField = new TextField();
	TextField firstField = new TextField("First Name");
	TextField lastField = new TextField("Last Name");
	TextField genderField = new TextField("Gender");
	TextField majorField = new TextField("Major");
	TextField idGenField = new TextField("Student ID");

	//NOTE: Labels
	Label lab_ecGen = new Label("This is your Event Code:");
	Label lab_title = new Label("Event Registration");
	Label lab_eventCode = new Label("Enter Event Code: ");
	Label lab_eventName = new Label("Event Name: ");
	Label lab_eventDesc = new Label("Event Description: ");
	Label lab_homeTitle = new Label("Event Registration");
	Label lab_studentId = new Label("Enter Your Student ID:");

	//NOTE: Pane Properties
	int paneWidth = 400;
	int paneHeight = 400;

	//NOTE: Other global properties
	int min = 10000;
	int max = 99999;
	XYChart.Series series = new XYChart.Series();

	String currentEvent;
	String currentStudent;

	Random rand = new Random();

	// NOTE: Scene/Stage
	Scene homeScene;
	Scene findScene;
	Scene createEventScene;
	Scene studentIdScene;
	Scene statsScene;
	Scene majorScene;
	Scene genderScene = new Scene(new Group());
	Stage prStage;

	Scene addStudentScene;

	public void start(Stage pStage) throws Exception
	{
		prStage = pStage;
		

		
		//NOTE: Pane options
		Pane findEventPane = new Pane();
		Pane homePane = new Pane();
		Pane createEventPane = new Pane();
		Pane studentIdPane = new Pane();
		Pane statsPane = new Pane();
		Pane addStudentPane = new Pane();
		
		
		//NOTE: Graph
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
        bc.setTitle("Majors Summary");
        xAxis.setLabel("Majors");       
        yAxis.setLabel("Students Attending");
		
		//NOTE: Title
		lab_title.setLayoutX(80);
		lab_title.setLayoutY(70);
		
		//home title
		lab_homeTitle.setLayoutX(150);
		lab_homeTitle.setLayoutY(20);
		
		//NOTE: Event Code
		//TODO: Must make character length limit
		lab_eventCode.setLayoutX(80);
		lab_eventCode.setLayoutY(80);
		lab_eventCode.setStyle("-fx-font: 24 arial;");
		ecField.setLayoutX(60);
		ecField.setLayoutY(120);
		ecField.setPrefSize(275.0, 100.0);
		ecField.setStyle("-fx-font: 64 arial;");
		
		//NOTE: Student ID
		lab_studentId.setLayoutX(120);
		lab_studentId.setLayoutY(80);
		studentIdField.setLayoutX(120);
		studentIdField.setLayoutY(100);
		
		//NOTE: Event Name
		lab_eventName.setLayoutX(5);
		lab_eventName.setLayoutY(43);
		enField.setLayoutX(5);
		enField.setLayoutY(70);
		
		//NOTE: Event Description
		lab_eventDesc.setLayoutX(5);
		lab_eventDesc.setLayoutY(123);
		eventDescField.setLayoutX(5);
		eventDescField.setLayoutY(150);
		
		//NOTE: Event Code Generator Fields
		ecGenField.setLayoutX(190);
		ecGenField.setLayoutY(50);
		ecGenField.setPrefSize(200.0, 75.0);
		lab_ecGen.setLayoutX(190);
		lab_ecGen.setLayoutY(30);
		ecGenField.setEditable(false);
		ecGenField.setStyle("-fx-font: 32 arial;");
		
		//NOTE: *Test* Fields for creating a student
		firstField.setLayoutX(10);
		firstField.setLayoutY(25);
		lastField.setLayoutX(10);
		lastField.setLayoutY(75);
		genderField.setLayoutX(10);
		genderField.setLayoutY(125);
		majorField.setLayoutX(10);
		majorField.setLayoutY(175);
		
		//NOTE: ID generator
		idGenField.setLayoutX(190);
		idGenField.setLayoutY(50);
		idGenField.setPrefSize(200.0, 75.0);
		idGenField.setEditable(false);
		idGenField.setStyle("-fx-font: 32 arial;");
		
		//NOTE: Search for event button
		Button searchBtn = new Button("Search");
		searchBtn.setLayoutX(230);
		searchBtn.setLayoutY(250);
		
		//NOTE: Find Event Button
		Button findEventBtn = new Button("Find Event");
		findEventBtn.setLayoutX(80);
		findEventBtn.setLayoutY(100);
		
		//NOTE: Create Event Button
		Button createEventBtn = new Button("Create Event");
		createEventBtn.setLayoutX(290);
		createEventBtn.setLayoutY(100);
		
		//NOTE: Statistics Button
		Button statsBtn = new Button("Statistics");
		statsBtn.setLayoutX(50);
		statsBtn.setLayoutY(50);
		
		//NOTE: Submit event button for creating an Event
		Button submitEventBtn = new Button("Create Event");
		submitEventBtn.setLayoutX(200);
		submitEventBtn.setLayoutY(150);
		
		//NOTE: Enter button for student IDs
		Button enterBtn = new Button("Enter");
		enterBtn.setLayoutX(280);
		enterBtn.setLayoutY(100);
		
		//NOTE: Pick statistic, click view
		Button viewBtn = new Button("View");
		viewBtn.setLayoutX(80);
		viewBtn.setLayoutY(100);
		
		//NOTE: combo box layout
		statsComboBox.getItems().addAll
		(
			"Genders",
			"Majors"
		);
		statsComboBox.setValue("Select Statistic");
		statsComboBox.setLayoutX(40);
		statsComboBox.setLayoutY(50);
		
		//NOTE: Create Event -> Home Page
		Button ce_backBtn = new Button("Back");
		ce_backBtn.setLayoutX(320);
		ce_backBtn.setLayoutY(150);
		
		//NOTE: Find Event -> Home Page
		Button fe_backBtn = new Button("Back");
		fe_backBtn.setLayoutX(290);
		fe_backBtn.setLayoutY(250);
		
		
		//TEST: Add student button. Not a function in final product.
		Button addStudentBtn = new Button("Add Student");
		addStudentBtn.setLayoutX(300);
		addStudentBtn.setLayoutY(50);
		
		//TEST: Submit student
		Button submitStudentBtn = new Button("Submit Student");
		submitStudentBtn.setLayoutX(200);
		submitStudentBtn.setLayoutY(175);
		
		//NOTE: Initialize Panes
		findEventPane.getChildren().addAll(ecField, lab_eventCode, fe_backBtn, searchBtn);
		homePane.getChildren().addAll(createEventBtn, findEventBtn, addStudentBtn, statsBtn, lab_homeTitle);
		createEventPane.getChildren().addAll(lab_eventName, enField, lab_eventDesc, eventDescField, 
				submitEventBtn, ce_backBtn, ecGenField, lab_ecGen);
		studentIdPane.getChildren().addAll(lab_studentId, studentIdField, enterBtn);
		addStudentPane.getChildren().addAll(firstField, lastField, genderField, majorField, idGenField, submitStudentBtn);
		statsPane.getChildren().addAll(statsComboBox, viewBtn);
		
		
		//NOTE: Initialize scenes
		findScene = new Scene(findEventPane, paneWidth, paneHeight);
		homeScene = new Scene(homePane, paneWidth, paneHeight / 2);
		createEventScene = new Scene(createEventPane, paneWidth, paneHeight / 2);
		studentIdScene = new Scene(studentIdPane, paneWidth, paneHeight / 2);
		addStudentScene = new Scene(addStudentPane, paneWidth, paneHeight);
		statsScene = new Scene(statsPane, paneWidth/2, paneHeight/2);
		majorScene = new Scene(bc,800,600);

		//NOTE: This is needed for the bar graph to add the data
        bc.getData().addAll(series);
		
		//Stage Setup
		prStage.setTitle("Event Registration");
		prStage.setScene(homeScene);
		prStage.setResizable(false);
		prStage.show();
		
		//NOTE: Button Functions
		enterBtn.setOnAction(e -> Enter());
		findEventBtn.setOnAction(e -> changeFindEvent());
		createEventBtn.setOnAction(e -> {changeCreateEvent(); initializeDB();});
		ce_backBtn.setOnAction(e -> Back());
		fe_backBtn.setOnAction(e -> Back());
		submitEventBtn.setOnAction(e -> createEvent());
		statsBtn.setOnAction(e-> changeStats());
		addStudentBtn.setOnAction(e -> changeAddStudent());
		viewBtn.setOnAction(e -> viewStats());
		
		searchBtn.setOnAction(e ->
		{
			if(eventCodeCheck())
				submit();
			else
				System.out.println("Event not found.");

		});
		
		
		/*************************************************************************************
		*TESTS
		*************************************************************************************/		
		submitStudentBtn.setOnAction(e -> 
		{
			if(textCheck())
			{
				createStudent();
				Warning.alert("Student Added Successfully!");
				System.out.println("Student successfully added.");
			}
			else
				Warning.warn("Failed to add student.");
		});
		
	}

	public void initializeDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// NOTE: Connect to the DB locally
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/eventregistration", "root", "");

			System.out.println("Database connected");
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

	}
	
	public void viewStats()
	{
		if (statsComboBox.getValue().equals("Majors"))
			prStage.setScene(majorScene);
		
		if (statsComboBox.getValue().equals("Genders"))
		{
			ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList(
            new PieChart.Data("Male", maleCount),
            new PieChart.Data("Female", femaleCount));
			final PieChart chart = new PieChart(pieChartData);
			chart.setTitle("Genders");
			((Group) genderScene.getRoot()).getChildren().add(chart);
			prStage.setScene(genderScene);
		}
	}

	public void submit() {
		try {
			initializeDB();
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

		try {
			ResultSet resultSet = stmt.executeQuery("SELECT EventID FROM events");
			while (resultSet.next()) {
				if (ecField.getText().equals(resultSet.getString(1))) {
					prStage.setScene(studentIdScene);
					stmt.close();
					resultSet.close();
					break;
				} else {
					System.out.println("Error.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void changeCreateEvent() {
		prStage.setScene(createEventScene);
	}

	public void changeStats() {
		countMajor();
		countGender();
		loadGraph();
		prStage.setScene(statsScene);
	}

	public void changeFindEvent() {
		prStage.setScene(findScene);
	}

	public void changeAddStudent() {
		prStage.setScene(addStudentScene);
	}

	public void Back() {
		if (prStage.getScene() == createEventScene)
			prStage.setScene(homeScene);

		if (prStage.getScene() == findScene)
			prStage.setScene(homeScene);

	}

	public boolean textCheck() {
		if (genderField.getText().equalsIgnoreCase("Male") || genderField.getText().equalsIgnoreCase("Female")) {
			if (majorField.getText().equalsIgnoreCase("Computer Science")) {
				return true;
			}

			if (majorField.getText().equalsIgnoreCase("Business")) {
				return true;
			}

			if (majorField.getText().equalsIgnoreCase("Nursing")) {
				return true;
			}

			if (majorField.getText().equalsIgnoreCase("Accounting")) {
				return true;
			}

			if (majorField.getText().equalsIgnoreCase("Liberal Arts")) {
				return true;
			}

			else {
				Warning.warn("Please enter a valid major.");
				return false;

			}

		} else
			Warning.warn("Please enter 'male' or 'female'.");
		return false;
	}

	public boolean eventCodeCheck() {
		if (ecField.getText().length() > 5 || ecField.getText().length() < 5) {
			Warning.warn("Please enter an event code that is assigned to an event.");
			return false;
		} else {
			currentEvent = ecField.getText();
			System.out.println(currentEvent);
			return true;
		}
	}

	public void Enter() {
		String insertStmt = ("INSERT INTO registered_students(StudentId, EventId) VALUE(" + currentStudent + ", "
				+ currentEvent + ");");

		try {
			initializeDB();
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

		try {
			ResultSet resultSet = stmt.executeQuery("SELECT StudentId FROM students;");
			while (resultSet.next()) {
				if (studentIdField.getText().equals(resultSet.getString(1))) {
					currentStudent = studentIdField.getText();
					System.out.print(currentStudent);
					stmt2.executeUpdate(insertStmt);
					studentIdField.clear();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void createEvent() {
		int code = rand.nextInt((max - min) + 1) + min;
		ecGenField.setText(code + "");

		// NOTE: SQL for making an Event
		String insertStmt = ("INSERT INTO events(EventId, EventName, EventDesc)" + "VALUES('"
				+ ecGenField.getText().trim() + "', '" + enField.getText().trim() + "', '"
				+ eventDescField.getText().trim() + "');");
		try {
			stmt.executeUpdate(insertStmt);
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

	}
	
	/*************************************************************************************
	 * GRAPHS
	 *************************************************************************************/
	public void countMajor() {
		try {
			initializeDB();
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

		try {
			ResultSet resultSet = stmt.executeQuery("SELECT major\r\n" + "FROM registered_students r, students s\r\n"
					+ "WHERE r.studentId = s.studentId\r\n" + "ORDER BY major");
			while (resultSet.next()) {
				if (resultSet.getString(1).equalsIgnoreCase("Accounting")) {
					accCount++;
					System.out.println(accCount + "Acc");
				} else if (resultSet.getString(1).equalsIgnoreCase("Business")) {
					busCount++;
					System.out.println(busCount + "Bus");
				} else if (resultSet.getString(1).equalsIgnoreCase("Computer Science")) {
					compCount++;
					System.out.println(compCount + "comp");
				} else if (resultSet.getString(1).equalsIgnoreCase("Liberal Arts")) {
					libCount++;
					System.out.println(libCount + "arts");
				} else if (resultSet.getString(1).equalsIgnoreCase("Nursing")) {
					nurCount++;
					System.out.println(nurCount + "nur");
				} else {
					otherCount++;
					System.out.println(otherCount + "other");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void countGender() {
		try {
			initializeDB();
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

		try {
			ResultSet resultSet = stmt.executeQuery("SELECT gender\r\n" + "FROM registered_students r, students s\r\n"
					+ "WHERE r.studentId = s.studentId\r\n" + "ORDER BY gender");
			
			while (resultSet.next()) {
				if (resultSet.getString(1).equalsIgnoreCase("Male")) 
					femaleCount++;
				
				else if (resultSet.getString(1).equalsIgnoreCase("Female"))
					maleCount++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadGraph() {
		series.getData().add(new XYChart.Data(computerscience, compCount));
		series.getData().add(new XYChart.Data(business, busCount));
		series.getData().add(new XYChart.Data(nursing, nurCount));
		series.getData().add(new XYChart.Data(accounting, accCount));
		series.getData().add(new XYChart.Data(liberalarts, libCount));
		series.getData().add(new XYChart.Data(other, otherCount));
	}

	/*************************************************************************************
	 * TESTS
	 *************************************************************************************/
	public void createStudent() {
		int id = rand.nextInt((max - min) + 1) + min;
		idGenField.setText(id + "");
		String insertStmt = ("INSERT INTO students(Student_First, Student_Last, Gender, Major, StudentId)" + "VALUES('"
				+ firstField.getText().trim() + "', '" + lastField.getText().trim() + "', '"
				+ genderField.getText().trim() + "', '" + majorField.getText().trim() + "', '"
				+ idGenField.getText().trim() + "');");

		try {
			initializeDB();
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}

		try {
			stmt.executeUpdate(insertStmt);
		} catch (Exception ex) {
			Warning.warn("Could not connect to Database: " + ex);
		}
	}

}
