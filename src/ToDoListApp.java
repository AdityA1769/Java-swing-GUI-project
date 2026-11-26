
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ToDoListApp extends JFrame {

    private JTextField taskInput;
    private DefaultListModel<Task> taskModel;
    private JList<Task> taskList;

    private JLabel totalLabel;
    private JLabel pendingLabel;
    private JLabel completedLabel;

    private JComboBox<String> filterBox;

    private ArrayList<Task> tasks = new ArrayList<>();

    // Task class
    static class Task {
        String description;
        boolean completed;

        Task(String description) {
            this.description = description;
            this.completed = false;
        }

        @Override
        public String toString() {
            return (completed ? "✓  " : "○  ") + description;
        }
    }

    // Constructor
    public ToDoListApp() {

        setTitle("To-Do List Application");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Heading
        JLabel heading = new JLabel("My To-Do List");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(new Color(25, 45, 75));

        JLabel subtitle = new JLabel("Organize your tasks and stay productive");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.setOpaque(false);
        header.add(heading);
        header.add(subtitle);

        // Task input section
        taskInput = new JTextField();
        taskInput.setFont(new Font("Arial", Font.PLAIN, 15));
        taskInput.setPreferredSize(new Dimension(350, 40));

        JButton addButton = createButton("Add Task",
                new Color(35, 105, 190));

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // Filter section
        filterBox = new JComboBox<>(
                new String[]{"All Tasks", "Pending", "Completed"});

        filterBox.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(filterBox);

        // Task list
        taskModel = new DefaultListModel<>();
        taskList = new JList<>(taskModel);

        taskList.setFont(new Font("Arial", Font.PLAIN, 16));
        taskList.setFixedCellHeight(40);
        taskList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean selected, boolean focus) {

                JLabel label = (JLabel) super
                        .getListCellRendererComponent(
                                list, value, index,
                                selected, focus);

                Task task = (Task) value;

                label.setBorder(new EmptyBorder(5, 12, 5, 12));

                if (task.completed) {
                    label.setForeground(new Color(110, 130, 110));
                } else {
                    label.setForeground(new Color(35, 45, 60));
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(220, 225, 230)));

        // Action buttons
        JButton completeButton = createButton(
                "Mark Completed", new Color(30, 145, 100));

        JButton deleteButton = createButton(
                "Delete Task", new Color(205, 65, 65));

        JButton clearButton = createButton(
                "Clear Completed", new Color(100, 110, 125));

        JPanel buttonPanel = new JPanel(
                new GridLayout(1, 3, 10, 0));

        buttonPanel.setOpaque(false);
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Statistics
        totalLabel = new JLabel("Total: 0");
        pendingLabel = new JLabel("Pending: 0");
        completedLabel = new JLabel("Completed: 0");

        JPanel statsPanel = new JPanel(
                new GridLayout(1, 3, 10, 0));

        statsPanel.setOpaque(false);
        statsPanel.add(totalLabel);
        statsPanel.add(pendingLabel);
        statsPanel.add(completedLabel);

        // Layout arrangement
        JPanel topPanel = new JPanel(new BorderLayout(10, 15));
        topPanel.setOpaque(false);
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(statsPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Event handling

        addButton.addActionListener(e -> addTask());

        taskInput.addActionListener(e -> addTask());

        completeButton.addActionListener(
                e -> markCompleted());

        deleteButton.addActionListener(
                e -> deleteTask());

        clearButton.addActionListener(
                e -> clearCompleted());

        filterBox.addActionListener(
                e -> updateTaskList());

        updateTaskList();
    }

    // Create styled buttons
    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 10, 12, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // Add task
    private void addTask() {

        String description = taskInput.getText().trim();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a task!",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tasks.add(new Task(description));

        taskInput.setText("");

        filterBox.setSelectedItem("All Tasks");

        updateTaskList();
    }

    // Mark task as completed
    private void markCompleted() {

        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a task!");
            return;
        }

        Task selectedTask = taskModel.getElementAt(index);

        selectedTask.completed = !selectedTask.completed;

        updateTaskList();
    }

    // Delete selected task
    private void deleteTask() {

        int index = taskList.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a task!");
            return;
        }

        Task selectedTask = taskModel.getElementAt(index);

        tasks.remove(selectedTask);

        updateTaskList();
    }

    // Clear completed tasks
    private void clearCompleted() {

        tasks.removeIf(task -> task.completed);

        updateTaskList();
    }

    // Update displayed task list
    private void updateTaskList() {

        taskModel.clear();

        String filter = (String) filterBox.getSelectedItem();

        for (Task task : tasks) {

            if ("Pending".equals(filter) && task.completed) {
                continue;
            }

            if ("Completed".equals(filter) && !task.completed) {
                continue;
            }

            taskModel.addElement(task);
        }

        updateStatistics();
    }

    // Update task statistics
    private void updateStatistics() {

        int total = tasks.size();
        int completed = 0;

        for (Task task : tasks) {
            if (task.completed) {
                completed++;
            }
        }

        int pending = total - completed;

        totalLabel.setText("Total: " + total);
        pendingLabel.setText("Pending: " + pending);
        completedLabel.setText("Completed: " + completed);
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ToDoListApp app = new ToDoListApp();
            app.setVisible(true);
        });
    }
}
