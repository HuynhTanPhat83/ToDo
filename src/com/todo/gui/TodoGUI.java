// src/com/todo/gui/TodoGUI.java
package com.todo.gui;

import com.todo.Task;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class TodoGUI extends JFrame {
    private ArrayList<Task> tasks;
    private DefaultListModel<String> listModel;
    private JList<String> taskList;
    private JTextField inputField;
    private static final String FILE_NAME = "tasks.txt";

    public TodoGUI() {
        tasks = new ArrayList<>();
        setupUI();
        loadTasksFromFile();
    }

    private void setupUI() {
        setTitle("To-Do List - GUI Version");
        setSize(520, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("MY TO-DO LIST", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(0, 102, 204));
        add(header, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setFont(new Font("Consolas", Font.PLAIN, 16));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        JButton addBtn = new JButton("Add Task");
        addBtn.setBackground(new Color(0, 150, 0));
        addBtn.setForeground(Color.BLACK);
        addBtn.setFocusPainted(false);
        addBtn.setToolTipText("Thêm công việc mới");

        addBtn.addActionListener(e -> addTask());
        inputField.addActionListener(e -> addTask());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(addBtn, BorderLayout.EAST);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton toggleBtn = new JButton("Toggle");
        JButton deleteBtn = new JButton("Delete");
        JButton exitBtn = new JButton("Exit");

        toggleBtn.setBackground(new Color(255, 193, 7));
        deleteBtn.setBackground(new Color(220, 53, 69));
        exitBtn.setBackground(new Color(108, 117, 125));
        toggleBtn.setForeground(Color.BLACK);
        deleteBtn.setForeground(Color.BLACK);
        exitBtn.setForeground(Color.BLACK);

        toggleBtn.setToolTipText("Đánh dấu hoàn thành / bỏ hoàn thành");
        deleteBtn.setToolTipText("Xóa công việc đã chọn");
        exitBtn.setToolTipText("Thoát ứng dụng");

        toggleBtn.addActionListener(e -> toggleTask());
        deleteBtn.addActionListener(e -> deleteTask());
        exitBtn.addActionListener(e -> System.exit(0));

        controlPanel.add(toggleBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(exitBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        south.add(inputPanel, BorderLayout.NORTH);
        south.add(controlPanel, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);

        refreshList();
    }

    private void addTask() {
        String title = inputField.getText().trim();
        if (!title.isEmpty()) {
            tasks.add(new Task(title));
            inputField.setText("");
            refreshList();
            saveTasksToFile();
        }
    }

    private void toggleTask() {
        int idx = taskList.getSelectedIndex();
        if (idx != -1) {
            tasks.get(idx).toggle();
            refreshList();
            taskList.setSelectedIndex(idx);
            saveTasksToFile();
        }
    }

    private void deleteTask() {
        int idx = taskList.getSelectedIndex();
        if (idx != -1) {
            tasks.remove(idx);
            refreshList();
            saveTasksToFile();
        }
    }

    private void refreshList() {
        listModel.clear();
        for (Task t : tasks) {
            listModel.addElement(t.toString());
        }
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.println(task.getTitle() + "|" + task.isCompleted());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi lưu file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            loadSampleData();
            saveTasksToFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    Task task = new Task(parts[0]);
                    if (Boolean.parseBoolean(parts[1])) task.toggle();
                    tasks.add(task);
                }
            }
            refreshList();
        } catch (IOException e) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
       
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new TodoGUI().setVisible(true);
        });
    }
}