package br.com.unipds.refactored.code;

import java.util.List;

public class FileManipulator {

	public int doCreateFile(ProgramOptions options) {
		try {
			RenderizadorMarkdown renderizadorMarkdown = new RenderizadorMarkdown();
			List<String> htmls = renderizadorMarkdown.doRenderMD(options);
			if ("pdf".equals(options.getFormato())) 
	           new GeradorPDF().gerarPDF(htmls, options);
	         else if ("epub".equals(options.getFormato())) 
	        	new GeradorEPUB().gerarEPUB(htmls, options);
	         else 
	            throw new IllegalArgumentException("Formato do ebook inválido: " + options.getFormato());
            System.out.println("Arquivo gerado com sucesso: " + options.getArquivoDeSaida());
            return 0;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (options.isModoVerboso()) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
	}
		
}