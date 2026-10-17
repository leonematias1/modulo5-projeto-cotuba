package br.com.unipds.refactored;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import br.com.unipds.refactored.entities.ProgramSettings;
import br.com.unipds.refactored.service.EbookManipulatorService;
import br.com.unipds.refactored.service.FilterOptionsService;

public class Main {
	
	void main(String[] args) {
		
		try(SeContainer container =SeContainerInitializer.newInstance().initialize()){
			EbookManipulatorService ebookManipulatorService = container.select(EbookManipulatorService.class).get();
			FilterOptionsService filterOptionsService = container.select(FilterOptionsService.class).get();
			ProgramSettings settings = filterOptionsService.doReturnExecOptions(args);
			ebookManipulatorService.doCreateEbook(settings);
		}catch(Exception e) {
			 System.err.println();
             e.printStackTrace();
             System.exit(1);
		}
		
    }
	
}
