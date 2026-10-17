package br.com.unipds.refactored.entities;

import java.nio.file.Path;
import java.util.List;

import br.com.unipds.refactored.enums.EbookTypes;

public class Ebook {

	private String titulo;
	
	private String autor;
	
	private EbookTypes formato;
	
	private List<Capitulo> capitulos;
	
	private Path arquivoDeSaida;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public EbookTypes getFormato() {
		return formato;
	}

	public void setFormato(EbookTypes formato) {
		this.formato = formato;
	}

	public List<Capitulo> getCapitulos() {
		return capitulos;
	}

	public void setCapitulos(List<Capitulo> capitulos) {
		this.capitulos = capitulos;
	}

	public Path getArquivoDeSaida() {
		return arquivoDeSaida;
	}

	public void setArquivoDeSaida(Path arquivoDeSaida) {
		this.arquivoDeSaida = arquivoDeSaida;
	}
	
}
