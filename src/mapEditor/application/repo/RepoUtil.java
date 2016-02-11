package mapEditor.application.repo;

import java.io.File;
import java.util.*;

/**
 *
 * Created by razvanolar on 11.02.2016.
 */
public class RepoUtil {

  protected RepoUtil() {}

  public List<File> loadTileSetsFile(String path) {
    if (path == null)
      return null;
    File pathFile = new File(path);
    if (!pathFile.exists())
      return null;

    List<File> result = new ArrayList<>();
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
    return result;
  }
}
