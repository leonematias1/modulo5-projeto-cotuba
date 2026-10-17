package br.com.unipds.refactored.code.service;

import br.com.unipds.refactored.code.entities.Ebook;
import br.com.unipds.refactored.code.entities.ProgramSettings;
import br.com.unipds.refactored.code.enums.EbookTypes;

public class EbookManipulatorService {

	public int doCreateEbook(ProgramSettings settings) {
		
		RenderizadorMarkdownService renderizadorMarkdownService = new RenderizadorMarkdownService();
		
		try {
			Ebook ebook = new Ebook();
			ebook.setCapitulos(renderizadorMarkdownService.doRenderMD(settings));
			ebook.setFormato(settings.getFormato());
			ebook.setAutor("UNIPDS");
			ebook.setTitulo("Arquitetura e modelagem de dados");
			ebook.setArquivoDeSaida(settings.getArquivoDeSaida());
			if (EbookTypes.PDF.equals(settings.getFormato())) 
	           new GeradorPDFService().gerarPDF(ebook);
	         else if (EbookTypes.EPUB.equals(settings.getFormato())) 
	        	new GeradorEPUBService().gerarEPUB(ebook);
	         else 
	            throw new IllegalArgumentException("Formato do ebook inválido: " + settings.getFormato());
            System.out.println("Arquivo gerado com sucesso: " + settings.getArquivoDeSaida());
            return 0;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (settings.isModoVerboso()) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
	}
		
}