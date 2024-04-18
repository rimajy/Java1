package Dialogs;

import Models.ArticleGroupModel;
import Models.ArticleModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

public class ArticleEditDialog extends javax.swing.JDialog {

    JTextField nameText;
    JTextField descriptionText;
    JTextField ProducerText;
    JTextField PriceText;
    static ArticleModel result = null;

    public ArticleEditDialog(java.awt.Frame parent, String _name, String _description, String _producer, String _price) {
        super(parent, true);
        setTitle("Редагувати товар");
        setSize(350, 250);

        JPanel content = new JPanel(new GridLayout(5,2, 10,10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameText = new JTextField();
        nameText.setText(_name);
        content.add(new JLabel("Назва"));
        content.add(nameText);

        descriptionText = new JTextField();
        descriptionText.setText(_description);
        content.add(new JLabel("Опис"));
        content.add(descriptionText);

        ProducerText = new JTextField();
        ProducerText.setText(_producer);
        content.add(new JLabel("Виробник"));
        content.add(ProducerText);

        PriceText = new JTextField();
        PriceText.setText(_price);
        content.add(new JLabel("Ціна"));
        content.add(PriceText);

        var okButton = new JButton("Зберегти");
        var cancelButton = new JButton("Відмінити");

        okButton.addActionListener(e->{
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
                    null,
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

        content.add(okButton);
        content.add(cancelButton);

        getContentPane().add(content, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    public static ArticleModel ShowDialog(java.awt.Frame parent, String name, String description, String producer, String price) {
        ArticleEditDialog dialog = new ArticleEditDialog(parent, name, description, producer, price);
        dialog.setVisible(true);
        return result;
    }
}
