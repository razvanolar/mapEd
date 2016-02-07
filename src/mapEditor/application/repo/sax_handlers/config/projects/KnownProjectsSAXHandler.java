package mapEditor.application.repo.sax_handlers.config.projects;

import mapEditor.application.repo.models.LWProjectModel;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 05.02.2016.
 */
public class KnownProjectsSAXHandler extends DefaultHandler {

  private static LWProjectModel project;
  private static List<LWProjectModel> projects;

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    switch (qName) {
      case "projects":
        projects = new ArrayList<>();
        break;
      case "project":
        project = new LWProjectModel(attributes.getValue("path"), Long.parseLong(attributes.getValue("lastAccess")));
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    switch (qName) {
      case "project":
        projects.add(project);
        break;
    }
  }

  public static List<LWProjectModel> getProjects() {
    return projects;
  }
}
