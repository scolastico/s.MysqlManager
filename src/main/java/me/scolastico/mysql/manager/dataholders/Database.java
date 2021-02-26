package me.scolastico.mysql.manager.dataholders;

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

@Table(tableName = "MySQL_Manager_Internal")
public class Database {

  public static void generateTable(MysqlManager manager) throws SQLException, NotATableException, NoSuchMethodException, NotSavedEntryException, NoFieldsException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, NoDataException {
    manager.generateTable(new Database(manager));
  }

  public static Database getById(Long id, MysqlManager mysqlManager) throws SQLException, NotATableException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
    Object obj = mysqlManager.getFromTableById(new Database(mysqlManager), id);
    if (obj != null) {
      return (Database) obj;
    }
    return null;
  }

  public static Database[] searchByDatabaseName(String databaseName, MysqlManager mysqlManager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Object[] objects = mysqlManager.getFromTableBySearch(new Database(mysqlManager), "databaseName", databaseName);
    ArrayList<Database> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((Database) obj);
    }
    return dataHolders.toArray(new Database[0]);
  }

  public static Database[] searchByFields(Fields fields, MysqlManager mysqlManager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Object[] objects = mysqlManager.getFromTableBySearch(new Database(mysqlManager), "fields", fields);
    ArrayList<Database> dataHolders = new ArrayList<>();
    for (Object obj:objects) {
      dataHolders.add((Database) obj);
    }
    return dataHolders.toArray(new Database[0]);
  }

  public static void deleteTable(MysqlManager manager) throws SQLException, NotATableException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException, InvocationTargetException, NotSavedEntryException {
    manager.deleteTable(new Database(manager));
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

  public Database(MysqlManager mysqlManager) {
    this.mysqlManager = mysqlManager;
  }

  public Database(MysqlManager mysqlManager, String databaseName, Fields fields) {
    this.mysqlManager = mysqlManager;
    this.databaseName = databaseName;
    this.fields = fields;
  }

  private final MysqlManager mysqlManager;

  @TableId
  private Long id = null;

  @TableEntry
  private String databaseName = "db";

  @TableEntry
  private Fields fields = new Fields();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  public Fields getFields() {
    return fields;
  }

  public void setFields(Fields fields) {
    this.fields = fields;
  }

}
