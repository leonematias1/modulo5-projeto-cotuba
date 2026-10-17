package br.com.unipds.refactored.code;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

public class GeradorEPUB {

	public void gerarEPUB(List<String> htmls, ProgramOptions options) {
		try {
           var epub = new Book();
           
           epub.getMetadata().addTitle("Livro");
           epub.getMetadata().addAuthor(new Author("Autor"));

           boolean[] ehPrimeiroCapitulo = {true};

           htmls.forEach(html -> {
        	   String epubHtml = """
                       <html xmlns="http://www.w3.org/1999/xhtml">
                         <head>
                           <title>Capítulo</title>
                         </head>
                         <body>
                           %s
                         </body>
                       </html>
                     """.formatted(html);
             var chapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
             epub.addSection("Capítulo", chapter);
             if (ehPrimeiroCapitulo[0]) {
                 epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
                 ehPrimeiroCapitulo[0] = false;
             }
           });

            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(options.getArquivoDeSaida()));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + options.getArquivoDeSaida().toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + options.getArquivoDeSaida().toAbsolutePath(), ex);
        }
	}
	
}
