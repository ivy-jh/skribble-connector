package com.axonivy.connector.skribble.demo;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import ch.ivyteam.ivy.IvyConstants;
import ch.ivyteam.ivy.environment.Ivy;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Loads file from the source folder
 * 
 */
public class LocalFileLoader {

	/**
	 * Loads files from resources
	 * 
	 */
	public static ch.ivyteam.ivy.scripting.objects.File loadFromResource(String relativePath)
			throws IOException, CoreException {
		ch.ivyteam.ivy.scripting.objects.File file = new ch.ivyteam.ivy.scripting.objects.File(relativePath, true);
		file.createNewFile();

		return file;
	}

	/**
	 * Loads files from webContent
	 * 
	 */
	@SuppressWarnings("restriction")
	public static ch.ivyteam.ivy.scripting.objects.File loadFromWebContent(String relativePath)
			throws IOException, CoreException {
		IProject project = Ivy.request().getProcessModelVersion().getProject();

		IFile file = project.getFolder(IvyConstants.DIRECTORY_WEB_CONTENT).getFile(relativePath);
		var ivyFile = new ch.ivyteam.ivy.scripting.objects.File(file.getName(), true);
		java.nio.file.Path path = ivyFile.getJavaFile().toPath();
		Files.copy(file.getContents(), path, StandardCopyOption.REPLACE_EXISTING);

		return ivyFile;
	}

}
