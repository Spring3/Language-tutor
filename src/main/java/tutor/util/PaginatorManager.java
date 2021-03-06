package tutor.util;

/**
 * Created by Spring on 8/27/2015.
 */
public class PaginatorManager {
    public PaginatorManager(int itemsCount){
        setItemsPerPage(10);
        init(itemsCount);
    }

    private int itemsPerPage;
    private int totalPages;
    private int currentPage;
    private static final int MAX_ITEMS_PER_PAGE = 10;

    public int getMaxItemsPerPage(){
        return MAX_ITEMS_PER_PAGE;
    }

    public void goToPage(int pageIndex){
        setCurrentPage(pageIndex + 1);
    }

    public int getCurrentPage(){
        return currentPage;
    }

    public void setCurrentPage(int currentPage){
        this.currentPage = currentPage;
    }

    public int getTotalPages(){
        return totalPages == 0 ? 1 : totalPages;
    }

    public void setTotalPages(int totalPages){
        this.totalPages = totalPages;
    }

    public int getItemsPerPage(){
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage){
        this.itemsPerPage = itemsPerPage;
    }

    private void init(int elementsCount){
        setCurrentPage(1);
        if (elementsCount % getItemsPerPage() == 0){
            setTotalPages(elementsCount / getItemsPerPage());
        }
        else{
            setTotalPages(elementsCount/getItemsPerPage() + 1);
        }
    }
}
