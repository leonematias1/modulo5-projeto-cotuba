package br.com.unipds.refactored;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

class MainIntegrationTest {

    @TempDir
    Path diretorioDosMd;

    private Path arquivoMd;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;
    private PrintStream capturedErr;

    @BeforeEach
    void setUp() throws Exception {
        capturedErr = new PrintStream(errContent);
        System.setErr(capturedErr);

        arquivoMd = diretorioDosMd.resolve("01-introducao.md");
        Files.writeString(arquivoMd, "# Capítulo Teste\n\nEste é um conteúdo de um arquivo Markdown.");
    }

    @AfterEach
    void tearDown() {
        System.setErr(originalErr);
        capturedErr.close();
    }

    @Test
    @DisplayName("Deve gerar o arquivo PDF corretamente e conter o texto do Markdown.")
    void deveGerarPdfComSucesso() throws Exception {
        Path arquivoSaida = diretorioDosMd.resolve("saida.pdf");

        new Main().main(new String[]{
                "-d", diretorioDosMd.toString(),
                "-f", "pdf",
                "-o", arquivoSaida.toString()
        });

        assertThat(arquivoSaida).exists().isRegularFile();

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(arquivoSaida.toFile()))) {
            assertThat(pdfDoc.getNumberOfPages()).isEqualTo(1);
            String textoDaPagina = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1));
            assertThat(textoDaPagina)
                    .contains("Capítulo Teste")
                    .contains("Este é um conteúdo de um arquivo Markdown.");
        }
    }

    @Test
    @DisplayName("Deve gerar o arquivo EPUB corretamente e conter o HTML renderizado")
    void deveGerarEpubComSucesso() throws Exception {
        Path arquivoSaida = diretorioDosMd.resolve("saida.epub");

        new Main().main(new String[]{
                "-d", diretorioDosMd.toString(),
                "-f", "epub",
                "-o", arquivoSaida.toString()
        });

        assertThat(arquivoSaida).exists().isRegularFile();

        Book epubLido;
        try (var entradaEpub = Files.newInputStream(arquivoSaida)) {
            epubLido = new EpubReader().readEpub(entradaEpub);
        }

        String htmlDoCapitulo = new String(
                epubLido.getSpine().getResource(0).getData(),
                StandardCharsets.UTF_8);

        assertThat(htmlDoCapitulo)
                .contains("<h1>Capítulo Teste</h1>")
                .contains("<p>Este é um conteúdo de um arquivo Markdown.</p>");
    }

    @Test
    @DisplayName("Deve rejeitar um formato que não existe.")
    void deveRejeitarFormatoInvalido() {
        Path arquivoSaida = diretorioDosMd.resolve("saida.mobi");

        assertThatThrownBy(() -> new Main().main(new String[]{
                "-d", diretorioDosMd.toString(),
                "-f", "mobi",
                "-o", arquivoSaida.toString()
        }))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("MOBI");

        assertThat(arquivoSaida).doesNotExist();
    }

    @Test
    @DisplayName("Deve exibir erro e não gerar arquivo quando não há arquivos .md.")
    void deveInformarErroQuandoNaoHaArquivosMd() throws Exception {
        Files.deleteIfExists(arquivoMd);
        Path arquivoSaida = diretorioDosMd.resolve("saida.pdf");

        new Main().main(new String[]{
                "-d", diretorioDosMd.toString(),
                "-f", "pdf",
                "-o", arquivoSaida.toString()
        });

        assertThat(errContent.toString()).contains("Não foram encontrados capítulos");
        assertThat(arquivoSaida).doesNotExist();
    }

}
