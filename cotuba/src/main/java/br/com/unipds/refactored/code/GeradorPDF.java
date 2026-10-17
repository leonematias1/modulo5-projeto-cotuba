package br.com.unipds.refactored.code;

import java.nio.file.Files;
import java.util.List;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.properties.AreaBreakType;

public class GeradorPDF {

	public void gerarPDF(List<String> htmls, ProgramOptions options) {
		
		 try (var writer = new PdfWriter(Files.newOutputStream(options.getArquivoDeSaida()));
            var pdf = new PdfDocument(writer);
            var pdfDocument = new Document(pdf)) {

            //TODO: definir título e autor para o livro
            pdf.getDocumentInfo().setTitle("Livro");
            pdf.getDocumentInfo().setAuthor("Autor");
          
            htmls.stream().forEach(html -> {
            	List<IElement> convertToElements = HtmlConverter.convertToElements(html);
            	
                if (pdf.getNumberOfPages() == 0) {
                    pdf.addNewPage();
                }
                PdfOutline rootOutline = pdf.getOutlines(false);
                if (rootOutline == null) {
                    pdf.initializeOutlines();
                    rootOutline = pdf.getOutlines(false);
                }

                PdfOutline chapterOutline = rootOutline.addOutline("Capítulo");
                chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                for (IElement element : convertToElements) {
                    pdfDocument.add((IBlockElement) element);
                }
                pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            
            });
          }catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar PDF: " + options.getArquivoDeSaida().toAbsolutePath(), ex);
        }
	}
	
}
