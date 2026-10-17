package br.com.unipds.refactored.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.com.unipds.refactored.entities.ProgramSettings;
import br.com.unipds.refactored.enums.EbookTypes;

@ApplicationScoped
public class FilterOptionsService {

	public ProgramSettings doReturnExecOptions(String[] args) {
		Options options = new Options();
		var opcaoDeDiretorioDosMD = new Option("d", "dir", true, "Diretório que contém os arquivos md. Default: diretório atual.");
		options.addOption(opcaoDeDiretorioDosMD);
		var opcaoDeFormatoDoEbook = new Option("f", "format", true, "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
		options.addOption(opcaoDeFormatoDoEbook);
		var opcaoDeArquivoDeSaida = new Option("o", "output", true, "Arquivo de saída do ebook. Default: book.{formato}.");
		options.addOption(opcaoDeArquivoDeSaida);
		var opcaoModoVerboso = new Option("v", "verbose", false,  "Habilita modo verboso.");
		options.addOption(opcaoModoVerboso);
		CommandLineParser cmdParser = new DefaultParser();
		
		@SuppressWarnings("deprecation")
		var ajuda = new HelpFormatter();
		try {
			CommandLine cmd = cmdParser.parse(options, args);
			Path diretorioDosMD;
			EbookTypes formato;
	        Path arquivoDeSaida;
	        boolean modoVerboso = false;
	        String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

            if (nomeDoDiretorioDosMD != null) {
                diretorioDosMD = Paths.get(nomeDoDiretorioDosMD);
                if (!Files.isDirectory(diretorioDosMD)) 
                    throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
            } else {
                Path diretorioAtual = Paths.get("");
                diretorioDosMD = diretorioAtual;
            }
	        
            String nomeDoFormatoDoEbook = cmd.getOptionValue("format");
            if (nomeDoFormatoDoEbook != null)
                formato = EbookTypes.valueOf(nomeDoFormatoDoEbook.toUpperCase());
             else 
                formato = EbookTypes.PDF;
            
            String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");
            if (nomeDoArquivoDeSaidaDoEbook != null)
                arquivoDeSaida = Paths.get(nomeDoArquivoDeSaidaDoEbook);
             else 
                arquivoDeSaida = Paths.get("book." + formato);
            
            modoVerboso = cmd.hasOption("verbose");
            
            try {
            	 if (Files.isDirectory(arquivoDeSaida)) 
                     Files.walk(arquivoDeSaida).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                 else 
                     Files.deleteIfExists(arquivoDeSaida);
            }catch(IOException ex) {
            	 System.err.println(ex.getMessage());
                 if (modoVerboso) {
                     System.err.println();
                     ex.printStackTrace();
                 }
                 return null;
            }
           
			return new ProgramSettings(diretorioDosMD, formato, nomeDoArquivoDeSaidaDoEbook,arquivoDeSaida, modoVerboso);
		} catch (ParseException e) {
	        System.err.println(e.getMessage());
	        ajuda.printHelp("cotuba", options);
	        return null;
		}
	}
}
