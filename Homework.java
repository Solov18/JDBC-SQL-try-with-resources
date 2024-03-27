package com.javacurse.ce.JavaJunior.Seminar.lesson3;

import java.lang.annotation.Retention;
import java.sql.*;

public class Homework {

  /**
   * 0. Разобрать код с семниара
   * 1. Повторить код с семниара без подглядываний на таблице Student с полями:
   * 1.1 id - int
   * 1.2 firstName - string
   * 1.3 secondName - string
   * 1.4 age - int
   * 2.* Попробовать подключиться к другой БД
   * 3.** Придумать, как подружить запросы и reflection:
   * 3.1 Создать аннотации Table, Id, Column
   * 3.2 Создать класс, у которого есть методы:
   * 3.2.1 save(Object obj) сохраняет объект в БД
   * 3.2.2 update(Object obj) обновляет объект в БД
   * 3.2.3 Попробовать объединить save и update (сначала select, потом update или insert)
   */


  public static void main(String[] args) {
      try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test")) {
          acceptConnection(connection);
      } catch (SQLException e) {
          throw new RuntimeException(e);
      }
  }
  public static void createTable(Connection connection) throws SQLException {
      try (Statement statement = connection.createStatement()) {
          statement.execute("""
                          CREATE TABLE Student (
                                           id INT PRIMARY KEY,
                                           firstName VARCHAR(255),
                                           secondName VARCHAR(255),
                                           age INT
                                       );
                  """);
      }

  }
  public static void acceptConnection(Connection connection) throws SQLException {
      createTable(connection);
      insertData(connection);
      updateRow(connection, "Igor", "Евгений");



      try (Statement statement = connection.createStatement()) {
          ResultSet resultSet = statement.executeQuery(""" 
                  SELECT id, firstName,secondName, age from  Student
                  """);
          while (resultSet.next()){
              int id = resultSet.getInt("id");
              String firstName = resultSet.getString("firstName");
              String secondName = resultSet.getString("secondName");
              int age = resultSet.getInt("age");

              System.out.println(" id = " + id + ",  firstName = " + firstName + ",  secondName = " +
                      secondName + ", age = " + age);
          }

      }


  }
  public static void insertData( Connection connection) throws SQLException {
      try (Statement statement = connection.createStatement()) {
          int update = statement.executeUpdate("""
        insert into Student(id, firstName, age) values
        (1, 'Igor' , 43), 
        (2, 'Person #2', 47), 
        (3, 'John', 19), 
        (4, 'Alex', 55), 
        (5, 'Peter', 44) 
        """);

          System.out.println("INSERT: affected rows: " + update);
      }

  }
  private static void updateRow(Connection connection, String firstName, String secondName) throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement("update Student set secondName = $1 where firstName = $2");
      preparedStatement.setString(1,secondName);
      preparedStatement.setString(2,firstName);


      preparedStatement.executeUpdate();


  }
}
