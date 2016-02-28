package mapEditor.application.main_part.project_tree.utils;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TreeItem;
import mapEditor.application.main_part.app_utils.inputs.FileExtensionUtil;
import mapEditor.application.main_part.app_utils.models.LazyTreeItem;
import mapEditor.application.main_part.app_utils.models.TreeItemType;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 *
 * Created by razvanolar on 10.02.2016.
 */
public class WatchDir {

  private WatchService watcher;
  private Map<WatchKey, Path> keys;
  private LazyTreeItem root;
  private ChangeListener<Boolean> listener;

  /**
   * Creates a WatchService and registers the given directory
   */
  public WatchDir(Path dir, LazyTreeItem item, ChangeListener<Boolean> listener) throws IOException {
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<>();
    this.root = item;
    this.listener = listener;

    System.out.format("Scanning %s ...\n", dir);
    registerAll(dir);
    System.out.println("Done.");
  }

  /**
   * Process all events for keys queued to the watcher
   */
  public void processEvents() {
    for (;;) {
      // wait for key to be signalled
      WatchKey key;
      try {
        key = watcher.take();
      } catch (InterruptedException x) {
        return;
      }

      Path dir = keys.get(key);
      if (dir == null) {
        System.err.println("WatchKey not recognized!!");
        continue;
      }

      for (WatchEvent<?> event: key.pollEvents()) {
        WatchEvent.Kind kind = event.kind();

        // TBD - provide example of how OVERFLOW event is handled
        if (kind == StandardWatchEventKinds.OVERFLOW) {
          continue;
        }

        // Context for directory entry event is the file name of entry
        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);

        // print out event
        System.out.format("%s: %s\n", event.kind().name(), child);

        // if directory is created, and watching recursively, then
        // register it and its sub-directories
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
          LazyTreeItem item = findItemByPath(dir.toAbsolutePath().toString());
          boolean isDirectory = Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS);
          if (item != null && item.wasExpanded()) {
            LazyTreeItem newItem = new LazyTreeItem(new File(child.toAbsolutePath().toString()), isDirectory,
                    isDirectory ? TreeItemType.FOLDER : FileExtensionUtil.getTreeItemTypeForName(child.toAbsolutePath().toString()));
            newItem.expandedProperty().addListener(listener);
            item.getChildren().add(newItem);
            item.getChildren().sort((o1, o2) -> o1.getValue().getName().compareTo(o2.getValue().getName()));
          }
          try {
            if (isDirectory)
              registerAll(child);
          } catch (IOException x) {
            // ignore to keep sample readbale
          }
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
          LazyTreeItem childItem = findItemByPath(child.toAbsolutePath().toString());
          if (childItem != null) {
            TreeItem<File> parentItem = childItem.getParent();
            if (parentItem != null)
              parentItem.getChildren().remove(childItem);
          }
        }
      }

      // reset key and remove from set if directory no longer accessible
      boolean valid = key.reset();
      if (!valid) {
        keys.remove(key);
        System.out.println("---- Key removed");
        // all directories are inaccessible
        if (keys.isEmpty()) {
          break;
        }
      }
    }
  }

  private LazyTreeItem findItemByPath(String path) {
    Queue<TreeItem<File>> queue = new LinkedList<>();
    queue.add(root);
    while (!queue.isEmpty()) {
      TreeItem<File> item = queue.poll();
      String itemPath = item.getValue().getAbsolutePath();
      if (itemPath.equals(path) || itemPath.equals(path + "\\"))
        return (LazyTreeItem) item;
      if (item.getChildren() != null && !item.getChildren().isEmpty())
        queue.addAll(item.getChildren());
    }
    return null;
  }

  /**
   * Register the given directory with the WatchService
   */
  private void register(Path dir) throws IOException {
    WatchKey key = dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY);
    Path prev = keys.get(key);
    if (prev == null) {
      System.out.format("register: %s\n", dir);
    } else {
      if (!dir.equals(prev)) {
        System.out.format("update: %s -> %s\n", prev, dir);
      }
    }
    keys.put(key, dir);
  }

  /**
   * Register the given directory, and all its sub-directories, with the
   * WatchService.
   */
  private void registerAll(final Path start) throws IOException {
    // register directory and sub-directories
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
              throws IOException {
        register(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>)event;
  }
}
