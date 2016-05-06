import java.util.List;
import org.sql2o.*;
import java.util.Arrays;

public class Stylist {
  private int id;
  private String sname;

  public Stylist(String sname) {
    this.sname = sname;
  }

  public String getSname() {
    return sname;
  }

  public int getId() {
    return id;
  }

  public static List<Stylist> all() {
    String sql = "SELECT id, sname FROM Stylists";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  public List<Client> getClients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients where stylist_id=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Client.class);
      }
    }

  @Override
  public boolean equals(Object otherStylist) {
    if (!(otherStylist instanceof Stylist)) {
      return false;
    } else {
      Stylist newStylist = (Stylist) otherStylist;
      return this.getSname().equals(newStylist.getSname()) &&
             this.getId() == newStylist.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Stylists(sname) VALUES (:sname)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("sname", this.sname)
        .executeUpdate()
        .getKey();
    }
  }

  public static Stylist find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Stylists where id=:id";
      Stylist stylist = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Stylist.class);
      return stylist;
    }
  }

  public void update(String sname) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET sname = :sname WHERE id = :id";
      con.createQuery(sql)
        .addParameter("sname", sname)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM stylists WHERE id = :id;";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }
}
