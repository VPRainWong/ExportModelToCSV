package com.vp.plugin.sample.exportcsv.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.model.IModelElement;
import com.vp.plugin.model.IProject;
import com.vp.plugin.model.IUseCase;
import com.vp.plugin.model.factory.IModelElementFactory;

public class ExportCSVActionControl implements VPActionController {
	

	@Override
	public void performAction(VPAction arg0) {
		// Retrieve the project
		IProject project = ApplicationManager.instance().getProjectManager().getProject();

		// Retrieve all use case model elements into an array 
		IModelElement[] modelElements = project.toAllLevelModelElementArray(IModelElementFactory.MODEL_TYPE_USE_CASE);
		
		// assume the model element array is not empty  
		if (modelElements != null && modelElements.length > 0) {

			// Create a StringBuffer to store the output
			StringBuffer sb = new StringBuffer();
		
			// Insert header row in our CSV
			sb.append("use case id,name,description");
		
			// Insert a new line 
			sb.append("\n");
		
			
			// walk through its containing elements one by one
			for (int i = 0; i < modelElements.length; i++) {
				IUseCase usecase = (IUseCase) modelElements[i];
				
				// Insert the use case's user ID and separator
				sb.append(usecase.getUserID());
				sb.append(",");

				// Insert the use case's name and separator
				sb.append(usecase.getName());
				sb.append(",");

				// Insert the use case's description and a new line
				sb.append(usecase.getDescription());
				sb.append("\n");				
			}			
		
			// Create File Chooser to let user specify the output path
			JFileChooser fileChooser = ApplicationManager.instance().getViewManager().createJFileChooser();
			fileChooser.setDialogTitle("Export CSV");
			
			// Create a File Filter for CSV file type and set it as default
			FileFilter filter = new FileNameExtensionFilter("CSV File", "csv");
			fileChooser.addChoosableFileFilter(filter);		
			fileChooser.setFileFilter(filter);
			
			// Show up the File Chooser dialog
			int returnVal = fileChooser.showSaveDialog(null);
		
			// If provided a file and press Save button
			if (returnVal == fileChooser.APPROVE_OPTION) {
				
				// Obtain the file path user selected
				File outputFile = fileChooser.getSelectedFile();
				
				// Add csv extension if user haven't specify it
				if (!outputFile.getAbsolutePath().toLowerCase().endsWith(".csv")) {
					outputFile = new File(outputFile.getAbsolutePath() + ".csv");				
				}
				
				try {
					// Write the StringBuffer to file
					FileOutputStream fout = new FileOutputStream(outputFile);
					fout.write(sb.toString().getBytes());
					fout.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				
				// Print a message in Message Pane to tell user export was completed.
				ApplicationManager.instance().getViewManager().showMessage("CSV exported to " + outputFile.getAbsolutePath());
			}
		} else {
			ApplicationManager.instance().getViewManager().showMessage("No use case was found in the project");
		}

	}
	
	@Override
	public void update(VPAction arg0) {
		// TODO Auto-generated method stub
		
	}

}
