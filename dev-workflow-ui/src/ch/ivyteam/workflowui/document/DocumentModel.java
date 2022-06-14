package ch.ivyteam.workflowui.document;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.Path;

public class DocumentModel {

  static final Set<String> extensionsDefault = Set.of("csv", "exe", "html", "rar", "xml", "zip");
  static final Set<String> extensionsImages = Set.of("bmp", "gif", "jpg", "png");
  static final Set<String> extensionsOffice = Set.of("pdf", "ppt", "txt", "xls");

  private final IDocument doc;

  public static List<DocumentModel> create(ICase selectedCase) {
    return selectedCase.documents().getAll().stream()
            .map(DocumentModel::new)
            .collect(Collectors.toList());
  }

  public DocumentModel(IDocument doc) {
    this.doc = doc;
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

  public StreamedContent getStreamedContent() {
    var contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(doc.getPath().asString());
    return DefaultStreamedContent
        .builder()
        .stream(() -> doc.read().asStream())
        .contentType(contentType)
        .name(doc.getPath().getLastSegment())
        .build();
  }

  public String getName() {
    return doc.getName();
  }

  public String getIcon() {
    return getDocumentIcon(FilenameUtils.getExtension(doc.getName()));
  }

  public Path getPath() {
    return doc.getPath();
  }
}
