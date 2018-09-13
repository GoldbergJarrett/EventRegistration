package alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Warning 
{
	Stage warningStage;
	

	public static void warn(String error)
	{		
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setTitle("ERROR");
		alert.setHeaderText("ERROR");
		alert.setContentText(error);
		
		alert.show();

	}
	
	public static void alert(String msg)
	{		
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setTitle("Event Registration");
		alert.setHeaderText("Alert!");
		alert.setContentText(msg);
		
		alert.show();

	}
}