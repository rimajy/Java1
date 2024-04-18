package Models;

import java.util.UUID;

public class ArticleModel {
    public UUID ID;
    public UUID GroupId;
    public String Name;
    public String Description;
    public String Producer;
    public long Amount;
    public long Price;

    public ArticleModel(UUID _ID, UUID GroupId, String Name, String Description, String Producer, long Amount, long Price)
    {
        ID = (_ID == null) ? UUID.randomUUID() : _ID;
        this.GroupId = GroupId;
        this.Name = Name;
        this.Description = Description;
        this.Producer = Producer;
        this.Amount = Amount;
        this.Price = Price;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s,%s,%s,%s,%s,%d,%d",
                ID.toString(), GroupId.toString(), Name, Description, Producer, Amount, Price
        );
    }


    public static ArticleModel parse(String s)
    {
        String[] parts = s.split(",");
        return new ArticleModel(
                UUID.fromString(parts[0]),
                UUID.fromString(parts[1]),
                parts[2],
                parts[3],
                parts[4],
                Long.parseLong(parts[5]),
                Long.parseLong(parts[6])
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
        var article = (ArticleModel) obj;
        return ID == article.ID || Name.equals(article.Name);
    }


}
