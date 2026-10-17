package br.com.unipds.refactored.code.service;

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

import br.com.unipds.refactored.code.entities.Ebook;

public class GeradorPDFService {

	public void gerarPDF(Ebook ebook) {
		
		 try (var writer = new PdfWriter(Files.newOutputStream(ebook.getArquivoDeSaida()));
            var pdf = new PdfDocument(writer);
            var pdfDocument = new Document(pdf)) {

            pdf.getDocumentInfo().setTitle(ebook.getTitulo());
            pdf.getDocumentInfo().setAuthor(ebook.getAutor());
          
            var iterator = ebook.getCapitulos().iterator();
            while (iterator.hasNext()) {
                var capitulo = iterator.next();

                String html = capitulo.getHtml();
            	List<IElement> convertToElements = HtmlConverter.convertToElements(html);
            	
                if (pdf.getNumberOfPages() == 0) 
                    pdf.addNewPage();
                
                PdfOutline rootOutline = pdf.getOutlines(false);
                if (rootOutline == null) {
                    pdf.initializeOutlines();
                    rootOutline = pdf.getOutlines(false);
                }

                PdfOutline chapterOutline = rootOutline.addOutline(capitulo.getTitulo());
                chapterOutline.addDestination(PdfExplicitDestination.createFit(pdf.getLastPage()));

                for (IElement element : convertToElements) 
                    pdfDocument.add((IBlockElement) element);
                
                if (iterator.hasNext()) {
                    pdfDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }
          }catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar PDF: " + ebook.getArquivoDeSaida().toAbsolutePath(), ex);
        }
	}
	
}
