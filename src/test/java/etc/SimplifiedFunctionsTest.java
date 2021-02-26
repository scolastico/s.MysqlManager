package etc;

import me.scolastico.mysql.manager.etc.SimplifiedFunctions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimplifiedFunctionsTest {

  @Test
  public void testIfUrlDecodeReturnsCorrectValues() {
    Assertions.assertEquals("1mAT€stV4lue", SimplifiedFunctions.urlDecode("1mAT%E2%82%ACstV4lue"));
    Assertions.assertEquals("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", SimplifiedFunctions.urlDecode("Lorem%20ipsum%20dolor%20sit%20amet%2C%20consetetur%20sadipscing%20elitr%2C%20sed%20diam%20nonumy%20eirmod%20tempor%20invidunt%20ut%20labore%20et%20dolore%20magna%20aliquyam%20erat%2C%20sed%20diam%20voluptua.%20At%20vero%20eos%20et%20accusam%20et%20justo%20duo%20dolores%20et%20ea%20rebum.%20Stet%20clita%20kasd%20gubergren%2C%20no%20sea%20takimata%20sanctus%20est%20Lorem%20ipsum%20dolor%20sit%20amet.%20Lorem%20ipsum%20dolor%20sit%20amet%2C%20consetetur%20sadipscing%20elitr%2C%20sed%20diam%20nonumy%20eirmod%20tempor%20invidunt%20ut%20labore%20et%20dolore%20magna%20aliquyam%20erat%2C%20sed%20diam%20voluptua.%20At%20vero%20eos%20et%20accusam%20et%20justo%20duo%20dolores%20et%20ea%20rebum.%20Stet%20clita%20kasd%20gubergren%2C%20no%20sea%20takimata%20sanctus%20est%20Lorem%20ipsum%20dolor%20sit%20amet."));
  }

  @Test
  public void testIfUrlEncodeReturnsCorrectValues() {
    Assertions.assertEquals("1mAT%E2%82%ACstV4lue", SimplifiedFunctions.urlEncode("1mAT€stV4lue"));
    Assertions.assertEquals("Lorem+ipsum+dolor+sit+amet%2C+consetetur+sadipscing+elitr%2C+sed+diam+nonumy+eirmod+tempor+invidunt+ut+labore+et+dolore+magna+aliquyam+erat%2C+sed+diam+voluptua.+At+vero+eos+et+accusam+et+justo+duo+dolores+et+ea+rebum.+Stet+clita+kasd+gubergren%2C+no+sea+takimata+sanctus+est+Lorem+ipsum+dolor+sit+amet.+Lorem+ipsum+dolor+sit+amet%2C+consetetur+sadipscing+elitr%2C+sed+diam+nonumy+eirmod+tempor+invidunt+ut+labore+et+dolore+magna+aliquyam+erat%2C+sed+diam+voluptua.+At+vero+eos+et+accusam+et+justo+duo+dolores+et+ea+rebum.+Stet+clita+kasd+gubergren%2C+no+sea+takimata+sanctus+est+Lorem+ipsum+dolor+sit+amet.", SimplifiedFunctions.urlEncode("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."));
  }

  @Test
  public void testIfUrlEncoderAndDecoderWorksTogether() {
    Assertions.assertEquals("1mAT€stV4lue", SimplifiedFunctions.urlDecode(SimplifiedFunctions.urlEncode("1mAT€stV4lue")));
    Assertions.assertEquals("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", SimplifiedFunctions.urlDecode(SimplifiedFunctions.urlEncode("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.")));
    for (int i = 0; i < 1000; i++) {
      String random = RandomStringUtils.randomAscii(500);
      Assertions.assertEquals(random, SimplifiedFunctions.urlDecode(SimplifiedFunctions.urlEncode(random)));
    }
  }

}
