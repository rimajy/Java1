import DataBase.FilePaths;
import DataBase.WareHouse;
import Dialogs.*;
import Models.ArticleGroupModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;
//Зробили Тарасенко Тимофій Семчук Владислав

public class Main {

    static FilePaths fp = new FilePaths(
            "articles.csv",
            "groups.csv"
    );

    static WareHouse wh = new WareHouse(fp);
    static JFrame frame;
    static JTabbedPane tabbedPane = new JTabbedPane();

    static DefaultTableModel articleGroupsTableModel = new DefaultTableModel();
    static JTable GroupsTable = new JTable(articleGroupsTableModel);

    static DefaultTableModel articlesTableModel = new DefaultTableModel();
    static JTable ArticlesTable = new JTable(articlesTableModel);

    static DefaultTableModel filteredArticlesTableModel = new DefaultTableModel();
    static JTable FilteredArticlesTable = new JTable(filteredArticlesTableModel);
    static JTextField searchField = new JTextField(45);


    static void AddTabs()
    {
        //------------------------------------------------------------------------------------
        // Перша вкладка про групи товарів
        //------------------------------------------------------------------------------------
        GroupsTable.setDefaultEditor(Object.class, null);
        GroupsTable.setCellSelectionEnabled(false);
        GroupsTable.setRowSelectionAllowed(true);
        GroupsTable.setColumnSelectionAllowed(false);
        JPanel tab1Content = new JPanel(new BorderLayout());

        JButton tab1AddButton = new JButton("Додати групу");
        tab1AddButton.addActionListener(e -> {
            var item = NewArticleGroupDialog.ShowDialog(frame, "","");
            if (item != null) {
                wh.AddArticleGroup(item);
                wh.Save();
                RefreshGroupsTable();
            }
        });

        JButton tab1DelButton = new JButton("Видалити групу");
        tab1DelButton.addActionListener(e -> {
            var row = GroupsTable.getSelectedRow();
            if (row != -1) {
                UUID groupID = (UUID) GroupsTable.getValueAt(row, 0);
                wh.DeleteArticleGroupById(groupID);
                wh.DeleteAllArticlesByGroup(groupID);
                wh.Save();
                RefreshGroupsTable();
            }
        });

        JButton tab1EditButton = new JButton("Редагувати групу");
        tab1EditButton.addActionListener(e -> {
            var row = GroupsTable.getSelectedRow();
            if (row != -1) {
                var name = GroupsTable.getValueAt(row, 1).toString();
                var desc = GroupsTable.getValueAt(row, 2).toString();
                var ID = (UUID)GroupsTable.getValueAt(row, 0);
                var changed = NewArticleGroupDialog.ShowDialog(frame, name, desc);
                changed.ID = ID;
                wh.ChangeArticleGroup(changed);
                wh.Save();
                RefreshGroupsTable();
            }
        });

        JPanel tab1Header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tab1Header.setBackground(new Color(0xc8ddf2));
        tab1Header.add(tab1AddButton);
        tab1Header.add(tab1DelButton);
        tab1Header.add(tab1EditButton);

        tab1Content.add(tab1Header, BorderLayout.NORTH);
        tab1Content.add(new JScrollPane(GroupsTable), BorderLayout.CENTER);
        tabbedPane.addTab("Групи товарів", tab1Content);

        //------------------------------------------------------------------------------------
        // Друга вкладка про товари
        //------------------------------------------------------------------------------------
        ArticlesTable.setDefaultEditor(Object.class, null);
        ArticlesTable.setCellSelectionEnabled(false);
        ArticlesTable.setRowSelectionAllowed(true);
        ArticlesTable.setColumnSelectionAllowed(false);
        ArticlesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    String groupName = target.getValueAt(row, 1).toString();
                    String articleName = target.getValueAt(row, 2).toString();
                    long[] statistic = wh.GetArticleStatistics(articleName, groupName);
                    System.out.println(String.format(" по товару %d по групі %d по всьому %d", statistic[0], statistic[1], statistic[2]));
                    StatisticDialog.ShowDialog(frame, String.valueOf(statistic[0]), String.valueOf(statistic[1]), String.valueOf(statistic[2]));
                }
            }
        });



        JPanel tab2Content = new JPanel(new BorderLayout());
        JPanel tab2Header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tab2Header.setBackground(new Color(0xc8ddf2));

        var tab2AddButton = new JButton("Додати новий товар");
        tab2AddButton.addActionListener(e -> {
            var article = NewArticleDialog.ShowDialog(frame, wh.GetAllGroups());
            if (article == null) return;
            wh.AddArticle(article);
            wh.Save();
            RefreshArticlesTable();
        });
        tab2Header.add(tab2AddButton);

        var tab2AppendButton = new JButton("Поповнити вибраний товар");
        tab2AppendButton.addActionListener(e -> {
            if (ArticlesTable.getSelectedRow() != -1)
            {
                UUID ID = UUID.fromString(ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 0).toString());
                String name = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 2).toString();
                String group = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 1).toString();
                Integer value = ArticleAmountDialog.ShowDialog(frame, "Видача товарів зі складу",group, name);
                if (value == null) return;
                wh.UpdateArticleAmount(ID, value);
                wh.Save();
                RefreshArticlesTable();
            }
        });
        tab2Header.add(tab2AppendButton);

        var tab2DeliveryButton = new JButton("Списати вибраний товар");
        tab2DeliveryButton.addActionListener(e -> {
            if (ArticlesTable.getSelectedRow() != -1)
            {
                UUID ID = UUID.fromString(ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 0).toString());
                String name = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 2).toString();
                String group = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 1).toString();
                int currAmount = Integer.parseInt(ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 5).toString());
                if (currAmount == 0) return;

                Integer value = ArticleAmountDialog.ShowDialog(frame, "Видача товарів зі складу",group, name);
                if (value == null) return;
                if (value <= currAmount)
                {
                    wh.UpdateArticleAmount(ID, -value);
                    wh.Save();
                    RefreshArticlesTable();
                }
                else
                {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Недостатньоо товару на складі! Спробуйте менше!",
                            "Виникла помилка!",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        tab2Header.add(tab2DeliveryButton);

        var tab2DeleteButton = new JButton("Видалити вибраний товар");
        tab2DeleteButton.addActionListener(e->{
            if (ArticlesTable.getSelectedRow() != -1){
                UUID ID = UUID.fromString(ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 0).toString());
                wh.DeleteArticleById(ID);
                wh.Save();
                RefreshArticlesTable();
            }
        });
        tab2Header.add(tab2DeleteButton);

        var tab2EditButton = new JButton("Редагувати вибраний товар");
        tab2EditButton.addActionListener(e->{
            if (ArticlesTable.getSelectedRow() != -1){
                UUID ID = UUID.fromString(ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 0).toString());
                String name = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 2).toString();
                String description = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 3).toString();
                String producer = ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 4).toString();
                int price = Integer.parseInt(ArticlesTable.getValueAt(ArticlesTable.getSelectedRow(), 6).toString());
                var value = ArticleEditDialog.ShowDialog(frame, name, description, producer, String.valueOf(price));
                if (value == null) return;
                value.ID = ID;
                wh.UpdateArticle(value);
                wh.Save();
                RefreshArticlesTable();
            }
        });
        tab2Header.add(tab2EditButton);


        tab2Content.add(tab2Header, BorderLayout.NORTH);
        tab2Content.add(new JScrollPane(ArticlesTable), BorderLayout.CENTER);
        tabbedPane.addTab("Товари на складі", tab2Content);



        //------------------------------------------------------------------------------------
        // Третя вкладка пошук
        //------------------------------------------------------------------------------------
        FilteredArticlesTable.setDefaultEditor(Object.class, null);
        FilteredArticlesTable.setCellSelectionEnabled(false);
        FilteredArticlesTable.setRowSelectionAllowed(true);
        FilteredArticlesTable.setColumnSelectionAllowed(false);

        JPanel tab3Content = new JPanel(new BorderLayout());
        JPanel tab3Header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tab3Header.setBackground(new Color(0xc8ddf2));
        tab3Header.add(searchField);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                  RefreshFilteredArticlesTable();
              }
              @Override
              public void removeUpdate(DocumentEvent e) {
                  RefreshFilteredArticlesTable();
              }
              @Override
              public void changedUpdate(DocumentEvent e) {
                  RefreshFilteredArticlesTable();
              }
        }
        );



        tab3Content.add(tab3Header, BorderLayout.NORTH);
        tab3Content.add(new JScrollPane(FilteredArticlesTable), BorderLayout.CENTER);
        tabbedPane.addTab("Пошук", tab3Content);




















        //------------------------------------------------------------------------------------
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            String name = tabbedPane.getTitleAt(index);
            System.out.println("Активна вкладка змінена на: " + name);
            switch (index)
            {
                case 0: RefreshGroupsTable(); break;
                case 1: RefreshArticlesTable(); break;
                case 2: RefreshFilteredArticlesTable(); break;
            }
        });

        frame.add(tabbedPane, BorderLayout.CENTER);
    }

    static void CreateMainWindow()
    {
        frame = new JFrame("Автоматизоване робоче місце");
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        AddTabs();

        frame.setVisible(true);
    }

    static void RefreshGroupsTable()
    {
        articleGroupsTableModel.setDataVector(wh.getAllArticleGroupsData(), ArticleGroupModel.GetFieldNames());
    }

    static void RefreshArticlesTable()
    {
        String[] columns = new String[]{
                "Код","Група товарів","Назва","Опис","Виробник","Кількість","Ціна"
        };
        articlesTableModel.setDataVector(wh.getAllArticlesData(), columns);
    }

    static void RefreshFilteredArticlesTable()
    {
        String[] columns = new String[]{
                "Код","Група товарів","Назва","Опис","Виробник","Кількість","Ціна"
        };
        String keyWord = searchField.getText().toLowerCase();
        filteredArticlesTableModel.setDataVector(wh.getFilteredArticlesData(keyWord), columns);
    }

    public static void main(String[] args) {
        CreateMainWindow();
        wh.Load();
        RefreshGroupsTable();
        RefreshArticlesTable();
        RefreshFilteredArticlesTable();

        wh.ShowAll();
        wh.Save();
    }
}

