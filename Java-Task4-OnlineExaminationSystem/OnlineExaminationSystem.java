import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Swing-based online examination system: login, profile update, timed MCQ exam
 * with Next/Previous navigation, auto-submit on timeout, and a result screen.
 */
public class OnlineExaminationSystem {
    static String user = "student", pass = "pass", display = "Student";
    static final int EXAM_SECONDS = 30 * 60;

    static final List<String[]> Q = Arrays.asList(
        new String[]{"Which keyword defines a class in Java?", "class", "struct", "def", "func", "0"},
        new String[]{"Default value of an int field?", "null", "0", "0.0", "undefined", "1"},
        new String[]{"Which collection disallows duplicates?", "List", "Map", "Set", "Array", "2"},
        new String[]{"JVM stands for?", "Java Virtual Machine", "Java Verified Module", "Joint Virtual Memory", "Java Value Model", "0"},
        new String[]{"Which is NOT a primitive?", "int", "boolean", "String", "char", "2"}
    );
    static int[] answers = new int[Q.size()];
    static int current = 0;
    static javax.swing.Timer timer; static int remaining = EXAM_SECONDS;

    public static void main(String[] args) { Arrays.fill(answers, -1); SwingUtilities.invokeLater(OnlineExaminationSystem::login); }

    static void login() {
        JFrame f = new JFrame("Exam Login");
        f.setSize(300, 180); f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(3, 2, 5, 5));
        JTextField u = new JTextField(); JPasswordField p = new JPasswordField();
        JButton b = new JButton("Login");
        f.add(new JLabel("User:")); f.add(u); f.add(new JLabel("Pass:")); f.add(p); f.add(new JLabel()); f.add(b);
        b.addActionListener(e -> {
            if (u.getText().equals(user) && new String(p.getPassword()).equals(pass)) { f.dispose(); profile(); }
            else JOptionPane.showMessageDialog(f, "Invalid login (try student/pass)");
        });
        f.setVisible(true);
    }

    static void profile() {
        JFrame f = new JFrame("Profile Update");
        f.setSize(320, 200); f.setLayout(new GridLayout(4, 2, 5, 5));
        JTextField name = new JTextField(display); JPasswordField np = new JPasswordField();
        JButton save = new JButton("Save & Start Exam");
        f.add(new JLabel("Display name:")); f.add(name);
        f.add(new JLabel("New password:")); f.add(np);
        f.add(new JLabel()); f.add(save);
        save.addActionListener(e -> {
            display = name.getText();
            if (np.getPassword().length > 0) pass = new String(np.getPassword());
            f.dispose(); exam();
        });
        f.setVisible(true);
    }

    static JFrame examFrame; static JLabel qLabel, timerLabel; static ButtonGroup group; static JRadioButton[] opts;

    static void exam() {
        examFrame = new JFrame("Exam - " + display);
        examFrame.setSize(500, 320);
        examFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        examFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (JOptionPane.showConfirmDialog(examFrame, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        examFrame.setLayout(new BorderLayout());
        timerLabel = new JLabel("", SwingConstants.RIGHT);
        qLabel = new JLabel();
        opts = new JRadioButton[4]; group = new ButtonGroup();
        JPanel center = new JPanel(new GridLayout(5, 1));
        center.add(qLabel);
        for (int i = 0; i < 4; i++) { opts[i] = new JRadioButton(); group.add(opts[i]); center.add(opts[i]); }
        JButton prev = new JButton("Previous"), next = new JButton("Next"), submit = new JButton("Submit");
        JPanel nav = new JPanel(); nav.add(prev); nav.add(next); nav.add(submit);
        examFrame.add(timerLabel, BorderLayout.NORTH);
        examFrame.add(center, BorderLayout.CENTER);
        examFrame.add(nav, BorderLayout.SOUTH);
        prev.addActionListener(e -> { save(); if (current > 0) current--; show(); });
        next.addActionListener(e -> { save(); if (current < Q.size() - 1) current++; show(); });
        submit.addActionListener(e -> { if (JOptionPane.showConfirmDialog(examFrame, "Submit exam?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { save(); finish(); } });
        timer = new javax.swing.Timer(1000, e -> {
            remaining--;
            timerLabel.setText(String.format("Time left: %02d:%02d", remaining / 60, remaining % 60));
            if (remaining <= 0) { save(); finish(); }
        });
        timer.start();
        show();
        examFrame.setVisible(true);
    }

    static void save() { for (int i = 0; i < 4; i++) if (opts[i].isSelected()) answers[current] = i; }

    static void show() {
        String[] q = Q.get(current);
        qLabel.setText(String.format("Q%d/%d: %s", current + 1, Q.size(), q[0]));
        group.clearSelection();
        for (int i = 0; i < 4; i++) { opts[i].setText(q[i + 1]); opts[i].setSelected(answers[current] == i); }
    }

    static void finish() {
        timer.stop(); examFrame.dispose();
        int correct = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Q.size(); i++) {
            int right = Integer.parseInt(Q.get(i)[5]);
            boolean ok = answers[i] == right;
            if (ok) correct++;
            sb.append(String.format("Q%d: %s%n", i + 1, ok ? "Correct" : "Incorrect"));
        }
        int used = EXAM_SECONDS - remaining;
        JFrame r = new JFrame("Result");
        r.setSize(360, 320); r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea ta = new JTextArea(String.format("Score: %d/%d%nTime taken: %02d:%02d%n%nBreakdown:%n%s",
                correct, Q.size(), used / 60, used % 60, sb));
        ta.setEditable(false);
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> { r.dispose(); Arrays.fill(answers, -1); current = 0; remaining = EXAM_SECONDS; login(); });
        r.add(new JScrollPane(ta), BorderLayout.CENTER); r.add(logout, BorderLayout.SOUTH);
        r.setVisible(true);
    }
}
