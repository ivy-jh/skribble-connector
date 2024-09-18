package com.axonivy.connector.skribble.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import ch.ivyteam.ivy.IvyConstants;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

@SuppressWarnings("restriction")
public class LocalFileLoader {

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
