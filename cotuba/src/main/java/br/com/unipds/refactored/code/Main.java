package br.com.unipds.refactored.code;

public class Main {
	
	void main(String[] args) {
		ProgramOptions programOptions = new FilterOptions().doReturnExecOptions(args);
		if (programOptions == null)
			System.exit(1);
		try {
			new FileManipulator().doCreateFile(programOptions);
		}catch(Exception e) {
			if(programOptions.isModoVerboso()) {
				 System.err.println();
	             e.printStackTrace();
	             System.exit(1);
			}
		}
    }
	
}
