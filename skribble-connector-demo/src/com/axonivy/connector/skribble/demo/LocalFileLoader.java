package com.axonivy.connector.skribble.demo;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import ch.ivyteam.ivy.IvyConstants;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@SuppressWarnings("restriction")
public class LocalFileLoader {

	/**
	 * Loads files from webContent
	 */
	public static File loadFromWebContent(String relativePath)
			throws IOException, CoreException {
		IProject project = Ivy.request().getProcessModelVersion().getProject();

		IFile ifile = project.getFolder(IvyConstants.DIRECTORY_WEB_CONTENT).getFile(relativePath);
		var ivyFile = new File(ifile.getName(), true);
		java.nio.file.Path path = ivyFile.getJavaFile().toPath();
		Files.copy(ifile.getContents(), path, StandardCopyOption.REPLACE_EXISTING);

		return ivyFile;
	}

}
