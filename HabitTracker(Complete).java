(Complete);

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Habit Data Class 
class Habit implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int goal;
    private List<String> completionDates;

    public Habit(String name, int goal) {
        this.name = name;
        this.goal = goal;
        this.completionDates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public List<String> getCompletionDates() {
        return completionDates;
    }

    public void addCompletion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm:ss)");
        completionDates.add(LocalDateTime.now().format(formatter));
    }

    public int getStreakCount() {
        return completionDates.size();
    }

    public boolean isGoalReached() {
        return getStreakCount() >= goal;
    }
}

// History Entry this is for the history log, it will store the habit name,
// current streak, goal, last log time, and whether the habit was deleted. This
// will be displayed in the history frame.
class HistoryEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String habitName;
    private int currentStreak;
    private int goal;
    private String lastLogTime;
    private boolean isDeleted;

    public HistoryEntry(String name, int streak, int goal, String time, boolean deleted) {
        this.habitName = name;
        this.currentStreak = streak;
        this.goal = goal;
        this.lastLogTime = time;
        this.isDeleted = deleted;
    }

    public String getHabitName() {
        return habitName;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getGoal() {
        return goal;
    }

    public String getLastLogTime() {
        return lastLogTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}

// Achievement Entry this is for the achievement log, it will store the habit
// name, goal, and completion date time when a habit is completed. This will be
// displayed in the achievement frame.
class AchievementEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String habitName;
    private int goal;
    private String completedDateTime;

    public AchievementEntry(String habitName, int goal, String completedDateTime) {
        this.habitName = habitName;
        this.goal = goal;
        this.completedDateTime = completedDateTime;
    }

    public String getHabitName() {
        return habitName;
    }

    public int getGoal() {
        return goal;
    }

    public String getCompletedDateTime() {
        return completedDateTime;
    }
}

// Simplified Achievements Frame here we will display the achievements in a
// simple list format, showing the habit name, goal, and completion date time.
// This will be a separate frame that can be opened from the main application.
class AchievementFrame extends JFrame {
    public AchievementFrame(List<AchievementEntry> achievements) {
        setTitle("My Achievements");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.setBackground(Color.WHITE);

        JLabel title = new JLabel("ACHIEVEMENT LOG");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 15)));

        if (achievements == null || achievements.isEmpty()) {
            content.add(new JLabel("No achievements yet. Keep working!"));
        } else {
            for (int i = 0; i < achievements.size(); i++) {
                AchievementEntry a = achievements.get(i);
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
                row.setMaximumSize(new Dimension(380, 55));

                JLabel numLabel = new JLabel("#" + (i + 1) + " ");
                numLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

                JLabel infoLabel = new JLabel("<html><b>" + a.getHabitName() + "</b><br>" +
                        "Goal: " + a.getGoal() + " Days | Done: " + a.getCompletedDateTime() + "</html>");
                infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

                row.add(numLabel, BorderLayout.WEST);
                row.add(infoLabel, BorderLayout.CENTER);

                content.add(row);
                content.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        add(new JScrollPane(content));
    }
}

// History Frame this is where we will display the history of all habits,
// including their current streak, goal, last log time, and whether they were
// deleted. This will be a separate frame that can be opened from the main
// application. Each habit will be displayed in a card format with color coding
// for completed (green) and in progress (red if deleted, default if not).
class CompletedHabitsFrame extends JFrame {
    public CompletedHabitsFrame(List<HistoryEntry> history) {
        setTitle("History & Progress");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (history == null || history.isEmpty()) {
            mainPanel.add(new JLabel("No history found."));
        } else {
            for (HistoryEntry entry : history) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                card.setMaximumSize(new Dimension(430, 70));

                String statusText;
                if (entry.getCurrentStreak() >= entry.getGoal()) {
                    statusText = "<font color='green'>Completed!</font>";
                } else if (entry.isDeleted()) {
                    statusText = "<font color='red'>In Progress (DELETED)</font>";
                } else {
                    statusText = "In Progress";
                }

                JLabel info = new JLabel("<html><b>" + entry.getHabitName() + "</b> (" +
                        entry.getCurrentStreak() + "/" + entry.getGoal() + ")<br>" +
                        "Last Log: " + entry.getLastLogTime() + "<br>Status: " + statusText + "</html>");

                card.add(info, BorderLayout.CENTER);
                mainPanel.add(card);
                mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        add(new JScrollPane(mainPanel));
    }
}

// Main Application here we will manage the main interface for the habit
// tracker, allowing users to add, update, delete habits, and view their history
// and achievements. The main frame will display active habits with progress
// bars and log buttons, and provide controls for managing habits and accessing
// other frames.
public class HabitTracker extends JFrame {
    private List<Habit> habits;
    private List<AchievementEntry> achievements;
    private List<HistoryEntry> historyList;

    private JPanel habitListContainer;
    private JComboBox<String> habitSelector;
    private final String FILE_NAME = "habits.dat";
    private final String ACHIEVEMENTS_FILE = "achievements.dat";
    private final String HISTORY_FILE = "history.dat";

    public HabitTracker() {
        loadAllData();
        setupUI();
        refreshUI();
    }

    private void setupUI() {
        setTitle("Student Habit Tracker");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        habitListContainer = new JPanel();
        habitListContainer.setLayout(new BoxLayout(habitListContainer, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(habitListContainer);
        scroll.setBorder(BorderFactory.createTitledBorder("Active Habits"));

        JPanel controls = new JPanel(new FlowLayout());
        habitSelector = new JComboBox<>();
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton delBtn = new JButton("Delete");
        JButton histBtn = new JButton("History");
        JButton achBtn = new JButton("Achievements");
        JButton resetBtn = new JButton("Reset All");
        resetBtn.setForeground(Color.RED);

        addBtn.addActionListener(e -> addNewHabit());
        updateBtn.addActionListener(e -> updateHabit());
        delBtn.addActionListener(e -> deleteHabit());
        histBtn.addActionListener(e -> new CompletedHabitsFrame(historyList).setVisible(true));
        achBtn.addActionListener(e -> new AchievementFrame(achievements).setVisible(true));
        resetBtn.addActionListener(e -> resetAll());

        controls.add(new JLabel("Select:"));
        controls.add(habitSelector);
        controls.add(addBtn);
        controls.add(updateBtn);
        controls.add(delBtn);
        controls.add(histBtn);
        controls.add(achBtn);
        controls.add(resetBtn);

        mainPanel.add(controls, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel);
    }

    public void refreshUI() {
        habitListContainer.removeAll();
        habitSelector.removeAllItems();

        for (Habit h : habits) {
            habitSelector.addItem(h.getName());
            if (!h.isGoalReached()) {
                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setMaximumSize(new Dimension(700, 50));

                JButton logBtn = new JButton("Log: " + h.getName());
                logBtn.addActionListener(e -> {
                    h.addCompletion();
                    updateHistory(h, false);
                    if (h.isGoalReached()) {
                        achievements.add(new AchievementEntry(h.getName(), h.getGoal(),
                                h.getCompletionDates().get(h.getCompletionDates().size() - 1)));
                        JOptionPane.showMessageDialog(this, "Great job! Habit Completed!");
                    }
                    refreshUI();
                });

                JProgressBar bar = new JProgressBar(0, h.getGoal());
                bar.setValue(h.getStreakCount());
                bar.setStringPainted(true);
                bar.setString(h.getStreakCount() + "/" + h.getGoal());

                row.add(logBtn, BorderLayout.WEST);
                row.add(bar, BorderLayout.CENTER);
                habitListContainer.add(row);
                habitListContainer.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        saveAllData();
        revalidate();
        repaint();
    }

    private void updateHistory(Habit h, boolean deleted) {
        String time = h.getCompletionDates().isEmpty() ? "N/A"
                : h.getCompletionDates().get(h.getCompletionDates().size() - 1);
        historyList.removeIf(entry -> entry.getHabitName().equals(h.getName()));
        historyList.add(new HistoryEntry(h.getName(), h.getStreakCount(), h.getGoal(), time, deleted));
    }

    private void addNewHabit() {
        String name = JOptionPane.showInputDialog(this, "Habit Name:");
        if (name == null || name.trim().isEmpty())
            return;

        // DUPLICATE CHECK will check if the habit name already exists, if it does it
        // will show an error message and return without adding the habit.
        for (Habit h : habits) {
            if (h.getName().equalsIgnoreCase(name.trim())) {
                JOptionPane.showMessageDialog(this,
                        "Error: A habit with the name '" + name.trim() + "' already exists!", "Duplicate Habit",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String goalStr = JOptionPane.showInputDialog(this, "Goal Days:", "7");
        if (goalStr != null) {
            try {
                habits.add(new Habit(name.trim(), Integer.parseInt(goalStr)));
                refreshUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid Goal Number");
            }
        }
    }

    private void updateHabit() {
        String selected = (String) habitSelector.getSelectedItem();
        if (selected == null)
            return;

        Habit toUpdate = null;
        for (Habit h : habits) {
            if (h.getName().equals(selected)) {
                toUpdate = h;
                break;
            }
        }

        if (toUpdate != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update '" + selected + "'?",
                    "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String newName = JOptionPane.showInputDialog(this, "New Name:", toUpdate.getName());
                if (newName == null || newName.trim().isEmpty())
                    return;

                // Check duplicate for new name if name is changed
                if (!newName.equalsIgnoreCase(toUpdate.getName())) {
                    for (Habit h : habits) {
                        if (h.getName().equalsIgnoreCase(newName.trim())) {
                            JOptionPane.showMessageDialog(this, "Error: That name is already taken!");
                            return;
                        }
                    }
                }

                String newGoal = JOptionPane.showInputDialog(this, "New Goal:", toUpdate.getGoal());
                try {
                    toUpdate.setName(newName.trim());
                    toUpdate.setGoal(Integer.parseInt(newGoal));
                    updateHistory(toUpdate, false);
                    refreshUI();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid Data.");
                }
            }
        }
    }

    private void deleteHabit() {
        String selected = (String) habitSelector.getSelectedItem();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + selected + "'?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                habits.removeIf(h -> {
                    if (h.getName().equals(selected)) {
                        updateHistory(h, true);
                        return true;
                    }
                    return false;
                });
                refreshUI();
            }
        }
    }

    private void resetAll() {
        if (JOptionPane.showConfirmDialog(this,
                "Delete EVERYTHING (History and Achievements)?") == JOptionPane.YES_OPTION) {
            habits.clear();
            achievements.clear();
            historyList.clear();
            refreshUI();
        }
    }

    private void saveAllData() {
        try {
            save(FILE_NAME, habits);
            save(ACHIEVEMENTS_FILE, achievements);
            save(HISTORY_FILE, historyList);
        } catch (Exception e) {
        }
    }

    private void save(String f, Object o) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }

    private void loadAllData() {
        habits = (List<Habit>) load(FILE_NAME);
        achievements = (List<AchievementEntry>) load(ACHIEVEMENTS_FILE);
        historyList = (List<HistoryEntry>) load(HISTORY_FILE);
    }

    private Object load(String f) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HabitTracker().setVisible(true));
    }
}