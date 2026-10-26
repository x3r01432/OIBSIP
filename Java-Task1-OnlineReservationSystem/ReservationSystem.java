import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

/**
 * GUI train reservation system (Swing + JDBC/SQLite).
 * Login -> Book (generates PNR) -> Cancel by PNR.
 * Requires the sqlite-jdbc driver on the classpath at runtime.
 */
public class ReservationSystem {
    static final String URL = "jdbc:sqlite:reservations.db";

    public static void main(String[] args) {
        initDb();
        SwingUtilities.invokeLater(ReservationSystem::loginFrame);
    }

    static Connection conn() throws SQLException { return DriverManager.getConnection(URL); }

    static void initDb() {
        try (Connection c = conn(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS users(username TEXT PRIMARY KEY, password TEXT)");
            s.execute("CREATE TABLE IF NOT EXISTS bookings(pnr TEXT PRIMARY KEY, passenger TEXT, trainNo TEXT, trainName TEXT, cls TEXT, doj TEXT, src TEXT, dest TEXT)");
            s.execute("INSERT OR IGNORE INTO users VALUES('admin','admin')");
        } catch (SQLException e) { System.out.println("DB init (driver needed at runtime): " + e.getMessage()); }
    }

    static void loginFrame() {
        JFrame f = new JFrame("Login");
        f.setSize(300, 180); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(3, 2, 5, 5));
        JTextField u = new JTextField(); JPasswordField p = new JPasswordField();
        JButton login = new JButton("Login");
        f.add(new JLabel("Username:")); f.add(u);
        f.add(new JLabel("Password:")); f.add(p);
        f.add(new JLabel()); f.add(login);
        login.addActionListener(e -> {
            try (Connection c = conn();
                 PreparedStatement ps = c.prepareStatement("SELECT 1 FROM users WHERE username=? AND password=?")) {
                ps.setString(1, u.getText()); ps.setString(2, new String(p.getPassword()));
                if (ps.executeQuery().next()) { f.dispose(); mainFrame(); }
                else JOptionPane.showMessageDialog(f, "Access denied: invalid credentials");
            } catch (SQLException ex) { JOptionPane.showMessageDialog(f, ex.getMessage()); }
        });
        f.setVisible(true);
    }

    static void mainFrame() {
        JFrame f = new JFrame("Reservation System");
        f.setSize(420, 300); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Book Ticket", bookPanel());
        tabs.add("Cancel Ticket", cancelPanel());
        f.add(tabs); f.setVisible(true);
    }

    static JPanel bookPanel() {
        JPanel p = new JPanel(new GridLayout(8, 2, 4, 4));
        JTextField name = new JTextField(), trainNo = new JTextField(), doj = new JTextField(), src = new JTextField(), dest = new JTextField();
        JComboBox<String> cls = new JComboBox<>(new String[]{"Sleeper", "AC 3-Tier", "AC 2-Tier", "First Class"});
        JButton book = new JButton("Book");
        p.add(new JLabel("Passenger:")); p.add(name);
        p.add(new JLabel("Train No:")); p.add(trainNo);
        p.add(new JLabel("Class:")); p.add(cls);
        p.add(new JLabel("Date (YYYY-MM-DD):")); p.add(doj);
        p.add(new JLabel("From:")); p.add(src);
        p.add(new JLabel("To:")); p.add(dest);
        p.add(new JLabel()); p.add(book);
        book.addActionListener(e -> {
            if (name.getText().isEmpty() || !trainNo.getText().matches("\\d+") || doj.getText().isEmpty()) {
                JOptionPane.showMessageDialog(p, "Fill all fields; train no must be numeric."); return;
            }
            String pnr = "PNR" + (100000 + new Random().nextInt(900000));
            String trainName = "Express " + trainNo.getText();
            try (Connection c = conn();
                 PreparedStatement ps = c.prepareStatement("INSERT INTO bookings VALUES(?,?,?,?,?,?,?,?)")) {
                ps.setString(1, pnr); ps.setString(2, name.getText()); ps.setString(3, trainNo.getText());
                ps.setString(4, trainName); ps.setString(5, (String) cls.getSelectedItem());
                ps.setString(6, doj.getText()); ps.setString(7, src.getText()); ps.setString(8, dest.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(p, "Booked!\nPNR: " + pnr + "\nTrain: " + trainName);
            } catch (SQLException ex) { JOptionPane.showMessageDialog(p, ex.getMessage()); }
        });
        return p;
    }

    static JPanel cancelPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel top = new JPanel();
        JTextField pnr = new JTextField(10); JButton fetch = new JButton("Fetch"), cancel = new JButton("Cancel Booking");
        top.add(new JLabel("PNR:")); top.add(pnr); top.add(fetch); top.add(cancel);
        JTextArea out = new JTextArea(); out.setEditable(false);
        p.add(top, BorderLayout.NORTH); p.add(new JScrollPane(out), BorderLayout.CENTER);
        fetch.addActionListener(e -> {
            try (Connection c = conn(); PreparedStatement ps = c.prepareStatement("SELECT * FROM bookings WHERE pnr=?")) {
                ps.setString(1, pnr.getText()); ResultSet rs = ps.executeQuery();
                if (rs.next()) out.setText(String.format("Passenger: %s%nTrain: %s (%s)%nClass: %s%nDate: %s%n%s -> %s",
                        rs.getString("passenger"), rs.getString("trainName"), rs.getString("trainNo"),
                        rs.getString("cls"), rs.getString("doj"), rs.getString("src"), rs.getString("dest")));
                else out.setText("No booking found for that PNR.");
            } catch (SQLException ex) { out.setText(ex.getMessage()); }
        });
        cancel.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(p, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            try (Connection c = conn(); PreparedStatement ps = c.prepareStatement("DELETE FROM bookings WHERE pnr=?")) {
                ps.setString(1, pnr.getText());
                out.setText(ps.executeUpdate() > 0 ? "Booking cancelled." : "No such PNR.");
            } catch (SQLException ex) { out.setText(ex.getMessage()); }
        });
        return p;
    }
}
