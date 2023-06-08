import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import java.text.NumberFormat;

/**
This class creates a GUI to calculate cumulative GPA and points for a specific course.
@author Arhaan Sami
*/
public class GPACalc extends Application{
	private TextField letterGradeIn;
	private TextField creditHrsIn;
	private TextField nameIn;
	private Text messageOut;
	private Text cumulativeGPAOut;
	private Button addButton;
	private Button clearButton;
    private TableView courseTable = new TableView<Course>();
	
	public void start(Stage primaryStage){
		primaryStage.setTitle("GPA Calculator");
		
		courseTable.setEditable(true);

        TableColumn nameColumn = new TableColumn<Course, String>("Course Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));

        TableColumn gradeColumn = new TableColumn<Course, String>("Grade");
        gradeColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("courseGrade"));

        TableColumn creditHrsColumn = new TableColumn<Course, Integer>("Credit Hours");
        creditHrsColumn.setCellValueFactory(new PropertyValueFactory<Course, Integer>("noCredits"));

        courseTable.getColumns().add(nameColumn);
        courseTable.getColumns().add(creditHrsColumn);
		courseTable.getColumns().add(gradeColumn);


        courseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label letterLabel = new Label("Course letter grade:");
		letterGradeIn = new TextField();
		Label hourLabel = new Label("Course credit hours:");
		creditHrsIn = new TextField();
		Label courseNameLabel = new Label("Course name:");
		nameIn = new TextField();
		letterGradeIn.setPrefWidth(50);
		creditHrsIn.setPrefWidth(50);
		nameIn.setPrefWidth(50);
		
		addButton = new Button("Add to GPA");
		addButton.setOnAction(this::processButtonPress);
		clearButton = new Button("Clear GPA");
		clearButton.setOnAction(this::processButtonPress);
		
		messageOut = new Text("Welcome to my GPA calculator!");
		cumulativeGPAOut = new Text("Enter your 1st grade & credit hrs.");
		

		FlowPane pane = 
			new FlowPane(messageOut,cumulativeGPAOut, courseNameLabel, nameIn, hourLabel, creditHrsIn, 
				letterLabel, letterGradeIn, addButton, clearButton, courseTable);
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(5);

		pane.setOrientation(Orientation.VERTICAL);
		
		Scene scene = new Scene(pane, 450, 410);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void processButtonPress(ActionEvent event){
		double gradeGPA;
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMaximumFractionDigits(1);
		formatter.setMinimumFractionDigits(1);
		int totalCreditHrs = 0;
		double totalPoints = 0;
		double cumulativeGPA = 0; 
		
		if (event.getSource() == addButton){
			String letterGrade = letterGradeIn.getText();
			String courseName = nameIn.getText();
			int courseNumCredits = Integer.parseInt(creditHrsIn.getText());
			boolean validInput = true;
			
			switch (letterGrade){
				case "A+": gradeGPA = 4.3; break;
				case "A": gradeGPA = 4.0; break;
				case "A-": gradeGPA = 3.7; break;
				case "B+": gradeGPA = 3.3; break;
				case "B": gradeGPA = 3.0; break;
				case "B-": gradeGPA = 2.7; break;
				case "C+": gradeGPA = 2.3; break;
				case "C": gradeGPA = 2.0; break;
				case "D": gradeGPA = 1.0; break;
				case "F": case "WF": gradeGPA = 0.0; break;
				default: gradeGPA = -1.0;
						 messageOut.setText("Invalid grade - GPA not changed");
						 int tableID = courseTable.getSelectionModel().getSelectedIndex();
						 courseTable.getItems().remove(tableID);
						 validInput = false;
						break;
			}
			
			if (gradeGPA > 0){
				totalCreditHrs += Integer.parseInt(creditHrsIn.getText());
				totalPoints += gradeGPA*totalCreditHrs;
				cumulativeGPA += totalPoints/totalCreditHrs;
				cumulativeGPAOut.setText("Your cumulative GPA is : " + formatter.format(cumulativeGPA));
			}

			if (validInput){
				courseTable.getItems().add(new Course(courseName, letterGrade, courseNumCredits));
			}
		}
		else{
			totalCreditHrs = 0;
			totalPoints = 0;
			cumulativeGPA = 0;
			messageOut.setText("Totals have been reset.");
			cumulativeGPAOut.setText("Enter your 1st grade & credit hrs.");
			letterGradeIn.clear();
			creditHrsIn.clear();
			nameIn.clear();
			courseTable.getItems().clear();
		}
	}
}
