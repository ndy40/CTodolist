/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.sussex.bean.backing;

/**
 *
 * @author ne51
 */
public class Paginator {
    
    private int total;
    
    private int firstPage;
    
    private int lastPage;
    
    private int pageSize;

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the firstPage
     */
    public int getFirstPage() {
        return firstPage;
    }

    /**
     * @param firstPage the firstPage to set
     */
    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    /**
     * @return the lastPage
     */
    public int getLastPage() {
        return lastPage;
    }

    /**
     * @param lastPage the lastPage to set
     */
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    
    
}
