package dataholders;

import me.scolastico.mysql.manager.MysqlManager;
import me.scolastico.mysql.manager.exceptions.NoDataException;
import me.scolastico.mysql.manager.exceptions.NoFieldsException;
import me.scolastico.mysql.manager.exceptions.NotATableException;
import me.scolastico.mysql.manager.exceptions.NotSavedEntryException;
import me.scolastico.mysql.manager.interfaces.Annotations.Table;
import me.scolastico.mysql.manager.interfaces.Annotations.TableEntry;
import me.scolastico.mysql.manager.interfaces.Annotations.TableId;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

@Table(tableName = "ExampleDataBase")
public class ExampleDataHolder1 {

  public static void generateTable(MysqlManager manager) throws SQLException, NotATableException, NoSuchMethodException, NotSavedEntryException, NoFieldsException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoDataException {
    manager.generateTable(new ExampleDataHolder1(manager));
  }

  public static ExampleDataHolder1 getById(Long id, MysqlManager mysqlManager) throws SQLException, NotATableException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
    Object obj = mysqlManager.getFromTableById(new ExampleDataHolder1(mysqlManager), id);
    if (obj != null) {
      return (ExampleDataHolder1) obj;
    }
    return null;
  }

  public static ExampleDataHolder1[] getAll(MysqlManager mysqlManager) throws IllegalAccessException, NotATableException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
    Object[] objects = mysqlManager.getAllFromTable(new ExampleDataHolder1(mysqlManager));
    ArrayList<ExampleDataHolder1> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((ExampleDataHolder1) obj);
    }
    return dataHolders.toArray(new ExampleDataHolder1[0]);
  }

  public static ExampleDataHolder1[] searchByS(String s, MysqlManager mysqlManager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Object[] objects = mysqlManager.getFromTableBySearch(new ExampleDataHolder1(mysqlManager), "exampleString", s);
    ArrayList<ExampleDataHolder1> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((ExampleDataHolder1) obj);
    }
    return dataHolders.toArray(new ExampleDataHolder1[0]);
  }

  public static ExampleDataHolder1[] searchByI(Integer i, MysqlManager mysqlManager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Object[] objects = mysqlManager.getFromTableBySearch(new ExampleDataHolder1(mysqlManager), "i", i);
    ArrayList<ExampleDataHolder1> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((ExampleDataHolder1) obj);
    }
    return dataHolders.toArray(new ExampleDataHolder1[0]);
  }

  public static ExampleDataHolder1[] searchByL(Long l, MysqlManager mysqlManager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Object[] objects = mysqlManager.getFromTableBySearch(new ExampleDataHolder1(mysqlManager), "l", l);
    ArrayList<ExampleDataHolder1> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((ExampleDataHolder1) obj);
    }
    return dataHolders.toArray(new ExampleDataHolder1[0]);
  }

  public static ExampleDataHolder1[] searchByD(Double d, MysqlManager mysqlManager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Object[] objects = mysqlManager.getFromTableBySearch(new ExampleDataHolder1(mysqlManager), "d", d);
    ArrayList<ExampleDataHolder1> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((ExampleDataHolder1) obj);
    }
    return dataHolders.toArray(new ExampleDataHolder1[0]);
  }

  public static void deleteTable(MysqlManager manager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NotSavedEntryException {
    manager.deleteTable(new ExampleDataHolder1(manager));
  }

  public void delete() throws SQLException, NotATableException, NotSavedEntryException {
    if (id == null) throw new NotSavedEntryException();
    mysqlManager.delete(this, id);
  }

  public void update() throws NotSavedEntryException, SQLException, NoSuchFieldException, IllegalAccessException, NoFieldsException, NotATableException, NoSuchMethodException, InvocationTargetException, NoDataException {
    if (id == null) throw new NotSavedEntryException();
    mysqlManager.updateTable(this);
  }

  public void save() throws SQLException, NotATableException, NoFieldsException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoDataException {
    id = mysqlManager.saveTable(this);
  }

  public ExampleDataHolder1(MysqlManager mysqlManager) {
    this.mysqlManager = mysqlManager;
  }

  public ExampleDataHolder1(String s, int i, long l, double d, MysqlManager mysqlManager) {
    this.s = s;
    this.i = i;
    this.l = l;
    this.d = d;
    this.mysqlManager = mysqlManager;
  }

  @TableId
  private Long id = null;

  private final MysqlManager mysqlManager;

  @TableEntry(name = "exampleString")
  private String s = "Im a example!";

  @TableEntry()
  private Integer i = 123;

  @TableEntry()
  private Long l = 456L;

  @TableEntry()
  private Double d = 7.89D;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getS() {
    return s;
  }

  public void setS(String s) {
    this.s = s;
  }

  public Integer getI() {
    return i;
  }

  public void setI(Integer i) {
    this.i = i;
  }

  public Long getL() {
    return l;
  }

  public void setL(Long l) {
    this.l = l;
  }

  public Double getD() {
    return d;
  }

  public void setD(Double d) {
    this.d = d;
  }

}
