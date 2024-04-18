package Dialogs;

import Models.ArticleGroupModel;
import Models.ArticleModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class NewArticleDialog extends javax.swing.JDialog {

    JTextField nameText;
    JTextField descriptionText;
    JTextField ProducerText;
    JTextField PriceText;
    JComboBox<String> groupCombo;
    static ArticleModel result = null;

    public NewArticleDialog(java.awt.Frame parent, ArrayList<ArticleGroupModel> articleGroups) {
        super(parent, true);
        setTitle("Додати новий товар");
        setSize(350, 250);
        JPanel content = new JPanel(new GridLayout(6,2, 10,10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] values = new String[articleGroups.size()];
        for (int i = 0; i < articleGroups.size(); i++) { values[i] = articleGroups.get(i).Name; }
        groupCombo = new JComboBox<>(values);

        content.add(new JLabel("Група"));
        content.add(groupCombo);

        nameText = new JTextField();
        content.add(new JLabel("Назва"));
        content.add(nameText);

        descriptionText = new JTextField();
        content.add(new JLabel("Опис"));
        content.add(descriptionText);

        ProducerText = new JTextField();
        content.add(new JLabel("Виробник"));
        content.add(ProducerText);

        PriceText = new JTextField();
        content.add(new JLabel("Ціна"));
        content.add(PriceText);


        var addButton = new JButton("Додати");
        var cancelButton = new JButton("Відмінити");

        addButton.addActionListener(e->{
            int selectedIndex = groupCombo.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(
                        getParent(),
                        "Виберіть категорію товару",
                        "Виникла помилка!",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            UUID GroupID = articleGroups.get(selectedIndex).ID;
            String name = nameText.getText();
            String description = descriptionText.getText();
            String producer = ProducerText.getText();
            String price = PriceText.getText();

            if (name.isEmpty() || description.isEmpty() || producer.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(
                        getParent(),
                        "Всі поля мають бути заповнені!",
                        "Виникла помилка!",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            long priceLong;
            try {
                priceLong = Long.parseLong(price);
            }
            catch (NumberFormatException _)
            {
                JOptionPane.showMessageDialog(
                        getParent(),
                        "Числове поле містить неокректні дані!",
                        "Виникла помилка!",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            result = new ArticleModel(
                  null,
                    GroupID,
                    name,
                    description,
                    producer,
                    0,
                    priceLong
            );

            setVisible(false);
        });

        cancelButton.addActionListener(e->{
            result = null;
            setVisible(false);
        });

        content.add(addButton);
        content.add(cancelButton);

        getContentPane().add(content, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    public static ArticleModel ShowDialog(java.awt.Frame parent, ArrayList<ArticleGroupModel> articleGroups) {
        NewArticleDialog dialog = new NewArticleDialog(parent, articleGroups);
        dialog.setVisible(true);
        return result;
    }
}
