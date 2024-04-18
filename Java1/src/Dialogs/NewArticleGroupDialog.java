package Dialogs;

import Models.ArticleGroupModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewArticleGroupDialog extends javax.swing.JDialog {

    private JTextField nameText;
    private JTextField descriptionText;
    private ArticleGroupModel result = null;


    public NewArticleGroupDialog(java.awt.Frame parent, String name, String description) {
        super(parent, true);
        setTitle("Група товарів");
        setSize(350,150);
        JPanel content = new JPanel(new GridLayout(3, 2, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        var nameLbl = new JLabel("Назва:");
        nameText = new JTextField();
        nameText.setText(name);
        var descriptionLbl = new JLabel("Опис:");
        descriptionText = new JTextField();
        descriptionText.setText(description);

        content.add(nameLbl);
        content.add(nameText);
        content.add(descriptionLbl);
        content.add(descriptionText);

        JButton addBtn = new JButton("Зберегти");
        addBtn.addActionListener(e -> {
            String name1 = nameText.getText();
            String description1 = descriptionText.getText();

            if (name1.isEmpty() || description1.isEmpty()) {
                JOptionPane.showMessageDialog(
                        getParent(),
                        "Поля не можуть бути пустими",
                        "Виникла помилка!",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            result = new ArticleGroupModel(null, name1, description1);
            setVisible(false);
        });

        JButton cancelBtn = new JButton("Відмінити");
        cancelBtn.addActionListener(e -> {
            result = null;
            setVisible(false);
        });

        content.add(addBtn);
        content.add(cancelBtn);
        getContentPane().add(content, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    public static ArticleGroupModel ShowDialog(java.awt.Frame parent, String name, String description)
    {
        var dialog = new NewArticleGroupDialog(parent, name, description);
        dialog.setVisible(true);
        return dialog.result;
    }




}
