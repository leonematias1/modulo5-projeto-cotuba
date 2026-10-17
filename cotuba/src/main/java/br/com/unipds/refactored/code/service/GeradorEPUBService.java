package br.com.unipds.refactored.code.service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import br.com.unipds.refactored.code.entities.Capitulo;
import br.com.unipds.refactored.code.entities.Ebook;
import br.com.unipds.refactored.code.entities.ProgramSettings;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

public class GeradorEPUBService {

	public void gerarEPUB(Ebook ebook) {
		try {
           var epub = new Book();
           
           epub.getMetadata().addTitle(ebook.getTitulo());
           epub.getMetadata().addAuthor(new Author(ebook.getAutor()));

           boolean[] ehPrimeiroCapitulo = {true};

           ebook.getCapitulos().forEach(capitulo -> {
        	   String html = capitulo.getHtml();
        	   String titulo = capitulo.getTitulo();
        	   String epubHtml = """
                       <html xmlns="http://www.w3.org/1999/xhtml">
                         <head>
                           <title>%s</title>
                         </head>
                         <body>
                           %s
                         </body>
                       </html>
                     """.formatted(titulo, html);
             var chapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
             epub.addSection(titulo, chapter);
             if (ehPrimeiroCapitulo[0]) {
                 epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                 ehPrimeiroCapitulo[0] = false;
             }
           });
            var epubWriter = new EpubWriter();
            try {
                epubWriter.write(epub, Files.newOutputStream(ebook.getArquivoDeSaida()));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + ebook.getArquivoDeSaida().toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + ebook.getArquivoDeSaida().toAbsolutePath(), ex);
        }
	}
	
}
