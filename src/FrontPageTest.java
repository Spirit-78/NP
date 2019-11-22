import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message){
        super("Category " + message + " was not found");
    }
}

class Category{
    String name;
    public Category(String name){ this.name = name;}

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(this.getClass()!=obj.getClass())
            return false;
        Category other = (Category) obj;
        if(!this.name.equals(other.name))
            return false;
        return true;
    }
    public String getName(){return name;}
}

abstract class NewsItem{
    private String headline;
    private Date published;
    private Category cat;

    public NewsItem(String headline, Date published, Category cat){
        this.headline = headline;
        this.published = new Date(published.getTime());
        this.cat = new Category(cat.getName());
    }
    Category getCat(){return cat;}
    String getHeadline(){return headline;}
    Date getPublished(){return published;}


    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem{
    private String text;

    public TextNewsItem(String headline, Date published, Category cat,String text) {
        super(headline, published, cat);
        this.text = text;
    }
    char [] getText(){
        char [] tmp = new char[text.length()];
        for(int i=0; i<text.length();i++)
            tmp[i] = text.charAt(i);
        return tmp;
    }
    public String getTeaser(){
        Date today = new Date();
        return String.format("%s\n%d\n%.80s\n", getHeadline(),(today.getTime()- getPublished().getTime())/1000/60,
                text);
    }
}

class MediaNewsItem extends NewsItem{
    private String url;
    private int views;

    public MediaNewsItem(String headline, Date published, Category cat, String url, int views) {
        super(headline, published, cat);
        this.url = url;
        this.views =views;
    }
    public String getTeaser(){
        Date today = new Date();
        return String.format("%s\n%d\n%s\n%d\n", getHeadline(),(today.getTime()- getPublished().getTime())/1000/60,
                this.url, this.views);
    }

}

class FrontPage{
    private List<NewsItem> news;
    private Category [] categories;

    public FrontPage(Category [] categories){
        this.news = new ArrayList<>();
        this.categories = new Category[categories.length];
        for(int i=0;i<categories.length;i++)
            this.categories[i] = new Category(categories[i].getName());

    }
    void addNewsItem(NewsItem newsItem){
        news.add(newsItem);
    }
    List<NewsItem> listByCategory(Category category){
        return news.stream().filter(item -> item.getCat().equals(category)).collect(Collectors.toList());
    }
    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        boolean flag = false;
        for(Category item : categories)
            if(item.getName().equals(category))
                {
                    flag = true;
                    break;
                }
        if(flag)
            return news.stream().filter(item -> item.getCat().getName().equals(category)).collect(Collectors.toList());
        else
            throw new CategoryNotFoundException(category);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        news.forEach(item -> sb.append(item.getTeaser()));
        return sb.toString();
    }
}


public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde