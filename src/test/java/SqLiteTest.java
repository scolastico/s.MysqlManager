import me.scolastico.mysql.manager.MysqlManager;
import dataholders.ArrayDataHolder;
import dataholders.ExampleDataHolder1;
import dataholders.ExampleDataHolder2;
import java.io.File;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class SqLiteTest {

  private static MysqlManager mysqlManager = null;
  private static File file;
  private static ExampleDataHolder1 tmp;
  private static ExampleDataHolder1[] t;

  @Test
  @Order(1)
  public void testSetupDataBase() throws Exception {
    file = new File("./test.db");
    if (file.exists()) Assertions.assertTrue(file.delete());
    mysqlManager = new MysqlManager("./test.db");
    Assertions.assertNotNull(mysqlManager);
  }

  @Test
  @Order(2)
  public void testCreateTable() throws Exception {
    ExampleDataHolder1.generateTable(mysqlManager);
  }

  @Test
  @Order(3)
  public void testCreateEntry() throws Exception {
    tmp = new ExampleDataHolder1(mysqlManager);
    tmp.save();
    tmp.setI(1);
    tmp.update();
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
  }

  @Test
  @Order(4)
  public void testGetById() throws Exception {
    tmp = ExampleDataHolder1.getById(tmp.getId(), mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
    tmp = ExampleDataHolder1.getById(1L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
  }

  @Test
  @Order(5)
  public void testGetBySearch() throws Exception {
    t = ExampleDataHolder1.searchByS("Im a example!", mysqlManager);
    Assertions.assertNotNull(t);
    Assertions.assertEquals(1, t.length);
    tmp = t[0];
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
    t = ExampleDataHolder1.searchByI(1, mysqlManager);
    Assertions.assertNotNull(t);
    Assertions.assertEquals(1, t.length);
    tmp = t[0];
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
    t = ExampleDataHolder1.searchByL(456L, mysqlManager);
    Assertions.assertNotNull(t);
    Assertions.assertEquals(1, t.length);
    tmp = t[0];
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
    t = ExampleDataHolder1.searchByD(7.89D, mysqlManager);
    Assertions.assertNotNull(t);
    Assertions.assertEquals(1, t.length);
    tmp = t[0];
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
  }

  @Test
  @Order(6)
  public void testMigrateWithNewField() throws Exception {
    ExampleDataHolder2.generateTable(mysqlManager);
  }

  @Test
  @Order(7)
  public void testMigration() throws Exception {
    ExampleDataHolder2 tmp = new ExampleDataHolder2(mysqlManager);
    tmp.save();
    tmp = ExampleDataHolder2.getById(1L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
    Assertions.assertNull(tmp.getDataHolder());
    tmp = ExampleDataHolder2.getById(2L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals("Im a example!", tmp.getS());
    Assertions.assertEquals(123, tmp.getI());
    Assertions.assertEquals(456L, tmp.getL());
    Assertions.assertEquals(7.89D, tmp.getD(), 0.0);
    Assertions.assertEquals(new ArrayDataHolder().getArrayList().get(0), tmp.getDataHolder().getArrayList().get(0));
  }

  @Test
  @Order(8)
  public void testMigrateWithoutNewField() throws Exception {
    ExampleDataHolder1.generateTable(mysqlManager);
    t = ExampleDataHolder1.getAll(mysqlManager);
    Assertions.assertNotNull(t);
    Assertions.assertEquals(2, t.length);
  }

  @Test
  @Order(9)
  public void testNullValuesInTable() throws Exception {
    tmp = new ExampleDataHolder1(mysqlManager);
    tmp.setI(null);
    tmp.setD(null);
    tmp.setL(null);
    tmp.setS("some data");
    tmp.save();
    tmp = ExampleDataHolder1.getById(3L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals(0, tmp.getI());
    Assertions.assertEquals(0D, tmp.getD());
    Assertions.assertEquals(0L, tmp.getL());
    Assertions.assertEquals("some data", tmp.getS());
    tmp.update();
    tmp = ExampleDataHolder1.getById(3L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals(0, tmp.getI());
    Assertions.assertEquals(0D, tmp.getD());
    Assertions.assertEquals(0L, tmp.getL());
    Assertions.assertEquals("some data", tmp.getS());
    tmp = new ExampleDataHolder1(mysqlManager);
    tmp.setI(1);
    tmp.setD(null);
    tmp.setL(null);
    tmp.setS(null);
    tmp.save();
    tmp = ExampleDataHolder1.getById(4L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(0D, tmp.getD());
    Assertions.assertEquals(0L, tmp.getL());
    Assertions.assertNull(tmp.getS());
    tmp.update();
    tmp = ExampleDataHolder1.getById(4L, mysqlManager);
    Assertions.assertNotNull(tmp);
    Assertions.assertEquals(1, tmp.getI());
    Assertions.assertEquals(0D, tmp.getD());
    Assertions.assertEquals(0L, tmp.getL());
    Assertions.assertNull(tmp.getS());
  }

  @Test
  @Order(10)
  public void testSearchForNullValuesInTable() throws Exception {
    t = ExampleDataHolder1.searchByI(0, mysqlManager);
    Assertions.assertEquals(1, t.length);
    t = ExampleDataHolder1.searchByD(0D, mysqlManager);
    Assertions.assertEquals(2, t.length);
    t = ExampleDataHolder1.searchByL(0L, mysqlManager);
    Assertions.assertEquals(2, t.length);
    t = ExampleDataHolder1.searchByS(null, mysqlManager);
    Assertions.assertEquals(1, t.length);
  }

  @Test
  @Order(11)
  public void testDeleteEntry() throws Exception {
    tmp = ExampleDataHolder1.getById(1L, mysqlManager);
    Assertions.assertNotNull(tmp);
    tmp.delete();
    tmp = ExampleDataHolder1.getById(1L, mysqlManager);
    Assertions.assertNull(tmp);
    t = ExampleDataHolder1.searchByD(7.89D, mysqlManager);
    Assertions.assertNotNull(t);
    Assertions.assertEquals(1, t.length);
  }

  @Test
  @Order(12)
  public void testDeleteTable() throws Exception {
    ExampleDataHolder1.deleteTable(mysqlManager);
    try {
      ExampleDataHolder1.getById(1L, mysqlManager);
      Assertions.fail();
    } catch (Exception ignored) {}
  }

  @Test
  @Order(13)
  public void cleanUp() throws Exception {
    mysqlManager.closeConnection();
    Assertions.assertTrue(file.delete());
  }

}
