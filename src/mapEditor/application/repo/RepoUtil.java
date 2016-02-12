package mapEditor.application.repo;

import java.io.File;
import java.util.*;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class RepoUtil {

  private Map<String, List<File>> filesCachePerPath;

  protected RepoUtil() {
    filesCachePerPath = new HashMap<>();
  }

  public List<File> loadTileSetsFile(String path) {
    if (path == null)
      return null;
    List<File> result = filesCachePerPath.get(path);
    if (result != null) {
      System.out.println("load files from cache");
      return result;
    }
    File pathFile = new File(path);
    if (!pathFile.exists())
      return null;

    result = new ArrayList<>();
    File[] files = pathFile.listFiles();
    if (files != null && files.length > 0) {
      Queue<File> queue = new LinkedList<>();
      queue.addAll(Arrays.asList(files));
      while (!queue.isEmpty()) {
        File file = queue.poll();
        result.add(file);
        files = file.listFiles();
        if (files != null && files.length > 0)
          queue.addAll(Arrays.asList(files));
      }
    }
    filesCachePerPath.put(path, result);
    return result;
  }

  public void invalidateFileCachePath(String path) {
    if (path != null)
      filesCachePerPath.remove(path);
  }
}
