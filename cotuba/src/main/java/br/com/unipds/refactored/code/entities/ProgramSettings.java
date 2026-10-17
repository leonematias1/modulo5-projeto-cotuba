package br.com.unipds.refactored.code.entities;

import java.nio.file.Path;

import br.com.unipds.refactored.code.enums.EbookTypes;

public class ProgramSettings {

	private Path diretorioDosMD;
	
	private EbookTypes formato;
	
	private String nomeDoArquivoDeSaidaDoEbook;
	
	private Path arquivoDeSaida;
	
	private boolean modoVerboso;

	public ProgramSettings(Path diretorioDosMD, EbookTypes formato, String nomeDoArquivoDeSaidaDoEbook,
			Path arquivoDeSaida, boolean modoVerboso) {
		super();
		this.diretorioDosMD = diretorioDosMD;
		this.formato = formato;
		this.nomeDoArquivoDeSaidaDoEbook = nomeDoArquivoDeSaidaDoEbook;
		this.arquivoDeSaida = arquivoDeSaida;
		this.modoVerboso = modoVerboso;
	}

	public Path getDiretorioDosMD() {
		return diretorioDosMD;
	}

	public void setDiretorioDosMD(Path diretorioDosMD) {
		this.diretorioDosMD = diretorioDosMD;
	}

	public EbookTypes getFormato() {
		return formato;
	}

	public void setFormato(EbookTypes nomeDoFormatoDoEbook) {
		this.formato = nomeDoFormatoDoEbook;
	}

	public String getNomeDoArquivoDeSaidaDoEbook() {
		return nomeDoArquivoDeSaidaDoEbook;
	}

	public void setNomeDoArquivoDeSaidaDoEbook(String nomeDoArquivoDeSaidaDoEbook) {
		this.nomeDoArquivoDeSaidaDoEbook = nomeDoArquivoDeSaidaDoEbook;
	}

	
	public Path getArquivoDeSaida() {
		return arquivoDeSaida;
	}

	public void setArquivoDeSaida(Path arquivoDeSaida) {
		this.arquivoDeSaida = arquivoDeSaida;
	}

	public boolean isModoVerboso() {
		return modoVerboso;
	}

	public void setModoVerboso(boolean modoVerboso) {
		this.modoVerboso = modoVerboso;
	}
	 
	
}
