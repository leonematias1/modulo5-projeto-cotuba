package br.com.unipds.refactored.repository;

import java.nio.file.Path;
import java.util.List;

import br.com.unipds.refactored.entities.ProgramSettings;

public interface RenderizadorMarkDownRepository {

	List<Path> doGetMarkDowns(ProgramSettings settings);

}