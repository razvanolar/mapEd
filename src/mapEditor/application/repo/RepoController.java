package mapEditor.application.repo;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.sax_handlers.project_init_file.ProjectXMLConverter;
import mapEditor.application.repo.types.CreateProjectStatus;
import mapEditor.application.repo.types.MapType;

import java.io.File;
import java.io.PrintWriter;

/**
 * Used to take care of all the non-GUI operations
 * Created by razvanolar on 02.02.2016.
 */
public class RepoController {

  private static RepoController INSTANCE;

  /**
   * Create a project with the specified name at the specified path.
   * All the necessary files till be written here.
   * @param name - project name
   * @param path - project path; must be a valid directory path
   * @return true if the project was successfully created; false otherwise
   */
  public ProjectModel createProject(String name, String path) {
    try {
      ProjectModel project = new ProjectModel(name, path, MapType.ORTHOGONAL, AppParameters.CELL_SIZE);
      ProjectXMLConverter xmlConverter = new ProjectXMLConverter();
      String xmlResult  = xmlConverter.convertProjectToXML(project);
      if (!path.endsWith("\\"))
        path += "\\";
      writeContentToFile(xmlResult, path + name);
      return project;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    }
  }

  public void writeContentToFile(String content, String path) throws Exception {
    PrintWriter writer = new PrintWriter(new File(path));
    writer.write(content);
    writer.close();
  }

  /**
   * Check to see if the provided name and path of the the project that will be created, are valid.
   * @param name - project name
   * @param path - path on which the project will be stored
   *
   * @return CreateProjectStatus - status that indicates the current state for the project home directory
   */
  public CreateProjectStatus checkIfProjectFieldsAreValid(String name, String path) {
    File pathFile = new File(path);
    if (!pathFile.exists())
      return CreateProjectStatus.PATH_MISSING;

    if (!path.endsWith("\\"))
      path += "\\";

    File nameFile = new File(path + name);
    if (nameFile.exists())
      return CreateProjectStatus.NAME_EXISTS;

    return CreateProjectStatus.VALID;
  }

  /**
   * If the specified path does not exist, and if it's a valid one, it will be created.
   * @param path - path to be created; it has to be the absolute path to a directory
   * @return true if the path already exists or if it was successfully created; false otherwise
   */
  public boolean createPathIfNotExist(String path) {
    File file = new File(path);
    return file.exists() || file.mkdirs();
  }

  public static RepoController getInstance() {
    if (INSTANCE == null)
      INSTANCE = new RepoController();
    return INSTANCE;
  }
}
