package robot;

import java.util.ArrayList;
import java.util.List;

public final class Topic {
    private String topic;
    private List<String> facts;
    public Topic(final String topic) {
        this.topic = topic;
        this.facts = new ArrayList<>();
    }
    public String getTopic() {
        return topic;
    }
    public List<String> getFacts() {
        return facts;
    }
}
