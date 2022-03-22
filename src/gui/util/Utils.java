package gui.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.entities.Integrante;
import model.entities.Projeto;

public class Utils {
	
	public static Stage currentStage (ActionEvent event) {
		return (Stage)(((Node)event.getSource()).getScene().getWindow());
	}
	
	public static Stage currentStage (MouseEvent event) {
		return (Stage)(((Node)event.getSource()).getScene().getWindow());
	}
	
	public static Integer tryParseToInt(String string) {
		try {
			return Integer.parseInt(string);
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	
	public static List<String> listComboBoxSituation() {
		List<String> list = new ArrayList<>();
		list.add("Executado");
		list.add("Em andamento");
		list.add("Não executado");
		return list;
	}
	
	public static List<String> comboBoxSituacaoProjetoPesquisa(List<String> list){
		list.add("Homologado");
		list.add("Não Homologado");
		return list;
	}
	
	public static String[] years(LocalDate date) {
		String[] years = new String[22];
		for(Integer i=0; i<years.length; i++) {
			if(i==0) {
				continue;
			}
			else {
				years[i] = ((Integer)(date.getYear() + 1 - i)).toString();
			}
		}
		return years;
	}
	
	public static String[] months(LocalDate date) {
		String[] months = new String[13];
		for (Integer i = 0; i < months.length; i++) {
			if (i == 0) {
				continue;
			} else {
				if(i<10) {
					months[i] = "0" + ((Integer) (date.getMonthValue() - date.getMonthValue() + i)).toString();
				}
				else {
					months[i] = ((Integer) (date.getMonthValue() - date.getMonthValue() + i)).toString();
				}
			}
		}
		return months;
	}
	
	public static String[] days(LocalDate date) {
		String[] days = new String[32];
		for(Integer i=0; i<days.length; i++) {
			if(i==0) {
				continue;
			}
			else {
				if(i<10) {
					days[i] = "0" + i.toString();
				}
				else {
					days[i] = i.toString();
				}
			}
		}
		return days;
	}
	
	public static List<String> listComboBoxFuncao(List<String> list){
		list.add("Coordenador(a)");
		list.add("Pesquisador(a)");
		list.add("Colaborador(a)");
		return list;
	}

	public static List<Integer> listComboBoxCargaHoraria(List<Integer> listCargaHoraria) {
		for(int i=1; i<21 ;i++) {
			listCargaHoraria.add(i);
		}
		return listCargaHoraria;
	}
	
	public static void formatDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}
	
	public static List<String> comboBoxDepartamentoProjeto(List<String> list){
		list.add("Estatística");
		list.add("Física");
		list.add("Geociências");
		list.add("Matemática");
		list.add("Química");
		return list;
	}
	
	public static List<String> comboBoxAreaProjeto(List<String> list){
		list.add("Estatística");
		list.add("Física");
		list.add("Geociências");
		list.add("Matemática");
		list.add("Química");
		list.add("Química Inorgânica");
		list.add("Química Analítica");
		list.add("Matemática Aplicada");
		return list;
	}
	
}
