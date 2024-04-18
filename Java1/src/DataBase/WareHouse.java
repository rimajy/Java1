package DataBase;
import Models.ArticleGroupModel;
import Models.ArticleModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class WareHouse {
    private ArrayList<ArticleModel> articles;
    private ArrayList<ArticleGroupModel> articleGroups;
    private FilePaths filePaths;


    public WareHouse(FilePaths _filePaths)
    {
        articles = new ArrayList<>();
        articleGroups = new ArrayList<>();
        filePaths = _filePaths;
    }


    public void AddArticle(ArticleModel articleModel)
    {
        if (!articles.contains(articleModel)){
            articles.add(articleModel);
        }
    }

    public void UpdateArticleAmount(UUID id, int amount)
    {
        for(ArticleModel article : articles)
        {
            if (article.ID.equals(id))
            {
                article.Amount += amount;
                return;
            }
        }
    }

    public void UpdateArticle(ArticleModel articleModel)
    {
        for(ArticleModel article : articles)
        {
            if (article.ID.equals(articleModel.ID))
            {
                article.Name = articleModel.Name;
                article.Description = articleModel.Description;
                article.Amount = articleModel.Amount;
                article.Producer = articleModel.Producer;
                article.Price = articleModel.Price;
                return;
            }
        }
    }



    public void DeleteArticleById(UUID id)
    {
        articles.removeIf(article -> article.ID.equals(id));
    }

    public  ArrayList<ArticleGroupModel> GetAllGroups()
    {
        return articleGroups;
    }


    public void AddArticleGroup(ArticleGroupModel articleGroupModel)
    {
        if (!articleGroups.contains(articleGroupModel)){
            articleGroups.add(articleGroupModel);
        }
    }

    public void DeleteArticleGroupById(UUID ID)
    {
        articleGroups.removeIf(articleGroupModel -> articleGroupModel.ID.equals(ID));
    }

    public void ChangeArticleGroup(ArticleGroupModel articleGroupModel)
    {
        for (var articleGroup : articleGroups)
        {
            if (articleGroup.ID == articleGroupModel.ID)
            {
                articleGroup.Name = articleGroupModel.Name;
                articleGroup.Description = articleGroupModel.Description;
                break;
            }
        }
    }


    public void Load()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePaths.GroupsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                articleGroups.add(ArticleGroupModel.parse(line));
            }
            System.out.println("Групи прочитані з файлу");
        }
        catch (IOException e) {
            System.out.println("Файл з групами товарів відсутній");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePaths.ArticlesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                articles.add(ArticleModel.parse(line));
            }
            System.out.println("Товари прочитані з файлу");
        }
        catch (IOException e) {
            System.out.println("Файл з товарами відсутні");
        }

    }


    public void Save()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePaths.ArticlesFilePath))) {
            for (ArticleModel articleModel : articles) {
                writer.write(articleModel.toString());
                writer.newLine();
            }
            System.out.println("Товари записані у файл");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePaths.GroupsFilePath))) {
            for (ArticleGroupModel articleGroup : articleGroups) {
                writer.write(articleGroup.toString());
                writer.newLine();
            }
            System.out.println("Товари записані у файл");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public Object[][] getAllArticleGroupsData()
    {
        Object[][] data = new Object[articleGroups.size()][3];
        for (int i = 0; i < articleGroups.size(); i++) {
            data[i][0] = articleGroups.get(i).ID;
            data[i][1] = articleGroups.get(i).Name;
            data[i][2] = articleGroups.get(i).Description;
        }
        return data;
    }

    public Object[][] getAllArticlesData()
    {
        Object[][] data = new Object[articles.size()][7];

        for (int i = 0; i < articles.size(); i++) {
            data[i][0] = articles.get(i).ID;
            data[i][1] = GetGroupNameById(articles.get(i).GroupId);
            data[i][2] = articles.get(i).Name;
            data[i][3] = articles.get(i).Description;
            data[i][4] = articles.get(i).Producer;
            data[i][5] = articles.get(i).Amount;
            data[i][6] = articles.get(i).Price;
        }
        return data;
    }

    public long[] GetArticleStatistics(String name, String group)
    {
        long[] result = new long[]{0,0,0};
        UUID groupId = null;
        for (ArticleGroupModel i : articleGroups) {
            if (i.Name.equals(group)) {
                groupId = i.ID;
            }
        }

        for (ArticleModel articleModel : articles) {
            if (articleModel.Name.equals(name)) {
                result[0] += articleModel.Amount * articleModel.Price;
            }
            if (articleModel.GroupId.equals(groupId)) {
                result[1] += articleModel.Amount * articleModel.Price;
            }
            result[2] += articleModel.Amount * articleModel.Price;
        }
        return result;
    }

    public Object[][] getFilteredArticlesData(String keyWord)
    {
        Object[][] data = new Object[articles.size()][7];

        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).Name.toLowerCase().contains(keyWord) ||
                articles.get(i).Description.toLowerCase().contains(keyWord) ||
                articles.get(i).Producer.toLowerCase().contains(keyWord) ||
                GetGroupNameById(articles.get(i).GroupId).contains(keyWord)
            )
            {
                data[i][0] = articles.get(i).ID;
                data[i][1] = GetGroupNameById(articles.get(i).GroupId);
                data[i][2] = articles.get(i).Name;
                data[i][3] = articles.get(i).Description;
                data[i][4] = articles.get(i).Producer;
                data[i][5] = articles.get(i).Amount;
                data[i][6] = articles.get(i).Price;
            }
        }
        return data;
    }


    public String GetGroupNameById(UUID ID)
    {
        for(var articleGroup : articleGroups)
        {
            if (articleGroup.ID.equals(ID))
            {
                return articleGroup.Name;
            }
        }
        return "---";
    }

    public void DeleteAllArticlesByGroup(UUID groupID)
    {
        articles.removeIf(articleModel -> articleModel.GroupId.equals(groupID));
    }


    public void ShowAll()
    {
        System.out.println("---------------------ГРУПИ------------------------");
        for (ArticleModel articleModel : articles) {
            System.out.println(articleModel.toString());
        }
        System.out.println("---------------------ТОВАРИ------------------------");
        for (ArticleGroupModel articleGroupModel : articleGroups) {
            System.out.println(articleGroupModel.toString());
        }
    }


}
