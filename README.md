# s.MysqlManager
[![GitHub](https://img.shields.io/github/license/scolastico/s.MysqlManager)](#)
[![GitHub](https://img.shields.io/github/languages/code-size/scolastico/s.MysqlManager)](#)
[![GitHub](https://img.shields.io/github/issues/scolastico/s.MysqlManager)](#)
[![GitHub](https://img.shields.io/github/v/tag/scolastico/s.MysqlManager?label=version)](#)
[![GitHub](https://github.com/scolastico/s.MysqlManager/actions/workflows/main.yml/badge.svg)](#)

Doctrine but for java!

## Description
s.MysqlManager is a library to make the management of MySQL or SqLite databases easier. It contains all the important functions such as database manipulation, data record search or migrations. In principle, it should be a substitute for Doctrine.

## Installation
### Maven:
s.MysqlManager is in the maven central repository so to install it just add the following dependency to your `pom.xml`.
```xml
  <dependencies>
    <dependency>
      <groupId>me.scolastico</groupId>
      <artifactId>mysql.manager</artifactId>
      <version>1.0.1</version>
    </dependency>
  </dependencies>
```
### Other:
Instructions for installing with a different build manager can be found [here](https://search.maven.org/artifact/me.scolastico/mysql.manager/1.0.1/jar).

## Usage
To use s.MysqlManager first go to https://smm-generator.scolasti.co and create a database class. Next add them to your project. In order to be able to use the database class, use the following example code and replace ExampleDataHolder with the name of your class.
```java
public static void main(String[] args) {
  try {
  
    // To start the connection:
    MysqlManager mysqlManager = new MysqlManager("./test.db");
    // To use MySQL instead of SqLite use: MysqlManager(String host, int port, String db, String username, String password)
    
    // To generate the table:
    ExampleDataHolder.generateTable(mysqlManager);
    // For better performance run this only one time in your code. For example while your software is starting.
    // This will also migrate your db if its changed scince the last time.
    // Don't forget to replace 'ExampleDataHolder' with the name of your class!
    
    // To create a entry:
    ExampleDataHolder dataHolder = new ExampleDataHolder(mysqlManager);
    
    // After that you can set your values with normal getter and setters:
    dataHolder.setExampleString("My great value!");
    
    // To save the entry in the database use:
    dataHolder.save();
    
    // If you edit an already saved entry you can update it like so:
    dataHolder.setExampleString("My changed value!");
    dataHolder.update();
    
    // If you want to delete an entry use:
    dataHolder.delete();
    
    // To search for an entry try:
    ExampleDataHolder[] searchResult = ExampleDataHolder.searchByExampleString("String to search...", mysqlManager);
    
    // To get an entry by its id use:
    dataHolder = ExampleDataHolder.getById(1L, mysqlManager);
    
    // To get all entrys in an database use:
    searchResult = ExampleDataHolder.getAll(mysqlManager);
    
    // To close the connection call:
    mysqlManager.closeConnection();
    
  } catch (Exception e) {
    // Your error handling should be here...
  }
}
```

## Documentation
A more detailed version of the documentation can be found [here](https://docs.scolasti.co/s.MysqlManager/).

## License
This project is licensed under the **Mozilla Public License Version 2.0**. Read more [here](https://www.mozilla.org/en-US/MPL/2.0/).

## Contribution
I look forward to any help with the project. So feel free and contribute to the project and start a sub branch. If you think your code should be in the next version start a pull request. Github Actions should automatically test whether your version works. Please describe exactly what you have edited in your pull request.

## Liability
I or any other contributor is not responsible for any errors or mistakes that may appear! Use at your own risk.

## Other Projects
Check out my other projects: [On Github](https://github.com/scolastico/) or [on my Website](https://scolasti.co/).
