package br.com.unipds.refactored.code.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import br.com.unipds.refactored.code.entities.Capitulo;
import br.com.unipds.refactored.code.entities.ProgramSettings;
import br.com.unipds.refactored.code.repository.RenderizadorMarkdownRepository;

public class RenderizadorMarkdownService {

	
	public List<Capitulo> doRenderMD(ProgramSettings settings) {
		RenderizadorMarkdownRepository renderizadorMarkdownRepository = new RenderizadorMarkdownRepository();
		List<Path> arquivosMD = renderizadorMarkdownRepository.doGetMarkDowns(settings);
        return arquivosMD.stream().map(arquivoMD -> {
        	Capitulo capitulo = new Capitulo();
            Parser parser = Parser.builder().build();
            Node document = null;
            try {
            	String markdown = Files.readString(arquivoMD);
            	capitulo.setArquivoMarkdown(arquivoMD);
            	capitulo.setMarkdown(markdown);
                document = parser.parse(markdown);
                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            capitulo.setTitulo(((Text) heading.getFirstChild()).getLiteral());
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }

                });
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao fazer parse do arquivo " + arquivoMD, ex);
            }
            try {
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                capitulo.setHtml(renderer.render(document));
                return capitulo;
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + arquivoMD, ex);
            }
        }).toList();
	}
	
}
