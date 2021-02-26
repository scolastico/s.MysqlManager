package me.scolastico.mysql.manager.etc;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SimplifiedFunctions {

  public static String urlEncode(String string) {
    return URLEncoder.encode(string, StandardCharsets.UTF_8);
  }

  public static String urlDecode(String string) {
    return URLDecoder.decode(string, StandardCharsets.UTF_8);
  }

}
