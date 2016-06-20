package mapEditor.application.main_part.manage_maps.manage_characters;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import mapEditor.MapEditorController;
import mapEditor.application.main_part.app_utils.AppParameters;
import mapEditor.application.main_part.app_utils.data_types.CustomMap;
import mapEditor.application.main_part.app_utils.inputs.ImageProvider;
import mapEditor.application.main_part.app_utils.inputs.StringValidator;
import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.TabKey;
import mapEditor.application.main_part.app_utils.models.character.CharacterModel;
import mapEditor.application.main_part.app_utils.views.dialogs.Dialog;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.AbstractTabContainer;
import mapEditor.application.main_part.manage_maps.manage_tiles.tab_container_types.CharactersTabContainer;
import mapEditor.application.main_part.manage_maps.utils.*;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectableEntityListener;
import mapEditor.application.main_part.manage_maps.utils.listeners.SelectedEntityListener;
import mapEditor.application.main_part.types.Controller;
import mapEditor.application.main_part.types.View;
import mapEditor.application.repo.models.ProjectModel;

import java.io.File;
import java.util.List;

/**
 *
 * Created by razvanolar on 22.03.2016.
 */
public class ManageCharactersController implements Controller, SelectableEntityListener {

  public interface IManageCharactersView extends View {
    TabPane getTabPane();
    void addTab(Tab tab);
  }

  private IManageCharactersView view;
  private SelectedEntityListener listener;

  private SelectableCharacterView selectableCharacterView;

  public ManageCharactersController(IManageCharactersView view, SelectedEntityListener listener) {
    this.view = view;
    this.listener = listener;
  }

  @Override
  public void bind() {
    CustomMap<TabKey, List<File>> tabs = AppParameters.CURRENT_PROJECT.getOpenedTileTabs();
    if (tabs != null && !tabs.isEmpty()) {
      for (TabKey key : tabs.keys()) {
        List<File> drawModels = tabs.get(key);
        if (drawModels == null || drawModels.isEmpty())
          continue;
        if (key.getType() == TabType.CHARACTERS)
          addCharacterTabForCharacterFiles(key.getName(), drawModels);
      }
    }

    addListeners();
  }

  private void addListeners() {
    System.out.println("view" + (view != null));
    System.out.println("tabP" + (view.getTabPane() != null));
    System.out.println("tabs" + (view.getTabPane().getTabs() != null));
    view.getTabPane().getTabs().addListener((ListChangeListener<Tab>) c -> {
      ProjectModel currentProject = AppParameters.CURRENT_PROJECT;
      while (c.next()) {
        List<? extends Tab> deleted = c.getRemoved();
        if (deleted != null && !deleted.isEmpty()) {
          deleted.stream().filter(tab -> tab.getUserData() != null).forEach(
                  tab -> currentProject.removeTileTabKey(((AbstractTabContainer) tab.getUserData()).getKey())
          );
        }

        List<? extends Tab> added = c.getAddedSubList();
        if (added != null && !added.isEmpty()) {
          for (Tab tab : added) {
            if (tab.getUserData() != null && tab.getUserData() instanceof AbstractTabContainer) {
              AbstractTabContainer abstractTab = (AbstractTabContainer) tab.getUserData();
              if (abstractTab.getTabType() == TabType.CHARACTERS && abstractTab instanceof CharactersTabContainer) {
                CharactersTabContainer charactersTabContainer = (CharactersTabContainer) abstractTab;
                for (CharacterModel characterModel : charactersTabContainer.getCharacterModels())
                  currentProject.addTileForTileTabKey(charactersTabContainer.getKey(), characterModel.getFile());
              }
            }
          }
        }
      }
    });
  }

  private void addCharacterTabForCharacterFiles(String title, List<File> characterFiles) {
    try {
      List<CharacterModel> characterModels = loadCharacterModelListForFiles(characterFiles);
      addCharacterTabFromXMLModels(title, characterModels);
    } catch (Exception ex) {
      Dialog.showWarningDialog(null, "ManageCharactersController error occurred. Error message: " + ex.getMessage());
    }
  }

  public List<CharacterModel> loadCharacterModelListUnderDirectory(File dirFile) throws Exception {
    List<CharacterModel> characters = MapEditorController.getInstance().getRepoController().openCharactersUnderDir(dirFile, null);
    loadTilesForCharacterModels(characters);
    return characters;
  }

  private List<CharacterModel> loadCharacterModelListForFiles(List<File> characterFiles) throws Exception {
    List<CharacterModel> characterModels = MapEditorController.getInstance().getRepoController().openCharactersForFiles(characterFiles, null);
    loadTilesForCharacterModels(characterModels);
    return characterModels;
  }

  private void loadTilesForCharacterModels(List<CharacterModel> characterModels) throws Exception {
    for (CharacterModel characterModel : characterModels) {
      List<ImageModel> upTiles = characterModel.getUpTiles();
      List<ImageModel> downTiles = characterModel.getDownTiles();
      List<ImageModel> leftTiles = characterModel.getLeftTiles();
      List<ImageModel> rightTiles = characterModel.getRightTiles();
      if (upTiles == null || downTiles == null || leftTiles == null || rightTiles == null ||
              !(upTiles.size() == downTiles.size() && downTiles.size() == leftTiles.size() && leftTiles.size() == rightTiles.size()))
        continue;
      for (int i = 0; i < upTiles.size(); i ++) {
        ImageModel up = upTiles.get(i);
        ImageModel down = downTiles.get(i);
        ImageModel left = leftTiles.get(i);
        ImageModel right = rightTiles.get(i);

        up.setImage(ImageProvider.getImage(up.getFile()));
        down.setImage(ImageProvider.getImage(down.getFile()));
        left.setImage(ImageProvider.getImage(left.getFile()));
        right.setImage(ImageProvider.getImage(right.getFile()));
      }
    }
  }

  public void addCharacterTabFromXMLModels(String title, List<CharacterModel> characters) {
    if (StringValidator.isNullOrEmpty(title) || characters == null || characters.isEmpty())
      return;

    CharactersTabContainer charactersTabContainer = new CharactersTabContainer(title, true);
    for (CharacterModel character : characters)
      charactersTabContainer.addTile(new SelectableCharacterView(character, true, this));

    Tab tab = new Tab(title, charactersTabContainer.asNode());
    tab.setUserData(charactersTabContainer);
    view.addTab(tab);
  }

  @Override
  public void selectedCharacterChanged(SelectableCharacterView characterView) {
    if (selectableCharacterView == null) {
      selectableCharacterView = characterView;
    } else if (selectableCharacterView != characterView) {
      selectableCharacterView.unselect();
      selectableCharacterView = characterView;
    }

    listener.selectedEntityChanged(selectableCharacterView != null ? selectableCharacterView.getCharacterModel() : null);
  }

  @Override
  public void selectedTileChanged(SelectableTileView selectedView) {
    // do nothing
  }

  @Override
  public void selectedBrushChanged(SelectableBrushView brushView) {
    // do nothing
  }

  @Override
  public void selectedObjectChanged(SelectableObjectView objectView) {
    // do nothing
  }
}
