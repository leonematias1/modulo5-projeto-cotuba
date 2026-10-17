package br.com.unipds.refactored.entities;

import java.nio.file.Path;

public class Capitulo {

	private String titulo;
	
	private Path arquivoMarkdown;
	
	private String markdown;
	
	private String html;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Path getArquivoMarkdown() {
		return arquivoMarkdown;
	}

	public void setArquivoMarkdown(Path arquivoMarkdown) {
		this.arquivoMarkdown = arquivoMarkdown;
	}

	public String getMarkdown() {
		return markdown;
	}

	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
		
}
