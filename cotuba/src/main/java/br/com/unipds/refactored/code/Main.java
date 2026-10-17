package br.com.unipds.refactored.code;

import br.com.unipds.refactored.code.entities.ProgramSettings;
import br.com.unipds.refactored.code.service.EbookManipulatorService;
import br.com.unipds.refactored.code.service.FilterOptionsService;

public class Main {
	
	void main(String[] args) {
		EbookManipulatorService ebookManipulatorService = new EbookManipulatorService();
		FilterOptionsService filterOptionsService = new FilterOptionsService();
		ProgramSettings settings = filterOptionsService.doReturnExecOptions(args);
		if (settings == null)
			System.exit(1);
		try {
			ebookManipulatorService.doCreateEbook(settings);
		}catch(Exception e) {
			if(settings.isModoVerboso()) {
				 System.err.println();
	             e.printStackTrace();
	             System.exit(1);
			}
		}
    }
	
}
