package mapEditor.application.main_part.manage_maps.manage_tiles;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by razvanolar on 23.01.2016.
 */
public class TabModel {

  private List<Image> tileList;
  private boolean isCanvasTilesTab;

  public TabModel(boolean isCanvasTilesTab) {
    this(new ArrayList<Image>(), isCanvasTilesTab);
  }

  public TabModel(List<Image> imageList, boolean isCanvasTilesTab) {
    this.tileList = imageList;
    this.isCanvasTilesTab = isCanvasTilesTab;
  }

  public void addTile(Image tile) {
    tileList.add(tile);
  }

  public void addTiles(List<Image> tiles) {
    tileList.addAll(tiles);
  }

  public final List<Image> getTileList() {
    return tileList;
  }

  public boolean isCanvasTilesTab() {
    return isCanvasTilesTab;
  }
}
