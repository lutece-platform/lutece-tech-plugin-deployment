package fr.paris.lutece.plugins.deployment.util.vcs;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class GitSearchResult
{

    private Integer _bTotalCount;
    
    private List<GitSearchRepoItem> _listRepoItem;
    
    
    @JsonProperty("items")
    public List<GitSearchRepoItem> getListRepoItem( )
    {
        return _listRepoItem;
    }
    @JsonProperty("items")
    public void setListRepoItem( List<GitSearchRepoItem> listRepoItem )
    {
        this._listRepoItem = listRepoItem;
    }
    @JsonProperty("total_count")
    public Integer getTotalCount( )
    {
        return _bTotalCount;
    }
    @JsonProperty("total_count")
    public void setTotalCount( Integer bTotalCount )
    {
        this._bTotalCount = bTotalCount;
    } 
    
    
}
