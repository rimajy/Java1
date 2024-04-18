package Dialogs;

import javax.swing.*;
import java.awt.*;

public class StatisticDialog extends javax.swing.JDialog{
    public StatisticDialog(java.awt.Frame parent, String byArticle, String byGroup, String byEverything) {
            super(parent, true);
            setTitle("Статистика по скаду");
            setSize(500, 200);
            JPanel content = new JPanel(new GridLayout(4,2, 10,10));
            content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            content.add(new JLabel("Загальна вартість даного товару:"));
            content.add(new JLabel(byArticle));
            content.add(new JLabel("Загальна вартість по даній групі:"));
            content.add(new JLabel(byGroup));
            content.add(new JLabel("Загальна вартіст по складу:"));
            content.add(new JLabel(byEverything));

            var okBtn = new JButton("Oк");

            okBtn.addActionListener(e->{
                setVisible(false);
            });

            content.add(okBtn);

            getContentPane().add(content, BorderLayout.CENTER);
            setLocationRelativeTo(null);
        }

        public static void ShowDialog(java.awt.Frame parent, String byArticle, String byGroup, String byEverything)
        {
            var dialog = new Dialogs.StatisticDialog(parent, byArticle, byGroup, byEverything);
            dialog.setVisible(true);
        }
    }

