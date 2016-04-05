package mapEditor.application.repo;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.KnownFileExtensions;
import mapEditor.application.main_part.app_utils.models.LWMapModel;
import mapEditor.application.main_part.app_utils.models.MapDetail;
import mapEditor.application.main_part.app_utils.models.brush.LWBrushModel;
import mapEditor.application.repo.html_exporter.MapHtmlExporter;
import mapEditor.application.repo.models.LWProjectModel;
import mapEditor.application.repo.models.ProjectModel;
import mapEditor.application.repo.results.SaveImagesResult;
import mapEditor.application.repo.sax_handlers.config.known_projects.KnownProjectsXMLConverter;
import mapEditor.application.repo.sax_handlers.config.known_projects.KnownProjectsXMLHandler;
import mapEditor.application.repo.sax_handlers.maps.MapXMLConverter;
import mapEditor.application.repo.sax_handlers.maps.MapXMLHandler;
import mapEditor.application.repo.sax_handlers.project_init_file.ProjectXMLConverter;
import mapEditor.application.repo.sax_handlers.project_init_file.ProjectXMLHandler;
import mapEditor.application.repo.statuses.SaveFilesStatus;
import mapEditor.application.repo.types.CreateProjectStatus;
import mapEditor.application.repo.types.MapType;
import mapEditor.application.repo.types.ProjectStatus;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to take care of all the non-GUI operations
 * Created by razvanolar on 02.02.2016.
 */
public class RepoController {

  private static RepoController INSTANCE;
  private RepoUtil repoUtil;

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
    saveLWProjects(existingProjects);
  }

  public void saveLWProjects(List<LWProjectModel> projects) {
    if (projects == null)
      return;
    try {
      Collections.sort(projects, (o1, o2) -> {
        long t1 = o1.getLastAccessedTime();
        long t2 = o2.getLastAccessedTime();
        if (t1 == t2)
          return 0;
        return t1 - t2 > 0 ? -1 : 1;
      });
      KnownProjectsXMLConverter converter = new KnownProjectsXMLConverter();
      String result = converter.convertLWProjectsToXML(projects);
      PrintWriter writer = new PrintWriter(new FileWriter(SystemParameters.KNOWN_PROJECTS_FILE_PATH, false));
      writer.write(result);
      writer.close();
      SystemParameters.PROJECTS = projects;
    } catch (Exception ex) {
      System.out.println("Unable to save projects. Error message: " + ex.getMessage());
      ex.printStackTrace();
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
      ProjectModel project = new ProjectModel(name, path, MapType.ORTHOGONAL, AppParameters.DEFAULT_CELL_SIZE);
      ProjectXMLConverter xmlConverter = new ProjectXMLConverter();
      String xmlResult  = xmlConverter.convertProjectToXML(project);
      if (!path.endsWith("\\"))
        path += "\\";
      writeContentToFile(xmlResult, path + name + SystemParameters.PROJECT_FILE_EXT);
      saveToExistingProjects(SystemParameters.PROJECTS, new LWProjectModel(name, path, System.currentTimeMillis(), ProjectStatus.OPENED));
      createProjectFiles(path);
      return project;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return null;
    }
  }

  /**
   * Create the main project directories (tile_groups, characters, maps).
   * If a directory can not be created, only log the error.
   * @param projectPath - project path; must be a valid directory path
   */
  private void createProjectFiles(String projectPath) {
    if (!projectPath.endsWith("\\"))
      projectPath += "\\";
    File tileGroupsFile = new File(projectPath + SystemParameters.TILE_GROUPS_FOLDER_PATH);
    File charactersFile = new File(projectPath + SystemParameters.CHARACTERS_FOLDER_PATH);
    File mapsFile = new File(projectPath + SystemParameters.MAPS_FOLDER_PATH);
    if (!tileGroupsFile.exists() && !tileGroupsFile.mkdirs())
      System.out.println("Unable to create tile groups directory. Project path: " + projectPath);
    if (!charactersFile.exists() && !charactersFile.mkdirs())
      System.out.println("Unable to create characters directory. Project path: " + projectPath);
    if (!mapsFile.exists() && !mapsFile.mkdirs())
      System.out.println("Unable to create maps directory. Project path: " + projectPath);
  }

  /**
   * Load the project settings (i.e. hex counter, light weight version of the maps that were opened when the project was closed).
   * @param lwProject - light weight version of the project
   * @return model - ProjectModel
   * @throws Exception
   */
  public ProjectModel loadProjectSettings(LWProjectModel lwProject) throws Exception {
    // compute project file path
    String projectFilePath = lwProject.getPath();
    if (!projectFilePath.endsWith("\\")) {
      projectFilePath += "\\";
    }
    projectFilePath += lwProject.getName() + SystemParameters.PROJECT_FILE_EXT;

    // parse the file
    String content = readContentFromFile(projectFilePath);
    ProjectXMLHandler handler = new ProjectXMLHandler(content, lwProject.getPath());
    handler.parse();
    ProjectModel projectModel = handler.getProjectModel();

    projectModel.setHomePath(lwProject.getPath());
    projectModel.setLwMapModels(handler.getMapModels());

    return projectModel;
  }

  public void loadProjectMapModels(ProjectModel project) throws Exception {
    if (project == null)
      return;
    List<LWMapModel> lwMapModels = project.getLwMapModels();
    if (lwMapModels == null || lwMapModels.isEmpty())
      return;

    MapXMLHandler handler = new MapXMLHandler(project.getHomePath());
    String projectMapsPath = project.getMapsFile().getAbsolutePath();
    boolean showGrid = project.isShowGrid();
    for (LWMapModel lwModel : lwMapModels) {
      try {
        String mapAbsolutePath = projectMapsPath + lwModel.getRelativePath();
        mapAbsolutePath = mapAbsolutePath.endsWith("\\") ? mapAbsolutePath : mapAbsolutePath + "\\";
        MapDetail mapDetail = createMapModelFromFile(project.getHomePath(), new File(mapAbsolutePath + lwModel.getName()), handler);
        mapDetail.setSelected(lwModel.isSelected());
        mapDetail.setShowGrid(showGrid);
        project.addMapModel(mapDetail);
        handler.clearHandlerFields();
      } catch (Exception ex) {
        System.out.println("*** RepoController - loadProjectMapModels - Unable to load map model for map name: " +
        lwModel.getName() + " and project maps path: " + projectMapsPath + " Error message: " + ex.getMessage());
      }
    }
  }

  /**
   * Create the map model for the specified file.
   * @param projectPath
   * Projetc home directory
   * @param mapFile
   * XML map file (make sure you provide the full path of the file, not just the parent directory)
   * @param handler
   * MapXMLHandler, if it's null a new one will be created.
   * @return the corresponding map model of the file; null otherwise.
   */
  public MapDetail createMapModelFromFile(String projectPath, File mapFile, MapXMLHandler handler) throws Exception {
    if (mapFile == null)
      return null;
    if (handler == null)
      handler = new MapXMLHandler(projectPath);
    String content = readContentFromFile(mapFile);
    handler.parse(content);
    MapDetail mapDetail = handler.getMapModel();
    mapDetail.setAbsolutePath(mapFile.getParentFile().getAbsolutePath());
    return mapDetail;
  }

  /**
   * Set project main directories. If a directory is missing, we'll try to create it.
   * @param project - ProjectModel
   * @throws Exception
   */
  public void loadProjectFiles(ProjectModel project) throws Exception {
    if (project == null)
      return;
    String projectPath = project.getHomePath();
    File tileGroupsFile = new File(projectPath + SystemParameters.TILE_GROUPS_FOLDER_PATH);
    File tileSetsFile = new File(projectPath + SystemParameters.TILE_SETS_FOLDER_PATH);
    File tilesFile = new File(projectPath + SystemParameters.TILES_FOLDER_PATH);
    File brushesFile = new File(projectPath + SystemParameters.BRUSHES_FOLDER_PATH);
    File charactersFile = new File(projectPath + SystemParameters.CHARACTERS_FOLDER_PATH);
    File mapsFile = new File(projectPath + SystemParameters.MAPS_FOLDER_PATH);
    if (!tileGroupsFile.exists() && !tileGroupsFile.mkdirs())
      throw new Exception("Tile groups directory does not exist and failed to create.");
    if (!tileSetsFile.exists() && !tileSetsFile.mkdirs())
      throw new Exception("Tile sets directory does not exist and failed to create.");
    if (!tilesFile.exists() && !tilesFile.mkdirs())
      throw new Exception("Tiles directory does not exist and failed to create.");
    if (!brushesFile.exists() && !brushesFile.mkdirs())
      throw new Exception("Brushes directory does not exist and failed to create.");
    if (!charactersFile.exists() && !charactersFile.mkdirs())
      throw new Exception("Characters directory does not exist and failed to create.");
    if (!mapsFile.exists() && !mapsFile.mkdirs())
      throw new Exception("Maps directory does not exist and failed to create.");
    project.setTileGroupsFile(tileGroupsFile);
    project.setTileSetsFile(tileSetsFile);
    project.setTilesFile(tilesFile);
    project.setBrushesFile(brushesFile);
    project.setCharactersFile(charactersFile);
    project.setMapsFile(mapsFile);
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
      ProjectModel projectModel = loadProjectSettings(model);
      loadProjectFiles(projectModel);
      loadProjectMapModels(projectModel);
      projectModel.setLwMapModels(null);
      return projectModel;
    } catch (Exception ex) {
      System.out.println("*** RepoController - loadProject - Unable to load project. Error message: " + ex.getMessage());
      if (throwException)
        throw ex;
    }
    return null;
  }

  /**
   * Saves the project settings into its configuration file.
   * @param project ProjectModel
   * @return true if the project was saved successfully; false otherwise
   */
  public boolean saveProject(ProjectModel project) {
    try {
      ProjectXMLConverter xmlConverter = new ProjectXMLConverter();
      String result = xmlConverter.convertProjectToXML(project);
      writeContentToFile(result, project.getConfigFilePath());
      MapXMLConverter converter = new MapXMLConverter();
      for (MapDetail mapDetail : project.getMapDetails())
        saveMap(project.getHomePath(), mapDetail, converter, true);
      return true;
    } catch (Exception ex) {
      System.out.println("RepoController - saveProject - Unable to save project. Error message: " + ex.getMessage());
    }
    return false;
  }

  /**
   * Saves the map on the disk, into XML format. Based on the provided boolean flag, you can choose to overwrite the map
   * if already exist, or to create a new one. When creating a new one, the map name will be computed to establish if an
   * order number is required.
   * @param projectPath
   * Project absolute path.
   * @param mapDetail
   * MapDetail
   * @param converter
   * MapXMLConverter, if it's null, a new one will be created.
   * @param overwrite
   * TRUE if you want to overwrite the map if already exist.
   * @return The name of the map that was saved on the disk (if a map with the same name already exist into that
   *         directory and overwrite flag is TRUE, a new name will be computed with a higher order number)
   * @throws Exception
   */
  public String saveMap(String projectPath, MapDetail mapDetail, MapXMLConverter converter, boolean overwrite) throws Exception {
    String mapAbsolutePath = mapDetail.getAbsolutePath();
    String mapName = mapDetail.getName();
    if (projectPath == null || mapAbsolutePath == null || mapName == null)
      return null;
    mapAbsolutePath = mapAbsolutePath.endsWith("\\") ? mapAbsolutePath : mapAbsolutePath + "\\";
    if (!overwrite) {
      mapName = getFileAlternativeNameIfExists(mapAbsolutePath, mapName);
      mapDetail.setName(mapName);
      if (mapName == null)
        return null;
    }

    if (converter == null)
      converter = new MapXMLConverter();

    try {
      String result = converter.convertMapToXML(mapDetail, projectPath);
      writeContentToFile(result, mapAbsolutePath + mapName);
      return mapName;
    } catch (Exception ex) {
      System.out.println("*** RepoController - saveMap - Unable to save map. name: " + mapName +
              " path: " + mapAbsolutePath + " Error message: " + ex.getMessage());
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * Create a new directory named after directoryName parameter at the specified parent location.
   * If a directory with the same name already exists, the new directory will have a higher order number.
   * @param parent
   * Parent directory file
   * @param directoryName
   * Directory to be created.
   * @return TRUE if the directory was created successfully; FALSE otherwise
   * @throws Exception
   */
  public boolean createDirectory(File parent, String directoryName) throws Exception {
    if (parent == null || !parent.isDirectory() || directoryName == null)
      return false;
    try {
      directoryName = getRepoUtil().checkNameOrGetAnAlternativeOne(parent.getAbsolutePath(), directoryName);
      String parentPath = parent.getAbsolutePath();
      parentPath = parentPath.endsWith("\\") ? parentPath : parentPath + "\\";
      File file = new File(parentPath + directoryName);
      return file.mkdir();
    } catch (Exception ex) {
      ex.printStackTrace();
      ex.printStackTrace();
      System.out.println("*** RepoController - createDirectory - Unable to create directory. Dir name: " + directoryName + " , Parent path: " + parent.getAbsolutePath());
      throw ex;
    }
  }

  /**
   * Rename the file. From @renameFrom to @renameTo.
   * @param renameFrom
   * Old file name (Absolute file path).
   * @param renameTo
   * New file name (Absolute file path).
   * @return The new file name; NULL otherwise
   */
  public String renameFile(String renameFrom, String renameTo) {
    if (renameFrom == null || renameTo == null)
      return null;

    File fromFile = new File(renameFrom);
    File toFile = new File(renameTo);

    if (!fromFile.exists())
      return null;
    if (toFile.exists()) {
      String absolutePath = toFile.getParentFile().getAbsolutePath();
      absolutePath = absolutePath.endsWith("\\") ? absolutePath : absolutePath + "\\";
      String name = getRepoUtil().checkNameOrGetAnAlternativeOne(absolutePath, toFile.getName());
      toFile = new File(absolutePath + name);
    }
    try {
      if (fromFile.renameTo(toFile))
        return toFile.getName();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.out.println("*** RepoController - renameFile - Error message: " + ex.getMessage());
    }
    return null;
  }

  /**
   * Delete the specified file from the disk.
   * @param file
   * File to be deleted
   * @return TRUE if the file was deleted successfully; FALSE otherwise
   */
  public boolean deleteFile(File file) {
    if (file == null)
      return false;
    try {
      Files.delete(file.toPath());
      return true;
    } catch (NoSuchFileException ex) {
      ex.printStackTrace();
      System.out.println("*** RepoController - deleteFile - NoSuchFileException - Unable to delete file. Error message: " + ex.getMessage());
    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println("*** RepoController - deleteFile - IOException - Unable to delete file. Error message: " + ex.getMessage());
    }
    return false;
  }

  public boolean exportMapToHtml(String projectPath, File mapFile) throws Exception {
    if (projectPath == null || projectPath.isEmpty() || mapFile == null || !mapFile.exists())
      return false;
    MapDetail mapDetail = createMapModelFromFile(projectPath, mapFile, null);
    MapHtmlExporter htmlExporter = new MapHtmlExporter(mapDetail);
    String preloadImagesText = htmlExporter.getPreloadImagesText();
    String attributesText = htmlExporter.getAttributesText();

    String path = "C:\\Users\\razvanolar\\Desktop\\MapEditor_Util\\exported_html_maps\\";
    File directory = new File(path);
    if (!directory.exists() && !directory.mkdirs())
      return false;
    writeContentToFile(preloadImagesText, path + SystemParameters.PRELOAD_IMAGES_FILE_NAME);
    writeContentToFile(attributesText, path + SystemParameters.ATTRIBUTES_FILE_NAME);

    // copy template files
    File templatesDir = new File(SystemParameters.HTML_EXPORTER_TEMPLATES_FILE_PATH);
    if (!templatesDir.exists())
      return false;
    File[] templates = templatesDir.listFiles();
    if (templates == null || templates.length == 0)
      return false;
    for (File template : templates) {
      copyToPath(template.getAbsolutePath(), path, template.getName(), true);
    }

    //copy tiles
    File tilesDir = new File(path + SystemParameters.EXPORTED_TILES_FOLDER_PATH);
    if (!tilesDir.exists() && !tilesDir.mkdirs())
      return false;
    CustomMap<Integer, File> indexedImages = mapDetail.getDiskIndexedTilesModel().getIndexedImages();
    if (indexedImages != null && !indexedImages.isEmpty()) {
      for (Integer key : indexedImages.keys()) {
        File tile = indexedImages.get(key);
        copyToPath(tile.getAbsolutePath(), tilesDir.getAbsolutePath(), tile.getName(), true);
      }
    }

    return true;
  }

  public boolean closeProject(String projectPath) {
    if (projectPath == null || projectPath.isEmpty())
      return false;

    for (LWProjectModel model : SystemParameters.PROJECTS) {
      if (model.getPath().equals(projectPath)) {
        model.setStatus(ProjectStatus.CLOSED);
        break;
      }
    }
    saveLWProjects(SystemParameters.PROJECTS);
    return true;
  }

  public void writeContentToFile(String content, String path) throws Exception {
    PrintWriter writer = new PrintWriter(new File(path));
    writer.write(content);
    writer.close();
  }

  public String readContentFromFile(String filePath) throws Exception {
    return readContentFromFile(new File(filePath));
  }

  public String readContentFromFile(File file) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    StringBuilder builder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null)
      builder.append(line);
    reader.close();
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

  /**
   * Copy the content of the file specified by 'from' into the directory represented by 'where'.
   * Name of the saved file is represented by the 'name' parameter.
   *
   * @param from
   * File that will be copied.
   * @param where
   * Path of the directory where the file will be copied.
   * @param name
   * Name of the file that will be saved.
   * @param overwrite
   * TRUE to overwrite the destination file if already exist; If FALSE, a file with a higher order number will be created.
   *
   * @return fileName - if the file was saved; null otherwise
   */
  public String copyToPath(String from, String where, String name, boolean overwrite) {
    File fromFile = new File(from);
    where = where.endsWith("\\") ? where : where + "\\";
    File whereFile = new File(where + name);
    if (!fromFile.exists())
      return null;
    if (whereFile.exists() && !overwrite) {
      String auxName = getRepoUtil().getAlternativeNameForExistingFile(where, name);
      if (auxName == null)
        return null;
      whereFile = new File(where + auxName);
    }

    try {
      Files.copy(fromFile.toPath(), whereFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      return whereFile.getName();
    } catch (IOException e) {
      System.out.println("*** Unable to copy file to the specified path. From: " + from + " To: " + where + " Error message: " + e.getMessage());
      return null;
    } catch (InvalidPathException e) {
      System.out.println("*** Unable to copy file to the specified path. From: " + from + " To: " + where + " Error message: " + e.getMessage());
      return null;
    }
   }

  /**
   * Save the image with using the specified name to 'where' location.
   * The final image name could be different than the specified one, if at that location, already exist
   * a file using it. In this case, an integer value will be assigned for the new image.
   * @param image
   * Image that will be saved.
   * @param where
   * Represents a directory path. The location where the image will be saved.
   * @param name
   * Name of the saved image
   * @param overwrite
   * True, to overwrite the image if already exists. False, otherwise.
   * @return null in case that one of the specified parameters is null or if an error occurs during the saving
   *         process;
   *         otherwise the name of the image will be returned.
   */
  public String saveImage(Image image, String where, String name, boolean overwrite) {
    if (image == null || where == null || name == null)
      return null;

    try {
      where = where.endsWith("\\") ? where : where + "\\";
      File whereFile = new File(where + name);
      if (whereFile.exists() && !overwrite) {
        String auxName = getRepoUtil().getAlternativeNameForExistingFile(where, name);
        if (auxName == null)
          return null;
        name = auxName;
        whereFile = new File(where + auxName);
      }

      try {
        boolean value = ImageIO.write(SwingFXUtils.fromFXImage(image, null), getRepoUtil().getFileExtensionWithoutDot(name), whereFile);
        return value ? name : null;
      } catch (IOException e) {
        System.out.println("*** Unable to save image to disk. Location: " + where + " image name: " + name + " Error message: " + e.getMessage());
      }
    } catch (Exception ex) {
      System.out.println("*** Unexpected error occurred while saving the image. Error message: " + ex.getMessage());
    }
    return null;
  }

  public SaveImagesResult saveImages(List<ImageModel> images) {
    SaveFilesStatus status = SaveFilesStatus.NONE;
    if (images == null || images.isEmpty())
      return new SaveImagesResult(status, null);
    List<ImageModel> unsavedImages = new ArrayList<>();

    RepoUtil repoUtil = getRepoUtil();
    boolean areUnsavedImages = false;
    for (ImageModel image : images) {
      String name = image.getName();
      String path = image.getPath();
      path = !path.endsWith("\\") ? path + "\\" : path;
      try {
        name = repoUtil.checkNameOrGetAnAlternativeOne(path, name);
        ImageIO.write(SwingFXUtils.fromFXImage(image.getImage(), null), KnownFileExtensions.PNG.getFormat(), new File(path + name));
      } catch (Exception ex) {
        areUnsavedImages = true;
        unsavedImages.add(image);
        System.out.println("*** Unable to save image. Name: " + image.getPath() + " path: " + image.getPath() +
                      " Error message: " + ex.getMessage());
      }
    }

    return !areUnsavedImages ? new SaveImagesResult(SaveFilesStatus.COMPLETE, null) : new SaveImagesResult(SaveFilesStatus.PARTIAL, unsavedImages);
  }

  public SaveFilesStatus saveBrushModels(List<LWBrushModel> models, String path) {
    if (path == null || path.isEmpty() || models == null || models.isEmpty())
      return SaveFilesStatus.NONE;
    path = path.endsWith("\\") ? path : path + "\\";
    File file = new File(path);
    if (!file.exists())
      return SaveFilesStatus.NONE;

    int count = 0;

    for (LWBrushModel brush : models) {
      try {
        String name = getRepoUtil().checkBrushNameOrGetANewOne(path, brush.getName());
        String brushDirectory = getRepoUtil().checkNameOrGetAnAlternativeOne(path, "_" + brush.getName());
        if (name == null || brushDirectory == null)
          continue;
        File directory = new File(path + brushDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
          System.out.println("*** RepoController - saveBrushModels - Unable to create directory : " + path + brushDirectory);
          continue;
        }
        saveBrushImages(brush, directory);
        count ++;
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    return count == 0 ? SaveFilesStatus.NONE : count == models.size() ? SaveFilesStatus.COMPLETE : SaveFilesStatus.PARTIAL;
  }

  private void saveBrushImages(LWBrushModel brush, File directory) throws Exception {
    String pngExt = KnownFileExtensions.PNG.getExtension();
    // save primary image
    int imageCounter = 0;
    if (saveImage(brush.getPrimaryImage(), directory.getAbsolutePath(), brush.getName() + "_" + imageCounter + pngExt, true) == null)
      throw new Exception("Unable to save brush primary image");
    imageCounter ++;
    // save other images
    for (Image image : brush.getOtherImages()) {
      if (saveImage(image, directory.getAbsolutePath(), brush.getName() + "_" + imageCounter + pngExt, true) == null)
        throw new Exception("Unable to save brush secondary images");
      imageCounter ++;
    }
    // save corner images
    for (Image image : brush.getOtherImages()) {
      if (saveImage(image, directory.getAbsolutePath(), brush.getName() + "_" + imageCounter + pngExt, true) == null)
        throw new Exception("Unable to save brush corner images");
      imageCounter ++;
    }
    // save preview image
    if (saveImage(brush.getPreviewImage(), directory.getAbsolutePath(), brush.getName() + "_preview" + pngExt, true) == null)
      throw new Exception("Unable to save brush preview image");
  }

  /**
   * Return the file name with a higher order number if the specified file already exists.
   * @param path
   * Valid directory path.
   * @param name
   * File name.
   * @return A new file with a higher order number if the file exists; otherwise, the specified file name will
   *         be returned without being changed.
   */
  public String getFileAlternativeNameIfExists(String path, String name) {
    if (path == null || name == null)
      return null;
    path = path.endsWith("\\") ? path : path + "\\";
    File file = new File(path + name);
    return file.exists() ? getRepoUtil().getAlternativeNameForExistingFile(path, name) : name;
  }

  private RepoUtil getRepoUtil() {
    if (repoUtil == null)
      repoUtil = new RepoUtil();
    return repoUtil;
  }

  public static RepoController getInstance() {
    if (INSTANCE == null)
      INSTANCE = new RepoController();
    return INSTANCE;
  }
}
