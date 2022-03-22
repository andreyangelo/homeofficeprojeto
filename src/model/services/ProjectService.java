package model.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.entities.Projeto;

public class ProjectService {
	
	public List<Projeto> findAll(){
		List<Projeto> list = new ArrayList<Projeto>();
		list.add(new Projeto(1,"Em execução", "Derivada", LocalDate.of(2020, 9, 18), LocalDate.of(2020, 9, 19), 100));
		list.add(new Projeto(2, "Não executado", "Integral", LocalDate.of(2020, 9, 18), LocalDate.of(2020, 9, 19), 101));
		return list;
	}

}
