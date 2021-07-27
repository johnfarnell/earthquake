package au.com.auspost.dto;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public class ResponseDTO<T> {

    private String url;
    private T data;
    private List<Link> links = new ArrayList<Link>();
    public String getUrl() {
        return url;
    }

    public void setUrl(Link link) {
        this.url = link.getHref();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void addLink(Link link)
    {
        this.links.add(link);
    }

}
