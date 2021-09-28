package ch.ivyteam.workflowui.document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.common.io.Files;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.Path;

public class DocumentModel {

  static final List<String> extensionsDefault = Arrays.asList("csv", "exe", "html", "rar", "xml", "zip");
  static final List<String> extensionsImages = Arrays.asList("bmp", "gif", "jpg", "png");
  static final List<String> extensionsOffice = Arrays.asList("pdf", "ppt", "txt", "xls");

  private final String name;
  private final Path path;
  private final String icon;

  public static List<DocumentModel> create(ICase selectedCase) {
    return selectedCase.documents().getAll().stream()
            .map(DocumentModel::new).collect(Collectors.toList());
  }

  public DocumentModel(IDocument doc) {
    this.name = doc.getName();
    this.path = doc.getPath();
    this.icon = getDocumentIcon(Files.getFileExtension(this.name));
  }

  private static String getDocumentIcon(String extension) {
    if (extensionsDefault.contains(extension)) {
      return "file-" + extension;
    } else if (extensionsImages.contains(extension)) {
      return "image-file-" + extension;
    } else if (extensionsOffice.contains(extension)) {
      return "office-file-" + extension + "-1";
    }
    return "common-file-text";
  }

  public StreamedContent getStreamedContent() throws FileNotFoundException {
    String contentType = FacesContext.getCurrentInstance().getExternalContext()
            .getMimeType(this.path.asString());
    String filePath = IApplication.current().getFileArea().getAbsolutePath() + "/" + this.path.asString();
    return new DefaultStreamedContent(new FileInputStream(filePath), contentType, this.path.getLastSegment());
  }

  public String getName() {
    return name;
  }

  public String getIcon() {
    return icon;
  }

  public Path getPath() {
    return path;
  }
}
