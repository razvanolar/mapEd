package mapEditor.application.main_part.manage_maps.primary_map;

import mapEditor.application.main_part.app_utils.models.ImageModel;
import mapEditor.application.main_part.app_utils.models.brush.BrushModel;

import java.util.*;

/**
 *
 * Created by razvanolar on 25.05.2016.
 */
public class PrimaryMapUtil {

  public static List<Node> NODE_LIST;
  public static final Map<Integer, Node> NODE_MAP = initNodes();

  // binary matrix, used to on which positions the brush tiles are in the final position
  // 1 - tile is in its final position
  // 0 - position that need to be checked (find the intersection of the surrounding tiles)
  private static final int[][] matrixScheme = new int[BrushModel.PRIMARY_MATRIX_SIZE][BrushModel.PRIMARY_MATRIX_SIZE];

  // make sure they are set at each time the beginning of run method
  private int width;
  private int height;

  public void run(int cellX, int cellY, BrushModel brush, ImageModel[][] tilesMatrix) {
    height = tilesMatrix.length;
    width = tilesMatrix[0].length;

    tilesMatrix[cellY][cellX] = brush.getPrimaryImageModel();
    Node primaryNode = NODE_MAP.get(BrushModel.PRIMARY_TILE_NUMBER);
    int number;

    /*
      set the top, down, bottom and left tiles relative to the primary tile
      (checks to see if the primary tile does not contain the verified tile, but the verified tile contains the primary tile - in the same direction)
     */
    // top
    if (checkBounds(cellX, cellY - 1)) {
      number = brush.getNumberForTile(tilesMatrix[cellY - 1][cellX]);
      if (number != -1) {
        Node node = NODE_MAP.get(number);
        if (!primaryNode.getTop().contains(node) && node.getTop() != null && node.getTop().contains(primaryNode))
          tilesMatrix[cellY - 1][cellX] = brush.getPrimaryImageModel();
      }
    }
    // right
    if (checkBounds(cellX + 1, cellY)) {
      number = brush.getNumberForTile(tilesMatrix[cellY][cellX + 1]);
      if (number != -1) {
        Node node = NODE_MAP.get(number);
        if (!primaryNode.getRight().contains(node) && node.getRight() != null && node.getRight().contains(primaryNode))
          tilesMatrix[cellY][cellX + 1] = brush.getPrimaryImageModel();
      }
    }
    // bottom
    if (checkBounds(cellX, cellY + 1)) {
      number = brush.getNumberForTile(tilesMatrix[cellY + 1][cellX]);
      if (number != -1) {
        Node node = NODE_MAP.get(number);
        if (!primaryNode.getBottom().contains(node) && node.getBottom() != null && node.getBottom().contains(primaryNode))
          tilesMatrix[cellY + 1][cellX] = brush.getPrimaryImageModel();
      }
    }
    // left
    if (checkBounds(cellX -1, cellY)) {
      number = brush.getNumberForTile(tilesMatrix[cellY][cellX - 1]);
      if (number != -1) {
        Node node = NODE_MAP.get(number);
        if (!primaryNode.getLeft().contains(node) && node.getLeft() != null && node.getLeft().contains(primaryNode))
          tilesMatrix[cellY][cellX - 1] = brush.getPrimaryImageModel();
      }
    }

    /*
      resolve brush content, first try
      construct the matrixScheme
     */
    for (int i = -1; i<BrushModel.PRIMARY_MATRIX_SIZE - 1; i++) {
      for (int j = -1; j < BrushModel.PRIMARY_MATRIX_SIZE - 1; j++) {
        int x = cellX + j;
        int y = cellY + i;
        if (!checkBounds(x, y)) {
          matrixScheme[i+1][j+1] = 1;
          continue;
        }

        // if in the current position there is no tile or the tile is not part of the current brush, put the tile from
        // the brush corresponding to that position
        ImageModel imageModel = brush.getPrimaryMatrix()[i + 1][j + 1].getImageModel();
        if (tilesMatrix[y][x] == null || brush.getNumberForTile(tilesMatrix[y][x]) == -1) {
          tilesMatrix[y][x] = imageModel;
          matrixScheme[i+1][j+1] = 1;
          continue;
        }

        matrixScheme[i+1][j+1] = 0;

        Node top = null, right = null, bottom = null, left = null;
        // top
        if (checkBounds(x, y - 1)) {
          number = brush.getNumberForTile(tilesMatrix[y -1 ][x]);
          if (number != -1)
            top = NODE_MAP.get(number);
        }
        // right
        if (checkBounds(x + 1, y)) {
          number = brush.getNumberForTile(tilesMatrix[y][x + 1]);
          if (number != -1)
            right = NODE_MAP.get(number);
        }
        // bottom
        if (checkBounds(x, y + 1)) {
          number = brush.getNumberForTile(tilesMatrix[y + 1][x]);
          if (number != -1)
            bottom = NODE_MAP.get(number);
        }
        // left
        if (checkBounds(x - 1, y)) {
          number = brush.getNumberForTile(tilesMatrix[y][x -1]);
          if (number != -1)
            left = NODE_MAP.get(number);
        }
        number = brush.getNumberForTile(imageModel);
        Node node = NODE_MAP.get(number);
        List<Node> intersection = getIntersection(top, right, bottom, left);
        if (intersection != null && intersection.contains(node))
          tilesMatrix[y][x] = imageModel;
      }
    }

    /*
      resolve brush content, second try, based on the matrixScheme values
      ignore position with value of 1
      try to find the intersection of the surrounding tiles only for 0
     */
    for (int i = -1; i<BrushModel.PRIMARY_MATRIX_SIZE - 1; i++) {
      for (int j = -1; j < BrushModel.PRIMARY_MATRIX_SIZE - 1; j++) {
        int x = cellX + j;
        int y = cellY + i;
        if (!checkBounds(x, y) || matrixScheme[i+1][j+1] == 1)
          continue;

        Node top = null, right = null, bottom = null, left = null;
        // top
        if (checkBounds(x, y - 1)) {
          number = brush.getNumberForTile(tilesMatrix[y -1 ][x]);
          if (number != -1)
            top = NODE_MAP.get(number);
        }
        // right
        if (checkBounds(x + 1, y)) {
          number = brush.getNumberForTile(tilesMatrix[y][x + 1]);
          if (number != -1)
            right = NODE_MAP.get(number);
        }
        // bottom
        if (checkBounds(x, y + 1)) {
          number = brush.getNumberForTile(tilesMatrix[y + 1][x]);
          if (number != -1)
            bottom = NODE_MAP.get(number);
        }
        // left
        if (checkBounds(x - 1, y)) {
          number = brush.getNumberForTile(tilesMatrix[y][x -1]);
          if (number != -1)
            left = NODE_MAP.get(number);
        }

        List<Node> intersection = getIntersection(top, right, bottom, left);
        if (intersection == null || intersection.isEmpty())
          continue;

        Node node = intersection.get(0);
        if (node.isPrimaryNode()) {
          tilesMatrix[y][x] = brush.getPrimaryMatrix()[node.getRow()][node.getCol()].getImageModel();
        } else {
          tilesMatrix[y][x] = brush.getSecondaryMatrix()[node.getRow()][node.getCol()].getImageModel();
        }
      }
    }
  }

  private List<Node> getIntersection(Node topNode, Node rightNode, Node bottomNode, Node leftNode) {
    List<Node> aux = new ArrayList<>(NODE_LIST);

    if (topNode != null && topNode.getBottom() != null)
      aux.retainAll(topNode.getBottom());

    if (rightNode != null && rightNode.getLeft() != null)
      aux.retainAll(rightNode.getLeft());

    if (bottomNode != null && bottomNode.getTop() != null)
      aux.retainAll(bottomNode.getTop());

    if (leftNode != null && leftNode.getRight() != null)
      aux.retainAll(leftNode.getRight());

    return aux;
  }

  private boolean checkBounds(int cellX, int cellY) {
    return cellX >= 0 && cellX < width && cellY >=0 && cellY < height;
  }

  private static Map<Integer, Node> initNodes() {
    Node node1 = new Node(1);Node node2 = new Node(2);Node node3 = new Node(3);Node node4 = new Node(4);Node node5 = new Node(5);
    Node node6 = new Node(6);Node node7 = new Node(7);Node node8 = new Node(8);Node node9 = new Node(9);Node node11 = new Node(11);
    Node node12 = new Node(12);Node node13 = new Node(13);Node node14 = new Node(14);

    node1.addRight(node2, node14);
    node1.addBottom(node4, node14);

    node2.addRight(node2, node3, node14);
    node2.addBottom(node5, node11, node12);
    node2.addLeft(node2, node1, node13);

    node3.addBottom(node6, node13);
    node3.addLeft(node2, node13);

    node4.addTop(node4, node1, node12);
    node4.addRight(node5, node11, node13);
    node4.addBottom(node4, node7, node14);

    node5.addTop(node5, node2, node13, node14);
    node5.addRight(node5, node6, node11, node13);
    node5.addBottom(node5, node8, node11, node12);
    node5.addLeft(node5, node4, node12, node14);

    node6.addTop(node6, node3, node11);
    node6.addBottom(node6, node9, node13);
    node6.addLeft(node5, node12, node14);

    node7.addTop(node4, node12);
    node7.addRight(node8, node12);

    node8.addTop(node5, node13, node14);
    node8.addRight(node8, node9, node12);
    node8.addLeft(node8, node7, node11);

    node9.addTop(node6, node11);
    node9.addLeft(node8, node11);

    node11.addTop(node5, node2, node13, node14);
    node11.addRight(node8, node9, node12);
    node11.addBottom(node6, node9, node13);
    node11.addLeft(node4, node5, node12, node14);

    node12.addTop(node5, node2, node13, node14);
    node12.addRight(node5, node6, node11, node13);
    node12.addBottom(node4, node7, node14);
    node12.addLeft(node7, node8, node11);

    node13.addTop(node3, node6, node11);
    node13.addRight(node2, node3, node14);
    node13.addBottom(node5, node8, node11, node12);
    node13.addLeft(node5, node4, node12, node14);

    node14.addTop(node1, node4, node12);
    node14.addRight(node5, node6, node11, node13);
    node14.addBottom(node5, node8, node11, node12);
    node14.addLeft(node1, node2, node13);

    NODE_LIST = new ArrayList<>(13);
    NODE_LIST.add(node1);
    NODE_LIST.add(node2);
    NODE_LIST.add(node3);
    NODE_LIST.add(node4);
    NODE_LIST.add(node5);
    NODE_LIST.add(node6);
    NODE_LIST.add(node7);
    NODE_LIST.add(node8);
    NODE_LIST.add(node9);
    NODE_LIST.add(node11);
    NODE_LIST.add(node12);
    NODE_LIST.add(node13);
    NODE_LIST.add(node14);

    Map<Integer, Node> nodeMap = new HashMap<>();
    nodeMap.put(1, node1);
    nodeMap.put(2, node2);
    nodeMap.put(3, node3);
    nodeMap.put(4, node4);
    nodeMap.put(5, node5);
    nodeMap.put(6, node6);
    nodeMap.put(7, node7);
    nodeMap.put(8, node8);
    nodeMap.put(9, node9);
    nodeMap.put(11, node11);
    nodeMap.put(12, node12);
    nodeMap.put(13, node13);
    nodeMap.put(14, node14);
    return nodeMap;
  }
}



class Node {
  private int nodeNumber;

  // starting from 0
  private int row;
  private int col;

  private List<Node> top;
  private List<Node> bottom;
  private List<Node> left;
  private List<Node> right;

  public Node(int nodeNumber) {
    this.nodeNumber = nodeNumber;
    if (nodeNumber > 0 && nodeNumber < 10) {
      int size = BrushModel.PRIMARY_MATRIX_SIZE;
      this.row = nodeNumber / size;
      if (nodeNumber % size == 0)
        this.row --;
      this.col = nodeNumber % size == 0 ? 2 : nodeNumber % size - 1;
    } else if (nodeNumber == 11) {
      this.row = 0;
      this.col = 0;
    } else if (nodeNumber == 12) {
      this.row = 0;
      this.col = 1;
    } else if (nodeNumber == 13) {
      this.row = 1;
      this.col = 0;
    } else if (nodeNumber == 14) {
      this.row = 1;
      this.col = 1;
    }
  }

  public void addTop(Node... nodes) {
    top = new ArrayList<>(Arrays.asList(nodes));
  }

  public void addBottom(Node... nodes) {
    bottom = new ArrayList<>(Arrays.asList(nodes));
  }

  public void addLeft(Node... nodes) {
    left = new ArrayList<>(Arrays.asList(nodes));
  }

  public void addRight(Node... nodes) {
    right = new ArrayList<>(Arrays.asList(nodes));
  }

  public int getNodeNumber() {
    return nodeNumber;
  }

  public boolean isPrimaryNode() {
    return nodeNumber > 0 && nodeNumber < 10;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public List<Node> getTop() {
    return top;
  }

  public List<Node> getBottom() {
    return bottom;
  }

  public List<Node> getLeft() {
    return left;
  }

  public List<Node> getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    return nodeNumber;
  }

  @Override
  public boolean equals(Object obj) {
    return !(obj == null || !(obj instanceof Node)) && nodeNumber == ((Node) obj).getNodeNumber();
  }
}
