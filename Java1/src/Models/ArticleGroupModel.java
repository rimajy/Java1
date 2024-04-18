package Models;

import java.util.UUID;

public class ArticleGroupModel {
    public UUID ID;
    public String Name;
    public String Description;


    public static String[] GetFieldNames()
    {
        return new String[]{"Код", "Назва", "Опис"};
    }

    public ArticleGroupModel(UUID _ID, String Name, String Description) {
        this.Name = Name;
        this.Description = Description;
        ID = (_ID == null) ? UUID.randomUUID() : _ID;
    }

    @Override
    public String toString()
    {
        return String.format("%s,%s,%s",ID.toString(), Name, Description);
    }


    public static ArticleGroupModel parse(String s)
    {
        String[] parts = s.split(",");
        return new ArticleGroupModel(
                UUID.fromString(parts[0]),
                parts[1],
                parts[2]
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        var articleGroup = (ArticleGroupModel) obj;
        return ID == articleGroup.ID || Name.equals(articleGroup.Name);
    }

}
