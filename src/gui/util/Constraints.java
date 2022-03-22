package gui.util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Constraints {

	
	public static void setComboBoxFieldInteger(ComboBox<Integer> combo) {
		combo.skinProperty().addListener((obs,oldValue,newValue) -> {
			
		});
	}
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	       
			if (newValue != null && !newValue.matches("\\d*")) {
	        	txt.setText(oldValue);
	        }
			if(newValue.equals("0")) {
				txt.setText(oldValue);
			}
	     	
		});
	}
	
	public static void setTextFieldString(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			
			if (newValue != null && !newValue.matches("\\d{0,5}(([\\.]\\d{0,6})(([\\/]\\d{0,4})([\\-]\\d{0,2})?)?)?")) {
	        	txt.setText(oldValue);
	         }
			
		});
	}

	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
	        if (newValue != null && newValue.length() > max) {
	        	txt.setText(oldValue);
	        }
	    });
	}

	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
		    	if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
                    txt.setText(oldValue);
                }
		    });
	}
}