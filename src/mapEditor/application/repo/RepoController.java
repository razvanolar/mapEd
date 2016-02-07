package mapEditor.application.repo;

import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.repo.models.LWProjectModel;
import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.sax_handlers.config.projects.KnownProjectsXMLConverter;
import mapEditor.application.repo.sax_handlers.config.projects.KnownProjectsXMLHandler;
import mapEditor.application.repo.sax_handlers.project_init_file.ProjectXMLConverter;
import mapEditor.application.repo.sax_handlers.project_init_file.ProjectXMLHandler;
import mapEditor.application.repo.types.CreateProjectStatus;
import mapEditor.application.repo.types.MapType;
import mapEditor.application.repo.types.ProjectStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to take care of all the non-GUI operations
 * Created by razvanolar on 02.02.2016.
 */
public class RepoController {

  private static RepoController INSTANCE;

  public List<LWProjectModel> loadExistingProjects() throws Exception {
    File file = new File(SystemParameters.KNOWN_PROJECTS_FILE_PATH);
    if (!file.exists()) {
      if (!file.createNewFile())
        throw new Exception(SystemParameters.KNOWN_PROJECTS_FILE_PATH + " path was not found.");
      else {
        System.out.println(SystemParameters.KNOWN_PROJECTS_FILE_PATH + " was created.");
        return new ArrayList<>();
      }
    }

    BufferedReader reader = new BufferedReader(new FileReader(file));
    StringBuilder builder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null)
      builder.append(line);
    reader.close();

    if (builder.toString().isEmpty())
      return new ArrayList<>();

    KnownProjectsXMLHandler projectsXMLHandler = new KnownProjectsXMLHandler(builder.toString());
    return projectsXMLHandler.parse();
  }

  public void saveToExistingProjects(List<LWProjectModel> existingProjects, LWProjectModel newProject) {
    if (newProject == null)
      return;
    if (existingProjects == null)
      existingProjects = new ArrayList<>();
    existingProjects.add(newProject);
    saveProjects(existingProjects);
  }

  public void saveProjects(List<LWProjectModel> projects) {
    if (projects == null)
      return;
    try {
      Collections.sort(projects, (o1, o2) -> (int) (o2.getLastAccessedTime() - o1.getLastAccessedTime()));
      KnownProjectsXMLConverter converter = new KnownProjectsXMLConverter();
      String result = converter.convertLWProjectsToXML(projects);
      PrintWriter writer = new PrintWriter(new FileWriter(SystemParameters.KNOWN_PROJECTS_FILE_PATH, false));
      writer.write(result);
      writer.close();
      SystemParameters.PROJECTS = projects;
    } catch (Exception ex) {
      System.out.println("Unable to save projects. Error message: " + ex.getMessage());
    }
  }

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
      writeContentToFile(xmlResult, path + name + ".med");
      saveToExistingProjects(SystemParameters.PROJECTS, new LWProjectModel(name, path, System.currentTimeMillis(), ProjectStatus.OPENED));
      return project;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    }
  }

  /**
   * Load the project settings.
   * @param path - project path
   * @return model - ProjectModel
   * @throws Exception
   */
  public ProjectModel loadProject(String path) throws Exception {
    String content = readContentFromFile(path);
    ProjectXMLHandler handler = new ProjectXMLHandler(content);
    return handler.parse();
  }

  /**
   * Load the project settings.
   * @param model - project light weight version
   * @param throwException - true to throw the exception (if appears); false otherwise
   * @return model - ProjectModel
   * @throws Exception
   */
  public ProjectModel loadProject(LWProjectModel model, boolean throwException) throws Exception {
    try {
      String filePath = model.getPath();
      if (!filePath.endsWith("\\"))
        filePath += "\\";
      filePath += model.getName() + ".med";
      return loadProject(filePath);
    } catch (Exception ex) {
      System.out.println("RepoController - loadProject - Unable to load project. Error message: " + ex.getMessage());
      if (throwException)
        throw ex;
    }
    return null;
  }

  public void writeContentToFile(String content, String path) throws Exception {
    PrintWriter writer = new PrintWriter(new File(path));
    writer.write(content);
    writer.close();
  }

  public String readContentFromFile(String filePath) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
    StringBuilder builder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null)
      builder.append(line);
    return builder.toString();
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

    if (!pathFile.isDirectory())
      return CreateProjectStatus.NOT_DIRECTORY;

    File[] files = pathFile.listFiles();
    if (files != null)
      for (File file : files)
        if (file.getName().endsWith(".med"))
          return CreateProjectStatus.ANOTHER_CREATED;

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
