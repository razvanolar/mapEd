package mapEditor.application.repo;

/**
 *
 * Created by razvanolar on 20.02.2016.
 */
public class HexCounter {

  public static final String DEFAULT_VALUE = "00000000";
  public static final int LENGTH = 8;

  public static String getNextValue(String value) {
    if (!isHexString(value))
      return null;

    StringBuilder builder = new StringBuilder(value.toUpperCase());
    int i = 1;
    boolean q = true;
    while (q && i <= LENGTH) {
      char c = builder.charAt(LENGTH - i);
      c = getNext(c);
      builder.setCharAt(LENGTH - i, c);
      q = c == '0'; // continue if a '0' was obtained
      i ++;
    }
    return builder.toString();
  }

  public static boolean isHexString(String value) {
    return !(value == null || value.length() != LENGTH || !value.matches("^[a-fA-F0-9]{8}$"));
  }

  private static char getNext(char c) {
    if (c >= 'A' && c <= 'F') {
      if (c == 'F')
        c = '0';
      else
        c ++;
    } else if (c >= '0' && c <= '9') {
      if (c == '9')
        c = 'A';
      else
        c ++;
    }
    return c;
  }
}
