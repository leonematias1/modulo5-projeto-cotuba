package br.com.unipds.refactored.service;

import java.nio.file.Path;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.unipds.refactored.entities.Ebook;
import br.com.unipds.refactored.entities.ProgramSettings;
import br.com.unipds.refactored.enums.EbookTypes;
import br.com.unipds.refactored.repository.RenderizadorMarkDownRepository;

@ApplicationScoped
public class EbookManipulatorService {

	private final RenderizadorMarkdownService renderizadorMarkdownService;
	
	private final GeradorPDFService geradorPDFService;
	
	private final GeradorEPUBService geradorEPUBService;
	
	private final RenderizadorMarkDownRepository renderizadorMarkdownRepository;
	
	@Inject
	public EbookManipulatorService(
			RenderizadorMarkdownService renderizadorMarkdownService,
			GeradorPDFService geradorPDFService,
			GeradorEPUBService geradorEPUBService,
			RenderizadorMarkDownRepository renderizadorMarkdownRepository) {
		this.renderizadorMarkdownService=renderizadorMarkdownService;
		this.geradorPDFService = geradorPDFService;
		this.geradorEPUBService = geradorEPUBService;
		this.renderizadorMarkdownRepository = renderizadorMarkdownRepository;
	}
	
	public int doCreateEbook(ProgramSettings settings) {
		try {
			Ebook ebook = new Ebook();
			List<Path> arquivosMD = renderizadorMarkdownRepository.doGetMarkDowns(settings);
			ebook.setCapitulos(renderizadorMarkdownService.doRenderMD(arquivosMD));
			ebook.setFormato(settings.getFormato());
			ebook.setAutor("UNIPDS");
			ebook.setTitulo("Arquitetura e modelagem de dados");
			ebook.setArquivoDeSaida(settings.getArquivoDeSaida());
			if (EbookTypes.PDF.equals(settings.getFormato())) 
				geradorPDFService.gerar(ebook);
	         else if (EbookTypes.EPUB.equals(settings.getFormato())) 
	        	 geradorEPUBService.gerar(ebook);
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