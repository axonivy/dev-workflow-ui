package ch.ivyteam.workflowui.login;

public class OAuthProvider {
  public final String name;
  public final String image;
  public final String link;

  public OAuthProvider(String name, String image, String link) {
    this.name = name;
    this.image = image;
    this.link = link;
  }

  public String getName() {
    return name;
  }

  public String getImage() {
    return image;
  }

  public String getLink() {
    return link;
  }
}
