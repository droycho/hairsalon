import java.util.List;
import org.sql2o.*;

public class Client {
  private int id;
  private String cname;
  private int stylist_id;

  public Client(String cname, int stylist_id) {
    this.cname = cname;
    this.stylist_id = stylist_id;
  }

  public String getCname() {
    return cname;
  }

  public int getId() {
    return id;
  }

  public int getStylistId() {
    return stylist_id;
  }

  public static List<Client> all() {
    String sql = "SELECT id, cname, stylist_id FROM clients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Client.class);
    }
  }

  @Override
  public boolean equals(Object otherClient){
    if (!(otherClient instanceof Client)) {
      return false;
    } else {
      Client newClient = (Client) otherClient;
      return this.getCname().equals(newClient.getCname()) &&
             this.getId() == newClient.getId() &&
             this.getStylistId() == newClient.getStylistId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO clients(cname, stylist_id) VALUES (:cname, :stylist_id)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("cname", this.cname)
        .addParameter("stylist_id", this.stylist_id)
        .executeUpdate()
        .getKey();
    }
  }

  public static Client find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients where id=:id";
      Client client = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Client.class);
      return client;
    }
  }
  
  public void update(String cname) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE clients SET cname = :cname WHERE id = :id";
      con.createQuery(sql)
        .addParameter("cname", cname)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM clients WHERE id = :id;";
    con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }


}
