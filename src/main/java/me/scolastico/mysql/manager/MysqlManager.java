package me.scolastico.mysql.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.scolastico.mysql.manager.dataholders.Database;
import me.scolastico.mysql.manager.dataholders.Fields;
import me.scolastico.mysql.manager.etc.SimplifiedFunctions;
import me.scolastico.mysql.manager.exceptions.NoDataException;
import me.scolastico.mysql.manager.exceptions.NoFieldsException;
import me.scolastico.mysql.manager.exceptions.NotATableException;
import me.scolastico.mysql.manager.exceptions.NotSavedEntryException;
import me.scolastico.mysql.manager.interfaces.Annotations.Table;
import me.scolastico.mysql.manager.interfaces.Annotations.TableEntry;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MysqlManager {

  private final String host;
  private final int port;
  private final String db;
  private final String username;
  private final String password;
  private final boolean sqLite;
  private final Connection connection;
  private final Gson gson = new GsonBuilder().create();

  public MysqlManager(String sqLiteFileName) throws SQLException {
    this.host = sqLiteFileName;
    this.port = 0;
    this.db = null;
    this.username = null;
    this.password = null;
    this.sqLite = true;
    this.connection = DriverManager.getConnection("jdbc:sqlite:" + sqLiteFileName);
    try {
      Database.generateTable(this);} catch (NotATableException | NoSuchMethodException | NotSavedEntryException | NoFieldsException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoDataException ignored) {}
  }

  public MysqlManager(String host, int port, String db, String username, String password) throws SQLException {
    this.host = host;
    this.port = port;
    this.db = db;
    this.username = username;
    this.password = password;
    this.sqLite = false;
    this.connection = DriverManager.getConnection(
        "jdbc:mysql://" + host + "/" + db +
            "?user=" + SimplifiedFunctions.urlEncode(username) +
            "&password=" + SimplifiedFunctions.urlEncode(password) +
            "&autoReconnect=true"
    );
    try {Database.generateTable(this);} catch (NotATableException | NoSuchMethodException | NotSavedEntryException | NoFieldsException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoDataException ignored) {}
  }

  public MysqlManager(String host, int port, String db, String username, String password, String additionalArguments) throws SQLException {
    this.host = host;
    this.port = port;
    this.db = db;
    this.username = username;
    this.password = password;
    this.sqLite = false;
    this.connection = DriverManager.getConnection(
        "jdbc:mysql://" + host + "/" + db +
            "?user=" + SimplifiedFunctions.urlEncode(username) +
            "&password=" + SimplifiedFunctions.urlEncode(password) +
            "&autoReconnect=true" +
            "&" + additionalArguments
    );
    try {Database.generateTable(this);} catch (NotATableException | NoSuchMethodException | NotSavedEntryException | NoFieldsException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException | NoDataException ignored) {}
  }

  public void generateTable(Object table) throws NotATableException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException, NoFieldsException, NotSavedEntryException, NoDataException {
    String tableName = getTableName(table);
    Map<String, Field> fields = getAllFields(table);
    StringBuilder value = new StringBuilder();
    for (String key:fields.keySet()) {
      Field field = fields.get(key);
      getKeysWithType(value, key, field);
    }
    if (!tableName.equals("MySQL_Manager_Internal")) {
      Database[] databases = Database.searchByDatabaseName(tableName, this);
      if (databases.length == 1) {
        Database database = databases[0];
        ArrayList<String> notExistingFields = new ArrayList<>(database.getFields().getFieldsList());
        for (String key:fields.keySet()) {
          notExistingFields.remove(key);
          Field field = fields.get(key);
          if (!database.getFields().getFieldsList().contains(key)) {
            String query = null;
            if (field.getType() == String.class) {
              query = "ALTER TABLE `" + tableName + "` ADD `" + key + "` text;";
            } else if (field.getType() == Integer.class) {
              query = "ALTER TABLE `" + tableName + "` ADD `" + key + "` int;";
            } else if (field.getType() == Long.class) {
              query = "ALTER TABLE `" + tableName + "` ADD `" + key + "` bigint;";
            } else if (field.getType() == Double.class) {
              query = "ALTER TABLE `" + tableName + "` ADD `" + key + "` double;";
            } else {
              query = "ALTER TABLE `" + tableName + "` ADD `" + key + "` text;";
            }
            connection.prepareStatement(query).execute();
            database.getFields().getFieldsList().add(key);
          }
        }
        if (sqLite && notExistingFields.size() > 0) {
          StringBuilder keysWithType = new StringBuilder();
          StringBuilder keys = new StringBuilder();
          for (String key:fields.keySet()) {
            Field field = fields.get(key);
            keys.append(", ").append(key);
            getKeysWithType(keysWithType, key, field);
          }
          String[] query = {
              "PRAGMA foreign_keys=off;",
              "ALTER TABLE " + tableName + " RENAME TO _old_" + tableName + ";",
              "CREATE TABLE `" + tableName + "` (`id` INTEGER PRIMARY KEY" + keysWithType.toString() + ");",
              "INSERT INTO " + tableName + " (id" + keys.toString() + ") SELECT id" + keys.toString() + " FROM _old_" + tableName + ";",
              "PRAGMA foreign_keys=on;",
              "DROP TABLE _old_" + tableName + ";"
          };
          for (String q:query) {
            connection.prepareStatement(q).execute();
          }
        } else {
          for (String field:notExistingFields) {
            connection.prepareStatement("ALTER TABLE " + tableName + " DROP " + field + ";").execute();
            database.getFields().getFieldsList().remove(field);
          }
        }
        database.update();
        return;
      }
    }
    if (sqLite) {
      connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + tableName + "` (`id` INTEGER PRIMARY KEY" + value.toString() + ");").execute();
    } else {
      connection.prepareStatement("CREATE TABLE IF NOT EXISTS `" + tableName + "` (`id` INT NOT NULL AUTO_INCREMENT" + value.toString() + ", PRIMARY KEY( `id` ));").execute();
    }
    if (!tableName.equals("MySQL_Manager_Internal")) {
      Fields f = new Fields();
      ArrayList<String> fieldList = new ArrayList<>(fields.keySet());
      f.setFieldsList(fieldList);
      new Database(this, tableName, f).save();
    }
  }

  private void getKeysWithType(StringBuilder keysWithType, String key, Field field) {
    if (field.getType() == String.class) {
      keysWithType.append(", `").append(key).append("` text");
    } else if (field.getType() == Integer.class) {
      keysWithType.append(", `").append(key).append("` int");
    } else if (field.getType() == Long.class) {
      keysWithType.append(", `").append(key).append("` bigint");
    } else if (field.getType() == Double.class) {
      keysWithType.append(", `").append(key).append("` double");
    } else {
      keysWithType.append(", `").append(key).append("` text");
    }
  }

  public Object getFromTableById(Object table, Long id) throws NotATableException, SQLException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
    String tableName = getTableName(table);
    ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM `" + tableName + "` WHERE id=" + id + ";");
    if (rs.next()) {
      Map<String, Field> fields = getAllFields(table);
      fillDataHolder(table, rs, fields, table);
      rs.close();
      return table;
    }
    return null;
  }

  public Object[] getFromTableBySearch(Object table, String name, Object object) throws NotATableException, SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
    ArrayList<Object> ret = new ArrayList<>();
    String tableName = getTableName(table);
    String query = null;
    if (object == null) {
      query = "SELECT * FROM `" + tableName + "` WHERE " + name + " IS NULL;";
    } else if (object instanceof String) {
      query = "SELECT * FROM `" + tableName + "` WHERE " + name + " = '" + SimplifiedFunctions.urlEncode((String) object) + "';";
    } else if (object instanceof Integer) {
      query = "SELECT * FROM `" + tableName + "` WHERE " + name + " = " + (Integer) object + ";";
    } else if (object instanceof Long) {
      query = "SELECT * FROM `" + tableName + "` WHERE " + name + " = " + (Long) object + ";";
    } else if (object instanceof Double) {
      query = "SELECT * FROM `" + tableName + "` WHERE " + name + " = " + (Double) object + ";";
    } else {
      query = "SELECT * FROM `" + tableName + "` WHERE " + name + " = '" + SimplifiedFunctions.urlEncode(gson.toJson(object)) + "';";
    }
    ResultSet rs = connection.createStatement().executeQuery(query);
    return getObjectsFromResultSet(table, ret, rs);
  }

  public Object[] getAllFromTable(Object table) throws NotATableException, SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
    ArrayList<Object> ret = new ArrayList<>();
    String tableName = getTableName(table);
    ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM " + tableName + ";");
    return getObjectsFromResultSet(table, ret, rs);
  }

  private Object[] getObjectsFromResultSet(Object table, ArrayList<Object> ret, ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
    while (rs.next()) {
      Map<String, Field> fields = getAllFields(table);
      Object obj = table.getClass().getConstructor(new Class[]{MysqlManager.class}).newInstance(this);
      fillDataHolder(table, rs, fields, obj);
      ret.add(obj);
    }
    rs.close();
    return ret.toArray();
  }

  private void fillDataHolder(Object table, ResultSet rs, Map<String, Field> fields, Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException, NoSuchFieldException {
    for (String key:fields.keySet()) {
      Field field = fields.get(key);
      String fieldName = field.getName();
      String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      if (field.getType() == String.class) {
        Method method = table.getClass().getMethod(methodName, String.class);
        if (rs.getString(key) == null) {
          method.invoke(obj, String.class.cast(null));
        } else {
          method.invoke(obj, SimplifiedFunctions.urlDecode(rs.getString(key)));
        }
      } else if (field.getType() == Integer.class) {
        Method method = table.getClass().getMethod(methodName, Integer.class);
        method.invoke(obj, rs.getInt(key));
      } else if (field.getType() == Long.class) {
        Method method = table.getClass().getMethod(methodName, Long.class);
        method.invoke(obj, rs.getLong(key));
      } else if (field.getType() == Double.class) {
        Method method = table.getClass().getMethod(methodName, Double.class);
        method.invoke(obj, rs.getDouble(key));
      } else {
        String string = rs.getString(key);
        Method method = table.getClass().getMethod(methodName, field.getType());
        if (string == null) {
          method.invoke(obj, field.getType().cast(null));
        } else {
          method.invoke(obj, gson.fromJson(SimplifiedFunctions.urlDecode(string), field.getType()));
        }
      }
    }
    Field field = getIdField(table);
    Method method = table.getClass().getMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
    method.invoke(obj, gson.fromJson(SimplifiedFunctions.urlDecode(rs.getString("id")), field.getType()));
  }

  public void delete(Object table, Long id) throws NotATableException, SQLException {
    String tableName = getTableName(table);
    connection.prepareStatement("DELETE FROM " + tableName + " WHERE id = " + id + ";").executeUpdate();
  }

  public void deleteTable(Object table) throws NotATableException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException, NotSavedEntryException {
    String tableName = getTableName(table);
    connection.prepareStatement("DROP TABLE " + tableName + ";").executeUpdate();
    Database[] databases = Database.searchByDatabaseName(tableName, this);
    for (Database database:databases) database.delete();
  }

  public void updateTable(Object table) throws NotATableException, NoFieldsException, NoSuchFieldException, IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException, NoDataException {
    String tableName = getTableName(table);
    Map<String, Field> fields = getAllFields(table);
    if (fields.size() == 0) throw new NoFieldsException();
    StringBuilder value = new StringBuilder();
    Field idField = getIdField(table);
    Method method = table.getClass().getMethod("get" + idField.getName().substring(0, 1).toUpperCase() + idField.getName().substring(1), null);
    Object object = method.invoke(table, null);
    long id = (long) object;
    for (String key:fields.keySet()) {
      Field field = fields.get(key);
      String name = field.getName();
      method = table.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), null);
      object = method.invoke(table, null);
      if (field.getType() == String.class) {
        if (object == null) {
          value.append(", ").append(key).append(" = null");
        } else {
          value.append(", ").append(key).append(" = '").append(SimplifiedFunctions.urlEncode((String) object)).append("'");
        }
      } else if (field.getType() == Integer.class) {
        value.append(", ").append(key).append(" = ").append((Integer) object);
      } else if (field.getType() == Long.class) {
        value.append(", ").append(key).append(" = ").append((Long) object);
      } else if (field.getType() == Double.class) {
        value.append(", ").append(key).append(" = ").append((Double) object);
      } else {
        if (object == null) {
          value.append(", ").append(key).append(" = null");
        } else {
          value.append(", ").append(key).append(" = '").append(SimplifiedFunctions.urlEncode(gson.toJson(object))).append("'");
        }
      }
    }
    if (value.toString().length() == 0) throw new NoDataException();
    String completeQuery = "UPDATE `" + tableName + "` SET" + value.substring(1) + " WHERE id = " + id + ";";
    connection.prepareStatement(completeQuery).executeUpdate();
  }

  public long saveTable(Object table) throws IllegalAccessException, SQLException, NotATableException, NoFieldsException, NoSuchMethodException, InvocationTargetException, NoDataException {
    String tableName = getTableName(table);
    Map<String, Field> fields = getAllFields(table);
    if (fields.size() == 0) throw new NoFieldsException();
    StringBuilder query = new StringBuilder();
    StringBuilder value = new StringBuilder();
    for (String key:fields.keySet()) {
      Field field = fields.get(key);
      String name = field.getName();
      Method method = table.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), null);
      Object object = method.invoke(table, null);
      if (object == null) continue;
      query.append(", ").append(key);
      if (field.getType() == String.class) {
        value.append(", '").append(SimplifiedFunctions.urlEncode((String) object)).append("'");
      } else if (field.getType() == Integer.class) {
        value.append(", ").append((Integer) object);
      } else if (field.getType() == Long.class) {
        value.append(", ").append((Long) object);
      } else if (field.getType() == Double.class) {
        value.append(", ").append((Double) object);
      } else {
        value.append(",'").append(SimplifiedFunctions.urlEncode(gson.toJson(object))).append("'");
      }
    }
    if (query.toString().length() == 0) throw new NoDataException();
    String completeQuery = "INSERT INTO`" + tableName + "` (" + query.toString().substring(2) + ") VALUES (" + value.toString().substring(1) + ");";
    PreparedStatement statement = connection.prepareStatement(completeQuery, Statement.RETURN_GENERATED_KEYS);
    return statement.executeUpdate();
  }

  private Map<String, Field> getAllFields(Object table) {
    Map<String, Field> ret = new HashMap<>();
    Field[] fields = table.getClass().getDeclaredFields();
    for (Field field:fields) {
      if (!Modifier.isStatic(field.getModifiers())) {
        TableEntry annotation = field.getAnnotation(TableEntry.class);
        if (annotation != null) {
          field.setAccessible(true);
          if (annotation.name().equals("")) {
            ret.put(field.getName(), field);
          } else {
            ret.put(annotation.name(), field);
          }
        }
      }
    }
    return ret;
  }

  private Field getIdField(Object table) throws NoSuchFieldException {
    return table.getClass().getDeclaredField("id");
  }

  private String getTableName(Object table) throws NotATableException {
    Table annotation = table.getClass().getAnnotation(Table.class);
    if (annotation == null) throw new NotATableException();
    if (annotation.tableName().equals("")) {
      return table.getClass().getSimpleName();
    } else {
      return annotation.tableName();
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public void closeConnection() throws SQLException {
    connection.close();
  }

}
