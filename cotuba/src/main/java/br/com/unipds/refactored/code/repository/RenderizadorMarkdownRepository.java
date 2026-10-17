package br.com.unipds.refactored.code.repository;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import br.com.unipds.refactored.code.entities.ProgramSettings;

public class RenderizadorMarkdownRepository {

	public List<Path> doGetMarkDowns(ProgramSettings settings) {
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
        try (Stream<Path> streamMDs = Files.list(settings.getDiretorioDosMD())) {
            List<Path> arquivosMD = streamMDs
                    .filter(matcher::matches)
                    .sorted()
                    .toList();

            if (arquivosMD.isEmpty()) 
                throw new IllegalStateException("Não foram encontrados capítulos (arquivos .md) no diretório: " + settings.getDiretorioDosMD().toAbsolutePath());
            
            return arquivosMD;
        } catch (IOException ex) {
            throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + settings.getDiretorioDosMD().toAbsolutePath(), ex);
        }  
	}
        
}
