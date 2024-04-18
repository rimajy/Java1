package Dialogs;

import javax.swing.*;
import java.awt.*;

public class ArticleAmountDialog extends javax.swing.JDialog{

    JTextField amountText = new JTextField();
    static Integer result = null;

    public ArticleAmountDialog(java.awt.Frame parent, String title, String group, String name) {
        super(parent, true);
        setTitle(title);
        setSize(400, 200);
        JPanel content = new JPanel(new GridLayout(4,2, 10,10));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        content.add(new JLabel("Назва товару:"));
        content.add(new JLabel(name));
        content.add(new JLabel("Група товару:"));
        content.add(new JLabel(group));
        content.add(new JLabel("Кількість товару:"));
        content.add(amountText);

        var okBtn = new JButton("Oк");
        var cancelBtn = new JButton("Відміна");

        okBtn.addActionListener(e->{
            String amount = amountText.getText();
            try{
                result = Integer.parseInt(amount);
                setVisible(false);
            }
            catch(NumberFormatException _){
                result = null;
                JOptionPane.showMessageDialog(
                        getParent(),
                        "Числове поле містить недопустиме значення!!!",
                        "Виникла помилка!",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        cancelBtn.addActionListener(e->{
            result = null;
            setVisible(false);
        });

        content.add(okBtn);
        content.add(cancelBtn);

        getContentPane().add(content, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    public static Integer ShowDialog(java.awt.Frame parent, String title, String group, String name)
    {
        var dialog = new ArticleAmountDialog(parent, title, group, name);
        dialog.setVisible(true);
        return result;
    }
}
