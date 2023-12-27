package model;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Object> objects;

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public Request() {
    }

    public Request(List<Object> objects) {
        this.objects = objects;
    }
}
